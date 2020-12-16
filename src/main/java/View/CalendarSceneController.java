/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import RESTController.RESTConnectionTimeEdit;
import Model.Reservation;
import Model.ReservationContainer;
import RESTController.RESTConnectionCanvas;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Lukas
 */
public class CalendarSceneController implements Initializable {

    private ReservationContainer rc;
    private RESTConnectionTimeEdit conTimeEdit;
    private RESTConnectionCanvas conCanvas = new RESTConnectionCanvas();
    ;
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
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    @FXML
    private Button getButton;
    @FXML
    private Button saveButton;
    @FXML
    private ListView calendarList;

    @FXML
    public void backToStart(ActionEvent event) throws IOException {
        Parent calendarViewParent = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        Scene calendarViewScene = new Scene(calendarViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(calendarViewScene);
    }

    @FXML
    public void getCalendarList(ActionEvent event) {
        try {
            conTimeEdit = new RESTConnectionTimeEdit(courseCodeField.getText(),
                     startDate.getValue().toString().replaceAll("-", ""),
                     endDate.getValue().toString().replaceAll("-", "")
            );
            rc = conTimeEdit.getAllAsJson();
            loadCourseData(rc);
        } catch (Exception ex) {
            clearAllFields();
            calendarList.getItems().clear();
            
            alertWindow("Varning!", "Det gick inte att hämta schemat!", "Kontrollera fälten och försök igen.");
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("Varning!");
//            alert.setHeaderText("Det gick inte att hämta schemat!");
//            alert.setContentText("Kontrollera fälten och försök igen.");
//            System.out.println(ex);
//            alert.showAndWait();
        }
    }

    public void loadCourseData(ReservationContainer rc) {
        courseList.removeAll(courseList);
        for (Reservation res : rc.getReservations()) {
            courseList.add(res.getEventInfo());
        }
        calendarList.getItems().clear();
        clearAllFields();
        calendarList.getItems().addAll(courseList);
    }

    @FXML
    private void displaySelected(MouseEvent event) {
        try {
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
        } catch (Exception e) {
        }
    }

    private void clearAllFields() {
        lokalField.clear();
        larareField.clear();
        aktivitetField.clear();
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
        String input = dialogWindow("Push till Canvas", "Vilken kurs vill du pusha till:", "Kurs:");
        if (input != null && input.length() != 0) {
            ObservableList selected = calendarList.getSelectionModel().getSelectedIndices();
            List<Reservation> success = new ArrayList();
            List<Reservation> failed = new ArrayList();
            for (Object i : selected) {
                Reservation res = rc.getReservationByIndex((int) i);
                int status = conCanvas.postCaldendar(
                        //Lukas = 72164
                        "user_" + input,
                        res.getAktivitet(),
                        res.getStartdate() + "T" + res.getStarttime() + "Z",
                        res.getEnddate() + "T" + res.getEndtime() + "Z",
                        lokalField.getText(),
                        campusField.getText(),
                        textField1.getText() + " " + textField2.getText()
                );
                if (status == 201) {
                    success.add(res);
                } else {
                    failed.add(res);
                }
            }
            String pushed = "";
            String error = "";
            for(Reservation res: success){
                pushed += "\n" + res.getId();
            }
            for(Reservation res: failed){
                error += "\n" + res.getId();
            }
            infoWindow("Resultat av push"
                    ,"Event pushade: " + pushed + "\nEvent ej pushade: " + error );
            
        } else {
            alertWindow("Varning", "Något blev fel!", "Fel vid inmatning. Vänligen försök igen.");
        }
    }

    private void alertWindow(String title, String header, String content) {
        
        Alert alert = new Alert(AlertType.WARNING);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
        getClass().getResource("/styles/Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog"); 
            
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private String dialogWindow(String title, String header, String content){
        TextInputDialog dialog = new TextInputDialog("72164");
        
         DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
        getClass().getResource("/styles/Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog"); 
        
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        dialog.setGraphic(null);
        Optional<String> result = dialog.showAndWait();
        TextField input = dialog.getEditor();
        return input.getText().toString();
    }
    
    private void infoWindow(String title, String text){
        Alert alert = new Alert(AlertType.INFORMATION);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
        getClass().getResource("/styles/Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog"); 
        
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        calendarList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
    }

    @FXML
    private void onEditSpecs(KeyEvent event) {
        List<String> specs = new ArrayList();
        specs.add("");
        specs.add(lokalField.getText());
        specs.add(larareField.getText());
        specs.add(aktivitetField.getText());
        specs.add("");
        specs.add(kursField.getText());
        specs.add(campusField.getText());
        specs.add(textField1.getText());
        specs.add(textField2.getText());
        specs.add(syfteField.getText());
        specs.add(kursProgramField.getText());
        specs.add(kundField.getText());
        specs.add(utrustningField.getText());

        int index = calendarList.getSelectionModel().getSelectedIndex();
        rc.getReservationByIndex(index).setColumns(specs);
    }
}
