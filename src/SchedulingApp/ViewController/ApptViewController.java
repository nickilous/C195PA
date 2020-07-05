package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.InvalidAppointmentException;
import SchedulingApp.Exceptions.InvalidTimeAppointmentException;
import SchedulingApp.Exceptions.OverlappingAppointmentException;
import SchedulingApp.Models.Appointment;
import SchedulingApp.Models.Customer;
import SchedulingApp.Views.MainView;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class ApptViewController {
    Appointment appointment;
    LocalDate startDate;
    LocalDate endDate;
    LocalTime startTime;
    LocalTime endTime;

    boolean isModifying;
    SpinnerValueFactory svfStart = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(State.getFormatTime(), null));
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
    SpinnerValueFactory svfEnd = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(State.getFormatTime(), null));
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

    public ApptViewController(Customer customer) {
        this.appointment = new Appointment();
        appointment.setAppointmentId(State.getNextAppointmentId());
        appointment.setCustomerId(customer.getCustomerId());
        this.isModifying = false;
        setDateTime();
    }

    public ApptViewController(Appointment appointment) {
        this.appointment = appointment;
        this.isModifying = true;
        setDateTime();
    }


    public Appointment getAppointment() {
        return appointment;
    }

    public boolean isModifying() {
        return isModifying;
    }

    public void handleSave(Event event) {
        appointment.setStart(ZonedDateTime.of(LocalDate.parse(startDate.toString(), State.getFormatDate()), LocalTime.parse(startTime.toString(), State.getFormatTime()), State.getzId()));
        appointment.setEnd(ZonedDateTime.of(LocalDate.parse(endDate.toString(), State.getFormatDate()), LocalTime.parse(endTime.toString(), State.getFormatTime()), State.getzId()));

        try {
            if (appointment.isValidInput() && appointment.isNotOverlapping()) {
                if (isModifying) {
                    State.updateAppointment(appointment);
                } else {
                    State.addAppointment(appointment);
                }
            }
            MainViewController controller = new MainViewController();
            MainView mainView = new MainView(controller);
            Parent mainViewParent = mainView.getView();
            Scene mainViewScene = new Scene(mainViewParent);
            Stage winAddProduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
            winAddProduct.setTitle("Main Screen");
            winAddProduct.setScene(mainViewScene);
            winAddProduct.centerOnScreen();
            winAddProduct.show();
        } catch (InvalidAppointmentException ex) {
            Alert exAlert = new Alert(Alert.AlertType.ERROR);
            exAlert.setTitle("Appointment Field Empty");
            exAlert.setHeaderText("All Fields Must Be Filled");
            exAlert.setContentText(ex.getMessage());
            exAlert.showAndWait().filter(response -> response == ButtonType.OK);
        } catch (OverlappingAppointmentException ex) {
            Alert exAlert = new Alert(Alert.AlertType.ERROR);
            exAlert.setTitle("Appointment Over Laps ");
            exAlert.setHeaderText("This appointment overlaps with another.");
            exAlert.setContentText(ex.getMessage());
            exAlert.showAndWait().filter(response -> response == ButtonType.OK);
        } catch (InvalidTimeAppointmentException ex) {
            Alert exAlert = new Alert(Alert.AlertType.ERROR);
            exAlert.setTitle("Invalid Appointment Time ");
            exAlert.setHeaderText("This appointment has an invalid time");
            exAlert.setContentText(ex.getMessage());
            exAlert.showAndWait().filter(response -> response == ButtonType.OK);
        }

    }

    public void setDateTime() {
        if (isModifying) {
            svfStart.setValue(LocalTime.of(appointment.getStart().getHour(), appointment.getStart().getMinute()));
            svfEnd.setValue(LocalTime.of(appointment.getEnd().getHour(), appointment.getEnd().getMinute()));
        } else {
            svfStart.setValue(LocalTime.of(8, 00));
            svfEnd.setValue(LocalTime.of(17, 00));
        }
    }
    public void setStartDate(LocalDate newValue){
        startDate = newValue;
    };
    public void setEndDate(LocalDate newValue){
        endDate = newValue;
    };
    public void setStartTime(LocalTime newValue){
        startTime = newValue;
    };
    public void setEndTime(LocalTime newValue){
        endTime = newValue;
    };

    public SpinnerValueFactory getSvfStart() {
        return svfStart;
    }

    public SpinnerValueFactory getSvfEnd() {
        return svfEnd;
    }

}
