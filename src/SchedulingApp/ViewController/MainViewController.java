package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.Models.Appointment;
import SchedulingApp.Models.Customer;
import SchedulingApp.Views.AddModifyCustomerView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.function.Predicate;

public class MainViewController {
    private Customer customer;
    private LocalDateTime time;
    private StringProperty calendarLabel = new SimpleStringProperty();

    public MainViewController(){
        this.time = State.getTime();
        this.calendarLabel.setValue("");
    }

    public void setSelectedCustomer(Customer customer){
        this.customer = customer;
    }
    public Customer getSelectedCustomer(){
        return customer;
    }

    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getCalendarLabel() {
        return calendarLabel.get();
    }

    public StringProperty calendarLabelProperty() {
        return calendarLabel;
    }

    public void setCalendarLabel(String calendarLabel) {
        this.calendarLabel.set(calendarLabel);
    }

    public void calendarLabel(boolean isWeek){
        if(isWeek) {
            // Set the title based on current week
            LocalDateTime startDate = getTime();
            LocalDateTime endDate = getTime().plusDays(6);
            String localizedStartDateMonth = new DateFormatSymbols().getMonths()[startDate.getMonthValue() - 1];
            String startDateMonthProper = localizedStartDateMonth.substring(0, 1).toUpperCase() + localizedStartDateMonth.substring(1);
            String startDateTitle = startDateMonthProper + " " + startDate.getDayOfMonth();
            String localizedEndDateMonth = new DateFormatSymbols().getMonths()[endDate.getMonthValue() - 1];
            String endDateMonthProper = localizedEndDateMonth.substring(0, 1).toUpperCase() + localizedEndDateMonth.substring(1);
            String endDateTitle = endDateMonthProper + " " + endDate.getDayOfMonth();
            calendarLabel.setValue("  " + startDateTitle + " - " + endDateTitle + ", " + endDate.getYear() + "  ");
        } else {
            // Set the title based on current month
            String localizedMonth = new DateFormatSymbols().getMonths()[getTime().getMonthValue() - 1];
            String properMonth = localizedMonth.substring(0, 1).toUpperCase() + localizedMonth.substring(1);
            calendarLabel.setValue("  " + properMonth + " " + getTime().getYear() + "  ");
        }

    }

    public void handleForward(Tab selectedTab){

    }
    public void handleBackward(Tab selectedTab){
    }

    public void loadAddCustomerView(Event event){
        AddModifyCustomerViewController addModifyCustomerViewController = new AddModifyCustomerViewController();
        AddModifyCustomerView addModifyCustomerView = new AddModifyCustomerView(addModifyCustomerViewController);
        Parent addCustomerViewParent = addModifyCustomerView.getView();
        Scene mainViewScene = new Scene(addCustomerViewParent);
        Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
        winAddProduct.setTitle("Add Customer");
        winAddProduct.setScene(mainViewScene);
        winAddProduct.show();
    }
    public void loadModifyCustomerView(Event event){
        AddModifyCustomerViewController addModifyCustomerViewController = new AddModifyCustomerViewController(customer);
        AddModifyCustomerView addModifyCustomerView = new AddModifyCustomerView(addModifyCustomerViewController);
        Parent addCustomerViewParent = addModifyCustomerView.getView();
        Scene mainViewScene = new Scene(addCustomerViewParent);
        Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
        winAddProduct.setTitle("Modify Customer");
        winAddProduct.setScene(mainViewScene);
        winAddProduct.show();
    }

    public FilteredList<Appointment> getAppointmentsByWeek(LocalDateTime startTime){
        FilteredList<Appointment> items = new FilteredList<>(State.getAppointments());
        ZonedDateTime start = ZonedDateTime.ofInstant(startTime, ZoneOffset.UTC,State.getzId());

        Predicate<Appointment> startsAfter = i -> i.getStart().isAfter(start);
        Predicate<Appointment> startsBefore = i -> i.getStart().isBefore(start.plusDays(7));

        Predicate<Appointment> filter = startsAfter.and(startsBefore);
        items.filtered(filter);
        return items;
    }

    public FilteredList<Appointment> getAppointmentsByMonth(LocalDateTime startTime){
        FilteredList<Appointment> items = new FilteredList<>(State.getAppointments());
        ZonedDateTime start = ZonedDateTime.ofInstant(startTime,ZoneOffset.UTC,State.getzId());

        Predicate<Appointment> startsAfter = i -> i.getStart().isAfter(start);
        Predicate<Appointment> startsBefore = i -> i.getStart().isBefore(start.plusMonths(1));

        Predicate<Appointment> filter = startsAfter.and(startsBefore);
        items.filtered(filter);
        return items;
    }
}
