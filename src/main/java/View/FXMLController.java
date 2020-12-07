package View;

import MessageController.JmsAmqManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLController implements Initializable {
    
    @FXML
    private TextField messageTextField;
    @FXML
    private TextField queueTextField;
    @FXML
    private TextArea textArea;
    
    @FXML
    public void sendButtonAction(ActionEvent event) {
     try{
        new JmsAmqManager().produce(queueTextField.getText(), messageTextField.getText());
        queueTextField.setText("");
        messageTextField.setText("");
     }catch(Exception e){
         System.out.println(e);
     }
    }
    
    @FXML
    public void getButtonAction(ActionEvent event){
        try{
            String message = new JmsAmqManager().consume(queueTextField.getText());
            messageTextField.setText(message);
            queueTextField.setText("");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    @FXML
    public void startListener(ActionEvent event){
        try{
            JmsAmqManager jmsm = new JmsAmqManager();
            jmsm.autoConsume(queueTextField.getText());
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    @FXML
    public void startCalendar(ActionEvent event) throws IOException{
        Parent calendarViewParent = FXMLLoader.load(getClass().getResource("/fxml/CalendarScene.fxml"));
        Scene calendarViewScene = new Scene(calendarViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(calendarViewScene);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
