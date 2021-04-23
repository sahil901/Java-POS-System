package project.managers;

import org.hibernate.Session;
import org.hibernate.query.Query;
import project.Util;
import project.entity.Product;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Sahil Patel
 */
public class ProductManager {
    /**
     * @param product Product to be saved to database
     *                 Saves a product to the database
     * */
    public static void saveProduct(Product product){
        Session session = Util.openSession();
        session.beginTransaction();
        session.save(product);
        session.getTransaction().commit();
        session.close();
    }
    /**
     * @param product Product to be updated in the database
     *                 Updates a product to the database
     * */
    public static void updateProduct(Product product){
        Session session = Util.openSession();
        session.beginTransaction();
        session.update(product);
        session.getTransaction().commit();
        session.close();
    }
    /**
     * @param product Product to be deleted from the database
     *                 Deletes a product from the database
     * */

    public static void deleteProduct(Product product){
        Session session = Util.openSession();
        session.beginTransaction();
        session.delete(product);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * @param productID Id of the product to read
     *                   Reads a product from the database with the given Id
     * */
    public static Product readProduct(int productID){
        try (Session session = Util.openSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class,productID);
            session.getTransaction().commit();
            return product;
        }
    }

    /**
     * Reads all of the Products from the database
     * */
    public static List<Product> readAllProducts(){
        try (Session session = Util.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
            Root<Product> root = criteriaQuery.from(Product.class);
            Query<Product> query = session.createQuery(criteriaQuery.select(root).where(
                    criteriaBuilder.equal(root.get("visible"),true)
            ));
            return query.getResultList();
        }
    }

    /**
     * Checks if the product table is empty in the database
     * */
    public static boolean isEmpty(){
        try(Session session = Util.openSession()){
            return session.createQuery("select 1 from Product").setFetchSize(1).list().isEmpty();
        }
    }
}
