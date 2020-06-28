package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.Models.Appointment;
import SchedulingApp.Models.Customer;
import SchedulingApp.Models.User;
import SchedulingApp.Views.MainView;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ReportController {


    public ReportController() {

    }

    public String getApptTypes() {
        String stringToReport = "";

        ZonedDateTime month;
        month = ZonedDateTime.ofInstant(State.getTime(), ZoneOffset.UTC, State.getzId());

        Predicate<Appointment> appointments = i -> i.getStart().getMonthValue() == month.getMonthValue();
        FilteredList<Appointment> items = new FilteredList<>(State.getAppointments(), appointments);

        Integer value = 1;
        Map<String, Integer> map = new HashMap<>();
        for (Appointment appointment : items) {
            if (map.containsKey(appointment.getType())) {
                map.put(appointment.getType(), map.get(appointment.getType()) + 1);
            } else {
                map.put(appointment.getType(), value);
            }
        }
        for (String s : map.keySet()) {
            stringToReport += "There are: " + map.get(s) + " appointment type(s) of: " + s + " this month.\n";
        }
        return stringToReport;
    }

    public String getDeletedCustomerList() {
        String stringToReport = "";

        for (Customer c : State.getDeletedCustomers()) {
            stringToReport += "Customer: " + c.getCustomerName() + " has been marked inactive in the database.\n";
        }

        return stringToReport;
    }

    public String getSchedule() {
        String stringToReport = "";

        ObservableList<Appointment> allAppointments = State.loadAllAppointments();
        ObservableList<User> users = State.loadAllUsers();

        ZonedDateTime startZDT;

        for (User user : users) {
            FilteredList<Appointment> appointments = new FilteredList<>(allAppointments, a -> user.getUserID() == a.getUserId());
            for (Appointment appointment : appointments) {
                startZDT = appointment.getStart();
                stringToReport += "User: " + user.getUserName()
                        + " has an appointment"
                        + " on " + startZDT.format(State.getFormatDate())
                        + " at " + startZDT.format(State.getFormatTime()) + ".\n";
            }
        }
        return stringToReport;
    }


    public void handleExitAction(ActionEvent event) {
        MainViewController controller = new MainViewController();
        MainView mainView = new MainView(controller);
        Parent mainViewParent = mainView.getView();
        Scene mainViewScene = new Scene(mainViewParent);
        Stage winAddProduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
        winAddProduct.setTitle("Main Screen");
        winAddProduct.setScene(mainViewScene);
        winAddProduct.centerOnScreen();
        winAddProduct.show();

    }

}

