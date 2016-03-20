package com.bluelight.services;

import com.bluelight.model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
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
    public void testCsv() throws FileNotFoundException {

        List<PayRecord> records = service.loadCsvPeriods(TestCSVFile);
        assertTrue("Record count should equal 12 (" +
                records.toString() +
                ") TOTAL:" + records.size(),records.size() == 12);
    }

    @Test
    public void testGetEmployee() throws FileNotFoundException {
        List<PayRecord> records = service.loadCsvPeriods(TestCSVFile);

        List<Employee> employeeList = new ArrayList<>();

        for (PayRecord record : records) {
            Employee ee = record.getEmployee();

            employeeList.add(ee);
        }

        assertTrue("employees gotten=" + employeeList.toString(), employeeList.size() == 12);
    }


    @Test
    public void testUploadCsvToDb() throws FileNotFoundException, ServiceException {
        service.uploadCsvToDb(TestCSVFile);
        DatabaseEmployeeService dbService = new DatabaseEmployeeService();
        List<Employee> ees;
        List<PayPeriod> payPeriods;
        //List<WorkDay> workDays = null;
        BigDecimal pay;


        ees = dbService.getEmployees();
        assertTrue("employees added=" + ees.size(), ees.size() == 5);

        Employee sampleEmployee = ees.get(3);
        payPeriods = dbService.getPayPeriods(sampleEmployee);
        assertTrue("payPeriods added for " + sampleEmployee.toString() + "; count=" + payPeriods.size(), payPeriods.size() == 4);

        /*
        workDays = dbService.getAllWorkdays(sampleEmployee);
        assertTrue("total workdays for " + sampleEmployee.toString() + ": " + workDays.size(),
                workDays.size() == 59);
        */
        pay = dbService.getTotalPay(sampleEmployee,
                DatabaseEmployeeServiceTest.min,
                DatabaseEmployeeServiceTest.max);


        // 3 different employees are listed in the csv file, so 2 + 3 = 5


        assertTrue("pay for " + sampleEmployee + "; totalPay=" + pay, pay.compareTo(new BigDecimal("6303.44"))==0);

    }

}
