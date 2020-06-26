package SchedulingApp.Models;


import SchedulingApp.Exceptions.InvalidCustomerException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Customer {
    SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    SimpleIntegerProperty active = new SimpleIntegerProperty();
    SimpleIntegerProperty addressId = new SimpleIntegerProperty();
    SimpleIntegerProperty cityId = new SimpleIntegerProperty();
    SimpleIntegerProperty countryId = new SimpleIntegerProperty();


    StringProperty customerName = new SimpleStringProperty("");
    StringProperty address = new SimpleStringProperty("");
    StringProperty address2 = new SimpleStringProperty("");
    StringProperty postalCode = new SimpleStringProperty("");
    StringProperty phone = new SimpleStringProperty("");
    StringProperty city = new SimpleStringProperty("");
    StringProperty country = new SimpleStringProperty("");


    public Customer(){
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public SimpleIntegerProperty customerIdProperty() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }

    public int getActive() {
        return active.get();
    }

    public SimpleIntegerProperty activeProperty() {
        return active;
    }

    public void setActive(int active) {
        this.active.set(active);
    }

    public int getAddressId() {
        return addressId.get();
    }

    public SimpleIntegerProperty addressIdProperty() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
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

    public int getCountryId() {
        return countryId.get();
    }

    public SimpleIntegerProperty countryIdProperty() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId.set(countryId);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getAddress2() {
        return address2.get();
    }

    public StringProperty address2Property() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2.set(address2);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public static boolean isValidInput(Customer Cust) throws InvalidCustomerException {
        if (Cust.getCustomerName().equals("")) {
            throw new InvalidCustomerException("You must enter a customer name!");
        }
        return true;
    }
}
