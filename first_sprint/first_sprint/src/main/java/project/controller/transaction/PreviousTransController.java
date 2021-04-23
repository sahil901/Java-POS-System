package project.controller.transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import project.JavaFxApplication;
import project.Util;
import project.entity.Product;
import project.entity.Transaction;
import project.entity.TransactionItem;
import project.managers.TransactionManager;
import project.model.TransactionItemTR;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sahil Patel
 */
public class PreviousTransController extends JavaFxApplication implements Initializable {

	/*
	* This is for the back button which will take the user back to the screen with 3 buttons once clicked.
	*/
	@FXML
	public void home(ActionEvent event) throws Exception {
		try {

			changeScreen(event, "views/Home.fxml");

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * These are all the FXML elements
	 */
	@FXML
	private TextField txtTransId;
	@FXML
	private TextField txtTransDate;
	@FXML
	private TextField txtSubtotal;
	@FXML
	private TextField txtTax;
	@FXML
	private TextField txtTotal;

	  
	// made it product here
	@FXML
	private TableColumn<Product, Integer> product_id;
	@FXML
	private TableColumn<Product, String> product_name;
	@FXML
	private TableColumn<Product, BigDecimal> product_price;
	@FXML
	private TableColumn<Product, Integer> product_quantity;


	ObservableList<TransactionItemTR> listT = FXCollections.observableArrayList();

	@FXML
	private TableView<TransactionItemTR> tableView;

	/*
	* This is for the search for when we are looking for a transaction that has occured. Has not been tested yet since no socket.
	*/
	public void search(){
		if(!txtTransId.getText().isEmpty()){
			int id = Integer.parseInt(txtTransId.getText());
			Transaction transaction = TransactionManager.readTransaction(id);
			if(transaction == null){
				txtTransDate.clear();
				txtSubtotal.clear();
				txtTax.clear();
				txtTotal.clear();
				listT.clear();

				errorAlert("Error: Invalid Transaction Id!","Transaction Id not found in the database.","Please enter a valid Transaction Id.");
			}else {
				txtTransDate.setText(transaction.getDate().toString());
				txtSubtotal.setText(transaction.getSubTotal().toString());
				txtTax.setText(transaction.getTaxAmount().toString());
				txtTotal.setText(transaction.getTotalPrice().toString());
				listT.clear();

				for (TransactionItem item: transaction.getTransactionItems()){
					listT.add(new TransactionItemTR(item));
				}
				TransactionManager.closeSession();
			}
		}
	}


	  
	  
	  
	/**
	* Initializes the controller class.
	*/
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		Util.formatToNumberOnly(txtTransId);
		TextFields.bindAutoCompletion(txtTransId, TransactionManager.readAllTransactions()).setOnAutoCompleted(
				integerAutoCompletionEvent -> search()
		);

		product_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
		product_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
		product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
		product_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		tableView.setItems(listT);

	}
	
	
}
