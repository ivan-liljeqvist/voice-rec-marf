# voicerec
Speech independent voice recognition server.


# Endpoints

Each endpoint expects data as "form-data".


##  1) POST /enroll
a new user registers his/her voice

       Expects:
       - firstname
       - lastname
       - file
##  2) POST /authenticate
a user submits her/his voice for authentication

       Expects:
       -firstname
       -lastname
       -file
##  3) POST /train
when we start the server we need to train it with previously enrolled users. We read from the DB and train the server with all known enrollment files.

       Expects:
       nothing
