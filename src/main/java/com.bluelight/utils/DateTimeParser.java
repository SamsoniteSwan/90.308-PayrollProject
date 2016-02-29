package com.bluelight.utils;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * DateTimeParser takes a string and formats it as YYYY-MM-DD
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 11/3/2015
 */
public class DateTimeParser {

    /**
     * Takes a string from user input, and converts it into the format used
     * by the database.  Kept separate from StockData and other project-specific
     * classes to provide 'plug and play' functionality.
     *
     * @param datetext a string of text input
     * @return string in format YYYY-MM-DD hh:mm:ss
     */
    public static String fromString (String datetext) {

        String result;

        // Make array of strings by splitting string on certain chars
        String[] dateparts = datetext.split("/|:| |T");

        String timeString = " ";

        // set default values to 0
        int year = 0;
        int mo = 0;
        int day = 0;

        for (int i = 0; i < dateparts.length; i++) {
            // year is 4 digits, or the 3rd value
            if (dateparts[i].length() == 4 || (year == 0 && i == 2)) {
                year = Integer.parseInt(dateparts[i]);
            } else if (i < 3) {
                int temp = Integer.parseInt(dateparts[i]);
                if (temp <= 12 && mo == 0) { // assume month is first, unless first is greater than 12
                    mo = temp;
                } else {
                    day = temp;
                }
            } else if (i > 2) { // if the fourth position or more, it's a time value.  build a time string.
                timeString += dateparts[i] + ":";
            }
        }

        // remove the last character of timeString
        if (timeString.endsWith(":") || timeString.endsWith(" ")) {
            timeString = timeString.substring(0, timeString.length() - 1);
        }

        // if time isn't right (or is no value), enter default time
        if (timeString.length() < 8) {
            timeString = " 00:00:01";
        }

        // make a 2 digit year value into 4 digit
        if (year < 1000) { year = year + 2000; }

        // format mo and day with leading 0s and return string
        result = year + "-" + String.format("%02d", mo) + "-" + String.format("%02d", day) + timeString;
        return result;
    }

    public static ArrayList<String> startAndEnd(String bothDates) {
        String[] dateparts = bothDates.split("-");
        ArrayList<String> result = new ArrayList<>();
        for (String st : dateparts) {
            result.add(fromString(st));
        }
        return result;
    }

}