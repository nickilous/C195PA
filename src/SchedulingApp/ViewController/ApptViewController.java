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

import java.time.LocalTime;
import java.time.ZoneId;

public class ApptViewController {
    private final ZoneId zId = State.getzId();
    Customer customer;
    Appointment appointment;
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

    public ApptViewController(Customer customer, boolean isModifying) {
        this.customer = customer;
        this.isModifying = isModifying;
        setDateTime();
    }

    public ApptViewController(Customer customer, Appointment appointment, boolean isModifying) {
        this.customer = customer;
        this.appointment = appointment;
        this.isModifying = isModifying;
        setDateTime();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public boolean isModifying() {
        return isModifying;
    }

    public void handleSave(Event event, Appointment appt) {
        try {
            if (appt.isValidInput() && appt.isNotOverlapping()) {
                if (isModifying) {
                    State.updateAppointment(appt);
                } else {
                    State.addAppointment(appt);
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

    public SpinnerValueFactory getSvfStart() {
        return svfStart;
    }

    public SpinnerValueFactory getSvfEnd() {
        return svfEnd;
    }

}
