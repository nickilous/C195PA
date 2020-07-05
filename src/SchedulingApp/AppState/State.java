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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This should be the only place that Database manager is accessed
 * state will hold all the current app info and changes and handle saving the data
 */
public class State {
    //Date time variables to be used through out the app
    private static final LocalDateTime time = LocalDateTime.now();
    private static final ZoneId zId = ZoneId.systemDefault();
    private static final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm");
    //Variables to hold the next available ids
    private static int nextAppointmentId = 0;
    private static int nextCustomerId = 0;
    private static int nextAddressId = 0;
    //Arrays holding app required data
    private static final ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static final ObservableList<Appointment> upComingAppointments = FXCollections.observableArrayList();
    private static final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static final ObservableList<Customer> deletedCustomers = FXCollections.observableArrayList();
    private static final ObservableList<Address> addresses = FXCollections.observableArrayList();
    private static final ObservableList<City> cities = FXCollections.observableArrayList();
    private static final ObservableList<Country> countries = FXCollections.observableArrayList();
    //State Variable to hold the selected language in the login screen.
    private static Locale selectedLanguage;
    //State variable to hold the logged in user.
    private static User user;
    //State Variable to hold whether or not the notification has been show or not
    private static boolean notificationsShown = false;
    //listeners used to monitor the change to the arrays
    private static ListChangeListener<Address> addressListChangeListener;
    private static ListChangeListener<City> cityListChangeListener;
    private static ListChangeListener<Customer> customerListChangeListener;
    private static ListChangeListener<Appointment> appointmentListChangeListener;

    public static User getUser() {
        return user;
    }

    //Getters and Setters
    public static void setUser(User user) {
        State.user = user;
    }

    public static ZoneId getzId() {
        return zId;
    }

    public static LocalDateTime getTime() {
        return time;
    }

    public static DateTimeFormatter getFormatDate() {
        return formatDate;
    }

    public static DateTimeFormatter getFormatTime() {
        return formatTime;
    }

    public static Locale getSelectedLanguage() {
        return selectedLanguage;
    }

    public static void setSelectedLanguage(Locale selectedLanguage) {
        State.selectedLanguage = selectedLanguage;
    }

    public static int getNextAppointmentId() {
        return nextAppointmentId;
    }

    public static int getNextCustomerId() {
        return nextCustomerId;
    }

