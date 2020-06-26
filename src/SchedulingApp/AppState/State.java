package SchedulingApp.AppState;

import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This should be the only place that Database manager is accessed
 * state will hold all the current app info and changes and handle saving the data
 */
public class State {
    //State Variable to hold the selected language in the login screen.
    private static Locale selectedLanguage;

    //State variable to hold the logged in user.
    private static User user;

    //State Variable to hold whether or not the notification has been show or not
    private static boolean notificationsShown = false;

    //Date time variables to be used through out the app
    private static LocalDateTime time = LocalDateTime.now();
    private static ZoneId zId = ZoneId.systemDefault();

    //Arrays holding app required data
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> upComingAppointments = FXCollections.observableArrayList();
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static ObservableList<Address> addresses = FXCollections.observableArrayList();
    private static ObservableList<City> cities = FXCollections.observableArrayList();
    private static ObservableList<Country> countries = FXCollections.observableArrayList();

    //listeners used to monitor the change to the arrays
    private static ListChangeListener<Address> addressListChangeListener;
    private static ListChangeListener<City> cityListChangeListener;
    private static ListChangeListener<Customer> customerListChangeListener;
    private static ListChangeListener<Appointment> appointmentListChangeListener;

    //Getters and Setters
    public static void setUser(User user) {
        State.user = user;
    }
    public static User getUser() {
        return user;
    }

    public static ZoneId getzId(){
        return zId;
    }

    public static LocalDateTime getTime(){
        return time;
    }

    public static Locale getSelectedLanguage() {
        return selectedLanguage;
    }
    public static void setSelectedLanguage(Locale selectedLanguage) {
        State.selectedLanguage = selectedLanguage;
    }

    public static ObservableList<Appointment> getAppointments() {
        return appointments;
    }

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

    /**
     * Add customer to state customer array
     * @param customer customer to add to the observable array
     */
    public static void addCustomer(Customer customer){
        customers.add(customer);
    }

    /**
     * Update customer in database
     * @param customer customer to update in database
     */
    public static void updateCustomer(Customer customer){
        DataBaseManager.updateCustomer(customer);
    }

    /**
     * Delete customer in database
     * @param customer customer to delete
     */
    public static void deleteCustomer(Customer customer){
        customers.remove(customer);
    }

    /**
     * Add address to state address array
     * @param address address to add to addresses array.
     */
    public static void addAddress(Address address){
        addresses.add(address);
    }

    //CRUD for appointment observable array
    public static void addAppointment(Appointment appt){appointments.add(appt);}
    public static void updateAppointment(Appointment appt){DataBaseManager.updateAppointment(appt);}
    public static void deleteAppointment(Appointment appt){appointments.remove(appt);}

    //Clears the observable arrays
    public static void clearCustomers(){
        customers.clear();
    }
    public static void clearCities(){
        cities.clear();
    }
    public static void clearCountries(){
        countries.clear();
    }
    //Loads required data in to observable arrays
    public static void loadCustomers() {
        State.clearCustomers();
        ResultSet rs = DataBaseManager.getAllCustomerData();
        try {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customerId"));
                customer.setActive(rs.getInt("active"));
                customer.setCustomerName(rs.getString("customerName"));
                customer.setAddressId(rs.getInt("addressId"));
                customer.setAddress(rs.getString("address"));
                customer.setAddress2(rs.getString("address2"));
                customer.setCityId(rs.getInt("cityId"));
                customer.setCity(rs.getString("city"));
                customer.setCountryId(rs.getInt("countryId"));
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

    public static Address getAddressFromCustomer(Customer customer){
        FilteredList<Address> filteredAddressData = new FilteredList<>(State.getAddresses(), address ->{
            if(customer.getAddressId() == address.getAddressId()) {
                return true;
            }
            return false;
        });
        return filteredAddressData.get(0);
    }
    public static City getCityFromCustomer(Customer customer){
        FilteredList<City> filteredAddressData = new FilteredList<>(State.getCities(), city ->{
            if (customer.getCityId() ==  city.getCityId()){
                return true;
            }
            return false;
        });
        return filteredAddressData.get(0);
    }
    public static Country getCountryFromCustomer(Customer customer){
        FilteredList<Country> filteredAddressData = new FilteredList<>(State.getCountries(), country ->{
            if (customer.getCountryId() ==  country.getCountryId()){
                return true;
            }
            return false;
        });
        return filteredAddressData.get(0);
    }

    /**
     * Generates required notifications
     */
    public static void logInAppointmentNotification() {
        // Checks to see if the main screen has already been opened during this session
        if (!notificationsShown) {
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
        notificationsShown = true;
    }

    /**
     * setups listeners and adds them to the arrays.
     */
    public static void addListListeners(){
        addressListChangeListener = change -> {
            while(change.next()) {
                for (Address addedAddress : change.getAddedSubList()) {
                    System.out.println("Saving address: " + addedAddress.getAddress());
                    int dbId = DataBaseManager.saveAddress(addedAddress);
                    addedAddress.setAddressId(dbId);
                }
                for(Address removedAddress : change.getRemoved()) {
                    System.out.println("Removing Address: " + removedAddress.getAddress());
                }
            }
        };
        cityListChangeListener = change -> {
            while(change.next()) {
                for (City addedCity : change.getAddedSubList()) {
                    System.out.println("Saving address: " + addedCity.getCity());
                    int dbId = DataBaseManager.saveCity(addedCity);
                    addedCity.setCityId(dbId);
                }
                for(City removedCity : change.getRemoved()) {
                    System.out.println("Removing Address: " + removedCity.getCity());
                }
            }
        };
        customerListChangeListener = change -> {
            while (change.next()) {
                if(change.wasUpdated()){
                    System.out.println("Update detected");
                }
                for (Customer addedCustomer : change.getAddedSubList()) {
                    System.out.println("Saving customer: " + addedCustomer.getCustomerName());
                    int dbId = DataBaseManager.saveCustomer(addedCustomer);
                    addedCustomer.setCustomerId(dbId);
                }
                for(Customer removedCustomer : change.getRemoved()) {
                    System.out.println("Removing customer: " + removedCustomer.getCustomerName());
                    DataBaseManager.deleteCustomer(removedCustomer);
                }
            }
        };
        appointmentListChangeListener = change -> {
            while (change.next()) {
                for (Appointment addedAppointment : change.getAddedSubList()) {
                    System.out.println("Saving appointment: " + addedAppointment.getTitle());
                    int dbId = DataBaseManager.saveAppointment(addedAppointment);
                    addedAppointment.setAppointmentId(dbId);
                }
                for(Appointment removedAppointment : change.getRemoved()) {
                    System.out.println("Removing appointment: " + removedAppointment.getTitle());
                    DataBaseManager.deleteAppointment(removedAppointment);
                }
            }
        };
        State.getCustomers().addListener(customerListChangeListener);
        State.getAddresses().addListener(addressListChangeListener);
        State.getCities().addListener(cityListChangeListener);
        State.getAppointments().addListener(appointmentListChangeListener);
    }

    public static void addCity(City city) {
        cities.add(city);
    }
}
