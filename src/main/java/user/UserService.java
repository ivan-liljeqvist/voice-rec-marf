package user;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.javadoc.Doc;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IvanLiljeqvist on 07/03/16.
 * This class handles all logic that has to do with User.
 */

public class UserService {

    private final MongoDatabase db;
    private final MongoCollection<Document> collection;

    public UserService(MongoDatabase db) {
        this.db = db;
        this.collection = db.getCollection("user");
    }

    /**
     * Get all users in the DB.
     * @return List of all users.
     */
    public FindIterable<Document> findAll(){

        List<User> users = new ArrayList<>();

        return db.getCollection("user").find();



    }

    /**
     * Takes a JSON as input and inserts it as a User in the DB.
     * @param body
     */
    public void createNewUser(String body) {
        User user = new Gson().fromJson(body, User.class);



        Document document = new Document("firstname", user.getFirstname())
                            .append("lastname",user.getLastname())
                            .append("enrollmentFilePath",user.getEnrollmentFilePath());

        collection.insertOne(document);
    }

    /**
     * Removes all users with a given first and last name.
     * @param firstname
     * @param lastname
     */
    public void removeUsersWithName(String firstname, String lastname){

        db.getCollection("user").deleteMany(new Document("firstname", firstname).append("lastname",lastname));

    }

    /**
     * Finds a user with a specific id.
     * @param id
     */
    public void find(String id) {
        //return new User((BasicDBObject) collection.findOne(new BasicDBObject("_id", new ObjectId(id))));
    }


}
