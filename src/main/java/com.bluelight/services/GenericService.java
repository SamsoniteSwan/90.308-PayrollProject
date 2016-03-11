package com.bluelight.services;

import java.util.List;

/**
 * GenericService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 3/8/2016
 */
public interface GenericService<G> {

    List<G> getAll();

}
