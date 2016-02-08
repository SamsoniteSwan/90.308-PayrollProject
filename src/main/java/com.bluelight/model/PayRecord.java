package com.bluelight.model;

import com.opencsv.bean.CsvBind;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * PayRecord
 * Object for each row of the csv
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public class PayRecord extends PayrollData {



    @CsvBind
    private String period;
    @CsvBind
    private String employeeId;

    private int id;

    private Employee employee;

    public PayRecord() {
        // empty constructor required by Hibernate
    }

    public String getPeriod() {
        return period;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Create a new instance of a PayRecord.
     *
     * @param employeeId  ID of the employee
     * @param period   String consisting of start and end date
     *                 (will later need to be parsed)
     */
    public PayRecord(String employeeId, String period) {
        super();
        this.employeeId = employeeId;
        this.period = period;
        /*this.setCompany(company);
        this.symbol = company.getSymbol();
        */
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (employeeId != null ? employeeId.hashCode() : 0);
        result = 31 * result + (period != null ? period.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "PayrollRecord{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", period='" + period + '\'' +
                '}';
    }
}
