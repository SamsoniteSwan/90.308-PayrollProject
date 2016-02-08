package com.bluelight.services;

import com.bluelight.model.PayRecord;
import com.bluelight.utils.DatabaseUtils;
import com.bluelight.utils.DatabaseConnectionException;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DatabasePayrollService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public class DatabasePayrollService implements PayrollService {

    /**
     * Return the current price for a share of stock  for the given symbol
     *
     * @param employeeId the stock symbol of the company you want a quote for.
     *               e.g. APPL for APPLE
     * @return a  <CODE>BigDecimal</CODE> instance
     * @throws ServiceException if using the service generates an exception.
     *                               If this happens, trying the service may work, depending on the actual cause of the
     *                               error.
     */
    @Override
    public PayRecord getRecord(String employeeId) throws ServiceException {
        PayRecord result;

        try {
            Connection connection = DatabaseUtils.getConnection();
            Statement statement = connection.createStatement();

            // Nested query using the MIN() function to get
            String queryString = "SELECT * FROM tblRecords WHERE time =" +
                    "(SELECT MAX(time) FROM quotes WHERE symbol = '" + employeeId +
                    "') AND (symbol = '" + employeeId + "')";
            ResultSet resultSet = statement.executeQuery(queryString);

            if (!resultSet.isBeforeFirst()) { // make sure a value exists
                throw new ServiceException("There is no payroll data for:" + employeeId);
            } else {
                resultSet.next(); // move to the first (and only) record
                String period = resultSet.getString("period");
                //DateTime time = new DateTime(resultSet.getTimestamp("time"));
                //BigDecimal price = resultSet.getBigDecimal("price");
                result = new PayRecord(employeeId, period);
            }
            connection.close();

        } catch (DatabaseConnectionException | SQLException exception) {
            throw new ServiceException(exception.getMessage(), exception);
        }

        return result;
    }

    @Override
    public List getRecords(String employeeId) throws ServiceException {
        return getRecordStream(employeeId).collect(Collectors.toList());
    }
    /**
     * TEMP using stream
     *
     * @param employeeId
     * @return
     * @throws ServiceException
     */
    public Stream<PayRecord> getRecordStream(String employeeId) throws ServiceException {

        List<PayRecord> recordList = new ArrayList<>();

        try {
            Connection connection = DatabaseUtils.getConnection();
            Statement statement = connection.createStatement();
            // Sort by time so most recent quote is first, and is the only record returned
            // (LIMIT is MySQL specific)
            String queryString = "SELECT * from tblRecords WHERE employeeId = '" + employeeId + "'";
            ResultSet resultSet = statement.executeQuery(queryString);


            while(resultSet.next()) { // move to the first (and only) record
                String period = resultSet.getString("period");
                //DateTime time = new DateTime(resultSet.getDate("time"));
                //BigDecimal price = resultSet.getBigDecimal("price");
                recordList.add(new PayRecord(employeeId, period));
            }

        } catch (DatabaseConnectionException | SQLException exception) {
            throw new ServiceException(exception.getMessage(), exception);
        }

        return recordList.stream();
    }
}
