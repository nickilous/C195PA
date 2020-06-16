package SchedulingApp.AppState;

import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Models.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Calendar;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Filter;

/**
 * This should be the only place that Database manager is accessed
 * state will hold all the current app info and changes and handle saving the data
 */
public class State {
    private static Locale selectedLanguage;
    private static User user;

    private static boolean modifying;
    private static int openCount = 0;

    private static Calendar calendar = Calendar.getInstance();
    private static LocalDateTime time = LocalDateTime.now();
    private static ZoneId zId = ZoneId.systemDefault();

    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> upComingAppointments = FXCollections.observableArrayList();
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static ObservableList<Address> addresses = FXCollections.observableArrayList();
    private static ObservableList<City> cities = FXCollections.observableArrayList();
    private static ObservableList<Country> countries = FXCollections.observableArrayList();



    private static ListChangeListener<Address> addressListChangeListener;
    private static ListChangeListener<Customer> customerListChangeListener;
    private static ListChangeListener<Appointment> appointmentListChangeListener;

    public static void setUser(User user) {
        State.user = user;
    }
    public static User getUser() {
        return user;
    }

    public static void setzId(ZoneId zoneId){
        State.zId = zoneId;
    }
    public static ZoneId getzId(){
        return zId;
    }

    public static LocalDateTime getTime(){
        return time;
    }
    public static void setTime(LocalDateTime time){
        State.time = time;
    }

    public static void setModifying(boolean modifying) {
        State.modifying = modifying;
    }
    public static boolean isModifying() {
        return modifying;
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
        if(modifying){
            DataBaseManager.updateCustomer(customer);
        } else {
            customers.add(customer);
        }
    }
    public static void deleteCustomer(Customer customer){
        DataBaseManager.deleteCustomer(customer);
        customers.remove(customer);
    }
    public static void addAddress(Address address){
        addresses.add(address);
    }
    public static void addAppointment(Appointment appt){appointments.add(appt);}

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

    public static void loadAppointments(User user){
        ResultSet rs = DataBaseManager.getAllAppointments(user);
        try{
            while(rs.next()){
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointmentId"));
                appointment.setTitle(rs.getString("title"));
                appointment.setCustomerId(rs.getInt("customerId"));
                appointment.setDescription(rs.getString("description"));


                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zId);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zId);

                appointment.setStart(startLocal);
                appointment.setEnd(endLocal);


                appointment.setContact(rs.getString("contact"));
                appointment.setType(rs.getString("type"));
                appointment.setUrl(rs.getString("url"));
                appointment.setLocation(rs.getString("location"));
                appointment.setUserId(rs.getInt("userId"));
                appointments.add(appointment);
            }
        }catch(SQLException ex){

        }
    }
    public static void loadUpComingAppointments(User user){
        ResultSet rs = DataBaseManager.getUpcomingAppt(user);
        try{
            while(rs.next()){
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointmentId"));
                appointment.setTitle(rs.getString("title"));
                appointment.setCustomerId(rs.getInt("customerId"));
                appointment.setDescription(rs.getString("description"));


                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), zId);
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), zId);

                appointment.setStart(startLocal);
                appointment.setEnd(endLocal);


                appointment.setContact(rs.getString("contact"));
                appointment.setType(rs.getString("type"));
                appointment.setUrl(rs.getString("url"));
                appointment.setLocation(rs.getString("location"));
                appointment.setUserId(rs.getInt("userId"));
                upComingAppointments.add(appointment);
            }
        }catch(SQLException ex){

        }
    }



    public static void logInAppointmentNotification() {
        // Checks to see if the main screen has already been opened during this session
        if (openCount == 0) {
            for (Appointment appointment : upComingAppointments) {
                ResourceBundle rb = ResourceBundle.getBundle("SchedulingApp/MainScreen", State.getSelectedLanguage());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(rb.getString("notificationUpcomingAppointment"));
                alert.setHeaderText(rb.getString("notificationUpcomingAppointment"));
                alert.setContentText(rb.getString("notificationUpcomingAppointmentMessage") + "\n" + rb.getString("lblTitle")
                        + ": " + appointment.getTitle() + "\n" + rb.getString("lblDescription") + ": " + appointment.getDescription() +
                        "\n" + rb.getString("lblLocation") + ": " + appointment.getLocation() + "\n" + rb.getString("lblContact") +
                        ": " + appointment.getContact() + "\n" + rb.getString("lblUrl") + ": " + appointment.getUrl() + "\n" +
                        rb.getString("lblDate") + ": " + appointment.getStart().toString() + "\n" + rb.getString("lblStartTime") + ": " +
                        appointment.getStart().toString() + "\n" + rb.getString("lblEndTime") + ": " + appointment.getEnd().toString());
                alert.showAndWait();
            }
        }
        // Increment openCount so notifications won't be shown again this session
        openCount++;
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
        appointmentListChangeListener = new ListChangeListener<Appointment>(){
            @Override
            public void onChanged(Change<? extends Appointment> change) {
                while (change.next()) {
                    for (Appointment addedAppointment : change.getAddedSubList()) {
                        System.out.println("Saving appointment: " + addedAppointment.getTitle());
                        DataBaseManager.saveAppointment(addedAppointment);
                    }
                }
            }
        };
        State.getCustomers().addListener(customerListChangeListener);
        State.getAddresses().addListener(addressListChangeListener);
        State.getAppointments().addListener(appointmentListChangeListener);
    }

    public void removeListeners(){
        State.getAddresses().removeListener(addressListChangeListener);
        State.getCustomers().removeListener(customerListChangeListener);
    }
}
