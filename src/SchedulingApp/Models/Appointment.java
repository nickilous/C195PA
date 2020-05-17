package SchedulingApp.Models;

import java.sql.Date;
import java.sql.Timestamp;

public class Appointment {
    int appointmentId;
    int customerId;
    String title;
    String description;
    String location;
    String contact;
    String type;
    String url;
    Date start;
    Date end;
    Date createDate;
    User createdBy;
    Timestamp lastUpdate;
    User lastUpdateBy;
}
