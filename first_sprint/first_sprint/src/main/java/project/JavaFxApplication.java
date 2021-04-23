package project;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Sahil Patel
 */
public class JavaFxApplication extends Application {
	 /**
	  * This is for the start where once the user starts the project it will take them to the login screen
	  * The setOnCloseRequest is there so it will not close the project it will terminate it which will avoid it from running in the background.
	  */
    @Override
    public void start(Stage primaryStage) {

        try {
            Platform.setImplicitExit(false);

            Parent par = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")));

            Scene scene = new Scene(par, 438, 640);

            primaryStage.setResizable(false);
            primaryStage.setTitle("App");
            primaryStage.initStyle(StageStyle.DECORATED);

            primaryStage.setOnCloseRequest(windowEvent -> System.exit(0)); 
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * This is the code for changing screens so it will take the user to any different view
     * It has code for the login since the login dimensions are different
     * This is the action event
     */
    public void changeScreen(ActionEvent actionEvent, String FXML) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/" + FXML)));

        Scene scene = new Scene(parent);
        Stage stage;

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if (!FXML.equals("views/login.fxml")) {
            stage.setResizable(true);
        }

        stage.setScene(scene);
        stage.show();
    }

    /**
     * This is the code for changing screens so it will take the user to any different view
     * It has code for the login since the login dimensions are different
     * This is the mouse event
     */
    public void changeScreen(MouseEvent actionEvent, String FXML) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/" + FXML)));

        Scene scene = new Scene(parent);
        Stage stage;

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        if (!FXML.equals("views/login.fxml")) {
            stage.setResizable(true);
        }

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Displays an error alert to the user
     * @param title it is the title of the error
     * @param head the header of the error
     * @param massage the message of the error
     */
    protected void errorAlert(String title, String head, String massage) {

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText(head);
        errorAlert.setContentText(massage);
        errorAlert.showAndWait();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
