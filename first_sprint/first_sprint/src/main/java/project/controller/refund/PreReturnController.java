package project.controller.refund;

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
import project.entity.Refund;
import project.entity.TransactionItem;
import project.managers.RefundManager;
import project.model.TransactionItemTR;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sahil Patel
 */
public class PreReturnController extends JavaFxApplication implements Initializable {

	/*
	* This is for the back button which will take the user back to the screen with 3 buttons once clicked.
	*/
	@FXML
	public void home(ActionEvent event) {
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
	 private TextField txtRefundId;
	 @FXML
	 private TextField txtRefDate;
	 @FXML
	 private TextField txtSubtotal;
	 @FXML
	 private TextField txtTax;
	 @FXML
	 private TextField txtTotal;
	 
	 
	 // made it product here 
	  @FXML
	  private TableColumn<Product, Integer> tcProductId;
	  @FXML
	  private TableColumn<Product, String> tcProductName;
	  @FXML
	  private TableColumn<Product, BigDecimal> tcProductPrice;
	  @FXML
	  private TableColumn<Product, Integer> tcProductQuantity;


	  
	  ObservableList<TransactionItemTR> listT = FXCollections.observableArrayList();
	  
	  @FXML
	  private TableView<TransactionItemTR> tableView;


	  /*
	   * This is for the search for when we are looking for a refund that has occured. Has not been tested yet since no socket.  
	   */
	public void search(){
		if(!txtRefundId.getText().isEmpty()){
			int refundId = Integer.parseInt(txtRefundId.getText());
			Refund refund = RefundManager.readRefund(refundId);
			if(refund == null){
				txtRefDate.clear();
				txtSubtotal.clear();
				txtTax.clear();
				txtTotal.clear();
				listT.clear();
				errorAlert("Error: Invalid Return Id!","Return Id not found in the database.","Please enter a valid Return Id.");
			}else{
				listT.clear();
				txtRefDate.setText(refund.getReturnDate().toString());
				txtTotal.setText(refund.getTotalReturn().toString());
				txtTax.setText(refund.getTaxAmount().toString());
				txtSubtotal.setText(refund.getSubTotal().toString());
				for(TransactionItem transactionItem: refund.getTransactionItems()){
					listT.add(new TransactionItemTR(transactionItem));
				}
			}
			RefundManager.closeSession();
		}

	}


	@Override
	public void initialize(URL url, ResourceBundle rb) {

		Util.formatToNumberOnly(txtRefundId);
		TextFields.bindAutoCompletion(txtRefundId, RefundManager.readAllRefunds()).setOnAutoCompleted(
				integerAutoCompletionEvent -> search()
		);
		tcProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
		tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
		tcProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		tcProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		tableView.setItems(listT);

	}
}
