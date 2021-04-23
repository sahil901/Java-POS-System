package project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import project.JavaFxApplication;
import project.Util;
import project.entity.Employee;
import project.managers.EmployeeManager;
import project.model.EmployeeRole;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Sahil Patel
 */
public class LoginController extends JavaFxApplication implements Initializable {

   /**
    * This is for the login fxml elements
    */
    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    /**
     *This is the code for the sign in button so once they log in it will take them to the home screen
     * Otherwise if the user and password is not correct as the hard coded value it will make a pop up
     */
    public void signIn(ActionEvent event) {
        try {

            String pass;
            int employeeId;
            employeeId = Integer.parseInt(txtName.getText());
            pass = txtPassword.getText();

            if (EmployeeManager.checkCredentials(employeeId,pass)) {
                EmployeeManager.changeEmployeeStatus(EmployeeManager.currentEmployee);
                changeScreen(event, "views/Home.fxml");
            } else {
                errorAlert("Login Error", "Provided User or Password is incorrect", "Please Enter Valid Username And Password");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Once the user clicks the exit button it will have a pop up where you can confirm weather or not if you want to terminate or not
    */
    public void exit() {
        try {

            Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
            errorAlert.setTitle("Logout");
            errorAlert.setHeaderText("Are you sure you want to exit?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeCancel = new ButtonType("No", ButtonData.CANCEL_CLOSE);

            errorAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

            Optional<ButtonType> result = errorAlert.showAndWait();
            if ( result.isPresent() && result.get() == buttonTypeYes) {
                System.exit(0);
            }
            // do nothing if user chose CANCEL or closed the dialog


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the LoginController class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        * Checks if the Employee table is empty, if it is empty adds a dummy employee
        * */
        if(EmployeeManager.isEmpty()){
            EmployeeManager.saveEmployee(new Employee("Sahil","Patel", EmployeeRole.MANAGER,true,"111"));
        }
        /*
        * Set the product to only allow numbers (so no letters will be allowed)
        * */
        Util.formatToNumberOnly(txtName);
    	 /*
    	  * This code is for having the username and password so i don't have to type it in every single time. For hardcoded/testing purposes
     	  */
        txtName.setText("1");
        txtPassword.setText("111");
    }

}
