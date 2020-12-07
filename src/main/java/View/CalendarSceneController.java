/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import RESTController.RESTConnection;
import Model.Reservation;
import Model.ReservationContainer;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class CalendarSceneController implements Initializable {
    private ReservationContainer rc;
    private RESTConnection con;
    ObservableList courseList = FXCollections.observableArrayList();
    
    @FXML
    private TextField courseCodeField;
    @FXML
    private TextField lokalField;
    @FXML
    private TextField larareField;
    @FXML
    private TextField aktivitetField;
    @FXML
    private TextField kursField;
    @FXML
    private TextField campusField;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField syfteField;
    @FXML
    private TextField kursProgramField;
    @FXML
    private TextField kundField;
    @FXML
    private TextField utrustningField;
    @FXML
    private Button backButton;
    
    @FXML
    private Button getButton;
    @FXML
    private Button saveButton;
    @FXML
    private ListView calendarList;
       
    @FXML
    public void backToStart(ActionEvent event) throws IOException{
        Parent calendarViewParent = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        Scene calendarViewScene = new Scene(calendarViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(calendarViewScene);
    }
    
    @FXML
    public void getCalendarList(ActionEvent event){
        try {
            con = new RESTConnection(courseCodeField.getText());
             rc = con.getAllAsJson(); 
             loadCourseData(rc);   
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Varning!");
            alert.setHeaderText("Kursen " + courseCodeField.getText() + " kan ej hittas!");
            alert.setContentText("Var god försök igen.");
            System.out.println(ex);
            alert.showAndWait();
        }  
    }
    
    public void loadCourseData(ReservationContainer rc){
        courseList.removeAll(courseList);
        for(Reservation res : rc.getReservations()){
               courseList.add(res.getEventInfo());
            } 
        calendarList.getItems().clear();
        clearAllFields();
        calendarList.getItems().addAll(courseList);
    }
     

    @FXML
    private void displaySelected(MouseEvent event) {
     try{
         int index = calendarList.getSelectionModel().getSelectedIndex();
            Reservation selected = rc.getReservationByIndex(index);
            List<String> specs = selected.getColumns();
            //0an är tom
            lokalField.setText(specs.get(1));
            larareField.setText(specs.get(2));
            aktivitetField.setText(specs.get(3));
            //4an är tom
            kursField.setText(specs.get(5));
            campusField.setText(specs.get(6));
            textField1.setText(specs.get(7));
            textField2.setText(specs.get(8));
           syfteField.setText(specs.get(9));
           kursProgramField.setText(specs.get(10));
            kundField.setText(specs.get(11));
            utrustningField.setText(specs.get(12));
            }catch(Exception e){     
            }
    }
     
    private void clearAllFields(){
           lokalField.clear();
            larareField.clear();
            aktivitetField.clear();
            //4an är tom
            kursField.clear();
            campusField.clear();
            textField1.clear();
            textField2.clear();
           syfteField.clear();
           kursProgramField.clear();
            kundField.clear();
            utrustningField.clear();
    }
    
     @FXML
    private void saveEvent(ActionEvent event) {
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
}
