package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.InvalidAppointmentException;
import SchedulingApp.Exceptions.InvalidTimeAppointmentException;
import SchedulingApp.Exceptions.OverlappingAppointmentException;
import SchedulingApp.Models.Appointment;
import SchedulingApp.Models.Customer;
import SchedulingApp.Views.MainView;
import javafx.event.ActionEvent;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SpinnerValueFactory;

import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;


import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ApptViewController {
    Customer customer;
    Appointment appointment;
    boolean isModifying;
    public ApptViewController(Customer customer, boolean isModifying){
        this.customer = customer;
        this.isModifying = isModifying;
        setDateTime();
    }
    public ApptViewController(Customer customer, Appointment appointment, boolean isModifying){
        this.customer = customer;
        this.appointment = appointment;
        this.isModifying = isModifying;
        setDateTime();
    }

    public static Appointment appt = new Appointment();

    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm");

    private ZoneId zId = State.getzId();

    public Customer getCustomer() {
        return customer;
    }

    public Appointment getAppointment() {
        return appointment;
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public boolean isModifying() {
        return isModifying;
    }
    public void setModifying(boolean modifying) {
        isModifying = modifying;
    }

    public DateTimeFormatter getFormatDate() {
        return formatDate;
    }

    public DateTimeFormatter getFormatTime() {
        return formatTime;
    }

    void handleCancel(ActionEvent eExitAction) {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit Scheduling App");
        exitAlert.setHeaderText("Are you sure you want to exit?");
        exitAlert.setContentText("Press OK to exit the program. \nPress Cancel to stay on this screen.");
        exitAlert.showAndWait();
        if (exitAlert.getResult() == ButtonType.OK) {

        }
        else {
            exitAlert.close();
        }
    }


    public void handleSave(Appointment appt) {
        if(isModifying) {
            State.updateAppointment(appt);
        } else {
            State.addAppointment(appt);
        }
    }
    public void setDateTime() {
        if(isModifying){
            svfStart.setValue(LocalTime.of(appointment.getStart().getHour(), appointment.getStart().getMinute()));
            svfEnd.setValue(LocalTime.of(appointment.getEnd().getHour(), appointment.getEnd().getMinute()));
        } else {
            svfStart.setValue(LocalTime.of(8, 00));
            svfEnd.setValue(LocalTime.of(17, 00));
        }
    }

    SpinnerValueFactory svfStart = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(formatTime,null));
        }
        @Override public void decrement(int steps) {
            LocalTime time = getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }
        @Override public void increment(int steps) {
            LocalTime time = getValue();
            setValue(time.plusHours(steps));
            setValue(time.plusMinutes(steps + 14));
        }
    };

    public SpinnerValueFactory getSvfStart() {
        return svfStart;
    }

    SpinnerValueFactory svfEnd = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(formatTime,null));
        }
        @Override
        public void decrement(int steps) {
            LocalTime time = getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }
        @Override
        public void increment(int steps) {
            LocalTime time = getValue();
            setValue(time.plusHours(steps));
            setValue(time.plusMinutes(steps + 14));
        }
    };

    public SpinnerValueFactory getSvfEnd() {
        return svfEnd;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
        setDateTime();
    }
}
