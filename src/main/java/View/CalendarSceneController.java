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
 * @author Lukas Skog Andersen, luksok-8
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

    /**
     * Funktion som startar ActiveMQ sidan (ej längre aktiv)
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void backToStart(ActionEvent event) throws IOException {
        Parent calendarViewParent = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        Scene calendarViewScene = new Scene(calendarViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(calendarViewScene);
    }

    /**
     * Hanterar anropet till TimeEdit för att hämta schemat
     *
     * @param event
     */
    @FXML
    public void getCalendarList(ActionEvent event) {
        try {
            //Skapar en connection till TimeEdit med den akutella kursen
            conTimeEdit = new RESTConnectionTimeEdit(courseCodeField.getText(),
                    //Trimmar datumen för att passa i anropet
                    startDate.getValue().toString().replaceAll("-", ""),
                    endDate.getValue().toString().replaceAll("-", "")
            );
            rc = conTimeEdit.getAllAsJson();
            //Laddar listan med aktuell kursdata
            loadCourseData(rc);
        } catch (Exception ex) {
            clearAllFields();
            calendarList.getItems().clear();
            //pop-up om något gick fel
            alertWindow("Varning!", "Det gick inte att hämta schemat!",
                    "Kontrollera fälten och försök igen.");
        }
    }

    /**
     * Sätter datan i listan för den aktuella kursen
     *
     * @param rc
     */
    public void loadCourseData(ReservationContainer rc) {
        courseList.removeAll(courseList);
        for (Reservation res : rc.getReservations()) {
            courseList.add(res.getEventInfo());
        }
        calendarList.getItems().clear();
        //Tömmer alla textfält
        clearAllFields();
        calendarList.getItems().addAll(courseList);
    }

    /**
     * Sätter informationen i textfälten för det valda eventet i listan
     *
     * @param event
     */
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

    /**
     * Tömmer alla textfields
     */
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

    /**
     * Hanterar pushen av event till Canvas
     *
     * @param event
     */
    @FXML
    private void saveEvent(ActionEvent event) {
        //Hämtar vilken person(eller kurs) som eventen ska pushas till
        String input = dialogWindow("Push till Canvas", "Vilken kurs vill du pusha till:", "Kurs:");
        if (input != null && input.length() != 0) {
            //Hämtar alla valda event
            ObservableList selected = calendarList.getSelectionModel().getSelectedIndices();
            //Strängar med lyckade och failade pushningar
            String pushed = "";
            String error = "";
            for (Object i : selected) {
                Reservation res = rc.getReservationByIndex((int) i);
                //Skickar en förfrågan till Canvas
                int status = conCanvas.postCaldendar(
                        //Lukas = 72164
                        "user_" + input,
                        res.getAktivitet(),
                        res.getStartdate() + "T" + res.getStarttime() + "Z",
                        res.getEnddate() + "T" + res.getEndtime() + "Z",
                        res.getLokal(),
                        res.getCampus(),
                        res.getText1() + " " + res.getText2()
                );
                //Om eventet är lyckat postat
                if (status == 201) {
                    pushed += "\n" + res.getId();
                    //Om eventet inte lyckades pushas
                } else {
                    error += "\n" + res.getId();
                }
            }
            //Visar reslutat av pushen
            infoWindow("Resultat av push",
                    "Event pushade: " + pushed + "\nEvent ej pushade: " + error);

        } else {
            alertWindow("Varning", "Något blev fel!", "Fel vid inmatning. Vänligen försök igen.");
        }
    }

    /**
     * Aktiveras varje gång ett textfield ändras.
     *
     * @param event
     */
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

    //---------POP-UP WINDOWS---------------------
    /**
     * Startar ett varningsfönster
     *
     * @param title
     * @param header
     * @param content
     */
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

    /**
     * Startar ett dialogfönster som tar emot input och returnerar det
     *
     * @param title
     * @param header
     * @param content
     * @return inskrivet värde
     */
    private String dialogWindow(String title, String header, String content) {
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

    /**
     * Skapar ett informationsfönster
     *
     * @param title
     * @param text
     */
    private void infoWindow(String title, String text) {
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
     * Startar kontrollklassen
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        calendarList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }
}
