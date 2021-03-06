package com.bluelight.utils;

import com.bluelight.services.CSVImportService;
import com.bluelight.services.CsvTest;
import com.bluelight.services.ServiceException;
import org.junit.After;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *  Tests for the DatabaseUtils class
 */
public class DatabaseUtilsTest {



    @Test(expected = DatabaseInitializationException.class)
    public void testBadInitFile() throws Exception {
        DatabaseUtils.initializeDatabase("fail");
    }

    @Test
    public void testGoodInitFile() throws Exception {
        DatabaseUtils.initializeDatabase(DatabaseUtils.initializationFile);
    }

    @Test
    public void testGetConnection() throws Exception{
        Connection connection = DatabaseUtils.getConnection();
        assertNotNull("verify that we can get a connection ok",connection);
    }

    @Test
    public void testGetConnectionWorks() throws Exception{
        Connection connection = DatabaseUtils.getConnection();
        Statement statement = connection.createStatement();
        boolean execute = statement.execute("select * from payroll.tblRecords");
        assertTrue("verify that we can execute a statement",execute);
    }

    @After
    public void addCsvPostTests() throws Exception {
        CSVImportService service = new CSVImportService();

        service.uploadCsvToDb(CsvTest.TestCSVFile);
    }

}
