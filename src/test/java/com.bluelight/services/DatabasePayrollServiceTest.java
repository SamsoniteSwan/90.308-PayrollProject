package com.bluelight.services;

import com.bluelight.services.DatabasePayrollService;
import com.bluelight.utils.DatabaseInitializationException;
import com.bluelight.utils.DatabaseUtils;
import org.junit.Before;

/**
 * DatabasePayrollServiceTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/9/2016
 */
public class DatabasePayrollServiceTest {

    private DatabasePayrollService databasePayrollService;

    @Before
    public void setUp() throws DatabaseInitializationException {
        DatabaseUtils.initializeDatabase(DatabaseUtils.initializationFile);
        databasePayrollService = new DatabasePayrollService();

    }
}
