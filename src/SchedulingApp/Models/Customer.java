package SchedulingApp.Models;

import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Exceptions.UserFieldsEmptyException;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;

public class Customer {
    int customerId;
    int active;
    int addressId;
    int cityId;
    int countryId;


    String customerName;
    String address;
    String address2;
    String postalCode;
    String phone;
    String city;
    String country;


    public Customer(){
        customerId = DataBaseManager.getNextId("customer");
    }
    public Customer(int customerId){
        this.customerId = customerId;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Validation
    public static void isCustomerValid(String customerName) throws UserFieldsEmptyException {
        String errorMessage = "";
        if (customerName.length() == 0) {
            errorMessage = errorMessage + "Customer Name Field Empty";
        }
        if (!errorMessage.isEmpty()){
            throw new UserFieldsEmptyException(errorMessage);
        }
    }
}
