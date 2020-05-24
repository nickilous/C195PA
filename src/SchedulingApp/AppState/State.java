package SchedulingApp.AppState;

import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Locale;

public class State {
    private static Locale selectedLanguage;
    private static User user;
    private static Calendar calendar = Calendar.getInstance();
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static ObservableList<Address> addresses = FXCollections.observableArrayList();
    private static ObservableList<City> cities = FXCollections.observableArrayList();
    private static ObservableList<Country> countries = FXCollections.observableArrayList();

    public static void setUser(User user) {
        State.user = user;
    }
    public static User getUser() {
        return user;
    }

    public static Locale getSelectedLanguage() {
        return selectedLanguage;
    }
    public static void setSelectedLanguage(Locale selectedLanguage) {
        State.selectedLanguage = selectedLanguage;
    }

    // Getter for appointments
    public static ObservableList<Appointment> getAppointments() {
        return appointments;
    }
    // Getter for customers
    public static ObservableList<Customer> getCustomers() {
        return customers;
    }

    public static ObservableList<City> getCities() {
        return cities;
    }

    public static ObservableList<Country> getCountries() {
        return countries;
    }

    public static ObservableList<Address> getAddresses() {
        return addresses;
    }

    public static void setAddresses(ObservableList<Address> addresses) {
        State.addresses = addresses;
    }

    public static void loadCustomers() {
        State.clearCustomers();
        ResultSet rs = DataBaseManager.getAllCustomerData();
        try {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setActive(rs.getInt("active"));
                customer.setCustomerName(rs.getString("customerName"));
                customer.setAddressId(rs.getInt("addressId"));
                customer.setCustomerId(rs.getInt("customerId"));
                customers.add(customer);
            }
            rs.close();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void clearCustomers(){
        customers.clear();
    }
    public static void clearCities(){
        cities.clear();
    }
    public static void clearCountries(){
        countries.clear();
    }
    public static void addCustomer(Customer customer){
        customers.add(customer);
    }
    public static void addAddress(Address address){
        addresses.add(address);
    }
    public static void loadCities(){
        State.clearCities();
        ResultSet rs = DataBaseManager.getAllCities();
        try {
            while (rs.next()) {
                City city = new City();
                city.setCityId(rs.getInt("cityId"));
                city.setCity(rs.getString("city"));
                city.setCountryId(rs.getInt("countryId"));
                cities.add(city);
            }
            rs.close();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void loadAddress(){
        ResultSet rs = DataBaseManager.getAllAddresses();
        try{
            while (rs.next()){
                Address address = new Address();
                address.setAddressId(rs.getInt("addressId"));
                address.setAddress(rs.getString("address"));
                address.setAddress(rs.getString("address2"));
                address.setPhone(rs.getString("phone"));
                address.setPostalCode(rs.getString("postalCode"));
                addresses.add(address);
            }
            rs.close();
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void loadCountries(){
        State.clearCountries();
        ResultSet rs = DataBaseManager.getAllCountries();
        try {
            while (rs.next()) {
                Country country = new Country();
                country.setCountryId(rs.getInt("countryId"));
                country.setCountry(rs.getString("country"));
                countries.add(country);
            }
            rs.close();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
}
