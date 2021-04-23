/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.controller.product;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.JavaFxApplication;
import project.Util;
import project.managers.ProductManager;
import project.model.ProductTR;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;


/**
 * @author Sahil Patel
 */
public class UpdateProductController extends JavaFxApplication implements Initializable {
	/*
	 * This is for the all the FXML 
	 */
    @FXML
    private TextField txtProductID;
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtProductStock;
    @FXML
    private TextField txtProductPrice;
    @FXML
    private DatePicker txtFirstStocked;
    @FXML
    private DatePicker txtLastStocked;

    private ObservableList<ProductTR> productList;

    private int index; 
    
    
    /*
     * This is for the update button in the inventory UI
     * It has some error checking and allows us to change anything we want
     */
    @FXML
    private void updateAction(ActionEvent event) {
        if(!txtProductName.getText().isEmpty() && !txtProductStock.getText().isEmpty()
                && !txtProductPrice.getText().isEmpty()){

            productList.get(index).setProductName(txtProductName.getText());
            productList.get(index).setPrice( new BigDecimal(txtProductPrice.getText()));
            productList.get(index).setStockQuantity(Integer.parseInt(txtProductStock.getText()));
            productList.get(index).setLastModified(Date.valueOf(txtLastStocked.getValue()));

            ProductManager.updateProduct(productList.get(index).toProduct());

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }else{
            errorAlert("Error: Required Field Missing.","Required Field Missing","Please fill in the required field");
        }
    }

    public void addProductList(ObservableList<ProductTR> list) {
        this.productList = list;
    }

    /*
     * This sets the dates to uneditable since it will automatically do it so we cannot change it
     * we set the stock of the product to numbers only and the price to decimals so 9.99 instead of 9
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtProductID.setEditable(false);
        txtFirstStocked.setEditable(false);
        txtLastStocked.setEditable(false);
        txtFirstStocked.setDisable(true);
        txtLastStocked.setDisable(true);
        Util.formatToNumberOnly(txtProductStock);
        Util.formatToDecimalOnly(txtProductPrice);
    }

    public void setIndex(int selectedIndex) {
        this.index = selectedIndex;
    }

    /*
     * Will actually allows us to update the product and set the last restocked date to the current date of when we update
     */
    public void initializeFields(){
        txtProductName.setText(
                productList.get(index).getProductName()
        );
        txtProductID.setText(productList.get(index).getProductID()+"");
        txtProductPrice.setText(productList.get(index).getPrice().toString());
        txtProductStock.setText(productList.get(index).getStockQuantity()+"");
        txtFirstStocked.setValue(productList.get(index).getFirstStocked().toLocalDate());
        txtLastStocked.setValue(LocalDate.now());
    }
}
