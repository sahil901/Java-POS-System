package project.managers;

import org.hibernate.Session;
import org.hibernate.query.Query;
import project.Util;
import project.entity.Refund;
import project.entity.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

/**
 * @author Sahil Patel
 */
public class ReportManager {
    private final Session session;
    private final Date date;
    private boolean isNotClosed = true;
    public ReportManager(LocalDate date){
        session = Util.openSession();
        this.date = Date.valueOf(date);
    }
    /**
     * Gets the total money earned today
     * */
    public BigDecimal getGeneratedGross(){
        if(isNotClosed){
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);
            Root<Transaction> root = criteriaQuery.from(Transaction.class);
            Query<Transaction> query = session.createQuery(criteriaQuery.select(root).where(
                    criteriaBuilder.equal(root.get("date"),date)
            ));
            return query.getResultList().stream().map(Transaction::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
        }else{
            return BigDecimal.ZERO;
        }
    }

    /**
     * Gets the total money earned today
     * */
    public BigDecimal getTotalRefundsForToday(){
        if(isNotClosed){
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Refund> criteriaQuery = criteriaBuilder.createQuery(Refund.class);
            Root<Refund> root = criteriaQuery.from(Refund.class);
            Query<Refund> query = session.createQuery(criteriaQuery.select(root).where(
                    criteriaBuilder.equal(root.get("returnDate"),date)
            ));
            return query.getResultList().stream().map(Refund::getTotalReturn).reduce(BigDecimal.ZERO,BigDecimal::add);
        }else{
            return BigDecimal.ZERO;
        }
    }

    /**
     * Gets the amount of cash available at start of day
     * */
    public BigDecimal getStartOfDayCash(){
        if(isNotClosed){
            return getPreviousDayTransactionsTotal().subtract(getPreviousDayReturnTotal());
        }else{
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get the sum of all transaction from yesterday to start of business
     * */
    private BigDecimal getPreviousDayTransactionsTotal(){
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);
        Root<Transaction> root = criteriaQuery.from(Transaction.class);
        Query<Transaction> query = session.createQuery(criteriaQuery.select(root).where(
                criteriaBuilder.lessThan(root.get("date"),date)
        ));
        return query.getResultList().stream().map(Transaction::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    /**
     * Get the sum of all returns from yesterday to start of business
     * */
    private BigDecimal getPreviousDayReturnTotal(){
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Refund> criteriaQuery = criteriaBuilder.createQuery(Refund.class);
        Root<Refund> root = criteriaQuery.from(Refund.class);
        Query<Refund> query = session.createQuery(criteriaQuery.select(root).where(
                criteriaBuilder.lessThan(root.get("returnDate"),date)
        ));
        return query.getResultList().stream().map(Refund::getTotalReturn).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    /**
     * Closes the connection to the database
     * */
    public void closeSession(){
        session.close();
        isNotClosed = false;
    }
}
