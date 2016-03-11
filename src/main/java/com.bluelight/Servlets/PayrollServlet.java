package com.bluelight.Servlets;

import com.bluelight.model.Employee;
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
public class PayrollServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        EmployeeService service = ServiceFactory.getDBEmployeeServiceInstance();
        List<Employee> employees = new ArrayList<>();
        try {
            employees = service.getEmployees();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        session.setAttribute("employees", employees);

        ServletContext servletContext = getServletContext();
        // dispatch attribute values to the Results page
        RequestDispatcher dispatcher =
                servletContext.getRequestDispatcher("/payperiods.jsp");
        dispatcher.forward(request, response);

    }
}
