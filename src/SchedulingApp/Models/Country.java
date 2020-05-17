package SchedulingApp.Models;

import javafx.beans.property.SimpleIntegerProperty;

import java.sql.Date;
import java.sql.Timestamp;

public class Country {
    int countryId;
    String country;
    Date createDate;
    User createdBy;
    Timestamp lastUpdate;
    User lastUpdatedBy;
}