    public static int getNextAddressId() {
        return nextAddressId;
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

    public static ObservableList<Customer> getDeletedCustomers() {
        return deletedCustomers;
    }

    /**
     * Add customer to state customer array
     *
     * @param customer customer to add to the observable array
     */
    public static void addCustomer(Customer customer) {
        customers.add(customer);
    }

    /**
     * Update customer in database
     *
     * @param customer customer to update in database
     */
    public static void updateCustomer(Customer customer) {
        DataBaseManager.updateCustomer(customer);
    }

    /**
     * Delete customer in database
     *
     * @param customer customer to delete
     */
    public static void deleteCustomer(Customer customer) {
        customers.remove(customer);
        deletedCustomers.add(customer);
        customer.setActive(0);
    }

    /**
     * Add address to state address array
     *
     * @param address address to add to addresses array.
     */
    public static void addAddress(Address address) {
        addresses.add(address);
    }

    //CRUD for appointment observable array
    public static void addAppointment(Appointment appt) {
        appointments.add(appt);
    }

    public static void updateAppointment(Appointment appt) {
        DataBaseManager.updateAppointment(appt);
    }

    public static void deleteAppointment(Appointment appt) {
        appointments.remove(appt);
    }

    //Clears the observable arrays
    public static void clearCustomers() {
        customers.clear();
    }

    public static void clearCities() {
        cities.clear();
    }

    public static void clearCountries() {
        countries.clear();
    }

    //Loads required data in to observable arrays
    public static void loadCustomers() {
        State.clearCustomers();
        ResultSet rs = DataBaseManager.getAllActiveCustomerData();
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
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        nextCustomerId = DataBaseManager.getStartingIdForTable("customer");

        rs = DataBaseManager.getAllInActiveCustomerData();
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
                deletedCustomers.add(customer);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void loadCities() {
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
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void loadAddresses() {
        ResultSet rs = DataBaseManager.getAllAddresses();
        try {
            while (rs.next()) {
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
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        nextAddressId = DataBaseManager.getStartingIdForTable("address");
    }

    public static void loadCountries() {
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
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void loadAppointments(User user) {
        ResultSet rs = DataBaseManager.getAllAppointmentsByUser(user);
        try {
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointmentId"));
                appointment.setTitle(rs.getString("title"));
                appointment.setCustomerId(rs.getInt("customerId"));
                appointment.setDescription(rs.getString("description"));


                LocalDateTime start = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = start.atZone(zId);
                ZonedDateTime endLocal = end.atZone(zId);

                appointment.setStart(startLocal);
                appointment.setEnd(endLocal);


                appointment.setContact(rs.getString("contact"));
                appointment.setType(rs.getString("type"));
                appointment.setUrl(rs.getString("url"));
                appointment.setLocation(rs.getString("location"));
                appointment.setUserId(rs.getInt("userId"));
                appointments.add(appointment);
                System.out.println("Loading Appointment: " + appointment.getTitle());
                System.out.println("Appointment Id: " + appointment.getAppointmentId());
                System.out.println("Appointment Start: " + appointment.getStart());
                System.out.println("Appointment End: " + appointment.getEnd());
            }
        } catch (SQLException ex) {

        }
        nextAppointmentId = DataBaseManager.getStartingIdForTable("appointment");
    }

    //functions specifically for reports
    public static ObservableList<Appointment> loadAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        ResultSet rs = DataBaseManager.getAllAppointments();
        try {
            while (rs.next()) {
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
                System.out.println("Loading Appointment: " + appointment.getTitle() + "\n");
                System.out.println("Appointment Id: " + appointment.getAppointmentId() + "\n");
                System.out.println("Appointment Start: " + appointment.getStart() + "\n");
                System.out.println("Appointment End: " + appointment.getEnd() + "\n");
            }
        } catch (SQLException ex) {

        }
        return appointments;
    }

    public static ObservableList<User> loadAllUsers() {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        ResultSet rs = DataBaseManager.getAllUsers();
        try {
            while (rs.next()) {
                User user = new User();
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setUserID(rs.getInt("userId"));
                allUsers.add(user);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return allUsers;
    }

    public static Address getAddressFromCustomer(Customer customer) {
        FilteredList<Address> filteredAddressData = new FilteredList<>(State.getAddresses(), address -> {
            return customer.getAddressId() == address.getAddressId();
        });
        return filteredAddressData.get(0);
    }

    public static City getCityFromCustomer(Customer customer) {
        FilteredList<City> filteredAddressData = new FilteredList<>(State.getCities(), city -> {
            return customer.getCityId() == city.getCityId();
        });
        return filteredAddressData.get(0);
    }

    public static Country getCountryFromCustomer(Customer customer) {
        FilteredList<Country> filteredAddressData = new FilteredList<>(State.getCountries(), country -> {
            return customer.getCountryId() == country.getCountryId();
        });
        return filteredAddressData.get(0);
    }

    /**
     * Generates required notifications
     */
    public static void logInAppointmentNotification() {
        FilteredList<Appointment> upComingAppointments = new FilteredList<>(appointments,
                a -> (a.getStart().isAfter(ZonedDateTime.now(State.getzId())) && a.getStart().isBefore(ZonedDateTime.now(State.getzId()).plusMinutes(15))));

        if (!notificationsShown) {
            for (Appointment appointment : upComingAppointments) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upcoming Appointments");
                alert.setHeaderText("Here is more information about your upcoming appointment:");
                alert.setContentText("Title: " + appointment.getTitle() + "\n" +
                        "Description" + ": " + appointment.getDescription() + "\n" +
                        "Location: " + appointment.getLocation() + "\n" +
                        "Contact: " + appointment.getContact() + "\n" +
                        "Url: " + appointment.getUrl() + "\n" +
                        "Date: " + appointment.getStart().toString() + "\n" +
                        "Start Time: " + appointment.getStart().toString() + "\n" +
                        "End Time: " + appointment.getEnd().toString());
                alert.showAndWait();
            }
        }
        notificationsShown = true;
    }

    /**
     * setups listeners and adds them to the arrays.
     */
    public static void addListListeners() {
        addressListChangeListener = change -> {
            while (change.next()) {
                if(change.wasAdded()) {
                    for (Address addedAddress : change.getAddedSubList()) {
                        System.out.println("Saving address: " + addedAddress.getAddress());
                        int dbId = DataBaseManager.saveAddress(addedAddress);
                        addedAddress.setAddressId(dbId);
                        nextAddressId = DataBaseManager.getStartingIdForTable("address");
                    }
                } else if(change.wasRemoved()) {
                    for (Address removedAddress : change.getRemoved()) {
                        System.out.println("Removing Address: " + removedAddress.getAddress());
                    }
                }
            }
        };
        cityListChangeListener = change -> {
            while (change.next()) {
                for (City addedCity : change.getAddedSubList()) {
                    System.out.println("Saving address: " + addedCity.getCity());
                    int dbId = DataBaseManager.saveCity(addedCity);
                    addedCity.setCityId(dbId);
                }
                for (City removedCity : change.getRemoved()) {
                    System.out.println("Removing Address: " + removedCity.getCity());
                }
            }
        };
        customerListChangeListener = new ListChangeListener<Customer>() {
            public void onChanged(Change<? extends Customer> change) {
                    change.next();
                    if (change.wasPermutated()) {
                        System.out.println("Permutation Detected");
                    } else if (change.wasUpdated()) {
                        System.out.println("Update detected");
                    } else {
                        if(change.wasAdded()) {
                            for (Customer addedCustomer : change.getAddedSubList()) {
                                System.out.println("Saving customer: " + addedCustomer.getCustomerName());
                                int dbId = DataBaseManager.saveCustomer(addedCustomer);
                                addedCustomer.setCustomerId(dbId);
                                nextCustomerId = DataBaseManager.getStartingIdForTable("customer");
                            }
                        } else if(change.wasRemoved()) {
                            for (Customer removedCustomer : change.getRemoved()) {
                                System.out.println("Removing customer: " + removedCustomer.getCustomerName());
                                DataBaseManager.deleteCustomer(removedCustomer);
                            }
                        }
                    }
                }

        };
        appointmentListChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Appointment addedAppointment : change.getAddedSubList()) {
                        System.out.println("Saving appointment: " + addedAppointment.getTitle());
                        DataBaseManager.saveAppointment(addedAppointment);
                        nextAppointmentId = DataBaseManager.getStartingIdForTable("appointment");
                        System.out.println("Appointment Id: " + addedAppointment.getAppointmentId());
                        System.out.println("Appointment Start: " + addedAppointment.getStart());
                        System.out.println("Appointment End: " + addedAppointment.getEnd());
                    }
                } else if (change.wasRemoved()) {
                    for (Appointment removedAppointment : change.getRemoved()) {
                        System.out.println("Removing appointment: " + removedAppointment.getTitle());
                        System.out.println("Appointment Id: " + removedAppointment.getAppointmentId());
                        System.out.println("Appointment Start: " + removedAppointment.getStart());
                        System.out.println("Appointment End: " + removedAppointment.getEnd());
                        DataBaseManager.deleteAppointment(removedAppointment);
                    }
                }
            }
        };
        State.getCustomers().addListener(customerListChangeListener);
        State.getAddresses().addListener(addressListChangeListener);
        State.getCities().addListener(cityListChangeListener);
        State.getAppointments().addListener(appointmentListChangeListener);
    }
}
