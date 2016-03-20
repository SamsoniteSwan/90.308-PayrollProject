package com.bluelight.model;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


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

    //@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<WorkDay> workDays;

    //public List<WorkDay> getWorkDays() {
    //    return workDays;
    //}

    //public void setWorkDays (List<WorkDay> days) {
    //    this.workDays = days;
    //}


    //private BigDecimal vacationEarned;
    //private BigDecimal vacationUsed;

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
        //return hourlyRate;
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


    public List<WorkDay> dayList (ArrayList<WorkDay> set) {
        List<WorkDay> result = new ArrayList<>();
        for (WorkDay day : set) {
            if (day.getDate().compareTo(this.getStartDay()) >= 0 &&
                    day.getDate().compareTo(this.getEndDay()) <= 0) {
                result.add(day);
            }
        }
        return result;
    }

    public boolean hasDay (WorkDay day) {
        DateTime start = new DateTime(startDay);
        start = start.minusDays(1);
        DateTime end = new DateTime(endDay);
        //end = end.plusDays(1);
        if (day.getDate().after(start.toDate()) && day.getDate().before(end.toDate())) {
            return true;
        }
        return false;
    }
    /**
     *
     * @return Gross pay (before taxes)... wage * hours
     */
    public BigDecimal grossPay() {
        BigDecimal totalHours = this.hoursWorked.add(this.hoursVacationUsed);
        return hourlyRate.multiply(totalHours);
    }

    /**
     *
     * @return Amount of taxes withheld during the pay period
     */
    public BigDecimal taxesWithheld() {
        return grossPay().multiply(TAX_RATE);
    }

    public BigDecimal takeHomePay() {
        return grossPay().subtract(taxesWithheld());
    }

    /**
     * Total hours worked as sum of hours for all the days
     * @return BigDecimal of total hours
     */
    /*
    public BigDecimal hoursWorked() {
        /* Stream method to test
        days.stream().mapToDouble(day -> day.getHoursWorked().doubleValue()).sum();


        //BigDecimal result = new BigDecimal(0);
        /* OLD
        for(WorkDay day : days) {
            result = result.add(day.getHoursWorked());
        }


        return result;
    }*/

    public List<WorkDay> getWorkDays() {

        List<WorkDay> result = new ArrayList<>();

        int dayct = (int) (1 + (endDay.getTime() - startDay.getTime()) / (1000 * 60 * 60 * 24));
        BigDecimal days = new BigDecimal(dayct).setScale(BigDecimal.ROUND_HALF_UP);

        BigDecimal hrsPerDay = this.getHoursWorked().divide(days, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal vacaPerDay = this.getVacationUsed().divide(days, 2, BigDecimal.ROUND_HALF_UP);

        DateTime endDay = new DateTime(this.getEndDay());
        DateTime curDay = new DateTime(this.getStartDay());


        while (endDay.isAfter(curDay) || endDay.isEqual(curDay)) {
            WorkDay day = new WorkDay();
            day.setEmployee(employee);
            day.setDate(new Timestamp(curDay.getMillis()));
            day.setHoursWorked(hrsPerDay);
            day.setVacationUsed(vacaPerDay);


            // v might not be needed v
            employee.getWorkDays().add(day);

            result.add(day);

            curDay = curDay.plusDays(1);
        }

        return result;
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
