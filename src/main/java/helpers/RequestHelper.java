package helpers;
/**
 * Created by IvanLiljeqvist on 07/03/16.
 */

import spark.Request;
import spark.utils.IOUtils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;


public class RequestHelper {


    /**
     * Takes a request object received form the "authenticate" endpoint and handles the saving of the authentication file.
     * The request has to be "form-data" and contain "file" - the enrollment voice .wav, "firstname" - the first name of the
     * person enrolling and "lastname" - the last name of the person enrolling.
     * @param req
     * @return File - the File object with the saved authentication file.
     * @throws Exception
     */
    public static File saveAuthenticationFile(Request req) throws Exception{

        if (req.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
            req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
        }

        Part uploadFile = req.raw().getPart("file");

        String filename = ThreadLocalRandom.current().nextInt(10, Integer.MAX_VALUE-10)+"_auth.wav";

        Path filePath = Paths.get(".",filename);
        try{
            Files.delete(filePath); //replace the old file
        }catch(Exception e){
            System.out.println("Couldn't delete old authentication file.");
        }

        Files.copy(uploadFile.getInputStream(),filePath);

        Thread.sleep(500);

        return filePath.toFile();

    }


    /**
     * Takes a request object recieved from the "enroll" endpoint and handles the saving of the enrollment file.
     * The request has to be "form-data" and contain "file" - the enrollment voice .wav, "firstname" - the first name of the
     * person enrolling and "lastname" - the last name of the person enrolling.
     * @param req
     * @return File - the File object with the saved enrollment file
     * @throws Exception
     */
    public static File saveEnrollmentFile(Request req) throws Exception{


        if (req.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
            req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
        }

        Part uploadFile = req.raw().getPart("file");

        String firstname = RequestHelper.getStringFromPart(req.raw().getPart("firstname"));
        String lastname = RequestHelper.getStringFromPart(req.raw().getPart("lastname"));
        String filename = lastname+"_"+firstname+"_enrollment.wav";


        Path filePath = Paths.get(".",filename);
        try{
            Files.delete(filePath); //replace the old file
        }catch(Exception e){
            System.out.println("Couldn't delete old enrollment file.");
        }

        Files.copy(uploadFile.getInputStream(),filePath);

        return filePath.toFile();

    }

    /**
     * Takes a Part object that contains a string and returns that string.
     * Make sure you pass a part that actually contains a simple String and not a file or something else.
     * @param part
     * @return
     */
    public static String getStringFromPart(Part part){

        try{
            return IOUtils.toString(part.getInputStream());
        }
        catch(Exception e){
            return "Error when extracting String.";
        }


    }

}
