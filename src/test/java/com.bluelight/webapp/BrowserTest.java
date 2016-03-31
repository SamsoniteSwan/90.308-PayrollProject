package com.bluelight.webapp;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * BrowserTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 3/29/2016
 */
public class BrowserTest {

    private WebDriver driver;

    private static final String TEST_ID = "1111";
    private static final String TEST_NAME = "Smith";

    private WebElement fldEmployeeId;
    private WebElement fldEmployeeLastName;

    @Before
    public void setup() {
        driver = new HtmlUnitDriver();
        driver.get("http://localhost:8080/308-PayrollProject/pickemployee.jsp");
        // Employee Id text field

        fldEmployeeId = driver.findElement(By.name("employeeId"));
        fldEmployeeId.sendKeys(TEST_ID);

        // Employee Id text field
        fldEmployeeLastName = driver.findElement(By.name("employeeLastName"));
        fldEmployeeLastName.sendKeys(TEST_NAME);
    }

    @Test
    public void testInit() {
        assertNotNull(driver);
    }


    @Test
    public void testInputs() {

        assertEquals("ID in text box should be " + TEST_ID,
                TEST_ID, fldEmployeeId.getAttribute("value"));


        assertEquals("NAME in text box should be " + TEST_NAME,
                TEST_NAME, fldEmployeeLastName.getAttribute("value"));
    }

}
