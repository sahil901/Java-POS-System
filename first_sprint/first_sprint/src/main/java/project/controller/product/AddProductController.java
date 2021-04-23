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
import project.entity.Product;
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
public class AddProductController extends JavaFxApplication implements Initializable {
    /*
     * This is the FXML code for the add product pop-up that will happen inside of the inventory view once you click the button
     */
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtProductStock;
    @FXML
    private TextField txtProductPrice;
    @FXML
    private DatePicker dpFirstStocked;
    @FXML
    private DatePicker dpLastStocked;

    private ObservableList<ProductTR> productList;

    @FXML
    private void saveAction(ActionEvent event) {
        /*
         * Once you type all the information into the text fields and save it will add to the table below
         * or else it will send a message alert to fill out all the fields to save. 
         */
    	
        if(!txtProductName.getText().isEmpty() && !txtProductStock.getText().isEmpty()
            && !txtProductPrice.getText().isEmpty() && dpFirstStocked.getValue()!=null && dpLastStocked.getValue()!=null){

            Product product = new Product(txtProductName.getText(), new BigDecimal(txtProductPrice.getText()),Integer.parseInt(txtProductStock.getText()));
            product.setFirstStocked(Date.valueOf(dpFirstStocked.getValue()));
            product.setLastModified(Date.valueOf(dpLastStocked.getValue()));

            ProductManager.saveProduct(product);

            productList.add(product.toProductTR());

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }else{
            errorAlert("Error: Required Field Missing.","Required Field Missing","Please fill in all the required fields!");
        }
    }

    /*
     * This will add the product into the list aka table and display it
     */
    public void addProductList(ObservableList<ProductTR> list) {
        this.productList = list;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	 /*
         * Here we initialize some important elements
         * The dates are going to be set to the current date so that we don't need user input for it 
         * I made them un-editable since the current date will always be correct 
         * Set the product to only allow numbers (so no letters will be allowed)
         * Set the price to have decimals only so even if its $9 its going to take a $9.00 value 
         */
        dpFirstStocked.setValue(LocalDate.now());
        dpLastStocked.setValue(LocalDate.now());
        dpFirstStocked.setDisable(true);
        dpLastStocked.setDisable(true);
        Util.formatToNumberOnly(txtProductStock);
        Util.formatToDecimalOnly(txtProductPrice);
    }

}
