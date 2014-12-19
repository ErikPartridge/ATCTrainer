package com.erikpartridge.graphics;

import com.erikpartridge.aircraft.Aircraft;
import com.erikpartridge.general.SLatLng;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import scala.collection.JavaConversions;

import java.net.URL;

public class Main extends Application {

    private static Stage stage;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        URL is = getClass().getClassLoader().getResource("fxml/home.fxml");
        assert is != null;
        Parent root = FXMLLoader.load(is);
        Scene scene = new Scene(root, 600, 520);
        stage.setTitle("ATC Trainer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @return the stage
     */
    public static Stage getStage(){
        return stage;
    }

    public static void createErrorMessage(String message){
        Dialogs.create().owner(stage).title("Error").masthead(null).message(message).showError();
    }
}
