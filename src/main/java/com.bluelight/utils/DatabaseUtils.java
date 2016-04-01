package com.bluelight.utils;

import com.ibatis.common.jdbc.ScriptRunner;
import com.bluelight.services.EmployeeService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

/**
 * A class that contains database related utility methods.
 */
public class DatabaseUtils {

    public static final String initializationFile = "./src/main/sql/DBInitialization_Payroll.sql" ;
    public static Connection connection;

    private static SessionFactory sessionFactory;
    private static Configuration configuration;
    /*
* @return SessionFactory for use with database transactions
*/
    public static SessionFactory getSessionFactory() {

        // singleton pattern
        synchronized (EmployeeService.class) {
            if (sessionFactory == null) {

                Configuration configuration = getConfiguration();

                ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .buildServiceRegistry();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            }
        }
        return sessionFactory;
    }

    /**
     * Create a new or return an existing database configuration object.
     *
     * @return a Hibernate Configuration instance.
     */
    private static Configuration getConfiguration() {

        synchronized (DatabaseUtils.class) {
            if (configuration == null) {
                configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
            }
        }
        return configuration;
    }

    public static Connection getConnection() throws DatabaseConnectionException {
        try {
            if (connection == null || connection.isClosed()) {
                //Connection connection = null;
                Configuration configuration = getConfiguration();


                Class.forName("com.mysql.jdbc.Driver");
                String databaseUrl = configuration.getProperty("connection.url");
                String username = configuration.getProperty("hibernate.connection.username");
                String password = configuration.getProperty("hibernate.connection.password");
                connection = DriverManager.getConnection(databaseUrl, username, password);
            }
                    // an example of throwing an exception appropriate to the abstraction.
                } catch (ClassNotFoundException | SQLException e) {
                    throw new DatabaseConnectionException("Could not connect to database." + e.getMessage(), e);
                }


        return connection;
    }

    /**
     * A utility method that runs a db initialize script.
     *
     * @param initializationScript full path to the script to run to create the schema
     * @throws DatabaseInitializationException
     */
    public static void initializeDatabase(String initializationScript) throws DatabaseInitializationException {

        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            ScriptRunner runner = new ScriptRunner(connection, false, false);
            InputStream inputStream = new FileInputStream(initializationScript);

            InputStreamReader reader = new InputStreamReader(inputStream);

            runner.runScript(reader);
            reader.close();
            connection.commit();
            connection.close();

        } catch (DatabaseConnectionException | SQLException | IOException e) {
            throw new DatabaseInitializationException("Could not initialize db because of:"
                    + e.getMessage(), e);
        }

    }

    /**
     * Execute SQL code
     * @param someSQL  the code to execute
     * @return true if the operation succeeded.
     * @throws DatabaseException if accessing and executing the sql failed in an unexpected way.
     *
     */
    public static boolean executeSQL(String someSQL) throws DatabaseException {
        Connection connection = null;
        boolean returnValue = false;
        try {
            connection = DatabaseUtils.getConnection();
            Statement statement = connection.createStatement();
            returnValue = statement.execute(someSQL);
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
        return returnValue;
    }

    /**
     * Helper method that converts Strings to DateTime instances.
     *
     * @param dateString a data and time in this format: StockData.dateFormat
     * @return a DateTime instance set to the time in the string.
     * @throws ParseException if the string is not in the correct format, we can't tell what
     *                        time it is, and therefore can't make a DateTime set to that time.
     */
    public static DateTime makeDateTimeFromString(String dateString) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return DateTime.parse(dateString,formatter);
    }

}
