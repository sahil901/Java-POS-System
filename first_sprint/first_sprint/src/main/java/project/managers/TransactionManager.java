package project.managers;

import org.hibernate.Session;
import org.hibernate.query.Query;
import project.Util;
import project.entity.Transaction;
import project.entity.TransactionItem;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sahil Patel
 */
public class TransactionManager {

    private static Session session = null;

    public static void closeSession(){
        if(session!=null){
            session.close();
        }
    }

    /**
     * @param transaction Transaction to be saved to database
     *                 Saves a transaction to the database
     * */
    public static void saveTransaction(Transaction transaction){
        Session session = Util.openSession();
        session.beginTransaction();
        session.save(transaction);
        for (TransactionItem transactionItem: transaction.getTransactionItems()){
            transactionItem.getProduct().setLastModified(Date.valueOf(LocalDate.now()));
            session.update(transactionItem.getProduct());
            session.save(transactionItem);
        }
        session.getTransaction().commit();
        session.close();
    }
    /**
     * @param transaction Transaction to be updated in the database
     *                 Updates a transaction to the database
     * */
    public static void updateTransaction(Transaction transaction){
        Session session = Util.openSession();
        session.beginTransaction();
        session.update(transaction);
        session.getTransaction().commit();
        session.close();
    }
    /**
     * @param transaction Transaction to be deleted from the database
     *                 Deletes a transaction from the database
     * */

    public static void deleteTransaction(Transaction transaction){
        Session session = Util.openSession();
        session.beginTransaction();
        for (TransactionItem transactionItem: transaction.getTransactionItems()){
            session.delete(transactionItem);
        }
        session.delete(transaction);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * @param transactionID Id of the transaction to read
     *                   Reads a transaction from the database with the given Id
     * */
    public static Transaction readTransaction(int transactionID){
        session = Util.openSession();
        session.beginTransaction();
        Transaction transaction = session.get(Transaction.class,transactionID);
        session.getTransaction().commit();
        return transaction;
    }

    /**
     * Reads all of the Transactions from the database
     * */
    public static List<Integer> readAllTransactions(){
        try (Session session = Util.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);
            Root<Transaction> root = criteriaQuery.from(Transaction.class);
            Query<Transaction> query = session.createQuery(criteriaQuery.select(root));

            return query.getResultList().stream().map(Transaction::getTransactionId).collect(Collectors.toList());
        }
    }

    /**
     * Reads all of the Transactions non-refunded from the database
     * */
    public static List<Integer> readAllNonRefundedTransactions(){
        try (Session session = Util.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);
            Root<Transaction> root = criteriaQuery.from(Transaction.class);
            Query<Transaction> query = session.createQuery(criteriaQuery.select(root).where(
                    criteriaBuilder.equal(root.get("refunded"),false)
            ));

            return query.getResultList().stream().map(Transaction::getTransactionId).collect(Collectors.toList());
        }
    }
    /**
     * Checks if the transaction table is empty in the database
     * */
    public static boolean isEmpty(){
        try(Session session = Util.openSession()){
            return session.createQuery("select 1 from Transaction").setFetchSize(1).list().isEmpty();
        }
    }
}
