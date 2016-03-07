package helpers;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import java.util.Arrays;

/**
 * Created by IvanLiljeqvist on 07/03/16.
 */
public class MongoHelper {

    public static MongoDatabase mongo() throws Exception {


        MongoClient mongoClient = new MongoClient(
                new MongoClientURI( "mongodb://ivan:secret@ds035702.mlab.com:35702/liljeparse" )
        );
        MongoDatabase db = mongoClient.getDatabase("liljeparse");

        return db;


    }

}
