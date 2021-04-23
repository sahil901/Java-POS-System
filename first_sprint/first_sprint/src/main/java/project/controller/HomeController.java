package project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.JavaFxApplication;
import project.managers.EmployeeManager;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * @author Sahil Patel
 */
public class HomeController extends JavaFxApplication implements Initializable {
	@FXML
	private HBox hBoxReturn;
	@FXML
	private HBox hBoxSale;
	@FXML
	private VBox vBox;
	/**
     * This is the code that will take the user to the inventory screen once its clicked  
     */
	public void openInventory(ActionEvent event) {
	    try {
	       
	        changeScreen(event, "views/product/Inventory.fxml");
	             
	    } catch(Exception e) {
	        e.printStackTrace();
	        }
	    }
	    
	/**
     * This is the code that will take you to the user screen once its clicked 
     */
	     public void openEmployees(ActionEvent event) {
			try {

				changeScreen(event, "views/employee/Employees.fxml");

			} catch(Exception e) {
				e.printStackTrace();
	        }
	    }

	     /**
	      * This is the code that will take the user to the screen with 3 buttons (Return/Previous Transaction)
	      */
	     public void openBetween(ActionEvent event) {
	    	    try {
	    	       
	    	        changeScreen(event, "views/Between.fxml");
	    	             
	    	    } catch(Exception e) {
	    	        e.printStackTrace();
	    	        }
	    	    }

	     /**
	      * This is the code that will take the user to the report screen
	      */
	     public void openReports(ActionEvent event) {
			try {

				changeScreen(event, "views/Report.fxml");

			} catch(Exception e) {
				e.printStackTrace();
	        }
	    }
	     
	     /**
	      * This is the code that will take the user to the transaction (make sale) Screen
	      */
	      public void openSale(ActionEvent event) {
			try {

				changeScreen(event, "views/transaction/Transaction.fxml");

			} catch(Exception e) {
				e.printStackTrace();
	        }
	    }
	      
	      /**
	       * This is the code for the logout.
	       * It will create a pop up once its clicked and if you say yes it will take you back to the login where you can select exit to terminate 
	       */
	       public void logout(ActionEvent event) {
			try {

				Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
				errorAlert.setTitle("Logout");
				errorAlert.setHeaderText("Are you sure you want to logout ? ");

				ButtonType buttonTypeYes = new ButtonType("Yes");
				ButtonType buttonTypeCancel = new ButtonType("No", ButtonData.CANCEL_CLOSE);

				errorAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

				Optional<ButtonType> result = errorAlert.showAndWait();
			if ( result.isPresent() && result.get() == buttonTypeYes){
					// sets the current employee as null
					EmployeeManager.currentEmployee = null;
					changeScreen(event, "views/login.fxml");
			}  // does nothing if user chose CANCEL or closed the dialog

			} catch(Exception e) {
				e.printStackTrace();
	        }
	    }
	    /**
		 * This will open the return screen where employee can return a previous transaction
		 * */
	public void openReturn(ActionEvent event) {
		try {

			changeScreen(event, "views/refund/Return.fxml");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is the code for the previous return button which is the right button it will take the user to the view to see the returns that have taken place
	 */
	public void openPreviousReturn(ActionEvent event) {
		try {

			changeScreen(event, "views/refund/returnHistory.fxml");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This is the code for the previous transaction button which is the bottom button it will take the user to the view to see the previous transactions that have taken place
	 */
	public void openPreTransactions(ActionEvent event) {
		try {

			changeScreen(event, "views/transaction/Previous.fxml");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Removes buttons from the home screen depending upon the role of the current Employee
	 * */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		switch (EmployeeManager.currentEmployee.getEmployeeRole()){
			case SALES_REP:
				vBox.getChildren().remove(hBoxReturn);
			break;
			case CUSTOMER_REP:
				vBox.getChildren().remove(hBoxSale);
			break;
		}
	}

}