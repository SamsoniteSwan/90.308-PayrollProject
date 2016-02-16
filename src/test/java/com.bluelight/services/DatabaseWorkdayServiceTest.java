package com.bluelight.services;

import com.bluelight.model.WorkDay;
import com.bluelight.model.WorkDayTest;
import com.bluelight.utils.DatabaseUtils;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * DatabaseWorkdayServiceTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/14/2016
 */
public class DatabaseWorkdayServiceTest {

    private WorkdayService workdayService;

    /**
     * Initializes the database from the initialization file
     * @throws Exception
     */
    private void initDb() throws Exception {
        DatabaseUtils.initializeDatabase(DatabaseUtils.initializationFile);
    }

    // do not assume db is correct
    @Before
    public void setUp() throws Exception {
        // we could also copy db state here for later restore before initializing
        initDb();
        workdayService = ServiceFactory.getDBWorkdayServiceInstance();
    }

/*
    @Test
    public void test() {
        boolean found = false;


        for (WorkDay day : workdayList) {
            Timestamp returnedWorkDate = day.getDate();
            Calendar returnCalendar = Calendar.getInstance();
            returnCalendar.setTimeInMillis(returnedWorkDate.getTime());
            if (returnCalendar.get(Calendar.MONTH) == WorkDayTest.startCalendar.get(Calendar.MONTH)
                    &&
                    returnCalendar.get(Calendar.YEAR) == WorkDayTest.startCalendar.get(Calendar.YEAR)
                    &&
                    returnCalendar.get(Calendar.DAY_OF_MONTH) == WorkDayTest.startCalendar.get(Calendar.DAY_OF_MONTH)
                    &&
                    (day.getDate().compareTo(WorkDayTest.FIRSTTEST_DAY) == 0)) {
                found = true;
                break;
            }
        }
    }
    */
}
