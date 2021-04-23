package project.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sahil Patel
 */
@Entity
public class Refund {
	@Id
	@GeneratedValue
	private int refundId;
	@ManyToOne
	private Transaction transaction;
	private java.sql.Date returnDate;
	private java.math.BigDecimal subTotal = BigDecimal.ZERO;
	private java.math.BigDecimal taxAmount= BigDecimal.ZERO;
	private java.math.BigDecimal totalReturn= BigDecimal.ZERO;
	@OneToMany
	private List<TransactionItem> transactionItems = new ArrayList<>();

	public Refund() {
		this(null, Date.valueOf(LocalDate.now()));
	}
	
	public Refund(Transaction transaction, Date date) {
		setTransaction(transaction);
		setReturnDate(date);
	}

	public int getRefundId() {
		return refundId;
	}

	public void setRefundId(int refundId) {
		this.refundId = refundId;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getTotalReturn() {
		return totalReturn;
	}

	public void setTotalReturn(BigDecimal totalReturn) {
		this.totalReturn = totalReturn;
	}

	public List<TransactionItem> getTransactionItems() {
		return transactionItems;
	}

	public void setTransactionItems(List<TransactionItem> transactionItemList) {
		this.transactionItems = transactionItemList;
	}

	public void addTransactionItem(TransactionItem transactionItem){
		getTransactionItems().add(transactionItem);
		calculateSubtotalTaxAndTotal();
	}

	public void removeTransactionItem(TransactionItem transactionItem){
		getTransactionItems().remove(transactionItem);
		calculateSubtotalTaxAndTotal();
	}

	private void calculateSubtotalTaxAndTotal(){
		setSubTotal(
				getTransactionItems().stream().map(
						transactionItem ->
								transactionItem.getProduct().getPrice().multiply(
										BigDecimal.valueOf(transactionItem.getProductQuantity())
								)
				).reduce(BigDecimal.ZERO,BigDecimal::add)
		);
		setTaxAmount(
				getSubTotal().multiply(BigDecimal.valueOf(0.13))
		);
		setTotalReturn(getSubTotal().add(getTaxAmount()));
	}

}