package com.bluelight.model;

import org.joda.time.DateTime;
import org.joda.time.Instant;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * PayrollQuery
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public class PayrollQuery extends PayrollData {
    private final String employeeId;
    private final Instant from;
    private final Instant until;

    /**
     * Create a new instance from string data. This constructor will convert
     * dates described as a String to Date objects.
     *
     * @param employeeId the stock symbol
     * @param from   the start date as a string in the form of yyyy/MM/dd
     * @throws ParseException if the format of the date String is incorrect. If this happens
     *                        the only recourse is to try again with a correctly formatted String.
     */
    public PayrollQuery(@NotNull String employeeId, @NotNull String from, @NotNull String until) throws ParseException {
        super();

        this.employeeId = employeeId;
        this.from = dateTimeFormatter.parseDateTime(from).toInstant();
        this.until = dateTimeFormatter.parseDateTime(until).toInstant();

    }


    /**
     * @return get the stock symbol associated with this query
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @return get the start Calendar associated with this query
     */
    public DateTime getFrom() {
        return from.toDateTime();
    }

    /**
     * @return get the end Calendar associated with this query
     */
    public DateTime getUntil() {
        return until.toDateTime();
    }
}
