package project.controller.transaction;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import project.JavaFxApplication;
import project.entity.Employee;
import project.entity.Product;
import project.entity.Transaction;
import project.entity.TransactionItem;
import project.managers.EmployeeManager;
import project.managers.ProductManager;
import project.managers.TransactionManager;
import project.model.TransactionItemTR;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
//import javafxapplication.JavaFxApplication;
//import javafxapplication.SaleModel;

/**
 * @author Sahil Patel
 */
public class TransactionController extends JavaFxApplication implements Initializable {

    /*
    * This is the FXML code
    */
    @FXML
    private Button btnReceipt;
    @FXML
    private Button btnDeleteRow;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnConfirm;
    @FXML
    private TextField txtTransId;
    @FXML
    private TextField txtTransDate;
    @FXML
    private TextField txtEmployee;
    @FXML
    private ComboBox<Product> cbxProducts;
    @FXML
    // private TextField p_quantity;
    private Spinner<Integer> spinnerQuantity;
    @FXML
    private TextField txtSubTotal;
    @FXML
    private TextField txtTax;
    @FXML
    private TextField txtTotal;
    @FXML
    private TextArea txtBill;

    @FXML
    private TableColumn<TransactionItemTR, Integer> col_product_id;
    @FXML
    private TableColumn<TransactionItemTR, String> col_product_name;
    @FXML
    private TableColumn<TransactionItemTR, Integer> col_price;
    @FXML
    private TableColumn<TransactionItemTR, Integer> col_quantity;
    @FXML
    private TableColumn<TransactionItemTR, Integer> col_tax;
    @FXML
    private TableColumn<TransactionItemTR, Integer> col_total;

    ObservableList<TransactionItemTR> listT = FXCollections.observableArrayList();

    private Transaction transaction;
    private Product currentProduct = null;
    @FXML
    private TableView<TransactionItemTR> tableView;

    /**
     * Redirects back to the home screen
     */
    @FXML
    public void home(ActionEvent event) throws Exception {
        changeScreen(event, "views/Home.fxml");
    }

    /**
     * Uses the currentProduct and quantity to create TransactionItem and then saves that in the tableView and the transaction object
     */
    public void add() {
        if(currentProduct!=null && spinnerQuantity.getValue()>0){
            TransactionItem transactionItem = new TransactionItem(currentProduct, spinnerQuantity.getValue());
            for (TransactionItem item: transaction.getTransactionItems()){
                if(item.getProduct().equals(transactionItem.getProduct())){
                    transaction.removeTransactionItem(item);
                    transactionItem = transactionItem.add(item);
                    break;
                }
            }
            currentProduct.setStockQuantity(currentProduct.getStockQuantity()-spinnerQuantity.getValue());
            onProductChanged(true);
            transaction.addTransactionItem(transactionItem);
            for(TransactionItemTR transactionItemTR: listT){
                if(transactionItemTR.getTransactionItem().getProduct().equals(transactionItem.getProduct())){
                    listT.remove(transactionItemTR);
                    break;
                }
            }
            listT.add(new TransactionItemTR(transactionItem));
        }
    }

    /**
     * Removes the product from the tableView and also removes the TransactionItem from Transaction. It also resets the stock quantity of the prodduct
     */
    @FXML
    public void deleteRow() {
        if (tableView.getSelectionModel().getSelectedIndex()>=0){
            TransactionItem transactionItem = tableView.getSelectionModel()
                    .getSelectedItem().getTransactionItem();
            transaction.removeTransactionItem(transactionItem);
            tableView.getItems().remove(
                    tableView.getSelectionModel().getSelectedItem()
            );
            if(currentProduct!=null && currentProduct.equals(transactionItem.getProduct())){
                currentProduct.setStockQuantity(currentProduct.getStockQuantity()+transactionItem.getProductQuantity());
                onProductChanged(true);
            }else{
                for (TransactionItem item: transaction.getTransactionItems()){
                    if(item.getProduct().equals(transactionItem.getProduct())){
                        item.getProduct().setStockQuantity(
                                item.getProduct().getStockQuantity()
                                +transactionItem.getProductQuantity()
                        );
                        return;
                    }
                }
            }
        }
    }
    
