/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package RESTController;

import Model.ReservationContainer;
import com.google.gson.Gson;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Lukas
 */
public class RESTConnection {
    

    private static final Client CLIENT = ClientBuilder.newClient();
    private static WebTarget actorService; 
    
    public RESTConnection(String uri) throws Exception{
        actorService = CLIENT.target(getBaseURI(uri));
    }
    
    public static String getResponse(String searchID){
      
        String response = actorService
                .path(searchID)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        return response;
    }
    
    public static String getAll(){
        String response = actorService
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        return response;
    }
    
     public ReservationContainer getAllAsJson(){
        Response response = actorService
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        ReservationContainer rc = new Gson().fromJson(response.readEntity(String.class), ReservationContainer.class);
         return rc;
    }
    
    public static URI getBaseURI(String course) throws Exception{
        // return UriBuilder.fromUri("https://jsonplaceholder.typicode.com/users").build();
        switch (course) {
            case "D0019N":
                return UriBuilder.fromUri("https://cloud.timeedit.net/ltu/web/schedule1/ri176XQ2770Z5YQv250131Z6yQY750173YX5Y7gQ9076757.json").build();
            case "D0023E":
                return UriBuilder.fromUri("https://cloud.timeedit.net/ltu/web/schedule1/ri177XQQ770Z50Qv8Q013gZ6y5Y750476Y75Y.json").build();
            case "I0006N":
                return UriBuilder.fromUri("https://cloud.timeedit.net/ltu/web/schedule1/ri176XQ9770Z5YQv950131Z6yQY750173YX5Y7gQ2076757.json").build();
            default:
                throw new Exception("Course not found");
        }
    }
}
