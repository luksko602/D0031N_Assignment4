/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RESTController;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 *Klass som hanterar kopplingen till Canvas 
 * @author Lukas Skog Andersen, luksok-8
 */
public class RESTConnectionCanvas {

    private static final Client CLIENT = ClientBuilder.newClient();
    private static final WebTarget canvasService = CLIENT.target(getBaseURI());
    //Bearer för att kunna pusha till aktuell kalender (unik för användaren)
    private static final String BEARER = "Bearer 3755~QKRX29HtPZBcTFpVPpheg0x6mUxkspGHpfiOrCfPFv5XdfHiK6DLdB1COP3wZdiW";

    /**
     * Metod som pushar ett event till Canvas med givna parametrar. 
     * Returnerar status på anropet (201 betyder lyckad push).
     * 
     * @param context_code
     * @param title
     * @param start_at
     * @param end_at
     * @param locationName
     * @param locationAddress
     * @param description
     * @return int, status på anropet
     */
    public int postCaldendar(String context_code, String title, String start_at, String end_at,
            String locationName, String locationAddress, String description) {
        //Sätter alla form-parametrar
        Form form = new Form();
        form.param("calendar_event[context_code]", context_code);
        form.param("calendar_event[title]", title);
        form.param("calendar_event[start_at]", start_at);
        form.param("calendar_event[end_at]", end_at);
        form.param("calendar_event[location_name]", locationName);
        form.param("calendar_event[location_address]", locationAddress);
        form.param("calendar_event[description]", description);
        
        //Anrop till Canvas
        Response response = canvasService
                .request().header("Authorization", BEARER)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        return response.getStatus();
    }

    /**
     * Sätter URI för anrop till Canvas
     * @return 
     */
    public static URI getBaseURI() {
        return UriBuilder.fromUri("https://ltu.instructure.com/api/v1/calendar_events.json").build();

    }
}
