package com.bluelight.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * PayPeriod
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/5/2016
 */
public class PayPeriod {

    public static double taxRate = .20;
    private BigDecimal hourlyRate;
    private BigDecimal hoursWorked;

    /**
     *
     * @return the number of hours worked in the pay period.
     */
    @Basic
    @Column(name = "hours_worked", nullable = false, insertable = true, updatable = true)
    public BigDecimal getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(BigDecimal hours) {
        hoursWorked = hours;
    }

    /**
     *
     * @return the amount the employee earns per hour worked
     */
    @Basic
    @Column(name = "wage", nullable = false, insertable = true, updatable = true)
    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal rate) {
        hourlyRate = rate;
    }

    /**
     *
     * @return Gross pay (before taxes)... wage * hours
     */
    public BigDecimal grossPay() {
        return hourlyRate.multiply(hoursWorked);
    }

    /**
     *
     * @return Amount of taxes withheld during the pay period
     */
    public BigDecimal taxesWithheld() {
        return grossPay().multiply(new BigDecimal(taxRate));
    }

    public BigDecimal takeHomePay() {
        return grossPay().subtract(taxesWithheld());
    }

}
