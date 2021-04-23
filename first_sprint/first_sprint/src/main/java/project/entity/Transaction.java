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
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int transactionId;
	@ManyToOne
	private Employee employee;
	private java.sql.Date date;
	private java.math.BigDecimal subTotal=BigDecimal.ZERO;
	private java.math.BigDecimal taxAmount=BigDecimal.ZERO;
	private java.math.BigDecimal totalPrice=BigDecimal.ZERO;
	@OneToMany(mappedBy = "transaction")
	private List<TransactionItem> transactionItems = new ArrayList<>();
	private boolean refunded=false;

	public Transaction() {this(new Employee(),Date.valueOf(LocalDate.now()));}

    public Transaction(Employee employee, Date date) {
		setEmployee(employee);
		setDate(date);
    }

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public List<TransactionItem> getTransactionItems() {
		return transactionItems;
	}

	public void setTransactionItems(List<TransactionItem> transactionItems) {
		this.transactionItems = transactionItems;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date d) {
		this.date = d;
	}
	
	public int getTotalItems() {
		return getTransactionItems().size();
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public java.math.BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(java.math.BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public java.math.BigDecimal getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(BigDecimal tp) {
		this.totalPrice = tp;
	}

	public void addTransactionItem(TransactionItem transactionItem){
		transactionItem.setTransaction(this);
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
		setTotalPrice(getSubTotal().add(getTaxAmount()));
	}

	public boolean isRefunded() {
		return refunded;
	}

	public void setRefunded(boolean refunded) {
		this.refunded = refunded;
	}

	@Override
	public String toString() {
		return "transaction [transaction_number=" + transactionId
				+ ", employeeID=" + getEmployee().getEmployeeID() + ", date=" + date + ", totalItems=" + getTotalItems() + ", subTotal="
				+ subTotal + ", taxAmount=" + taxAmount + ", totalPrice=" + totalPrice + "]";
	}
	

}