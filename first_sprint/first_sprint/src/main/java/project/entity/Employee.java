package project.entity;

import project.model.EmployeeRole;
import project.model.EmployeeTR;

import javax.persistence.*;

/**
 * @author Sahil Patel
 */
@Entity
@Table(name = "employee")
public class Employee{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int employeeID;
	private String firstName; 
	private String lastName; 
	private EmployeeRole employeeRole;
	private Boolean employeeStatus; 
	private String employeePassword;
	private Boolean active=true;

    public Employee() {this( null, null, null, false, null);}

	public Employee(String fName, String lName, EmployeeRole role,
					Boolean status, String Pass)
    {
		setFirstName(fName);
		setLastName(lName);
		setEmployeeRole(role);
        setEmployeeStatus(status);
        setEmployeePassword(Pass);
    }

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public int getEmployeeID() {
		
		return this.employeeID;
		
	}
	
	public void setEmployeeID(int q) {
		this.employeeID = q;

	}

	public void setEmployeeStatus(Boolean employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String fn) {
		this.firstName = fn;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String ln) {
		this.lastName = ln;
	}

	public String getName(){
    	return getFirstName()+" "+getLastName();
	}

	public EmployeeRole getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(EmployeeRole employeeRole) {
		this.employeeRole = employeeRole;
	}

	public Boolean getEmployeeStatus() {
		return employeeStatus; 
	}
	
	public void setEmployeeStatus(boolean stat) {
		this.employeeStatus = stat;
	}

	public String getEmployeePassword() {
		return this.employeePassword;
	}
	
	public void setEmployeePassword(String ep) {
			this.employeePassword = ep;
	}
	
	public String toString() {

		return "Employee ID: " + getEmployeeID() + "\n" +
				"Name: " + getFirstName() + " " + getLastName() + "\n" +
				"Role: " + getEmployeeRole() + "\n" +
				"Is Working: " + getEmployeeStatus() + "\n";
	}
	
	/*
     * This is a new thing that was added. 
     */
	public EmployeeTR toEmployeeTR(){
    	return new EmployeeTR(this);
	}
	
}