package com.bluelight.model;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * PayPeriod
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/5/2016
 */
@Entity
@Table(name = "tblPayPeriods")
public class PayPeriod implements Serializable {

    public static double taxRate = .20;

     //(strategy = GenerationType.IDENTITY)
    //@Column(name = "id", unique = true, nullable = false)
     @Id
     @GeneratedValue
     private int id;
    //private ArrayList<WorkDay> days;
    @Basic
    @Column(name = "startDate", nullable = false, insertable = true, updatable = true)
    private Timestamp startDay;

    @Basic
    @Column(name = "endDate", nullable = false, insertable = true, updatable = true)
    private Timestamp endDay;

    @Basic
    @Column(name = "wage", nullable = false, insertable = true, updatable = true)
    private BigDecimal hourlyRate;

    //@OneToOne(mappedBy="payPeriods",  fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name="employee")
    private Employee employee;


    public PayPeriod() {
        //empty constructor required by Hibernate
    }

    public PayPeriod(BigDecimal rate) {
        hourlyRate = rate;
    }

    public int getId() { return id; }

    public Employee getEmployee() { return employee; }

    public void setEmployee(Employee ee) { this.employee = ee; }
    /**
     *
     * @return the amount the employee earns per hour worked
     */
     public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal rate) {
        hourlyRate = rate;
    }

    /**
     *
     * @return the PayPeriod start date.
     */

    public Timestamp getStartDay() {
        return startDay;
    }

    /**
     * Specify the PayPeriod start.
     * @param startDate  the day the PayPeriod starts.
     */
    public void setStartDay(Timestamp startDate) {
        this.startDay = startDate;
    }

    /**
     *
     * @return the PayPeriod end date.
     */
   public Timestamp getEndDay() {
        return endDay;
    }

    /**
     * Specify end of the PayPeriod.
     * @param endDate  the day the PayPeriod ends.
     */
    public void setEndDay(Timestamp endDate) {
        this.endDay = endDate;
    }

    /**
     *
     * @return Gross pay (before taxes)... wage * hours
     */
    //public BigDecimal grossPay() {
    //    return hourlyRate.multiply(this.hoursWorked());
    //}

    /**
     *
     * @return Amount of taxes withheld during the pay period
     */
    //public BigDecimal taxesWithheld() {
    //    return grossPay().multiply(new BigDecimal(taxRate));
    //}

    //public BigDecimal takeHomePay() {
    //    return grossPay().subtract(taxesWithheld());
    //}

    /**
     * Total hours worked as sum of hours for all the days
     * @return BigDecimal of total hours
     */
    /*
    public BigDecimal hoursWorked() {
        /* Stream method to test
        days.stream().mapToDouble(day -> day.getHoursWorked().doubleValue()).sum();
         */

        //BigDecimal result = new BigDecimal(0);
        /* OLD
        for(WorkDay day : days) {
            result = result.add(day.getHoursWorked());
        }
        */
/*
        return result;
    }*/

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        result = 31 * result + (startDay != null ? startDay.hashCode() : 0);
        result = 31 * result + (endDay != null ? endDay.hashCode() : 0);
        result = 31 * result + (hourlyRate != null ? hourlyRate.hashCode() : 0);
        return result;
    }
}
