package com.bluelight.servlets;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * PayPeriodServletTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 3/31/2016
 */
public class PayPeriodServletTest {

    private PayPeriodServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    private HttpSession mockSession;
    private ServletContext mockContext;

    private ServletConfig mockConfig;
    private RequestDispatcher mockDispatcher;

    @Before
    public void setUp() throws Exception {

        servlet = new PayPeriodServlet();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);

        mockSession = mock(HttpSession.class);
        mockConfig = mock(ServletConfig.class);

        mockContext = mock(ServletContext.class);
        mockDispatcher = mock(RequestDispatcher.class);

        // init is required when not processed as test
        servlet.init(mockConfig);

        when(mockRequest.getParameter(servlet.PARAMETER_KEY_EMPLOYEEID)).
                thenReturn("1111");

        when(mockRequest.getParameter(servlet.PARAMETER_KEY_LASTNAME)).
                thenReturn("Smith");

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(servlet.getServletContext()).thenReturn(mockContext);
        when(mockContext.getRequestDispatcher(anyString())).thenReturn(mockDispatcher);
    }

    /**
     * test the servlet construction
     */
    @Test
    public void testValidConstruction() {
        assertNotNull(servlet);
    }

    /**
     * Test the doPost method by verifying it works successfully
     *
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testDoPost() throws IOException, ServletException {

        servlet.doPost(mockRequest, mockResponse);

        verify(mockDispatcher).forward(mockRequest, mockResponse);

    }
}
