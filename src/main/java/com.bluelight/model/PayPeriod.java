package com.bluelight.model;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * PayPeriod
 * Database stored object for a length of time an employee worked
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/5/2016
 */
@Entity
@Table(name = "tblPayPeriods")
public class PayPeriod implements Serializable {

    public static final BigDecimal TAX_RATE = new BigDecimal(".20");
    public static final BigDecimal VACATION_RATE = new BigDecimal("0.075");

     @Id
     @GeneratedValue
     private int id;

    @Basic
    @Column(name = "startDate", nullable = false, insertable = true, updatable = true)
    private Timestamp startDay;

    @Basic
    @Column(name = "endDate", nullable = false, insertable = true, updatable = true)
    private Timestamp endDay;

    @Basic
    @Column(name = "wage", nullable = false, insertable = true, updatable = true)
    private BigDecimal hourlyRate;

    @Basic
    @Column(name = "hoursWorked", nullable = false, insertable = true, updatable = true)
    private BigDecimal hoursWorked;

    @Basic
    @Column(name = "hoursVacationUsed", nullable = false, insertable = true, updatable = true)
    private BigDecimal hoursVacationUsed;

    @ManyToOne
    @JoinColumn(name="employee")
    private Employee employee;
    

    public PayPeriod() {
        //empty constructor required by Hibernate
    }

    public int getId() { return id; }

    public Employee getEmployee() { return employee; }

    public void setEmployee(Employee ee) { this.employee = ee; }
    /**
     *
     * @return the amount the employee earns per hour worked
     */
     public BigDecimal getHourlyRate() {
         return hourlyRate.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setHourlyRate(BigDecimal rate) {
        hourlyRate = rate;
    }

    public BigDecimal getHoursWorked() {
        return hoursWorked.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setHoursWorked(BigDecimal hours) {
        hoursWorked = hours;
    }

    public BigDecimal getVacationUsed() {
        return hoursVacationUsed.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setVacationUsed(BigDecimal hours) {
        hoursVacationUsed = hours;
    }

    public BigDecimal vacationEarned() {
        return hoursWorked.multiply(
                VACATION_RATE).setScale(2, BigDecimal.ROUND_HALF_UP);
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
    public BigDecimal grossPay() {
        BigDecimal totalHours = this.hoursWorked.add(this.hoursVacationUsed);
        return hourlyRate.multiply(totalHours).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     *
     * @return Amount of taxes withheld during the pay period
     */
    public BigDecimal taxesWithheld() {
        return grossPay().multiply(TAX_RATE).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal takeHomePay() {
        return grossPay().subtract(taxesWithheld().setScale(2, BigDecimal.ROUND_HALF_UP));
    }
    

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
