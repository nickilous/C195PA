package SchedulingApp.AppState;

import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Models.Appointment;
import SchedulingApp.Models.Customer;
import SchedulingApp.Models.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class State {
    private static Locale selectedLanguage;
    private static User user;
    private static Calendar calendar = Calendar.getInstance();
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();

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
    public static void updateCustomers() {
        State.clearCustomers();
        ResultSet rs = DataBaseManager.getAllCustomerData();
        try {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setActive(rs.getInt("active"));
                customer.setCustomerName(rs.getString("customerName"));
                customer.setAddressId(rs.getInt("addressId"));
                customer.setCustomerId(rs.getInt("customerId"));
            }
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void clearCustomers(){
        customers.clear();
    }
    public static void addCustomer(Customer customer){
        customers.add(customer);
    }
}
