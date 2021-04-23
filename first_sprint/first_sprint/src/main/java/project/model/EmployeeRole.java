package project.model;

/**
 * @author Sahil Patel
 * Used in Employee table
 */
public enum EmployeeRole {
    MANAGER("Manager"),SALES_REP("Sales Rep"),CUSTOMER_REP("Customer Rep");
    private final String stringName;
    EmployeeRole(String name) {
        this.stringName = name;
    }

    @Override
    public String toString() {
        return stringName;
    }
}
