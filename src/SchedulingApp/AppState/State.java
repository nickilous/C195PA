package SchedulingApp.AppState;

import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Models.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ListIterator;
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

    private static ObservableList<Appointment> newAppointments = FXCollections.observableArrayList();
    private static ObservableList<Customer> newCustomers = FXCollections.observableArrayList();
    private static ObservableList<Address> newAddresses = FXCollections.observableArrayList();
    private static ObservableList<City> newCities = FXCollections.observableArrayList();
    private static ObservableList<Country> newCountries = FXCollections.observableArrayList();

    private static ListChangeListener<Address> addressListChangeListener;
    private static ListChangeListener<Customer> customerListChangeListener;

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

    public static ObservableList<City> getCitiesByCountry(int countryId){
      ListIterator<City> it = cities.listIterator();
      ObservableList<City> citiesByCountry = FXCollections.observableArrayList();

      while(it.hasNext()){
          City city = it.next();
          if(city.getCountryId() == countryId){
              citiesByCountry.add(city);
          }
      }
      return citiesByCountry;
    };

    public static ObservableList<Country> getCountries() {
        return countries;
    }
    public static ObservableList<City> getCities() {
        return cities;
    }
    public static ObservableList<Address> getAddresses() {
        return addresses;
    }
    public static ObservableList<Customer> getCustomers() {
        return customers;
    }


    public static void addCustomer(Customer customer){
        customers.add(customer);
    }
    public static void addAddress(Address address){
        addresses.add(address);
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


    public static void loadCustomers() {
        State.clearCustomers();
        ResultSet rs = DataBaseManager.getAllCustomerData();
        try {
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("customerId"));
                customer.setActive(rs.getInt("active"));
                customer.setCustomerName(rs.getString("customerName"));
                customer.setAddressId(rs.getInt("addressId"));
                customer.setAddress(rs.getString("address"));
                customer.setAddress2(rs.getString("address2"));
                customer.setCity(rs.getString("city"));
                customer.setCountry(rs.getString("country"));
                customers.add(customer);
            }
            rs.close();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
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
    public static void loadAddresses(){
        ResultSet rs = DataBaseManager.getAllAddresses();
        try{
            while (rs.next()){
                Address address = new Address(rs.getInt("addressId"));
                address.setAddress(rs.getString("address"));
                address.setAddress2(rs.getString("address2"));
                address.setPhone(rs.getString("phone"));
                address.setPostalCode(rs.getString("postalCode"));
                address.setCityId(rs.getInt("cityId"));
                address.setCity(rs.getString("city"));
                address.setCountry(rs.getString("country"));
                address.setCountryId(rs.getInt("countryId"));
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

    public static void addListListeners(){
        addressListChangeListener = new ListChangeListener<Address>() {
            @Override
            public void onChanged(Change<? extends Address> change) {
                while(change.next()) {
                    for (Address addedAddress : change.getAddedSubList()) {
                        System.out.println("Saving address: " + addedAddress.getAddress());
                        DataBaseManager.saveAddress(addedAddress);
                    }
                }
            }
        };
        customerListChangeListener = new ListChangeListener<Customer>() {
            @Override
            public void onChanged(Change<? extends Customer> change) {
                while (change.next()) {
                    for (Customer addedCustomer : change.getAddedSubList()) {
                        System.out.println("Saving customer: " + addedCustomer.getCustomerName());
                        DataBaseManager.saveCustomer(addedCustomer);
                    }
                }
            }
        };
        State.getCustomers().addListener(customerListChangeListener);
        State.getAddresses().addListener(addressListChangeListener);
    }

    public void removeListeners(){
        State.getAddresses().removeListener(addressListChangeListener);
        State.getCustomers().removeListener(customerListChangeListener);
    }
}
