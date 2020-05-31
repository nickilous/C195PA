package SchedulingApp.Models;

import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Exceptions.AddressFieldsEmptyException;
import SchedulingApp.Exceptions.UserFieldsEmptyException;

import java.sql.Date;
import java.sql.Timestamp;

public class Address {
    int addressId;
    String address;
    String address2;
    String postalCode;
    String phone;

    int cityId;
    String city;

    int countryId;
    String country;

    public Address(){
        addressId = DataBaseManager.getNextId("address");
    }
    public Address(int addressId){this.addressId = addressId;}

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static void isAddressValid(String address,
                                      String postalCode,
                                      String phone) throws AddressFieldsEmptyException {
        String errorMessage = "";
        if (address.length() == 0) {
            errorMessage = errorMessage + "Address Field Empty";
        } else
        if (postalCode.length() == 0) {
            errorMessage = errorMessage + "Postal Code Field Empty";
        } else
        if (phone.length() == 0) {
            errorMessage = errorMessage + "Phone Field Empty";
        }
        if (!errorMessage.isEmpty()){
            throw new AddressFieldsEmptyException(errorMessage);
        }
    }
}
