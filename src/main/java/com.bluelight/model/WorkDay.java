package com.bluelight.model;

import com.bluelight.services.DatabaseEmployeeService;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * WorkDay
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/12/2016
 */
@Entity
@Table(name="tblWorkLog")
public class WorkDay {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name="employee")
    private Employee employee;

    //@ManyToOne
    //@JoinColumn(name="period")
    //private PayPeriod period;

    @Basic
    @Column(name = "workday", nullable = false, insertable = true, updatable = true)
    private Timestamp day;

    @Basic
    @Column(name = "hours", nullable = false, insertable = true, updatable = true)
    private BigDecimal hoursWorked;

    @Basic
    @Column(name = "vacationhrs", nullable = false, insertable = true, updatable = true)
    private BigDecimal vacationUsed;

    public WorkDay() {
        //empty constructor required by Hibernate
    }

    public WorkDay(BigDecimal hours) {
        hoursWorked = hours;
    }

    /*
    public WorkDay(String eeId, Timestamp day, BigDecimal hrsWorked, BigDecimal hrsVaca) {
        this.employee = new Employee(eeId);
        this.day = day;
        this.hoursWorked = hrsWorked;
        this.vacationUsed = hrsVaca;
    }
    */
    /**
     * Primary Key - Unique ID for a particular row in the tbl_Favorites table.
     *
     * @return an integer value
     */
    public int getId() {
        return id;
    }

    /**
     * Set the unique ID for a particular row in tblWorkLog.
     * This method should not be called by client code. The value is managed in internally.
     *
     * @param id a unique value.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return get the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Specify the Employee associated with the workday.
     *
     * @param ee and Employee instance
     */
    public void setEmployee(Employee ee) {
        this.employee = ee;
    }

    //public PayPeriod getPeriod() { return period; }

    //public void setPeriod(PayPeriod payPeriod) {
    //    this.period = payPeriod;
    //}

    /**
     * @return the number of hours worked in the pay period.
     */
    public BigDecimal getHoursWorked() {
        return hoursWorked;
        //return hoursWorked.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setHoursWorked(BigDecimal hours) {
        hoursWorked = hours;
    }

    /**
     * @return the number of hours worked in the pay period.
     */
    public BigDecimal getVacationUsed() {
        return vacationUsed;
    }

    public void setVacationUsed(BigDecimal hours) {
        vacationUsed = hours;
    }

    /**
     * @return the date of the work log.
     */
    public Timestamp getDate() {
        return day;
    }

    /**
     * Specify the date.
     * @param date  the day of work.
     */
    public void setDate(Timestamp date) {
        this.day = date;
    }

    @Override
    public String toString() {
        return "Employee " + getEmployee().getEmployeeId() +
                " worked " + getHoursWorked() + " hours on " +
                getDate();
    }
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (hoursWorked != null ? hoursWorked.hashCode() : 0);
        return result;
    }
}
