package com.bluelight.model;

import com.bluelight.utils.DatabaseUtils;
import com.opencsv.bean.CsvBind;
import org.joda.time.DateTime;
import com.bluelight.utils.DateTimeParser;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.logging.Logger;


/**
 * PayRecord
 * Object for each row of the csv.  Gets converted to PayPeriod object
 * for storing in the database.
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public class PayRecord extends PayrollData {



    private int id;

   @CsvBind
    private String period;

    @CsvBind
    private String employeeId;


    @CsvBind
    private String employeeFirst;

    @CsvBind
    private String employeeLast;

    @CsvBind
    private String wage;

    @CsvBind
    private String hoursWorked;

    @CsvBind
    private String vacationUsed;

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
    public Employee getEmployee() {

        Employee ee = new Employee();
        ee.setEmployeeId(employeeId);
        ee.setFirstName(employeeFirst);
        ee.setLastName(employeeLast);
        ee.setBirthDate(new Timestamp(Employee.DEFAULT_BIRTHDAY.getTimeInMillis()));
        ee.setStatus(Employee.DEFAULT_STATUS);
        this.setEmployee(ee);

        return ee;
    }

    /**
     * Specify the Employee associated with the record.
     *
     * @param employee an employee instance
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Create a new instance of a PayRecord.
     *
     * @param //employeeId  ID of the employee
     * @param //period   String consisting of start and end date
     *                 (will later need to be parsed)
     */


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

        }
        return result;
    }

    public String getEmployeeFirst() { return employeeFirst; }

    public String getEmployeeLast() { return employeeLast; }

    public BigDecimal getWage() { return new BigDecimal(wage); }
    public BigDecimal getHoursWorked() { return new BigDecimal(hoursWorked); }
    public BigDecimal getVacationUsed() { return new BigDecimal(vacationUsed); }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        result = 31 * result + (period != null ? period.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "PayrollRecord{" +
                "id=" + id +
                ", employeeString=" + employee.toString() +
                /*
                ", employeeId=" + employeeId +
                ", employeeName=" + employeeFirst + " " + employeeLast + */
                ", period='" + period + '\'' +
                '}';
    }
}
