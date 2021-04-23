package project.entity;

import project.model.ProductTR;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Sahil Patel
 */
@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int productID;
	private String productName;
	private java.math.BigDecimal price;
	private int stockQuantity;
	private java.sql.Date firstStocked;
	private java.sql.Date lastModified;
	private boolean visible = true;

	public Product() {this(null, new BigDecimal(0), 0);}

	public Product(String productName, BigDecimal price, int stockQuantity) {
		setProductName(productName);
		setPrice(price);
		setStockQuantity(stockQuantity);
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public Date getFirstStocked() {
		return firstStocked;
	}

	public void setFirstStocked(Date firstStocked) {
		this.firstStocked = firstStocked;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		return getProductID() + " - " + getProductName();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Product)){
			return false;
		}else{
			return getProductID() == ((Product)obj).getProductID();
		}
	}

	/**
   * Converts Product object to ProductTR
   */
	public ProductTR toProductTR(){
		return new ProductTR(this);
	}
}