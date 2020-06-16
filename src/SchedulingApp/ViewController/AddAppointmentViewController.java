package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.InvalidAppointmentException;
import SchedulingApp.Exceptions.InvalidTimeAppointmentException;
import SchedulingApp.Exceptions.OverlappingAppointmentException;
import SchedulingApp.Models.Appointment;
import SchedulingApp.Models.Customer;
import javafx.event.ActionEvent;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SpinnerValueFactory;

import javafx.util.converter.LocalTimeStringConverter;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddAppointmentViewController {
    Customer customer;
    public AddAppointmentViewController(Customer customer){
        this.customer = customer;
    }
    public static Appointment appt = new Appointment();
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm");

    private ZoneId zId = State.getzId();

    public Customer getCustomer() {
        return customer;
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
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setTitle("Save Appointment Modifications");
        saveAlert.setHeaderText("Are you sure you want to save?");
        saveAlert.setContentText("Press OK to save the addition. \nPress Cancel to stay on this screen.");
        saveAlert.showAndWait();
        if (saveAlert.getResult() == ButtonType.OK) {
            try {
                if (appt.isValidInput() && appt.isNotOverlapping()) {
                    State.addAppointment();
                }
            }
            catch (InvalidAppointmentException e) {
                Alert exAlert = new Alert(Alert.AlertType.ERROR);
                exAlert.setTitle("Appointment Field Empty");
                exAlert.setHeaderText("All Fields Must Be Filled");
                exAlert.setContentText(e.getMessage());
                exAlert.showAndWait().filter(response -> response == ButtonType.OK);
            }
            catch (OverlappingAppointmentException e){
                Alert exAlert = new Alert(Alert.AlertType.ERROR);
                exAlert.setTitle("Appointment Over Laps ");
                exAlert.setHeaderText("This appointment overlaps with another.");
                exAlert.setContentText(e.getMessage());
                exAlert.showAndWait().filter(response -> response == ButtonType.OK);
            }
            catch (InvalidTimeAppointmentException e){
                Alert exAlert = new Alert(Alert.AlertType.ERROR);
                exAlert.setTitle("Invalid Appointment Time ");
                exAlert.setHeaderText("This appointment has an invalid time");
                exAlert.setContentText(e.getMessage());
                exAlert.showAndWait().filter(response -> response == ButtonType.OK);
            }
        }
        else {
            saveAlert.close();
        }
    }




    public void setDefaultDateTime() {
        svfStart.setValue(LocalTime.of(8, 00));
        svfEnd.setValue(LocalTime.of(17, 00));
    }

    SpinnerValueFactory svfStart = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(formatTime,null));
        }
        @Override public void decrement(int steps) {
            LocalTime time = (LocalTime) getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }
        @Override public void increment(int steps) {
            LocalTime time = (LocalTime) getValue();
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
            LocalTime time = (LocalTime) getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }
        @Override
        public void increment(int steps) {
            LocalTime time = (LocalTime) getValue();
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
        setDefaultDateTime();
    }
}
