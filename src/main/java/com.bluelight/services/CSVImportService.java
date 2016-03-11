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
            //period.setHourlyRate(new BigDecimal(record.getWage()));
            period.setHourlyRate(record.getWage());
            eeService.addPayPeriod(period, ee);
            ee.setPayPeriods(new ArrayList<>());
            ee.getPayPeriods().add(period);


            // establish Worklog days
            int dayct = (int) (1 + (endstamp.getTime() - startstamp.getTime()) / (1000 * 60 * 60 * 24));
            BigDecimal days = new BigDecimal(dayct).setScale(BigDecimal.ROUND_HALF_UP);
            //MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
            BigDecimal hrsPerDay = record.getHoursWorked().divide(days, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal vacaPerDay = record.getVacationUsed().divide(days, 2, BigDecimal.ROUND_HALF_UP);

            DateTime endDay = new DateTime(endstamp);
            DateTime curDay = new DateTime(startstamp);

            ee.setWorkDays(new ArrayList<>());

            while (endDay.isAfter(curDay) || endDay.isEqual(curDay)) {
                WorkDay day = new WorkDay();
                day.setEmployee(ee);
                day.setDate(new Timestamp(curDay.getMillis()));
                day.setHoursWorked(hrsPerDay);
                day.setVacationUsed(vacaPerDay);

                eeService.addWorkDay(ee, day);

                ee.getWorkDays().add(day);
                curDay = curDay.plusDays(1);
            }

            eeService.addOrUpdateEmployee(ee);
        }

    }

}
