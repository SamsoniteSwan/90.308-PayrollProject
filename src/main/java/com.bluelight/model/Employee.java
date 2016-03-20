package com.bluelight.model;

import com.bluelight.services.EmployeeService;
import com.bluelight.services.ServiceException;
import com.bluelight.services.ServiceFactory;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * Employee object for storing in database
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/5/2016
 */
@Entity
@Table(name="tblEmployees")
public class Employee {


    public static final String DEFAULT_STATUS = "active";
    public static final Calendar DEFAULT_BIRTHDAY = Calendar.getInstance();

    static {
        DEFAULT_BIRTHDAY.set(1900, Calendar.JANUARY, 1);
    }

    @Id
    @Column(name = "employeeId", nullable = false, insertable = true, updatable = true)
    private String employeeId;
    @Basic
    @Column(name = "firstName", nullable = false, insertable = true, updatable = true, length = 100)
    private String firstName;
    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true, length = 100)
    private String lastName;
    @Basic
    @Column(name = "dob", insertable = true, updatable = true)
    private Timestamp birthDate;

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = true, length = 20)
    private String status;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkDay> workDays;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PayPeriod> payPeriods;

    public Employee() {
        //empty constructor required by Hibernate
    }

    public Employee(String id) {
        this.employeeId = id;
        try {
            EmployeeService service = ServiceFactory.getDBEmployeeServiceInstance();
            List<Employee> list = service.getEmployees();
            for (Employee ee : list) {
                if (ee.getEmployeeId().compareTo(id) == 0) {
                    this.firstName = ee.getFirstName();
                    this.lastName = ee.getLastName();
                    this.birthDate = ee.getBirthDate();
                    this.status = ee.getStatus();
                    break;
                }
            }
        } catch (ServiceException e) {
            //TODO - add exception handling here
        }
    }

    /**
     * Primary Key - Unique ID for a particular row in the person table.
     *
     * @return an integer value
     */

    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Set the unique ID for a particular row in the person table.
     * This method should not be called by client code. The value is managed in internally.
     *
     * @param employeeId a unique value.
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     *
     * @return the person's first name
     */

    public String getFirstName() {
        return firstName;
    }

    /**
     * Specify the person's first name
     * @param firstName a String value
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return the person's last name
     */

    public String getLastName() {
        return lastName;
    }

    /**
     * Specify the person's last name
     * @param lastName a String value
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return the person's birthdate.
     */

    public Timestamp getBirthDate() {
        return birthDate;
    }

    /**
     * Specify the person's date of birth.
     * @param birthDate  the time the person was born.
     */
    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }


    public String getStatus() { return status; }

    public void setStatus(String val) { this.status = val; }

    public List<PayPeriod> getPayPeriods() { return payPeriods; }

    public void setPayPeriods(List<PayPeriod> periods) {this.payPeriods = periods; }

    public List<WorkDay> getWorkDays() { return workDays; }

    public void setWorkDays(List<WorkDay> days) {this.workDays = days; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (birthDate != null ? !birthDate.equals(employee.getBirthDate()) : employee.getBirthDate() != null)
            return false;
        if (firstName != null ? !firstName.equals(employee.getFirstName()) : employee.getFirstName() != null)
            return false;
        if (lastName != null ? !lastName.equals(employee.getLastName()) : employee.getLastName() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
