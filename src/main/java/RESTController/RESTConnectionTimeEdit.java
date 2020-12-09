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
public class RESTConnectionTimeEdit {
    

    private static final Client CLIENT = ClientBuilder.newClient();
    private static WebTarget timeEditService; 
    
    public RESTConnectionTimeEdit(String uri, String startDate, String endDate) throws Exception{
        timeEditService = CLIENT.target(getBaseURI(uri, startDate, endDate));
    }
    
    public static String getResponse(String searchID){
      
        String response = timeEditService
                .path(searchID)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        return response;
    }
    
    public static String getAll(){
        String response = timeEditService
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        return response;
    }
    
     public ReservationContainer getAllAsJson(){
        Response response = timeEditService
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        ReservationContainer rc = new Gson().fromJson(response.readEntity(String.class), ReservationContainer.class);
         return rc;
    }
    
    public static URI getBaseURI(String course, String startDate, String endDate) throws Exception{
        switch (course) {
            case "D0019N":
                return UriBuilder.fromUri("https://cloud.timeedit.net/ltu/web/schedule1/ri.json?h=t&sid=3&p=" + startDate + 
                        ".x,"+ endDate + ".x&objects=133676.28&ox=0&types=0&fe=0").build();
                //133676
            case "D0023E":
                return UriBuilder.fromUri("https://cloud.timeedit.net/ltu/web/schedule1/ri.json?h=t&sid=3&p=" + startDate + 
                        ".x,"+ endDate + ".x&objects=133650.28&ox=0&types=0&fe=0").build();
                //133650
            case "D0031N":
                return UriBuilder.fromUri("https://cloud.timeedit.net/ltu/web/schedule1/ri.json?h=t&sid=3&p=" + startDate + 
                        ".x,"+ endDate + ".x&objects=132868.28&ox=0&types=0&fe=0").build();
                //132868
            default:
                throw new Exception("Course not found");
        }
    }
}
