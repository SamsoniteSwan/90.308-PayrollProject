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
        String strStart = "2015-10-29 12:12:1";
        String strEnd = "2015-11-29 12:12:1";

        PayrollQuery qry = new PayrollQuery(employeeId, strStart, strEnd);

        assertEquals("Verify id", employeeId, qry.getEmployeeId());
        assertEquals("Verify start",
                new DateTime(qry.dateTimeFormatter.parseDateTime(strStart).toInstant().toDateTime()),
                qry.getFrom());
        assertEquals("Verify end",
                new DateTime(qry.dateTimeFormatter.parseDateTime(strEnd).toInstant().toDateTime()),
                qry.getUntil());

    }
}
