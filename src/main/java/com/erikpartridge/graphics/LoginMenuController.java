package com.erikpartridge.graphics;

import com.erikpartridge.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

public class LoginMenuController extends Window{

    @FXML
    private AnchorPane basePane;

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML // fx:id="passwordField"
    private PasswordField passwordField; // Value injected by FXMLLoader

    @FXML // fx:id="cidField"
    private TextField cidField; // Value injected by FXMLLoader

    @FXML
    void submitCredentials(ActionEvent event) {
        if(passwordField.getCharacters().length() == 0 || cidField.getCharacters().length() == 0){
            cidField.setTooltip(new Tooltip("Length must be > 0"));
            passwordField.setTooltip(new Tooltip("Length must be > 0"));
            event.consume();
        }else{
            Network.disconnect();
            Network.setCid(cidField.getId());
            Network.setPassword(passwordField.getText());
            event.consume();
        }
        this.hide();
    }

}