    /**
     * This is for the bill that i have on the right hand size. 
     * I set how it looks here and then get the values from the textField and display it
     * still needs work since i cant do it all without the socket
     */
    public void bill()
    {
    	txtBill.setText("");
    	txtBill.setText(txtBill.getText() + "  TECH CATALYST                \n");
    	txtBill.setText(txtBill.getText() + "  Address: 3278 Gateway Blvd, Toronto, ON T6H 1J5    \n");
        txtBill.setText(txtBill.getText() + "  Phone Number: 416-636-0787          \n");
        txtBill.setText(txtBill.getText() + "  Transaction done by: "+txtEmployee.getText()+"\n");
    	txtBill.setText(txtBill.getText() + "  **********************************************************   \n");
    	txtBill.setText(txtBill.getText() + " Transaction ID:        " + "\t" + "        Transaction Date:       " + "\n");
    	txtBill.setText(txtBill.getText() + "\t" + txtTransId.getText() + "\t" + "\t" + "\t" + "\t" + txtTransDate.getText() +"\n");
    	txtBill.setText(txtBill.getText() + "  **********************************************************   \n");
        txtBill.setText(txtBill.getText() + "  Product ID:    Product Name:    Quantity:    Price($):  \n");
        for (TransactionItemTR transactionItemTR: tableView.getItems()){
            txtBill.setText(txtBill.getText() +String.format("  %-17d%-19s%-16d%s",transactionItemTR.getProductId(),transactionItemTR.getProductName(),transactionItemTR.getQuantity(),transactionItemTR.getPrice().toString())+"\n");
            
        }
    	txtBill.setText(txtBill.getText() + "  **********************************************************   \n");
    	txtBill.setText(txtBill.getText() + " SubTotal($):            " + "       Tax($):         " + "      Total($):      " + "\n");
    	txtBill.setText(txtBill.getText() + txtSubTotal.getText() + "\t" + "\t" + "\t" + txtTax.getText() + "\t" + "\t"  + "\t"  + "\t" + txtTotal.getText() + "\n"     );
    	txtBill.setText(txtBill.getText() + "  **********************************************************   \n");
    	txtBill.setText(txtBill.getText() + " Thank You for Shopping With Us!   \n");
    	txtBill.setText(txtBill.getText() + " Please Come Again   \n");
    	
    }
    
    /**
     * Asks the user for confirmation of saving the transaction in the database, if the user click Yes, then it saves the transaction to database. It also displays the transactionID and disables all buttons except bill button.
     */
    public void confirm(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to save the transaction in Database?",ButtonType.YES,ButtonType.NO);
        alert.initOwner(((Node) event.getSource()).getScene().getWindow());
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            btnAdd.setDisable(true);
            btnDeleteRow.setDisable(true);
            cbxProducts.setValue(null);
            cbxProducts.setDisable(true);
            currentProduct = null;
            btnConfirm.setDisable(true);
            btnReceipt.setDisable(false);
            spinnerQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,0,0));
            TransactionManager.saveTransaction(transaction);
            txtTransId.setText(transaction.getTransactionId()+"");

        }
    }

    /**
     * Here we set some things uneditable such as the date of the transaction and the bill
     * A listener was set for the items in the table so once we add more the subtotal, tax and total would change automatically as more stuff gets added/removed
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Employee employee = EmployeeManager.currentEmployee;
        if(employee!=null){
            txtEmployee.setText(employee.getName());
        }

        LocalDate date = LocalDate.now();
        txtTransDate.setText(date.toString());
        transaction = new Transaction(employee, Date.valueOf(date));

        cbxProducts.getItems().addAll(
                ProductManager.readAllProducts()
        );

        // Uses controlsFX to add autocompletion to the ComboBox
        TextFields.bindAutoCompletion(cbxProducts.getEditor(),cbxProducts.getItems()).setOnAutoCompleted(productAutoCompletionEvent -> onProductChanged(false));
        cbxProducts.setOnAction(actionEvent -> onProductChanged(false));

        txtTransId.setEditable(false);
        txtEmployee.setEditable(false);
        txtTransDate.setEditable(false);
        txtTax.setEditable(false);
        txtTotal.setEditable(false);
        txtSubTotal.setEditable(false);

        col_product_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
        col_product_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_tax.setCellValueFactory(new PropertyValueFactory<>("tax"));
        col_total.setCellValueFactory(new PropertyValueFactory<>("total"));

        tableView.setItems(listT);

        listT.addListener((ListChangeListener<TransactionItemTR>) change -> {
            BigDecimal subTotal=BigDecimal.ZERO;
            BigDecimal tax = BigDecimal.ZERO;
            BigDecimal total;
            for(TransactionItemTR transactionItemTR: change.getList()){
                subTotal= subTotal.add(transactionItemTR.getPrice());
                tax = tax.add(transactionItemTR.getTax());
            }
            total = tax.add(subTotal);
            txtSubTotal.setText(subTotal+"");
            txtTax.setText(tax+"");
            txtTotal.setText(total+"");
        });

        this.spinnerQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));

        spinnerQuantity.valueProperty().addListener((observableValue, oldValue, newValue) -> btnAdd.setDisable(newValue <= 0));

        spinnerQuantity.setEditable(true);
        txtBill.setEditable(false);
        btnAdd.setDisable(true);
        btnReceipt.setDisable(true);

    }

    /**
     * Whenever a product is changed in the combo box, the spinner max value is reset
     * */
    private void onProductChanged(boolean implicitCall){
        if((cbxProducts.getValue() != null && cbxProducts.getValue() instanceof Product) || implicitCall){
            if (!implicitCall){
                currentProduct = cbxProducts.getValue();
            }
            spinnerQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,currentProduct.getStockQuantity(),0));
        }
    }
}
