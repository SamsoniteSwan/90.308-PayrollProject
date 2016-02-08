package com.bluelight.model;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * PayrollData
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public abstract class PayrollData {

    /**
     * Provide a single SimpleDateFormat for consistency
     * and to avoid duplicated code.
     */
    protected DateTimeFormatter dateTimeFormatter;

    public static final String dateFormat = "YYYY-MM-dd HH:mm:ss";

    /**
     * Base constructor for StockData classes.
     * Initialize member data that is shared with sub classes.
     */

    public PayrollData() {
        dateTimeFormatter = DateTimeFormat.forPattern(dateFormat);
    }
}
