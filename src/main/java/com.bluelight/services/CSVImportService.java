package com.bluelight.services;

import com.bluelight.model.Employee;
import com.bluelight.model.PayPeriod;
import com.bluelight.model.PayRecord;
import com.bluelight.model.WorkDay;
import com.bluelight.utils.DatabaseUtils;
import com.bluelight.utils.DateTimeParser;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import org.joda.time.DateTime;

import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * CSVImportService
 * Class for reading .csv file
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public class CSVImportService {

    /**
     * Hashmap for setting the csv fields to the corresponding
     * variable names in PayRecord class
     * @return hashmap of <csvfield, classvariable>
     */
    private HashMap<String, String> columnMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("Period", "Period");
        result.put("Employee ID", "employeeId");
        result.put("First Name", "employeeFirst");
        result.put("Last Name", "employeeLast");
        result.put("Hourly Rate", "wage");
        result.put("Number of hours worked this period", "hoursWorked");
        result.put("Vacation used", "vacationUsed");
        return result;
    }

    /**
     * Helper method for creating the csv reader
     * @param fileSource path to csv file
     * @return a CSV Reader instance
     */
    public CSVReader getReader(String fileSource) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(fileSource));
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
        return reader;
    }


    /**
     * Map each line in csv file to PayRecord object
     *
     * @param fileSource string path to csv file
     *
     * @return a list of PayRecord instances created
     */
    public List<PayRecord> importedCsvPeriod(String fileSource) {

        HeaderColumnNameTranslateMappingStrategy<PayRecord> strat = new HeaderColumnNameTranslateMappingStrategy<>();
        strat.setType(PayRecord.class);
        strat.setColumnMapping(columnMap());
        CsvToBean<PayRecord> csvToBean = new CsvToBean<>();
        List<PayRecord> recordList = csvToBean.parse(strat, getReader(fileSource));


        for (PayRecord rec : recordList) {
            rec.setEmployee(rec.getEmployee());
        }
        return recordList;
    }

    /**
     * read a csv file and copy the values into the database.
     *
     * @param source string path to csv file.
     */
    public void uploadCsvToDb(String source) {

        DatabaseEmployeeService eeService = new DatabaseEmployeeService();
        List<PayRecord> records = importedCsvPeriod(source);
        for (PayRecord record : records) {

            Employee ee = record.getEmployee();
            if (ee == null) {

                ee = new Employee();
                ee.setEmployeeId(record.getEmployeeId());
                ee.setFirstName(record.getEmployeeFirst());
                ee.setLastName(record.getEmployeeLast());
                ee.setStatus("active");
                ee.setBirthDate(new Timestamp(1000));
            }



            /*
            * establish PayPeriod instace
             */
            PayPeriod period = new PayPeriod();
            List<String> startandend = DateTimeParser.startAndEnd(record.getPeriod());
            Timestamp startstamp = null;
            Timestamp endstamp = null;
            try {
                startstamp = new Timestamp(DatabaseUtils.makeDateTimeFromString(startandend.get(0)).getMillis());
                endstamp = new Timestamp(DatabaseUtils.makeDateTimeFromString(startandend.get(1)).getMillis());
            } catch (ParseException e) {
                Logger.getGlobal().warning("could not parse dates:" + e.getMessage());
            }


            period.setStartDay(startstamp);
            period.setEndDay(endstamp);
            period.setHourlyRate(new BigDecimal(record.getWage()));
            ee.getPayPeriods().add(period);
            /*
            //period.setEmployee(ee);
            try {
                eeService.addPayPeriod(period, ee);

                eeService.addOrUpdateEmployee(ee);
            } catch (ServiceException e) {
                Logger.getGlobal().warning("could not add PayPeriod:" + e.getMessage());

            }
            */
            // establish Worklog days
            int dayct = (int)(endstamp.getTime() - startstamp.getTime()) / (1000 * 60 * 60 * 24);
            float hrsPerDay = record.getHoursWorked()/dayct;
            float vacaPerDay = record.getVacationUsed()/dayct;

            DateTime endDay = new DateTime(endstamp);
            DateTime curDay = new DateTime(startstamp);

            while (endDay.isAfter(curDay)) {
                WorkDay day = new WorkDay();
                day.setEmployee(ee);
                day.setDate(new Timestamp(curDay.getMillis()));
                day.setHoursWorked(new BigDecimal(hrsPerDay));
                day.setVacationUsed(new BigDecimal(vacaPerDay));
                /*
                try {
                    eeService.addOrUpdateWorkday(day, ee);
                    eeService.addOrUpdateEmployee(ee);
                } catch (ServiceException e) {
                    Logger.getGlobal().warning("could not add workday:" + e.getMessage());
                }
                */
                ee.getWorkDays().add(day);
                curDay = curDay.plusDays(1);
            }

            try {

                eeService.addOrUpdateEmployee(ee);
            } catch (ServiceException e) {

                //TODO - add handling
            }


            // TODO - finish
            //employeeService.addOrUpdateEmployee(ee);
        }

    }

}
