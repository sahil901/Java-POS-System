package project.model;

import javafx.beans.property.*;
import project.entity.TransactionItem;

import java.math.BigDecimal;

/**
 * @author Sahil Patel
 * Specific for Table row (TR)
 * This class if added for the table row.
 * You need this since without it, just using the java classes was not enough 
 */
public class TransactionItemTR {
    private final IntegerProperty quantity = new SimpleIntegerProperty();

    private final IntegerProperty productId = new SimpleIntegerProperty();
    private final StringProperty productName = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> price = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ObjectProperty<BigDecimal> tax = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ObjectProperty<BigDecimal> total = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final BooleanProperty refunded = new SimpleBooleanProperty(false);

    private TransactionItem transactionItem;
    /**
     * Creates an object of TransactionItemTR from a TransactionItem object
     * */
    public TransactionItemTR(TransactionItem transactionItem) {
        setTransactionItem(transactionItem);
        setQuantity(transactionItem.getProductQuantity());
        setProductId(transactionItem.getProduct().getProductID());
        setProductName(transactionItem.getProduct().getProductName());
        setPrice(transactionItem.getProduct().getPrice().multiply(BigDecimal.valueOf(getQuantity())));
        setRefunded(transactionItem.getTransaction().isRefunded());
        setTax(getPrice().multiply(BigDecimal.valueOf(0.13)));
        setTotal(getPrice().add(getTax()));
    }

    public TransactionItem getTransactionItem() {
        return transactionItem;
    }

    public void setTransactionItem(TransactionItem transactionItem) {
        this.transactionItem = transactionItem;
    }

    public int getProductId() {
        return productId.get();
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productIdProperty().set(productId);
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

    public int getQuantity() {
        return quantityProperty().get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantityProperty().set(quantity);
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

    public BigDecimal getTax() {
        return taxProperty().get();
    }

    public ObjectProperty<BigDecimal> taxProperty() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.taxProperty().set(tax);
    }

    public BigDecimal getTotal() {
        return totalProperty().get();
    }

    public ObjectProperty<BigDecimal> totalProperty() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.totalProperty().set(total);
    }

    public boolean isRefunded() {
        return refundedProperty().get();
    }

    public BooleanProperty refundedProperty() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded.set(refunded);
    }
}
