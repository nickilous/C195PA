package SchedulingApp.Models;

import java.sql.Date;
import java.sql.Timestamp;

public class Customer {
    int customerId;
    String customerName;
    int addressId;
    int active;
    Date createDate;
    User createdBy;
    Timestamp lastUpdate;
    User lastUpdatedBy;
}
