package com.bluelight.services;

import com.bluelight.model.Employee;
import com.bluelight.model.PayRecord;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        return result;
    }

    /**
     * Helper method for creating the csv reader
     * @param fileSource
     * @return
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

    public List importedCsv(String fileSource) {
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(Employee.class);
        // the fields to bind to in your JavaBean
        String[] columns = new String[] {"First Name", "Last Name", "id"};

        strat.setColumnMapping(columns);

        CsvToBean csv = new CsvToBean();
        return csv.parse(strat, getReader(fileSource));
    }

    public List importedCsvPeriod(String fileSource) {

        //HeaderColumnNameMappingStrategy<PayRecord> strat = new HeaderColumnNameMappingStrategy<>();
        HeaderColumnNameTranslateMappingStrategy<PayRecord> strat = new HeaderColumnNameTranslateMappingStrategy<>();
        strat.setType(PayRecord.class);
        strat.setColumnMapping(columnMap());
        CsvToBean<PayRecord> csvToBean = new CsvToBean<>();
        List<PayRecord> recordList = csvToBean.parse(strat, getReader(fileSource));

        return recordList;
    }


    public void importCSV(String fileSource) {
        CSVReader reader = null;
        List values = new ArrayList<String>();
        try {
            reader = new CSVReader(new FileReader(fileSource));
            values = reader.readAll();

        } catch (java.io.IOException e) {

                e.printStackTrace();
            }
    }

}
