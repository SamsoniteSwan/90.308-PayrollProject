
package com.bluelight.apps;

import com.bluelight.apps.BasicPayrollApplication;
import com.bluelight.services.PayrollService;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import static org.junit.Assert.assertNotNull;


/**
 * PayrollApplicationTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/8/2016
 */
public class PayrollApplicationTest {

    private BasicPayrollApplication app;
    private PayrollService prServiceMock;

    @Before
    public void setUp() {
        prServiceMock = mock(PayrollService.class);
        app = new BasicPayrollApplication(prServiceMock);
    }

    @Test
    public void testValidConstruction() {

        assertNotNull(app);
    }

}
