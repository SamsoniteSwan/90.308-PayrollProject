package com.bluelight.services;

import com.bluelight.model.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * CsvTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public class CsvTest {

    public static String TestCSVFile = "./src/main/resources/Payroll-Sheet1.csv";

    CSVImportService service;

    @Before
    public void setup() {
        service = new CSVImportService();
    }

    @Test
    public void testCsv() {

        List<PayRecord> records = service.importedCsvPeriod(TestCSVFile);
        assertTrue("Record count should equal 12 (" +
                records.toString() +
                ") TOTAL:" + records.size(),records.size() == 12);
    }

    @Test
    public void testGetEmployee() {
        List<PayRecord> records = service.importedCsvPeriod(TestCSVFile);

        List<Employee> employeeList = new ArrayList<>();

        for (PayRecord record : records) {
            Employee ee = record.getEmployee();

            employeeList.add(ee);
        }

        assertTrue("employees gotten=" + employeeList.toString(), employeeList.size() == 12);
    }

    @Test
    public void testUploadCsvToDb() {
        service.uploadCsvToDb(CsvTest.TestCSVFile);
        List<Employee> ees = null;
        List<PayPeriod> payPeriods = null;
        List<WorkDay> workDays = null;
        BigDecimal pay = null;
        try {
            ees = new DatabaseEmployeeService().getEmployees();
            //assertTrue("original employee count", ees.size() == 2);
            payPeriods = new DatabaseEmployeeService().getPayPeriods(ees.get(3));
            workDays = new DatabaseEmployeeService().getAllWorkdays(ees.get(3));

            pay = new DatabaseEmployeeService().getTotalPay(ees.get(3).getEmployeeId(),
                    DatabaseEmployeeServiceTest.min,
                    DatabaseEmployeeServiceTest.max);

        } catch (ServiceException e) {
            Logger.getGlobal().warning("could not get employees:" + e.getMessage());
        }
        // 3 different employees are listed in the csv file, so 2 + 3 = 5
        assertTrue("employees added=" + ees.size(), ees.size() == 5);
        assertTrue("payPeriods added for " + ees.get(3).toString(), payPeriods.size() == 4);
        assertTrue("totalPay=" + pay, pay.compareTo(new BigDecimal(300))==0);
        assertTrue("total workdays for " + ees.get(3).toString() + ": " + workDays.size(),
                workDays.size() == 21);

    }
}
