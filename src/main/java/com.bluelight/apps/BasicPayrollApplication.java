package com.bluelight.apps;

import com.bluelight.model.*;
import com.bluelight.services.*;
import com.bluelight.utils.DatabaseUtils;
import com.bluelight.utils.DateTimeParser;
import com.bluelight.utils.Interval;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

/**
 * BasicPayrollApplication
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public class BasicPayrollApplication {

    private CSVImportService csvImportService;
    private EmployeeService employeeService;

    /**
     * An enumeration that indicates how the program terminates (ends)
     */
    private enum ProgramTerminationStatusEnum {

        // for now, we just have normal or abnormal but could more specific ones as needed.
        NORMAL(0),
        ABNORMAL(-1);

        // when the program exits, this value will be reported to underlying OS
        private int statusCode;

        /**
         * Create a new  ProgramTerminationStatusEnum
         *
         * @param statusCodeValue the value to return the OS. A value of 0
         *                        indicates success or normal termination.
         *                        non 0 numbers indicate abnormal termination.
         */
        ProgramTerminationStatusEnum(int statusCodeValue) {
            this.statusCode = statusCodeValue;
        }

        /**
         * @return The value sent to OS when the program ends.
         */
        private int getStatusCode() {
            return statusCode;
        }
    }

    /**
     * Terminate the application.
     *
     * @param statusCode        an enum value that indicates if the program terminated ok or not.
     * @param diagnosticMessage A message to display to the user when the program ends.
     *                          This should be an error message in the case of abnormal termination
     *                          <p/>
     *                          NOTE: This is an example of DRY in action.
     *                          A program should only have one exit point. This makes it easy to do any clean up
     *                          operations before a program quits from just one place in the code.
     *                          It also makes for a consistent user experience.
     */
    private static void exit(ProgramTerminationStatusEnum statusCode, String diagnosticMessage) {
        if (statusCode == ProgramTerminationStatusEnum.NORMAL) {
            System.out.println(diagnosticMessage);
        } else if (statusCode == ProgramTerminationStatusEnum.ABNORMAL) {
            System.err.println(diagnosticMessage);
        } else {
            throw new IllegalStateException("Unknown ProgramTerminationStatusEnum.");
        }
        System.exit(statusCode.getStatusCode());
    }

    /**
     * Create a new Application.
     *
     * @param employeeService the StockService this application instance should use for
     *                     payroll queries.
     *                     <p/>
     *                     NOTE: this is a example of Dependency Injection in action.
     */
    public BasicPayrollApplication(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Given a <CODE>PayrollQuery</CODE> get back a the info about the stock to display to th user.
     *
     * @param payrollQuery the stock to get data for.
     * @return a String with the stock data in it.
     * @throws ServiceException If data about the stock can't be retrieved. This is a
     *                               fatal error.
     */
    public String displayDayRecords(PayrollQuery payrollQuery) throws ServiceException {
        StringBuilder stringBuilder = new StringBuilder();

        List<WorkDay> days = employeeService.getWorkdays(payrollQuery.getEmployeeId(), payrollQuery.getFrom(), payrollQuery.getUntil());
        stringBuilder.append("Pay Periods for: " + payrollQuery.getEmployeeId() + "\n");
        for (WorkDay day : days) {
            stringBuilder.append(day.toString());
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        // be optimistic init to positive values
        ProgramTerminationStatusEnum exitStatus = ProgramTerminationStatusEnum.NORMAL;
        String programTerminationMessage = "Normal program termination.";
        if (args.length != 2) {
            exit(ProgramTerminationStatusEnum.ABNORMAL,
                    "Please supply 2 arguments a stock symbol, a start date (MM/DD/YYYY) and end date (MM/DD/YYYY)");
        }
        try {

            PayrollQuery payrollQuery = new PayrollQuery(args[0], args[1], args[2]);
            EmployeeService employeeService = ServiceFactory.getDBEmployeeServiceInstance();

            BasicPayrollApplication basicPayrollApplication =
                    new BasicPayrollApplication(employeeService);

            basicPayrollApplication.displayDayRecords(payrollQuery);

        } catch (ParseException e) {
            exitStatus = ProgramTerminationStatusEnum.ABNORMAL;
            programTerminationMessage = "Invalid date data: " + e.getMessage();
        } catch (ServiceException e) {
            exitStatus = ProgramTerminationStatusEnum.ABNORMAL;
            programTerminationMessage = "StockService failed: " + e.getMessage();
        } catch (Throwable t) {
            exitStatus = ProgramTerminationStatusEnum.ABNORMAL;
            programTerminationMessage = "General application error: " + t.getMessage();
        }

        exit(exitStatus, programTerminationMessage);
        System.out.println("Oops could not parse a date");
    }



}
