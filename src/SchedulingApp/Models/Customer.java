package SchedulingApp.Models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;

public class Customer {
    int customerId;
    String customerName;
    int addressId;
    int active;
    Date createDate;
    User createdBy;
    Timestamp lastUpdate;
    User lastUpdatedBy;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    // Validation
    public static String isCustomerValid(String customerName, String address, String city,
                                         String country, String postalCode, String phone) {
        ResourceBundle rb = ResourceBundle.getBundle("Customer", Locale.getDefault());
        String errorMessage = "";
        if (customerName.length() == 0) {
            errorMessage = errorMessage + rb.getString("errorCustomerName");
        }
        if (address.length() == 0) {
            errorMessage = errorMessage + rb.getString("errorAddress");
        }
        if (city.length() == 0) {
            errorMessage = errorMessage + rb.getString("errorCity");
        }
        if (country.length() == 0) {
            errorMessage = errorMessage + rb.getString("errorCountry");
        }
        if (postalCode.length() == 0) {
            errorMessage = errorMessage + rb.getString("errorPostalCode");
        }
        if (phone.length() == 0) {
            errorMessage = errorMessage + rb.getString("errorPhone");
        }
        return errorMessage;
    }
}
