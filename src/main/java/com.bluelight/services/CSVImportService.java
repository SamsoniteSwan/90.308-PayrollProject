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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
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
     * Map each line in csv file to PayRecord object
     *
     * @param fileSource string path to csv file
     *
     * @return a list of PayRecord instances created
     */
    public List<PayRecord> loadCsvPeriods(String fileSource) throws FileNotFoundException {

        HeaderColumnNameTranslateMappingStrategy<PayRecord> strat = new HeaderColumnNameTranslateMappingStrategy<>();
        strat.setType(PayRecord.class);
        strat.setColumnMapping(columnMap());
        CsvToBean<PayRecord> csvToBean = new CsvToBean<>();
        List<PayRecord> recordList = csvToBean.parse(strat, new CSVReader(new FileReader(fileSource)));


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
    public void uploadCsvToDb(String source) throws FileNotFoundException, ServiceException {
        DatabaseEmployeeService eeService = new DatabaseEmployeeService();
        List<PayRecord> records = loadCsvPeriods(source);

        for (PayRecord record : records) {

            Employee ee = record.getEmployee();

            eeService.addOrUpdateEmployee(ee);

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


            period.setEmployee(ee);
            period.setStartDay(startstamp);
            period.setEndDay(endstamp);
            period.setHourlyRate(record.getWage());
            period.setHoursWorked(record.getHoursWorked());
            period.setVacationUsed(record.getVacationUsed());
            eeService.addPayPeriod(period, ee);
            ee.setPayPeriods(new ArrayList<>());
            ee.getPayPeriods().add(period);

            eeService.addOrUpdateEmployee(ee);
        }
    }

}
