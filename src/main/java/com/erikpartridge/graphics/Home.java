package com.erikpartridge.graphics;

import com.erikpartridge.aircraft.Aircraft;
import com.erikpartridge.Network;
import com.erikpartridge.general.Weather;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import sun.nio.ch.Net;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Home {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="callsignColumn"
    private TableColumn<Aircraft, String> callsignColumn; // Value injected by FXMLLoader

    @FXML // fx:id="loadAircraftItem"
    private MenuItem loadAircraftItem; // Value injected by FXMLLoader

    @FXML // fx:id="pauseButton"
    private Button pauseButton; // Value injected by FXMLLoader

    @FXML // fx:id="commandFreqItem"
    private MenuItem commandFreqItem; // Value injected by FXMLLoader

    @FXML // fx:id="routeColumn"
    private TableColumn<String, Aircraft> routeColumn; // Value injected by FXMLLoader

    @FXML // fx:id="loadAirportItem"
    private MenuItem loadAirportItem; // Value injected by FXMLLoader

    @FXML // fx:id="connectionButton"
    private Button connectionButton; // Value injected by FXMLLoader

    @FXML // fx:id="windText"
    private Text windText; // Value injected by FXMLLoader

    @FXML // fx:id="weatherItem"
    private MenuItem weatherItem; // Value injected by FXMLLoader

    @FXML // fx:id="mainTable"
    private TableView<Aircraft> mainTable; // Value injected by FXMLLoader

    @FXML // fx:id="latColumn"
    private TableColumn<Double, Aircraft> latColumn; // Value injected by FXMLLoader

    @FXML // fx:id="cheatsheet"
    private MenuItem cheatsheet; // Value injected by FXMLLoader

    @FXML // fx:id="lonColumn"
    private TableColumn<Double, Aircraft> lonColumn; // Value injected by FXMLLoader

    @FXML // fx:id="loadNavItem"
    private MenuItem loadNavItem; // Value injected by FXMLLoader

    @FXML // fx:id="statusColumn"
    private TableColumn<String, Aircraft> statusColumn; // Value injected by FXMLLoader

    @FXML // fx:id="loginItem"
    private MenuItem loginItem; // Value injected by FXMLLoader

    @FXML
    private Text frequency;

    @FXML
    void loadAirport(ActionEvent event) {
        String json = getJson("Choose Airport to Load");
        ScalaUtils.loadAirport(json);
        event.consume();
    }

    private String getJson(String message){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setTitle(message);
        File chosen = fileChooser.showOpenDialog(Main.getStage());
        String json = "";
        try{
            Scanner in = new Scanner(chosen);
            while(in.hasNextLine()){
                json += in.nextLine();
            }
        }catch (IOException e){
            System.err.println("Had trouble loading file:: " + chosen.getAbsolutePath());
        }
        return json;
    }

    @FXML
    void loadAircraft(ActionEvent event) {
        String json = getJson("Choose Aircraft File");
        ScalaUtils.loadAircraft(json);
        event.consume();
    }

    @FXML
    void loadNav(ActionEvent event) {
        String json = getJson("Choose nav data file");
        ScalaUtils.loadNav(json);
        event.consume();
    }

    @FXML
    void commandFreqChange(ActionEvent event) {
        Optional<String> newFrequency = getDialog().title("Command Frequency")
                .masthead("Set Your Command Frequency").showTextInput(Network.commandFrequency());
        event.consume();
        if(newFrequency.isPresent()){
            String freq = newFrequency.get();
            String regex = "\\d{3}\\.\\d{3}";
            if(freq.matches(regex)){
                Network.setCommandFrequency(freq);
            }else{
                getDialog().title("Invalid Command Frequency").message("Must be of form ddd.ddd").showError();
            }
        }
    }

    @FXML
    void openLoginWindow(ActionEvent event) {
        Network.disconnect();
        connectionButton.setText("Connect");
        Optional<String> cid = getDialog().title("CID").masthead("Enter your CID").message("").showTextInput();
        if(cid.isPresent() && cid.get().matches("\\d*")){
            Network.setCid(cid.get());
            Optional<String> password = getDialog().title("Password").masthead("Enter your password").message("Unfortunately not hidden").showTextInput();
            Network.setPassword(password.get());
        }else{
            getDialog().title("Invalid CID").masthead("You entered an invalid CID").showError();
        }
    }

    @FXML
    void editWeather(ActionEvent event) {
        String currentWeather = Weather.weather().toString();
        Dialogs dialog = getDialog().title("Set the weather").masthead("Enter the wind ").message("Must be in form xxx@yy");
        Optional<String> result = dialog.showTextInput(currentWeather);
        String regex = "\\d{1,3}@\\d{1,2}";
        if(result.isPresent()){
            if(result.get().matches(regex) && Integer.parseInt(result.get().split("@")[0]) > -1 && Integer.parseInt(result.get().split("@")[0]) < 361){
                Weather.generateWeather(result.get());
                windText.setText("Wind: " + Weather.getWeather().toString() + " knots");
            }else{
                dialog.showTextInput(currentWeather);
            }
        }
    }

    @FXML
    void showCheatsheet(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Cheatsheet");
        WebView web = new WebView();
        Scene scene = new Scene(web, 600, 400);
        stage.setScene(scene);
        stage.show();
        URL url = getClass().getClassLoader().getResource("cheatsheet.html");
        web.getEngine().load(url.toString());
        event.consume();
    }

    @FXML
    void pause(ActionEvent event) {

    }

    @FXML
    void changeConnection(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert callsignColumn != null : "fx:id=\"callsignColumn\" was not injected: check your FXML file 'home.fxml'.";
        assert loadAircraftItem != null : "fx:id=\"loadAircraftItem\" was not injected: check your FXML file 'home.fxml'.";
        assert pauseButton != null : "fx:id=\"pauseButton\" was not injected: check your FXML file 'home.fxml'.";
        assert commandFreqItem != null : "fx:id=\"commandFreqItem\" was not injected: check your FXML file 'home.fxml'.";
        assert routeColumn != null : "fx:id=\"routeColumn\" was not injected: check your FXML file 'home.fxml'.";
        assert loadAirportItem != null : "fx:id=\"loadAirportItem\" was not injected: check your FXML file 'home.fxml'.";
        assert connectionButton != null : "fx:id=\"connectionButton\" was not injected: check your FXML file 'home.fxml'.";
        assert windText != null : "fx:id=\"windText\" was not injected: check your FXML file 'home.fxml'.";
        assert weatherItem != null : "fx:id=\"weatherItem\" was not injected: check your FXML file 'home.fxml'.";
        assert mainTable != null : "fx:id=\"mainTable\" was not injected: check your FXML file 'home.fxml'.";
        assert latColumn != null : "fx:id=\"latColumn\" was not injected: check your FXML file 'home.fxml'.";
        assert cheatsheet != null : "fx:id=\"cheatsheet\" was not injected: check your FXML file 'home.fxml'.";
        assert lonColumn != null : "fx:id=\"lonColumn\" was not injected: check your FXML file 'home.fxml'.";
        assert loadNavItem != null : "fx:id=\"loadNavItem\" was not injected: check your FXML file 'home.fxml'.";
        assert statusColumn != null : "fx:id=\"statusColumn\" was not injected: check your FXML file 'home.fxml'.";
        assert loginItem != null : "fx:id=\"loginItem\" was not injected: check your FXML file 'home.fxml'.";
        windText.setText("Wind: " + Weather.getWeather().toString() + " knots");
    }

    private Dialogs getDialog(){
        return Dialogs.create().owner(Main.getStage());
    }

}
