package SchedulingApp.Views;


import SchedulingApp.AppState.State;
import SchedulingApp.Models.Appointment;
import SchedulingApp.ViewController.ApptViewController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;


public class ApptView {
    ApptViewController controller;

    BorderPane bpMainPane = new BorderPane();
    GridPane mainGridPane = new GridPane();
    Label lbTitle = new Label("Title");
    Label lbDescription = new Label("Description");
    Label lbLocation = new Label("Location");
    Label lbContact = new Label("Contact");
    Label lbType = new Label("Type");
    Label lbUrl = new Label("Url");
    TextField tfTitle = new TextField();
    TextField tfDescription = new TextField();
    TextField tfLocation = new TextField();
    TextField tfContact = new TextField();
    TextField tfType = new TextField();
    TextField tfUrl = new TextField();
    DatePicker dpStartDate = new DatePicker();
    Spinner<LocalTime> startTime = new Spinner<>();
    DatePicker dpEndDate = new DatePicker();
    Spinner<LocalTime> endTime = new Spinner<>();
    HBox btHBox = new HBox();
    Button btSave = new Button();
    private final Appointment appt = new Appointment();


    public ApptView(ApptViewController controller) {
        this.controller = controller;
        setUIListeners();
        setupTextFields();
        setupButtons();
        setupDatePickers();
        setupGridPane();
    }

    public Parent getView() {
        return bpMainPane;
    }

    private void setupGridPane() {
        mainGridPane.setPadding(new Insets(10, 10, 10, 10));
        mainGridPane.setAlignment(Pos.CENTER);
        mainGridPane.setVgap(10);
        mainGridPane.setHgap(10);

        //mainGridPane.add(welcomeText, 0, 0, 2, 1);

        mainGridPane.add(lbTitle, 0, 1);
        mainGridPane.add(lbDescription, 0, 2);
        mainGridPane.add(lbLocation, 0, 3);
        mainGridPane.add(lbContact, 0, 4);
        mainGridPane.add(lbType, 0, 5);
        mainGridPane.add(lbUrl, 0, 6);

        mainGridPane.add(tfTitle, 1, 1);
        mainGridPane.add(tfDescription, 1, 2);
        mainGridPane.add(tfLocation, 1, 3);
        mainGridPane.add(tfContact, 1, 4);
        mainGridPane.add(tfType, 1, 5);
        mainGridPane.add(tfUrl, 1, 6);


        mainGridPane.add(dpStartDate, 0, 8);
        mainGridPane.add(startTime, 0, 9);

        mainGridPane.add(new Label("Start Date/Time"), 0, 7);
        mainGridPane.add(new Label("End Date/Time"), 1, 7);

        mainGridPane.add(dpEndDate, 1, 8);
        mainGridPane.add(endTime, 1, 9);

        btHBox.getChildren().add(btSave);

        btHBox.setAlignment(Pos.BOTTOM_RIGHT);

        mainGridPane.add(btHBox, 1, 10);

        bpMainPane.setCenter(mainGridPane);
    }

    private void setupTextFields() {
        if (controller.isModifying()) {
            tfTitle.setText(controller.getAppointment().getTitle());
            tfDescription.setText(controller.getAppointment().getDescription());
            tfLocation.setText(controller.getAppointment().getLocation());
            tfContact.setText(controller.getAppointment().getContact());
            tfType.setText(controller.getAppointment().getType());
            tfUrl.setText(controller.getAppointment().getUrl());
        }
            controller.getAppointment().titleProperty().bind(tfTitle.textProperty());
            controller.getAppointment().descriptionProperty().bind(tfDescription.textProperty());
            controller.getAppointment().locationProperty().bind(tfLocation.textProperty());
            controller.getAppointment().contactProperty().bind(tfContact.textProperty());
            controller.getAppointment().typeProperty().bind(tfType.textProperty());
            controller.getAppointment().urlProperty().bind(tfUrl.textProperty());

    }

    private void setupButtons() {
        btSave.setText("Save");

        btSave.setOnAction((event) -> {
            //TODO: Check order of alerts
            Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
            saveAlert.setTitle("Save Appointment Modifications");
            saveAlert.setHeaderText("Are you sure you want to save?");
            saveAlert.setContentText("Press OK to save the addition. \nPress Cancel to stay on this screen.");
            saveAlert.showAndWait();
            if (saveAlert.getResult() == ButtonType.OK) {
                controller.handleSave(event);
            } else {
                saveAlert.close();
            }
        });

    }

    private void setupDatePickers() {
        if (controller.isModifying()) {
            LocalDate startDate = LocalDate.of(controller.getAppointment().getStart().getYear(),
                    controller.getAppointment().getStart().getMonthValue(),
                    controller.getAppointment().getStart().getDayOfMonth());
            dpStartDate.setValue(startDate);

            LocalDate endDate = LocalDate.of(controller.getAppointment().getEnd().getYear(),
                    controller.getAppointment().getEnd().getMonthValue(),
                    controller.getAppointment().getEnd().getDayOfMonth());
            dpEndDate.setValue(endDate);
        } else {
            dpStartDate.setValue(LocalDate.now());
            dpEndDate.setValue(LocalDate.now());
        }
        startTime.setValueFactory(controller.getSvfStart());
        endTime.setValueFactory(controller.getSvfEnd());
    }

    public void setUIListeners() {
        dpStartDate.valueProperty().addListener((obs, oldValue, newValue) -> {
            controller.setStartDate(newValue);
        });
        dpStartDate.valueProperty().addListener((obs, oldValue, newValue) -> {
            controller.setEndDate(newValue);
        });

        startTime.valueProperty().addListener((obs, oldValue, newValue) ->{
            controller.setStartTime(newValue);
        });

        endTime.valueProperty().addListener((obs, oldValue, newValue) ->{
            controller.setEndTime(newValue);
        });
    }
}
