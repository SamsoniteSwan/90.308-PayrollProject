package com.bluelight.servlets;

import com.bluelight.model.Employee;
import com.bluelight.model.PayPeriod;
import com.bluelight.services.EmployeeService;
import com.bluelight.services.ServiceException;
import com.bluelight.services.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PayrollServlet
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 3/4/2016
 */
public class PayPeriodServlet extends HttpServlet {

    protected static final String PARAMETER_KEY_EMPLOYEEID = "employeeId";
    protected static final String PARAMETER_KEY_LASTNAME = "employeeLastName";

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        String lastName = request.getParameter(PARAMETER_KEY_LASTNAME);
        String eeId = request.getParameter(PARAMETER_KEY_EMPLOYEEID);

        HttpSession session = request.getSession();

        EmployeeService service = ServiceFactory.getDBEmployeeServiceInstance();
        List<PayPeriod> payPeriods = new ArrayList<>();
        try {
            /*
             * if id is entered, use id to return payperiods for a distinct employee.
             *   Otherwise, use last name to return payperiods for all employees with
             *   the specified last name.
             */
            if (eeId != null && !eeId.isEmpty()) {
                Employee ee = service.getEmployeeById(eeId);
                payPeriods.addAll(service.getPayPeriods(ee));
            } else {
                List<Employee> employees = service.getEmployeesByLast(lastName);
                for (Employee ee : employees) {
                    payPeriods.addAll(service.getPayPeriods(ee));
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        session.setAttribute("payperiods", payPeriods);

        ServletContext servletContext = getServletContext();
        // dispatch attribute values to the Results page
        RequestDispatcher dispatcher =
                servletContext.getRequestDispatcher("/payperiods.jsp");
        dispatcher.forward(request, response);
        //dispatcher.include(request, response);

    }
}
