package helpers;

/**
 * Created by IvanLiljeqvist on 07/03/16.
 */
public class ResponseHelper {

    public static String splash(){
        return "{\"success\":\"Server running!\"}";
    }

    public static String enrolled(){
        return "{\"success\":\"Enrollment complete!\"}";
    }

    public static String trainingDone(){
        return "{\"success\":\"Training complete!\"}";
    }

    public static String authenticated(String fn, String ln, int likelihood){
        return "{\"success\":\"Authenticated!\", \"firstname\":\""+fn+"\", \"lastname\":\""+ln+"\", \"likelihood\":\""+likelihood+"\"}";
    }

    public static String notAuthenticated(){
        return "{\"error\":\"Authentication failed!\"}";
    }

    public static String internalError(){
        return "{\"error\":\"Internal Server Error\"}";
    }

}
