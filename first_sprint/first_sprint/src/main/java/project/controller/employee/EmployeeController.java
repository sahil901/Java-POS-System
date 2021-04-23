package project.controller.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.JavaFxApplication;
import project.managers.EmployeeManager;
import project.model.EmployeeRole;
import project.model.EmployeeTR;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * @author Sahil Patel
 */
public class EmployeeController extends JavaFxApplication implements Initializable {
	/*
     * This is the FXML code for the user view 
     */
    @FXML
    private TableView<EmployeeTR> table_view;
    @FXML
    private TableColumn<EmployeeTR, Integer> emp_id;
    @FXML
    private TableColumn<EmployeeTR, String> first_name;
    @FXML
    private TableColumn<EmployeeTR, String> last_name;
    @FXML
    private TableColumn<EmployeeTR, String> emp_role;
    @FXML
    private TableColumn<EmployeeTR, Boolean> emp_status;
    @FXML
    private TableColumn<EmployeeTR, String> emp_pass;
    @FXML
    private TextField searchUser;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnUpdate;

    /*
     * Reads all Employees from the database and then converts them into EmployeeTRs and finally saves in an Observable Array List
     */
    ObservableList<EmployeeTR> list = FXCollections.observableArrayList(
            EmployeeTR.toEmployeeTRList(
                    EmployeeManager.readAllEmployees()
            )
    );
    SortedList<EmployeeTR> sortedData;

    /*
     * This is the FXML code for the home button at the top left. Once this is clicked it will take you back to the main screen. 
     */
    @FXML
    public void home(ActionEvent event) throws Exception {
        changeScreen(event, "views/Home.fxml");
    }

    /*
     * This is for the add user button and its just setting the screen settings for once the button is clicked 
     */
    @FXML
    public void addUserAction() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/employee/addEmployee.fxml"));
        Parent parent = fxmlLoader.load();
        AddEmployeeController dialogController = fxmlLoader.getController();
        dialogController.addUserList(list);

        Scene scene = new Scene(parent, 450, 300);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Add New User");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.showAndWait();
    }

    /*
     * This is for the update user button and its just setting the screen settings for once the button is clicked 
     */
    @FXML
    public void updateUserAction() throws Exception {
        if((EmployeeManager.currentEmployee.getEmployeeRole()==EmployeeRole.MANAGER)){
            if(table_view.getSelectionModel().getSelectedIndex()!=-1){
                update(table_view.getSelectionModel().getSelectedIndex());
            }
        }else{
            for (int index = 0; index < table_view.getItems().size(); index++) {
                if(table_view.getItems().get(index).getEmployeeID() == EmployeeManager.currentEmployee.getEmployeeID()){
                    update(index);
                    return;
                }
            }
        }

    }
    private void update(int index) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/employee/updateEmployee.fxml"));
        Parent parent = fxmlLoader.load();
        UpdateEmployeeController dialogController = fxmlLoader.getController();
        dialogController.setIndex(index);
        dialogController.addUserList(list);
        dialogController.initializeFields();

        Scene scene = new Scene(parent, 450, 300);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Update User");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.showAndWait();
    }

    /*
     * This is for the remove user button and its just setting the screen settings for once the button is clicked 
     */
    @FXML
    public void deleteRow() {
        int visibleIndex = table_view.getSelectionModel().getSelectedIndex();
        if (visibleIndex>-1){
            int sourceIndex = sortedData.getSourceIndexFor(list, visibleIndex);
            EmployeeManager.deleteEmployee(list.get(sourceIndex).toEmployee());
            list.remove(sourceIndex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	/*
         * This is for the table and how it creates the rows  
         */
        emp_id.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        last_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emp_role.setCellValueFactory(new PropertyValueFactory<>("employeeRole"));
        emp_status.setCellValueFactory(new PropertyValueFactory<>("employeeStatus"));
        emp_status.setCellFactory(tc -> new TableCell<>() {

            /*
             * This is for the status we can set it as either online or offline
             */
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item ? "Online" : "Offline");
            }
        });
        emp_pass.setCellValueFactory(new PropertyValueFactory<>("employeePassword"));
        /*emp_pass.setCellFactory(tc -> new TableCell<>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(!isEmpty()){
                    if(getTableRow().getItem()!=null){
                        setText(empty ? null :
                                getTableRow().getItem().getEmployeeID()== EmployeeManager.currentEmployee.getEmployeeID()
                                        ? item : "*****");
                    }
                }
            }
        });*/
        FilteredList<EmployeeTR> filteredData = new FilteredList<>(list, p -> true);

        /*
         * This is for the searching of the user just is for the search textbox
         * It allows us to actually remove the search button but I kept the button in the UI just in case 
         */
        searchUser.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(employeeTR -> {
            // If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            // Compare first name and last name of every person with filter text.
            String lowerCaseFilter = newValue.toLowerCase();
            // Does not match.
            return employeeTR.getFirstName().toLowerCase().contains(lowerCaseFilter);
        }));
        // 3. Wrap the FilteredList in a SortedList. 
        sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table_view.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table_view.setItems(sortedData);

        if (EmployeeManager.currentEmployee.getEmployeeRole()!= EmployeeRole.MANAGER){
            btnAdd.setDisable(true);
            btnRemove.setDisable(true);
            btnUpdate.setText("Update Information");
        }
    }
}
