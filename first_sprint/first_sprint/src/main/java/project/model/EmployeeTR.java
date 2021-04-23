package project.model;

import javafx.beans.property.*;
import project.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Sahil Patel
 */
/*
 * Specific to table row (TR)
 * This class if added for the pop-ups for both the add and update.
 * You need this since without it, just using the java classes was not enough 
 */

public class EmployeeTR {
    private final IntegerProperty employeeID = new SimpleIntegerProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final ObjectProperty<EmployeeRole> employeeRole = new SimpleObjectProperty<>();
    private final BooleanProperty employeeStatus = new SimpleBooleanProperty();
    private final StringProperty employeePassword = new SimpleStringProperty();
    /**
     * Creates an object of EmployeeTR from an object of Employee
     * */
    public EmployeeTR(Employee employee){
        setEmployeeID(employee.getEmployeeID());
        setFirstName(employee.getFirstName());
        setLastName(employee.getLastName());
        setEmployeeRole(employee.getEmployeeRole());
        setEmployeeStatus(employee.getEmployeeStatus());
        setEmployeePassword(employee.getEmployeePassword());
    }

    public int getEmployeeID() {
        return employeeID.get();
    }

    public IntegerProperty employeeIDProperty() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeIDProperty().set(employeeID);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstNameProperty().set(firstName);
    }

    public String getLastName() {
        return lastNameProperty().get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public boolean isEmployeeStatus() {
        return employeeStatusProperty().get();
    }

    public BooleanProperty employeeStatusProperty() {
        return employeeStatus;
    }

    public void setEmployeeStatus(boolean employeeStatus) {
        this.employeeStatus.set(employeeStatus);
    }

    public String getEmployeePassword() {
        return employeePassword.get();
    }

    public StringProperty employeePasswordProperty() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePasswordProperty().set(employeePassword);
    }

    public EmployeeRole getEmployeeRole() {
        return employeeRoleProperty().get();
    }

    public ObjectProperty<EmployeeRole> employeeRoleProperty() {
        return employeeRole;
    }

    public void setEmployeeRole(EmployeeRole employeeRole) {
        this.employeeRole.set(employeeRole);
    }

    /**
     * Converts EmployeeTR object to Employee object
     * */
    public Employee toEmployee(){
        Employee employee = new Employee(getFirstName(),getLastName(),getEmployeeRole(),isEmployeeStatus(),getEmployeePassword());
        employee.setEmployeeID(getEmployeeID());
        return employee;
    }
    /**
     * Converts a list of Employees to a list of EmployeeTRs
     * */
    public static List<EmployeeTR> toEmployeeTRList(List<Employee> employees){
        return employees.stream().map(EmployeeTR::new).collect(Collectors.toList());
    }
}
