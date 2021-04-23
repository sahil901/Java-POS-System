package project.managers;

import org.hibernate.Session;
import org.hibernate.query.Query;
import project.Util;
import project.entity.Refund;
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
public class RefundManager {

    private static Session session = null;

    public static void closeSession(){
        if(session!=null){
            session.close();
        }
    }

    /**
     * @param refund Refund to be saved to database
     *                 Saves a refund to the database
     * */
    public static void saveRefund(Refund refund){
        Session session = Util.openSession();
        session.beginTransaction();
        session.save(refund);
        for (TransactionItem transactionItem: refund.getTransactionItems()){
            transactionItem.getProduct().setLastModified(Date.valueOf(LocalDate.now()));
            session.update(transactionItem.getProduct());
            session.save(transactionItem);
        }
        session.getTransaction().commit();
        session.close();
        refund.getTransaction().setRefunded(true);
        TransactionManager.updateTransaction(refund.getTransaction());
    }
    /**
     * @param refund Refund to be updated in the database
     *                 Updates a refund to the database
     * */
    public static void updateRefund(Refund refund){
        Session session = Util.openSession();
        session.beginTransaction();
        for (TransactionItem transactionItem: refund.getTransactionItems()){
            session.update(transactionItem);
        }
        session.update(refund);
        session.getTransaction().commit();
        session.close();
    }
    /**
     * @param refund Refund to be deleted from the database
     *                 Deletes a refund from the database
     * */

    public static void deleteRefund(Refund refund){
        Session session = Util.openSession();
        session.beginTransaction();
        for (TransactionItem transactionItem: refund.getTransactionItems()){
            session.delete(transactionItem);
        }
        session.delete(refund);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * @param refundID Id of the refund to read
     *                   Reads a refund from the database with the given Id
     * */
        public static Refund readRefund(int refundID){
        session = Util.openSession();
        session.beginTransaction();
        Refund refund = session.get(Refund.class,refundID);
        session.getTransaction().commit();
        return refund;
    }

    /**
     * Reads all of the Refunds from the database
     * */
    public static List<Integer> readAllRefunds(){
        try (Session session = Util.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Refund> criteriaQuery = criteriaBuilder.createQuery(Refund.class);
            Root<Refund> root = criteriaQuery.from(Refund.class);
            Query<Refund> query = session.createQuery(criteriaQuery.select(root));

            return query.getResultList().stream().map(Refund::getRefundId).collect(Collectors.toList());
        }
    }

    /**
     * Checks if the refund table is empty in the database
     * */
    public static boolean isEmpty(){
        try(Session session = Util.openSession()){
            return session.createQuery("select 1 from Refund").setFetchSize(1).list().isEmpty();
        }
    }
}
