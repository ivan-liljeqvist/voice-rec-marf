package user;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

/**
 * Created by IvanLiljeqvist on 07/03/16.
 */
public class User {

    private String firstname;
    private String lastname;
    private String enrollmentFilePath;
    private ObjectId id;



    public User(BasicDBObject dbObject){

        this.id = (ObjectId)dbObject.get("_id");
        this.firstname = dbObject.getString("firstname");
        this.lastname = dbObject.getString("lastname");
        this.enrollmentFilePath = dbObject.getString("enrollmentFilePath");

    }

    public User(String firstname, String lastname, String enrollmentFilePath) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.enrollmentFilePath = enrollmentFilePath;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEnrollmentFilePath(String enrollmentFilePath) {
        this.enrollmentFilePath = enrollmentFilePath;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEnrollmentFilePath() {
        return enrollmentFilePath;
    }
}
