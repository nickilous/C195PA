package SchedulingApp.Models;

import SchedulingApp.DataBase.DataBaseManager;

import java.sql.Date;
import java.sql.Timestamp;

public class Address {
    int addressId;
    String address;
    String address2;
    int cityId;
    String postalCode;
    String phone;

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
}
