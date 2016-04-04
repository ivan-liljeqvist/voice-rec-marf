#!/bin/tcsh -f

#
# Batch Processing of Training/Testing Samples
# NOTE: Make take quite some time to execute
#
# Copyright (C) 2002 - 2012 The MARF Research and Development Group
#
# $Header: /cvsroot/marf/apps/SpeakerIdentApp/testing.sh,v 1.51 2012/01/09 04:03:22 mokhov Exp $
#

#
# Set environment variables, if needed
#

setenv CLASSPATH .:marf.jar
setenv EXTDIRS

#
# Set flags to use in the batch execution
#

set java = 'java -ea -verify -Xmx512m'
#set debug = '-debug'
set debug = ''
set graph = ''
#set graph = '-graph'
#set spectrogram = '-spectrogram'
set spectrogram = ''

if($1 == '--reset') then
	echo "Resetting Stats..."
	$java SpeakerIdentApp --reset
	exit 0
endif

if($1 == '--retrain') then
	echo "Training..."

	# Always reset stats before retraining the whole thing
	$java SpeakerIdentApp --reset

	foreach preprep ("" "-silence" "-noise" "-silence -noise")
		foreach prep (-norm -boost -low -high -band -bandstop -highpassboost -raw -endp -lowcfe -highcfe -bandcfe -bandstopcfe)
			foreach feat (-fft -lpc -randfe -minmax -aggr)

				# Here we specify which classification modules to use for
				# training.
				#
				# NOTE: for most distance classifiers it's not important
				# which exactly it is, because the one of the generic Distance is used.
				# Exception from this rule is the Mahalanobis Distance, which needs
				# to learn its Covariance Matrix.

				foreach class (-cheb -mah -randcl -nn)
					echo "Config: $preprep $prep $feat $class $spectrogram $graph $debug"
					date

					# XXX: We cannot cope gracefully right now with these combinations in the
					# typical PC/JVM set up --- too many links in the fully-connected NNet,
					# so can run out of memory quite often; hence, skip them for now.
					if("$class" == "-nn" && ("$feat" == "-fft" || "$feat" == "-randfe" || "$feat" == "-aggr")) then
						echo "skipping..."
						continue
					endif

					time $java SpeakerIdentApp --train training-samples $preprep $prep $feat $class $spectrogram $graph $debug
				end

			end
		end
	end

endif

echo "Testing..."

foreach preprep ("" "-silence" "-noise" "-silence -noise")
	foreach prep (-norm -boost -low -high -band -bandstop -highpassboost -raw -endp -lowcfe -highcfe -bandcfe -bandstopcfe)
		foreach feat (-fft -lpc -randfe -minmax -aggr)
			foreach class (-eucl -cheb -mink -mah -diff -hamming -cos -randcl -nn)
				echo "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
				echo "Config: $preprep $prep $feat $class $spectrogram $graph $debug"
				date
				echo "============================================="

				# XXX: We cannot cope gracefully right now with these combinations in the
				# typical PC/JVM set up --- too many links in the fully-connected NNet,
				# so can run out of memory quite often; hence, skip them for now.
				if("$class" == "-nn" && ("$feat" == "-fft" || "$feat" == "-randfe" || "$feat" == "-aggr")) then
					echo "skipping..."
					continue
				endif

				time $java SpeakerIdentApp --batch-ident testing-samples $preprep $prep $feat $class $spectrogram $graph $debug

				echo "---------------------------------------------"
			end
		end
	end
end

echo "Stats:"

$java SpeakerIdentApp --stats | tee stats.txt
$java SpeakerIdentApp --best-score | tee best-score.tex
date | tee stats-date.tex

echo "Testing Done"

exit 0

# EOF
