package project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


/**
 * @author Sahil Patel
 */
@Entity
public class TransactionItem {
	@Id
	@GeneratedValue
	private int transactionItemId;
	@ManyToOne
	private Transaction transaction;
	@ManyToOne
	private Product product;
	private int productQuantity;

	public TransactionItem(){this (new Product(),0);}
	
	public TransactionItem(Product product, int productQuantity) {
		setTransaction(new Transaction());
		setProduct(product);
		setProductQuantity(productQuantity);
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}


	public int getTransactionItemId() {
		return transactionItemId;
	}

	public void setTransactionItemId(int transactionItemId) {
		this.transactionItemId = transactionItemId;
	}

	@Override
	public String toString() {
		return "transactionItem [transaction_number=" + getTransaction().getTransactionId() + ", productID=" + getProduct().getProductID()
				+ ", productQuantity=" + productQuantity + ", productPrice=" + getProduct().getPrice() + "]";
	}

    public TransactionItem add(TransactionItem item) {
		return new TransactionItem(getProduct(),item.getProductQuantity()+getProductQuantity());
    }
}