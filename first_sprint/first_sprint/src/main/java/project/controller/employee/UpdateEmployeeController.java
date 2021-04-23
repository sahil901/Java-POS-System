/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controller.employee;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.JavaFxApplication;
import project.Util;
import project.managers.EmployeeManager;
import project.model.EmployeeRole;
import project.model.EmployeeTR;

import java.net.URL;
import java.util.ResourceBundle;



/**
 * @author Sahil Patel
 */
public class UpdateEmployeeController extends JavaFxApplication implements Initializable {
    /*
     * This is for all the FXML code
     */
    @FXML
    private TextField txtEmployeeID;
    @FXML
    private TextField txtFName;
    @FXML
    private TextField txtLName;
    @FXML
    private ComboBox<EmployeeRole> cbxRole;
    @FXML
    private CheckBox cbStatus;
    @FXML
    private TextField txtPassword;
    private int index;    


    private ObservableList<EmployeeTR> employeeList;

    /**
     * This is for the update button in the employee UI
     * It has some error checking and allows us to change anything we want in the Employee details except Id
     */
    @FXML
    private void updateAction(ActionEvent event) {
        if(!txtFName.getText().isEmpty() && !txtLName.getText().isEmpty() && cbxRole.getValue()!=null && cbxRole.getValue()!=null  && !txtPassword.getText().isEmpty()){

            employeeList.get(index).setFirstName(txtFName.getText());
            employeeList.get(index).setLastName(txtLName.getText());
            employeeList.get(index).setEmployeeRole(cbxRole.getValue());
            employeeList.get(index).setEmployeeStatus(cbStatus.isSelected());
            if(EmployeeManager.currentEmployee.getEmployeeRole()!=EmployeeRole.MANAGER ||
                    EmployeeManager.currentEmployee.getEmployeeID()==employeeList.get(index).getEmployeeID()){
                employeeList.get(index).setEmployeePassword(txtPassword.getText());
            }

            EmployeeManager.updateEmployee(
                    employeeList.get(index).toEmployee()
            );
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }else{
            errorAlert("Error: Required Field Missing.","Required Field Missing","Please fill in the required field");
        }
    }

    public void addUserList(ObservableList<EmployeeTR> list) {
        this.employeeList = list;
    }

    /**
     * Restricts us from changing the employee id and status and initializes UI
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbStatus.setDisable(true);
        txtEmployeeID.setEditable(false);
        cbxRole.getItems().addAll(EmployeeRole.values());
        if (EmployeeManager.currentEmployee.getEmployeeRole()!= EmployeeRole.MANAGER){
            cbxRole.setDisable(true);
        }
    }

    public void setIndex(int selectedIndex) {
        this.index = selectedIndex;
    }

    /**
     * Will actually allows us to update the user and make sure that the first name and last name can only take letters and not numbers
     *
     * Runs after UI has been initialized and initializes values of employee fields
     */
    public void initializeFields(){
        txtEmployeeID.setText(employeeList.get(index).getEmployeeID()+"");
        txtFName.setText(employeeList.get(index).getFirstName());
        txtLName.setText(employeeList.get(index).getLastName());
        cbxRole.setValue(employeeList.get(index).getEmployeeRole());
        cbStatus.setSelected(employeeList.get(index).isEmployeeStatus());
        txtPassword.setText(employeeList.get(index).getEmployeePassword());
        if(EmployeeManager.currentEmployee.getEmployeeRole()==EmployeeRole.MANAGER &&
                EmployeeManager.currentEmployee.getEmployeeID()!=employeeList.get(index).getEmployeeID()){
            txtPassword.setEditable(false);
        }
        if(EmployeeManager.currentEmployee.getEmployeeRole()!=EmployeeRole.MANAGER){
            cbxRole.setDisable(true);
        }
        Util.formatToAlphabetOnly(txtFName);
        Util.formatToAlphabetOnly(txtLName);

    }
}
