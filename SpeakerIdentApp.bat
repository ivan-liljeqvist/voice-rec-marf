::
:: Wrapper Batch Script for SpeakerIdentApp.class/.jar
:: for Windows environment. Accepts all the options
:: that SpeakerIdentApp.class/.jar accepts. Edit as
:: needed to suit your environment.
::
:: Serguei Mokhov
::
:: $Id: SpeakerIdentApp.bat,v 1.1 2007/01/03 21:47:58 mokhov Exp $
::

set JAVA=java
set CLASSPATH=.;marf.jar
set JFLAGS="-ea -verify -classpath %CLASSPATH%"
set APP=SpeakerIdentApp

:: Prefer .jar first over .class if exists
if exist "%APP%.jar" goto ADJUST
goto RUN

:ADJUST
set APP="%APP%.jar"
set JFLAGS="%JFLAGS% -jar"

:RUN
"%JAVA%" "%JFLAGS%" "%APP%" %1 %2 %3 %4 %5 %6 %7 %8 %9

exit %errorlevel%

:: EOF
