package SchedulingApp.Models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class Appointment {
    int appointmentId;
    int customerId;
    int userId;
    String title;
    String description;
    String location;
    String contact;
    String type;
    String url;
    Date start;
    Date end;

    public int getUserId(){
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    // Validation
    public static String isAppointmentValid(Customer customer, String title, String description, String location,
                                            LocalDate appointmentDate, String startHour, String startMinute, String startAmPm,
                                            String endHour, String endMinute, String endAmPm) throws NumberFormatException {
        ResourceBundle rb = ResourceBundle.getBundle("Appointment", Locale.getDefault());
        String errorMessage = "";
        try {
            if (customer == null) {
                errorMessage = errorMessage + rb.getString("errorCustomer");
            }
            if (title.length() == 0) {
                errorMessage = errorMessage + rb.getString("errorTitle");
            }
            if (description.length() == 0) {
                errorMessage = errorMessage + rb.getString("errorDescription");
            }
            if (location.length() == 0) {
                errorMessage = errorMessage + rb.getString("errorLocation");
            }
            if (appointmentDate == null || startHour.equals("") || startMinute.equals("") || startAmPm.equals("") ||
                    endHour.equals("") || endMinute.equals("") || endAmPm.equals("")) {
                errorMessage = errorMessage + rb.getString("errorStartEndIncomplete");
            }
            if (Integer.parseInt(startHour) < 1 || Integer.parseInt(startHour) > 12 || Integer.parseInt(endHour) < 1 || Integer.parseInt(endHour) > 12 ||
                    Integer.parseInt(startMinute) < 0 || Integer.parseInt(startMinute) > 59 || Integer.parseInt(endMinute) < 0 || Integer.parseInt(endMinute) > 59) {
                errorMessage = errorMessage + rb.getString("errorStartEndInvalidTime");
            }
            if ((startAmPm.equals("PM") && endAmPm.equals("AM")) || (startAmPm.equals(endAmPm) && Integer.parseInt(startHour) != 12 && Integer.parseInt(startHour) > Integer.parseInt(endHour)) ||
                    (startAmPm.equals(endAmPm) && startHour.equals(endHour) && Integer.parseInt(startMinute) > Integer.parseInt(endMinute))) {
                errorMessage = errorMessage + rb.getString("errorStartAfterEnd");
            }
            if ((Integer.parseInt(startHour) < 9 && startAmPm.equals("AM")) || (Integer.parseInt(endHour) < 9 && endAmPm.equals("AM")) ||
                    (Integer.parseInt(startHour) >= 5 && Integer.parseInt(startHour) < 12 && startAmPm.equals("PM")) || (Integer.parseInt(endHour) >= 5 && Integer.parseInt(endHour) < 12 && endAmPm.equals("PM")) ||
                    (Integer.parseInt(startHour) == 12 && startAmPm.equals("AM")) || (Integer.parseInt(endHour)) == 12 && endAmPm.equals("AM")) {
                errorMessage = errorMessage + rb.getString("errorStartEndOutsideHours");
            }
            if (appointmentDate.getDayOfWeek().toString().toUpperCase().equals("SATURDAY") || appointmentDate.getDayOfWeek().toString().toUpperCase().equals("SUNDAY")) {
                errorMessage = errorMessage + rb.getString("errorDateIsWeekend");
            }
        }
        catch (NumberFormatException e) {
            errorMessage = errorMessage + rb.getString("errorStartEndInteger");
        }
        finally {
            return errorMessage;
        }
    }
}
