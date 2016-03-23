package com.bluelight.servlets;

import com.bluelight.model.Employee;
import com.bluelight.model.PayPeriod;
import com.bluelight.services.CSVImportService;
import com.bluelight.services.EmployeeService;
import com.bluelight.services.ServiceException;
import com.bluelight.services.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PayrollServlet
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 3/4/2016
 */
public class CsvImportServlet extends HttpServlet {

    protected static final String LOCATION_PARAMETER_KEY = "filePath";

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {





        Part filePart = request.getPart(LOCATION_PARAMETER_KEY);

        String location = getSubmittedFileName(filePart);

        HttpSession session = request.getSession();

        CSVImportService service = ServiceFactory.getCsvImportServiceInstance();

        try {
            service.uploadCsvToDb(location);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        //TODO: is the rest needed?
        /*
        session.setAttribute("payperiods", payPeriods);

        ServletContext servletContext = getServletContext();
        // dispatch attribute values to the Results page
        RequestDispatcher dispatcher =
                servletContext.getRequestDispatcher("/payperiods.jsp");
        //dispatcher.forward(request, response);
        dispatcher.include(request, response);

        */
    }

    private static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}
