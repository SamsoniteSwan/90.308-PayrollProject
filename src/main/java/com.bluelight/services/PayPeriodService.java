package com.bluelight.services;

import com.bluelight.model.Employee;
import com.bluelight.model.PayPeriod;

import java.util.List;

/**
 * PayPeriodService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 3/4/2016
 */
public interface PayPeriodService {

    List<PayPeriod> getAllPayPeriods() throws ServiceException;

}
