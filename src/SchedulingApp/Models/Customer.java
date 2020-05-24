package SchedulingApp.Models;

import SchedulingApp.Exceptions.UserFieldsEmptyException;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;

public class Customer {
    int customerId;
    String customerName;
    int addressId;
    int active;

    public Customer(){

    }

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

    // Validation
    public static void isCustomerValid(String customerName, String address, String city,
                                         String country, String postalCode, String phone) throws UserFieldsEmptyException {
        String errorMessage = "";
        if (customerName.length() == 0) {
            errorMessage = errorMessage + "Customer Name Field Empty";
        } else
        if (address.length() == 0) {
            errorMessage = errorMessage + "Address Field Empty";
        } else
        if (city.length() == 0) {
            errorMessage = errorMessage + "City Not Selected";
        } else
        if (country.length() == 0) {
            errorMessage = errorMessage + "Country Not Selected";
        } else
        if (postalCode.length() == 0) {
            errorMessage = errorMessage + "Postal Code Field Empty";
        } else
        if (phone.length() == 0) {
            errorMessage = errorMessage + "Phone Field Empty";
        }
        if (!errorMessage.isEmpty()){
            throw new UserFieldsEmptyException(errorMessage);
        }
    }
}
