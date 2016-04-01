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
 * PayrollServletTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 4/1/2016
 */
public class PayrollServletTest {

    private PayrollServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    private HttpSession mockSession;
    private ServletContext mockContext;

    private ServletConfig mockConfig;
    private RequestDispatcher mockDispatcher;

    @Before
    public void setUp() throws Exception {

        servlet = new PayrollServlet();

        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);

        mockSession = mock(HttpSession.class);
        mockConfig = mock(ServletConfig.class);

        mockContext = mock(ServletContext.class);
        mockDispatcher = mock(RequestDispatcher.class);

        // init is required when not processed as test
        servlet.init(mockConfig);

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
