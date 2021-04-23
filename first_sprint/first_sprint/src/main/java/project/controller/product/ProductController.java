package project.controller.product;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.JavaFxApplication;
import project.entity.Product;
import project.managers.ProductManager;
import project.model.ProductTR;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * @author Sahil Patel
 */
public class ProductController extends JavaFxApplication implements Initializable {

  /*
   * This is where I'm declaring all the FXML and table values
   */
    @FXML
    private TableView<ProductTR> table_view;
    @FXML
    private TableColumn<ProductTR, Integer> tcProductId;
    @FXML
    private TableColumn<ProductTR, String> tcProductName;
    @FXML
    private TableColumn<ProductTR, BigDecimal> tcProductPrice;
    @FXML
    private TableColumn<ProductTR, Integer> tcProductQuantity;
    @FXML
    private TableColumn<ProductTR, Date> tcFirstStock;
    @FXML
    private TableColumn<ProductTR, Date> tcLastModification;
    @FXML
    private TextField searchField;

    /*
     * Reads all Products from the database and then converts them into EmployeeTRs and finally saves in an Observable Array List
     */
    ObservableList<ProductTR> list = FXCollections.observableArrayList(
            ProductTR.toProductTRList(
                    ProductManager.readAllProducts()
            )
    );
    SortedList<ProductTR> sortedData;

    /*
     * This is the FXML code for the home button at the top left. Once this is clicked it will take you back to the main screen. 
     */
    @FXML
    public void Home(ActionEvent event) throws Exception {
        try {
            changeScreen(event, "views/Home.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * This is for the add product button and its just setting the screen settings for once the button is clicked 
     */
    @FXML
    public void addProductAction() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/product/addProduct.fxml"));
        Parent parent = fxmlLoader.load();
        AddProductController dialogController = fxmlLoader.getController();
        dialogController.addProductList(list);

        Scene scene = new Scene(parent, 450, 300);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Add New ProductTR");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.showAndWait();
        table_view.refresh();
    }

    /*
     * This is for the update product button and its just setting the screen settings for once the button is clicked 
     */
    @FXML
    public void updateProductAction() throws Exception {
        if(table_view.getSelectionModel().getSelectedIndex()!=-1){  
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/product/updateProduct.fxml"));
            Parent parent = fxmlLoader.load();
            UpdateProductController dialogController = fxmlLoader.getController();
            dialogController.addProductList(list);
            dialogController.setIndex(table_view.getSelectionModel().getSelectedIndex()); 
            dialogController.initializeFields();
            Scene scene = new Scene(parent, 450, 300);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Update ProductTR");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
        }
    }

    /*
     * This is for the remove user button and its just setting the screen settings for once the button is clicked 
     */
    @FXML
    public void DeleteRow() {
        int visibleIndex = table_view.getSelectionModel().getSelectedIndex();
        if (visibleIndex>-1){
            int sourceIndex = sortedData.getSourceIndexFor(list, visibleIndex);
            Product product = list.get(sourceIndex).toProduct();
            product.setVisible(false);
            product.setStockQuantity(0);
            product.setLastModified(Date.valueOf(LocalDate.now()));
            ProductManager.updateProduct(product);
            list.remove(sourceIndex);
        }

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
   
  
    	/*
         * This is for the table and how it creates the rows  
         */
        tcProductId.setCellValueFactory(new PropertyValueFactory<>("productID"));
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcProductQuantity.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        tcFirstStock.setCellValueFactory(new PropertyValueFactory<>("firstStocked"));
        tcLastModification.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        
        FilteredList<ProductTR> filteredData = new FilteredList<>(list, p -> true);
        
        /*
         * This is for the searching of the user just is for the search textbox
         * It allows us to actually remove the search button but I kept the button in the UI just in case 
         */
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(productTR -> {
            // If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            // Compare first name and last name of every person with filter text.
            String lowerCaseFilter = newValue.toLowerCase();
            // Does not match.
            return productTR.getProductName().toLowerCase().contains(lowerCaseFilter);
        }));
        // 3. Wrap the FilteredList in a SortedList. 
        sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table_view.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table_view.setItems(sortedData);

    }

}
