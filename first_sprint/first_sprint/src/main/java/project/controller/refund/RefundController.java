package project.controller.refund;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import project.JavaFxApplication;
import project.Util;
import project.entity.Refund;
import project.entity.Transaction;
import project.entity.TransactionItem;
import project.managers.RefundManager;
import project.managers.TransactionManager;
import project.model.TransactionItemTR;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * @author Sahil Patel
 */
public class RefundController extends JavaFxApplication implements Initializable {
    /*
     * These are all the FXML elements 
     */
    @FXML
    private Button btnReturn;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txtRefundId;
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
    @FXML
    private TextField txtReturnSubtotal;
    @FXML
    private TextField txtReturnTax;
    @FXML
    private TextField txtReturnTotal;

    @FXML
    private TableColumn<TransactionItemTR, Integer> tcProductId;
    @FXML
    private TableColumn<TransactionItemTR, String> tcProductName;
    @FXML
    private TableColumn<TransactionItemTR, BigDecimal> tcProductPrice;
    @FXML
    private TableColumn<TransactionItemTR, Integer> tcProductQuantity;
    @FXML
    private TableColumn<TransactionItemTR, Boolean> tcReturn;

    ObservableList<TransactionItemTR> listT = FXCollections.observableArrayList();

    @FXML
    private TableView<TransactionItemTR> tableView;

    private Refund refund = null;


    /*
     * This is for the back button which will take the user back to the screen with 3 buttons once clicked.
     */
    public void home(ActionEvent event) throws Exception {
        try {

            changeScreen(event, "views/Home.fxml");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returnSelect() {
        if(tableView.getSelectionModel().getSelectedIndex()!=-1  && refund!=null){
            TransactionItemTR item = tableView.getSelectionModel().getSelectedItem();
            TransactionItem transactionItem = item.getTransactionItem();
            if(!refund.getTransactionItems().contains(transactionItem) && !item.isRefunded()){
                refund.addTransactionItem(transactionItem);
                item.setRefunded(true);
                txtReturnSubtotal.setText(refund.getSubTotal().toString());
                txtReturnTax.setText(refund.getTaxAmount().toString());
                txtReturnTotal.setText(refund.getTotalReturn().toString());
            }
        }
    }

    /*
     * confirming the return this is kind of like where the socket part would go i assume
     */
    public void confirmReturn(ActionEvent actionEvent) {
        if(refund!=null && !refund.getTransactionItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to save the transaction in Database?",ButtonType.YES,ButtonType.NO);
            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES){
                btnSearch.setDisable(true);
                btnReturn.setDisable(true);
                btnConfirm.setDisable(true);
                for (TransactionItem item: refund.getTransactionItems()){
                    item.getProduct().setStockQuantity(item.getProduct().getStockQuantity()+ item.getProductQuantity());
                }
                RefundManager.saveRefund(refund);
                txtRefundId.setText(refund.getRefundId()+"");

            }
        }else if(refund!=null && refund.getTransactionItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR,"No items have been selected for refund.");
            alert.setTitle("Error: No Items Selected");
            alert.showAndWait();
        }
    }

    /*
     * This is for the search for when we are looking for a refund that has occured. Has not been tested yet since no socket.
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

                refund = new Refund(transaction, Date.valueOf(LocalDate.now()));
            }
            TransactionManager.closeSession();
        }
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        txtReturnSubtotal.setText("0");
        txtReturnTax.setText("0");
        txtReturnTotal.setText("0");
        txtSubtotal.setText("0");
        txtTax.setText("0");
        txtTotal.setText("0");

        tcProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tcReturn.setCellValueFactory(new PropertyValueFactory<>("refunded"));
        tcReturn.setCellFactory(param->new TableCell<>(){
            @Override
            protected void updateItem(Boolean aBoolean, boolean b) {
                super.updateItem(aBoolean, b);
                if (!isEmpty() && aBoolean!=null){
                    setText(aBoolean?"RETURNING":"");
                    setStyle("-fx-text-fill: red");
                }
            }
        });
        Util.formatToNumberOnly(txtTransId);
        TextFields.bindAutoCompletion(txtTransId, TransactionManager.readAllNonRefundedTransactions()).setOnAutoCompleted(
                integerAutoCompletionEvent -> search()
        );

        tableView.setItems(listT);
    }

}
