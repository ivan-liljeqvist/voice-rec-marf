/**
 * Created by IvanLiljeqvist on 07/03/16.
 */

import com.bitsinharmony.recognito.*;
import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import helpers.MongoHelper;
import helpers.RequestHelper;
import org.bson.Document;
import user.User;
import user.UserService;

import java.io.File;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * This is the class that contains all the routes and implementation of each route.
 */

public class SpeakerRecognitionServer {

    //this is the recognizer that we need to train and give it sound to recognize.
    private static Recognito<User> recognito = new Recognito<User>(44100.0f);

    public static void main(String[] args) {


        get("/", (req,res)->"Speaker Recognition Server, running!");



        /* a new user enrolls and uploads voice to be remembered by the server */
        post("/enroll", (req,res) -> {


            //save the enrollment file
            File enrollmentFile = RequestHelper.saveEnrollmentFile(req);

            //create new user
            String firstname = RequestHelper.getStringFromPart(req.raw().getPart("firstname"));
            String lastname = RequestHelper.getStringFromPart(req.raw().getPart("lastname"));



            User newUser = new User(firstname,lastname,enrollmentFile.getAbsolutePath());

            //train recognito
            recognito.createVoicePrint(newUser,enrollmentFile);

            //save to Mongo
            UserService userService = new UserService(MongoHelper.mongo());
            userService.removeUsersWithName(firstname,lastname); // we dont want duplicates, remove the previous users with this name
            userService.createNewUser("{\"firstname\": \""+firstname+"\",\"lastname\": \""+lastname+"\",\"enrollmentFilePath\": \""+enrollmentFile.getAbsolutePath()+"\"}");


            return "enrolled";

        });



        /* a user wants to authenticate and uploads a sample of his/her voice */
        post("/authenticate", (req,res) -> {

            //save the authentication file
            File authenticationFile = RequestHelper.saveAuthenticationFile(req);

            //authenticate with recognito
            try{
                List<MatchResult<User>> matches = recognito.identify(authenticationFile);
                MatchResult<User> match = matches.get(0);

                authenticationFile.delete();

                return match.getKey().getFirstname() + " " + match.getKey().getLastname();
            }
            catch(Exception e){
                authenticationFile.delete();
                return "Internal Server Error";
            }

        });




        /* we just started the server and want to train the server with all enrolled voices */
        post("/train", (req,res) -> {

            System.out.println("train 1");

            UserService userService = new UserService(MongoHelper.mongo());
            FindIterable<Document> users = userService.findAll();

            System.out.println("train 2");

            users.forEach((Block<Document>) document -> {
                User user = new Gson().fromJson(document.toJson(), User.class);

                File enrollmentFile = new File(user.getEnrollmentFilePath());
                try{
                    System.out.println("training "+user.getEnrollmentFilePath());
                    recognito.createVoicePrint(user,enrollmentFile);
                }
                catch(Exception e){
                    System.out.println("Couldnt train. "+user.getEnrollmentFilePath());
                }



            });

            return "training done";
        });



    }

}
