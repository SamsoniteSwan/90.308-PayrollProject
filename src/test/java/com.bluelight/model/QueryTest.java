package com.bluelight.model;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * QueryTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 2, 2/23/2016
 */
public class QueryTest {

    @Test
    public void testBasicConstruction() throws Exception {

        String employeeId = "1111";
        PayrollQuery qry = new PayrollQuery(employeeId, "2015-10-29 12:12:1", "2015-11-29 12:12:1");
        assertEquals("Verify id", employeeId, qry.getEmployeeId());
        assertEquals("Verify start",
                new DateTime(qry.dateTimeFormatter.parseDateTime("2015-10-29 12:12:1").toInstant()),
                qry.getFrom());
        assertEquals("Verify end",
                new DateTime(qry.dateTimeFormatter.parseDateTime("2015-11-29 12:12:1").toInstant()),
                qry.getUntil());

    }
}
