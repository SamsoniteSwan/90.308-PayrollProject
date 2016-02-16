package com.bluelight.model;

import com.bluelight.utils.DatabaseUtils;
import com.opencsv.bean.CsvBind;
import org.joda.time.DateTime;
import com.bluelight.utils.DateTimeParser;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.text.ParseException;
import java.util.logging.Logger;


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
     *
     * @return get the employee
     */
    @ManyToOne
    @JoinColumn(name = "person_Id", referencedColumnName = "person_Id", nullable = false)
    public Person getEmployee() {
        return employee;
    }

    /**
     * Specify the Person associated with a stock.
     *
     * @param employee an employee instance
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    public DateTime startDate() {
        DateTime result = new DateTime();
        try {
            result = DatabaseUtils.makeDateTimeFromString(DateTimeParser.startAndEnd(period).get(0));
        } catch (ParseException e) {
            Logger.getGlobal().warning("could not parse start date:" + e.getMessage());
        }
        return result;
    }

    public DateTime endDate() {
        DateTime result = new DateTime();
        try {
            result = DatabaseUtils.makeDateTimeFromString(DateTimeParser.startAndEnd(period).get(1));
        } catch (ParseException e) {
            Logger.getGlobal().warning("could not parse end date:" + e.getMessage());
        }
        return result;
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
