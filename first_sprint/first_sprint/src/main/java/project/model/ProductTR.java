package project.model;

import javafx.beans.property.*;
import project.entity.Product;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sahil Patel
 */
public class ProductTR {

	/**
	 * Specific for Table row (TR)
	 * This class if added for the pop-ups for both the add and update.
	 * You need this since without it, just using the java classes was not enough 
	 */
    private final IntegerProperty productID = new SimpleIntegerProperty();
    private final StringProperty productName = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> price = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final IntegerProperty stockQuantity = new SimpleIntegerProperty();
    private final ObjectProperty<Date> firstStocked = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> lastModified = new SimpleObjectProperty<>();
    /**
     * Creates an object of ProductTR from an object of Product
     * */
    public ProductTR(Product product){
        setProductID(product.getProductID());
        setProductName(product.getProductName());
        setPrice(product.getPrice());
        setStockQuantity(product.getStockQuantity());
        setFirstStocked(product.getFirstStocked());
        setLastModified(product.getLastModified());
    }

    public int getProductID() {
        return productID.get();
    }

    public IntegerProperty productIDProperty() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productIDProperty().set(productID);
    }

    public String getProductName() {
        return productNameProperty().get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productNameProperty().set(productName);
    }

    public BigDecimal getPrice() {
        return priceProperty().get();
    }

    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.priceProperty().set(price);
    }

    public int getStockQuantity() {
        return stockQuantityProperty().get();
    }

    public IntegerProperty stockQuantityProperty() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantityProperty().set(stockQuantity);
    }

    public Date getFirstStocked() {
        return firstStockedProperty().get();
    }

    public ObjectProperty<Date> firstStockedProperty() {
        return firstStocked;
    }

    public void setFirstStocked(Date firstStocked) {
        this.firstStockedProperty().set(firstStocked);
    }

    public Date getLastModified() {
        return lastModifiedProperty().get();
    }

    public ObjectProperty<Date> lastModifiedProperty() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModifiedProperty().set(lastModified);
    }

    /**
     * Converts ProductTR object to Product object
     * */
    public Product toProduct(){
        Product product = new Product(getProductName(),getPrice(),getStockQuantity());
        product.setFirstStocked(getFirstStocked());
        product.setLastModified(getLastModified());
        product.setProductID(getProductID());
        return product;
    }
    /**
     * Converts a list of Products to a list of ProductTRs
     * */
    public static List<ProductTR> toProductTRList(List<Product> products){
        return products.stream().map(ProductTR::new).collect(Collectors.toList());
    }
}
