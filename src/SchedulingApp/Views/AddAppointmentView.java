package SchedulingApp.Views;


import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.InvalidAppointmentException;
import SchedulingApp.Exceptions.InvalidTimeAppointmentException;
import SchedulingApp.Models.Appointment;
import SchedulingApp.ViewController.AddAppointmentViewController;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;


public class AddAppointmentView {
    AddAppointmentViewController controller;

    AnchorPane mainAnchorPane = new AnchorPane();
    public Parent getView(){
        return mainAnchorPane;
    }

    GridPane mainGridPane = new GridPane();

    Label lbTitle = new Label("Title");
    Label lbDescription = new Label("Description");
    Label lbLocation = new Label("Location");
    Label lbUrl = new Label("Url");

    TextField tfTitle = new TextField();
    TextField tfDescription = new TextField();
    TextField tfLocation = new TextField();
    TextField tfContact = new TextField();
    TextField tfType = new TextField();
    TextField tfUrl = new TextField();

    DatePicker startDate = new DatePicker();
    Spinner<LocalTime> startTime = new Spinner<>();
    DatePicker endDate = new DatePicker();
    Spinner<LocalTime> endTime = new Spinner<>();

    Button btSave = new Button();
    Button btCancel = new Button();

    private Appointment appt = new Appointment();


    public AddAppointmentView(AddAppointmentViewController controller){
        this.controller = controller;
        setupDatePickers();
    }
    private void setupButtons(){
        btSave.setText("Save");
        btCancel.setText("Cancel");

        btSave.setOnAction((event) -> {
            //TODO
            getApptInfo();
            controller.handleSave(appt);

        });
        btCancel.setOnAction((event) -> {
            //TODO
        });
    }
    private void setupDatePickers(){
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());

        startTime.setValueFactory(controller.getSvfStart());
        endTime.setValueFactory(controller.getSvfEnd());
    }

    public void getApptInfo() throws InvalidAppointmentException {
        try {
            appt.setCustomer(controller.getCustomer());
            appt.setCustomerId(controller.getCustomer().getCustomerId());
            appt.setUserId(State.getUser().getUserID());
            appt.setTitle(tfTitle.getText());
            appt.setDescription(tfDescription.getText());
            appt.setLocation(tfLocation.getText());
            appt.setContact(tfContact.getText());
            appt.setType(tfType.getText());
            appt.setUrl(tfUrl.getText());
            appt.setStart(ZonedDateTime.of(LocalDate.parse(startDate.getValue().toString(), controller.getFormatDate()), LocalTime.parse(startTime.getValue().toString(), controller.getFormatTime()), State.getzId()));
            appt.setEnd(ZonedDateTime.of(LocalDate.parse(endDate.getValue().toString(), controller.getFormatDate()), LocalTime.parse(endTime.getValue().toString(), controller.getFormatTime()), State.getzId()));
        }
        catch (NullPointerException e) {
            Alert nullAlert = new Alert(Alert.AlertType.ERROR);
            nullAlert.setTitle("Appointment Addition Error");
            nullAlert.setHeaderText("The appointment is not able to be added!");
            nullAlert.setContentText("You must select a customer!");
            nullAlert.showAndWait();
        }
    }
}
