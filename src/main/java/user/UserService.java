package user;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.javadoc.Doc;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IvanLiljeqvist on 07/03/16.
 */

public class UserService {

    private final MongoDatabase db;
    private final MongoCollection<Document> collection;

    public UserService(MongoDatabase db) {



        this.db = db;
        this.collection = db.getCollection("user");
    }

    public FindIterable<Document> findAll(){

        List<User> users = new ArrayList<>();

        return db.getCollection("user").find();



    }

    public void createNewUser(String body) {
        User user = new Gson().fromJson(body, User.class);



        Document document = new Document("firstname", user.getFirstname())
                            .append("lastname",user.getLastname())
                            .append("enrollmentFilePath",user.getEnrollmentFilePath());

        collection.insertOne(document);
    }

    public void find(String id) {
        //return new User((BasicDBObject) collection.findOne(new BasicDBObject("_id", new ObjectId(id))));
    }


}
