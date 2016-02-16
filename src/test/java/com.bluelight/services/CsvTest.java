package com.bluelight.services;

import com.bluelight.model.PayPeriod;
import com.bluelight.model.PayRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * CsvTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public class CsvTest {

    public static String TestCSVFile = "./src/main/resources/Payroll - Sheet1.csv";

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


}
