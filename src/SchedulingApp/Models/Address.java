package SchedulingApp.Models;

import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Exceptions.AddressFieldsEmptyException;
import SchedulingApp.Exceptions.UserFieldsEmptyException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.sql.Timestamp;

public class Address {
    SimpleIntegerProperty addressId = new SimpleIntegerProperty();
    SimpleStringProperty address = new SimpleStringProperty("");
    SimpleStringProperty address2 = new SimpleStringProperty("");
    SimpleStringProperty postalCode = new SimpleStringProperty("");
    SimpleStringProperty phone = new SimpleStringProperty("");

    SimpleIntegerProperty cityId = new SimpleIntegerProperty(0);
    SimpleStringProperty city = new SimpleStringProperty("");

    SimpleIntegerProperty countryId = new SimpleIntegerProperty(0);
    SimpleStringProperty country = new SimpleStringProperty("");

    public Address(){}
    public Address(int addressId){this.addressId.set(addressId);}

    public int getAddressId() {
        return addressId.get();
    }

    public SimpleIntegerProperty addressIdProperty() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getAddress2() {
        return address2.get();
    }

    public SimpleStringProperty address2Property() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2.set(address2);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public SimpleStringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public int getCityId() {
        return cityId.get();
    }

    public SimpleIntegerProperty cityIdProperty() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId.set(cityId);
    }

    public String getCity() {
        return city.get();
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public int getCountryId() {
        return countryId.get();
    }

    public SimpleIntegerProperty countryIdProperty() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId.set(countryId);
    }

    public String getCountry() {
        return country.get();
    }

    public SimpleStringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
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
