package com.bluelight.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Models the tbl_People table
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 10/6/2015
 */
@Entity
@Table(name="tbl_People")
public interface Person {


    String getFirstName();
    String getLastName();
    Timestamp getBirthDate();

    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setBirthDate(Timestamp birthDate);


}
