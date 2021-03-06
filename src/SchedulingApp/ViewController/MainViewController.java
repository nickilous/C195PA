package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.Models.Appointment;
import SchedulingApp.Models.Customer;
import SchedulingApp.Views.ApptView;
import SchedulingApp.Views.CustomerView;
import SchedulingApp.Views.ReportView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.function.Predicate;

public class MainViewController {
    private Customer customer;
    private Appointment appointment;
    private LocalDateTime startOfWeek;
    private LocalDateTime startOfMonth;
    private final StringProperty calendarLabel = new SimpleStringProperty();
    private final BooleanProperty isWeek = new SimpleBooleanProperty();
    private final ObservableList<Appointment> currentAppointments = FXCollections.observableArrayList();


    public MainViewController() {
        this.calendarLabel.setValue("");
        setTimeToStartOfWeek();
        setTimeToStartOfMonth();
        setupIsWeekListener();
        filterAppointmentsTimeCustomer();
    }

    /**
     * sets start of week and hour to zero
     */
    public void setTimeToStartOfWeek() {
        DayOfWeek dayOfWeek = State.getTime().getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY -> {
                startOfWeek = State.getTime();
                break;
            }
            case TUESDAY -> {
                startOfWeek = State.getTime().minusDays(1);
                break;
            }
            case WEDNESDAY -> {
                startOfWeek = State.getTime().minusDays(2);
                break;
            }
            case THURSDAY -> {
                startOfWeek = State.getTime().minusDays(3);
                break;
            }
            case FRIDAY -> {
                startOfWeek = State.getTime().minusDays(4);
                break;
            }
            case SATURDAY -> {
                startOfWeek = State.getTime().minusDays(5);
                break;
            }
            case SUNDAY -> {
                startOfWeek = State.getTime().minusDays(6);
                break;
            }
        }
        int hour = startOfWeek.getHour();
        startOfWeek = startOfWeek.minusHours(hour);
    }

    public void setTimeToStartOfMonth() {
        int dayOfMonth = State.getTime().getDayOfMonth() - 1;
        startOfMonth = State.getTime().minusDays(dayOfMonth);
        int hour = startOfWeek.getHour();
        startOfWeek = startOfWeek.minusHours(hour);
    }

    private void setupIsWeekListener() {
        this.isWeek.addListener((obs, oldValue, newValue) -> {
            changeLabel();
        });
    }

    private void changeLabel() {
        if (isWeek.get()) {
            // Set the label based on current week
            LocalDateTime startDate = startOfWeek;
            LocalDateTime endDate = startOfWeek.plusDays(6);
            String localizedStartDateMonth = new DateFormatSymbols().getMonths()[startDate.getMonthValue() - 1];
            String startDateMonthProper = localizedStartDateMonth.substring(0, 1).toUpperCase() + localizedStartDateMonth.substring(1);
            String startDateTitle = startDateMonthProper + " " + startDate.getDayOfMonth();
            String localizedEndDateMonth = new DateFormatSymbols().getMonths()[endDate.getMonthValue() - 1];
            String endDateMonthProper = localizedEndDateMonth.substring(0, 1).toUpperCase() + localizedEndDateMonth.substring(1);
            String endDateTitle = endDateMonthProper + " " + endDate.getDayOfMonth();
            calendarLabel.setValue("  " + startDateTitle + " - " + endDateTitle + ", " + endDate.getYear() + "  ");
        } else {
            // Set the label based on current month
            String localizedMonth = new DateFormatSymbols().getMonths()[startOfMonth.getMonthValue() - 1];
            String properMonth = localizedMonth.substring(0, 1).toUpperCase() + localizedMonth.substring(1);
            calendarLabel.setValue("  " + properMonth + " " + startOfMonth.getYear() + "  ");
        }
    }

    public void triggerFilterChange() {
        filterAppointmentsTimeCustomer();
    }

    private void filterAppointmentsTimeCustomer() {
        currentAppointments.clear();
        FilteredList<Appointment> items = new FilteredList<>(State.getAppointments());

        ZonedDateTime start;
        ZonedDateTime end;
        if (isWeek.get()) {
            start = ZonedDateTime.ofInstant(startOfWeek, ZoneOffset.UTC, State.getzId());
            end = start.plusDays(6);
        } else {
            start = ZonedDateTime.ofInstant(startOfMonth, ZoneOffset.UTC, State.getzId());
            end = start.plusMonths(1);
        }
        Predicate<Appointment> startsAfter = i -> i.getStart().isAfter(start);
        Predicate<Appointment> startsBefore = i -> i.getStart().isBefore(end);
        if (customer != null) {
            Predicate<Appointment> customer = i -> i.getCustomerId() == this.customer.getCustomerId();
            Predicate<Appointment> time = startsAfter.and(startsBefore);
            Predicate<Appointment> filter = time.and(customer);
            items.setPredicate(filter);
        } else {
            Predicate<Appointment> time = startsAfter.and(startsBefore);
            items.setPredicate(time);
        }
        currentAppointments.addAll(items);
    }

    public ObservableList<Appointment> getCurrentAppointments() {
        return currentAppointments;
    }

    public BooleanProperty isWeekProperty() {
        return isWeek;
    }

    public Customer getSelectedCustomer() {
        return customer;
    }

    public void setSelectedCustomer(Customer customer) {
        this.customer = customer;
        filterAppointmentsTimeCustomer();
    }

    public Appointment getSelectedAppointment() {
        return appointment;
    }

    public void setSelectedAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public StringProperty calendarLabelProperty() {
        return calendarLabel;
    }

    public void handleForward() {
        if (isWeek.get()) {
            startOfWeek = startOfWeek.plusWeeks(1);
        } else {
            startOfMonth = startOfMonth.plusMonths(1);
        }
        filterAppointmentsTimeCustomer();
        changeLabel();
    }

    public void handleBackward() {
        if (isWeek.get()) {
            startOfWeek = startOfWeek.minusWeeks(1);
        } else {
            startOfMonth = startOfMonth.minusMonths(1);
        }
        filterAppointmentsTimeCustomer();
        changeLabel();
    }

    public void loadAddCustomerView(Event event) {
        CustomerViewController custViewController = new CustomerViewController();
        CustomerView addView = new CustomerView(custViewController);
        Parent addCustomerViewParent = addView.getView();
        Scene mainViewScene = new Scene(addCustomerViewParent);
        Stage winAddProduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
        winAddProduct.setTitle("Add Customer");
        winAddProduct.setScene(mainViewScene);
        winAddProduct.show();
    }

    public void loadModifyCustomerView(Event event) {
        if (!(customer == null)) {
            CustomerViewController custViewController = new CustomerViewController(customer);
            CustomerView addView = new CustomerView(custViewController);
            Parent addCustomerViewParent = addView.getView();
            Scene mainViewScene = new Scene(addCustomerViewParent);
            Stage winAddProduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
            winAddProduct.setTitle("Modify Customer");
            winAddProduct.setScene(mainViewScene);
            winAddProduct.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer not selected");
            alert.setHeaderText("Error: Customer Not Selected");
            alert.setContentText("Please Select a Customer");
            alert.showAndWait();
        }
    }

    public void loadAddAppointmentView(Event event) {
        if (!(customer == null)) {
        ApptViewController apptViewController = new ApptViewController(customer);

        ApptView apptView = new ApptView(apptViewController);
        Parent addAppointmentViewParent = apptView.getView();
        Scene mainViewScene = new Scene(addAppointmentViewParent);
        Stage winAddProduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
        winAddProduct.setTitle("Add Appointment");
        winAddProduct.setScene(mainViewScene);
        winAddProduct.centerOnScreen();
        winAddProduct.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer not selected");
            alert.setHeaderText("Error: Customer Not Selected");
            alert.setContentText("Please Select a Customer");
            alert.showAndWait();
        }

    }

    public void loadModifyAppointmentView(Event event) {
        if (!(appointment == null)) {
            ApptViewController apptViewController = new ApptViewController(appointment);

            ApptView apptView = new ApptView(apptViewController);
            Parent addAppointmentViewParent = apptView.getView();
            Scene mainViewScene = new Scene(addAppointmentViewParent);
            Stage winAddProduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
            winAddProduct.setTitle("Modify Appointment");
            winAddProduct.setScene(mainViewScene);
            winAddProduct.centerOnScreen();
            winAddProduct.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment not selected");
            alert.setHeaderText("Error: Appointment Not Selected");
            alert.setContentText("Please Select a Appointment");
            alert.showAndWait();
        }
    }

    public void loadReportView(Event event) {
        ReportController reportController = new ReportController();
        ReportView reportView = new ReportView(reportController);
        Parent addAppointmentViewParent = reportView.getView();
        Scene mainViewScene = new Scene(addAppointmentViewParent);
        Stage winAddProduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
        winAddProduct.setTitle("Reports");
        winAddProduct.setScene(mainViewScene);
        winAddProduct.centerOnScreen();
        winAddProduct.show();
    }

    public void handleDeleteCustomer(Event event) {
        if (!(customer == null)) {
            State.deleteCustomer(customer);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer not selected");
            alert.setHeaderText("Error: Customer Not Selected");
            alert.setContentText("Please Select a Customer");
            alert.showAndWait();
        }
    }

    public void handleDeleteAppointment(Event event) {
        if (!(appointment == null)) {
            State.deleteAppointment(appointment);
            triggerFilterChange();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment not selected");
            alert.setHeaderText("Error: Appointment Not Selected");
            alert.setContentText("Please Select a Appointment");
            alert.showAndWait();
        }
    }
}
