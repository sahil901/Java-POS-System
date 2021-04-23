package project.managers;

import org.hibernate.Session;
import org.hibernate.query.Query;
import project.Util;
import project.entity.Employee;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sahil Patel
 */
public class EmployeeManager {
    /**
     * Employee that logs into the Application
    */
    public static Employee currentEmployee = null;
    /**
    * @param employee Employee to be saved to database
    *                 Saves an employee to the database
    * */
    public static void saveEmployee(Employee employee){
        Session session = Util.openSession();
        session.beginTransaction();
        session.save(employee);
        session.getTransaction().commit();
        session.close();
    }
    /**
     * @param employee Employee to be updated in the database
     *                 Updates an employee to the database
     * */
    public static void updateEmployee(Employee employee){
        Session session = Util.openSession();
        session.beginTransaction();
        session.update(employee);
        session.getTransaction().commit();
        session.close();
    }
    /**
     * @param employee Employee to be deleted from the database
     *                 Updates the active flag of employee to false, so employee is not shown in table and can't log in anymore
     *                 (Active flag was used instead of deleting cause employee is used in transaction and the transaction records need to be kept even after an employee leaves)
     * */

    public static void deleteEmployee(Employee employee){
        employee.setActive(false);
        updateEmployee(employee);
    }

    /**
     * @param employeeID Id of the employee, trying to log in
     * @param password Password of the employee, trying to log in
     *                 Verifies login credentials with the records in the database
     * */
    public static boolean checkCredentials(int employeeID, String password){
        try (Session session = Util.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);
            Query<Employee> query = session.createQuery(criteriaQuery.select(root).where(
                   criteriaBuilder.and(
                           criteriaBuilder.equal(root.get("employeeID"),employeeID),
                           criteriaBuilder.equal(root.get("employeePassword"),password),
                           criteriaBuilder.equal(root.get("active"),true)
                   )
            ));
            if (!query.getResultList().isEmpty()){
                currentEmployee = query.getSingleResult();
            }else{
                currentEmployee = null;
            }
            return !query.getResultList().isEmpty();
        }
    }

    /**
     * @param employeeID Id of the employee to read
     *                   Reads an employee from the database with the given Id
     * */
    public static Employee readEmployee(int employeeID){
        try (Session session = Util.openSession()) {
            session.beginTransaction();
            Employee employee = session.get(Employee.class,employeeID);
            session.getTransaction().commit();
            return employee;
        }
    }

    /**
     * Reads all of the Employees from the database
     * */
    public static List<Employee> readAllEmployees(){
        try (Session session = Util.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);
            Query<Employee> query = session.createQuery(criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("active"),true)));
            return query.getResultList().stream().peek(employee -> {
                if(employee.getEmployeeID()!=currentEmployee.getEmployeeID()){
                    employee.setEmployeePassword("*****");
                }
            }).collect(Collectors.toList());
        }
    }
    /**
     * @param employee Employee to be set status as active
     *                 Sets all employees' status to offline except the given employee
    * */
    public static void changeEmployeeStatus(Employee employee){
        if (employee!=null){
            try (Session session = Util.openSession()){
                session.beginTransaction();
                String hqlUpdateQuery= "update Employee set employeeStatus=:employeeStatus where employeeID!=:employeeID and active=true";
                Query query=session.createQuery(hqlUpdateQuery);
                query.setParameter("employeeStatus",false);
                query.setParameter("employeeID",employee.getEmployeeID());
                query.executeUpdate();
            }
            employee.setEmployeeStatus(true);
            updateEmployee(employee);

        }
    }

    /**
     * Checks if the employee table is empty in the database
     * */
    public static boolean isEmpty(){
        try(Session session = Util.openSession()){
           return session.createQuery("select 1 from Employee where active=true").setFetchSize(1).list().isEmpty();
        }
    }
}
