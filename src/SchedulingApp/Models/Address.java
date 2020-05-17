package SchedulingApp.Models;

import java.sql.Date;
import java.sql.Timestamp;

public class Address {
    int addressId;
    String address;
    String address2;
    int cityId;
    String postalCode;
    String phone;
    User createdBy;
    Date createDate;
    Timestamp lastUpdate;
    User lastUpdatedBy;
}
