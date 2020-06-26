package SchedulingApp.Views;


import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.InvalidAppointmentException;
import SchedulingApp.Exceptions.InvalidTimeAppointmentException;
import SchedulingApp.Exceptions.OverlappingAppointmentException;
import SchedulingApp.Models.Appointment;
import SchedulingApp.ViewController.ApptViewController;
import SchedulingApp.ViewController.MainViewController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;


public class ApptView {
    ApptViewController controller;

    AnchorPane mainAnchorPane = new AnchorPane();
    public Parent getView(){
        return mainAnchorPane;
    }

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
    Button btCancel = new Button();

    private Appointment appt = new Appointment();


    public ApptView(ApptViewController controller){
        this.controller = controller;
        setupTextFields();
        setupButtons();
        setupGridPane();
        setupDatePickers();
    }
    private void setupGridPane(){
        mainGridPane.setPadding(new Insets(10,10,10,10));
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
        mainGridPane.add(tfType, 1,5 );
        mainGridPane.add(tfUrl, 1, 6);


        mainGridPane.add(dpStartDate, 0,8);
        mainGridPane.add(startTime,0,9);

        mainGridPane.add(new Label("Start Date/Time"),0,7);
        mainGridPane.add(new Label("End Date/Time"),1,7);

        mainGridPane.add(dpEndDate, 1, 8);
        mainGridPane.add(endTime, 1,9);

        //mainGridPane.add(btHBox, 1, 4);

        btHBox.getChildren().add(btSave);
        btHBox.getChildren().add(btCancel);

        btHBox.setAlignment(Pos.BOTTOM_RIGHT);

        mainGridPane.add(btHBox, 1,10);

        mainAnchorPane.getChildren().add(mainGridPane);
    }
    private void setupTextFields(){
        if(controller.isModifying()){
            tfTitle.setText(controller.getAppointment().getTitle());
            tfDescription.setText(controller.getAppointment().getDescription());
            tfLocation.setText(controller.getAppointment().getLocation());
            tfContact.setText(controller.getAppointment().getContact());
            tfType.setText(controller.getAppointment().getType());
            tfUrl.setText(controller.getAppointment().getUrl());
        }
    }
    private void setupButtons(){
        btSave.setText("Save");
        btCancel.setText("Cancel");

        btSave.setOnAction((event) -> {
            getApptInfo();
            Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
            saveAlert.setTitle("Save Appointment Modifications");
            saveAlert.setHeaderText("Are you sure you want to save?");
            saveAlert.setContentText("Press OK to save the addition. \nPress Cancel to stay on this screen.");
            saveAlert.showAndWait();
            if (saveAlert.getResult() == ButtonType.OK) {
                try {
                    if (appt.isValidInput() && appt.isNotOverlapping()) {
                        controller.handleSave(appt);
                    }
                    MainViewController controller = new MainViewController();
                    MainView mainView = new MainView(controller);
                    Parent mainViewParent = mainView.getView();
                    Scene mainViewScene = new Scene(mainViewParent);
                    Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
                    winAddProduct.setTitle("Main Screen");
                    winAddProduct.setScene(mainViewScene);
                    winAddProduct.centerOnScreen();
                    winAddProduct.show();
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
        });
        btCancel.setOnAction((event) -> {
            //TODO
        });
    }
    private void setupDatePickers(){
        if(controller.isModifying()){
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

    public void getApptInfo(){
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
            appt.setStart(ZonedDateTime.of(LocalDate.parse(dpStartDate.getValue().toString(), controller.getFormatDate()), LocalTime.parse(startTime.getValue().toString(), controller.getFormatTime()), State.getzId()));
            appt.setEnd(ZonedDateTime.of(LocalDate.parse(dpEndDate.getValue().toString(), controller.getFormatDate()), LocalTime.parse(endTime.getValue().toString(), controller.getFormatTime()), State.getzId()));
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
