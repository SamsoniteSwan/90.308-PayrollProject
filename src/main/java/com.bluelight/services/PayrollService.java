package com.bluelight.services;

import com.bluelight.model.PayRecord;

import java.util.List;

/**
 * PayrollService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public interface PayrollService {

    /**
     * Return the current price for a share of stock  for the given symbol
     *
     * @param id the stock symbol of the company you want a quote for.
     *               e.g. APPL for APPLE
     * @return a  <CODE>BigDecimal</CODE> instance
     * @throws ServiceException if using the service generates an exception.
     *                               If this happens, trying the service may work, depending on the actual cause of the
     *                               error.
     */
    PayRecord getRecord(String id) throws ServiceException;

    /**
     * Get a list of PayRecord instances based on an Id
     *
     * @param id ID of the payroll record
     * @throws ServiceException if using the service generates an exception.
     * If this happens, trying the service may work, depending on the actual cause of the
     * error.
     */
    List<PayRecord> getRecords(String id) throws ServiceException;



}
