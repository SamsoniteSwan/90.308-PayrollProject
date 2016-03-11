package com.bluelight.model;

import com.bluelight.utils.DatabaseUtils;
import com.opencsv.bean.CsvBind;
import org.joda.time.DateTime;
import com.bluelight.utils.DateTimeParser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;


/**
 * PayRecord
 * Object for each row of the csv
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
/*
    @ManyToOne
    @JoinColumn(name="employeeId")*/
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
        //employee.setEmployeeId(employeeId);
        //employee.setFirstName(employeeFirst);
        //employee.setLastName(employeeLast);
        //employee.setBirthDate(new Timestamp(Employee.DEFAULT_BIRTHDAY.getTimeInMillis()));
        //employee.setStatus("active");

        return ee;
    }

    //public Employee getEmployee() { return employee; }

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

    public PayRecord(String employeeId, String period)  {
        super();
        this.employee = new Employee();
        this.employee.setEmployeeId(employeeId);
        this.employeeId = employeeId;
        this.period = period;
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

        }
        return result;
    }

    public String getEmployeeFirst() { return employeeFirst; }

    public String getEmployeeLast() { return employeeLast; }

    //public String getWage() { return wage; }

    public BigDecimal getWage() { return new BigDecimal(wage); }
    public BigDecimal getHoursWorked() { return new BigDecimal(hoursWorked); }
    public BigDecimal getVacationUsed() { return new BigDecimal(vacationUsed); }

    //public float getWage() { return Float.parseFloat(wage); }

    //public float getHoursWorked() { return Float.parseFloat(hoursWorked); }

    //public float getVacationUsed() { return Float.parseFloat(vacationUsed); }
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
