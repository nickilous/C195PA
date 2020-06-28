package SchedulingApp.Views;

import SchedulingApp.ViewController.ReportController;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.nio.Buffer;

public class ReportView {
    ReportController controller;
    enum Report{
        type,
        schedule,
        deleted
    }
    BorderPane bpMainBorderPane = new BorderPane();

    TextArea tfReportField = new TextArea();
    ScrollPane spReportField = new ScrollPane(tfReportField);

    Button btExit = new Button();

    VBox rbHBox = new VBox();

    RadioButton rbAppointmentTypes = new RadioButton("Appointments By Type");
    RadioButton rbScheduleByConsultant = new RadioButton("Schedule by Consultant");
    RadioButton rbDeletedCustomers = new RadioButton("Deleted Customers");

    ToggleGroup rbToggleGroup = new ToggleGroup();

    public ReportView(ReportController controller){
        this.controller = controller;

        rbAppointmentTypes.setUserData(Report.type);
        rbScheduleByConsultant.setUserData(Report.schedule);
        rbDeletedCustomers.setUserData(Report.deleted);

        rbAppointmentTypes.setToggleGroup(rbToggleGroup);
        rbScheduleByConsultant.setToggleGroup(rbToggleGroup);
        rbDeletedCustomers.setToggleGroup(rbToggleGroup);

        rbHBox.getChildren().add(rbAppointmentTypes);
        rbHBox.getChildren().add(rbScheduleByConsultant);
        rbHBox.getChildren().add(rbDeletedCustomers);
        rbHBox.getChildren().add(btExit);

        tfReportField.setWrapText(true);

        spReportField.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        spReportField.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        bpMainBorderPane.setLeft(rbHBox);
        bpMainBorderPane.setCenter(spReportField);

        rbToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) ->{
            if (rbToggleGroup.getSelectedToggle() != null) {
                if(rbToggleGroup.getSelectedToggle().getUserData() == Report.type){
                    tfReportField.setText(controller.getApptTypes());
                } else if (rbToggleGroup.getSelectedToggle().getUserData() == Report.schedule) {
                    tfReportField.setText(controller.getSchedule());
                } else if(rbToggleGroup.getSelectedToggle().getUserData() == Report.deleted){
                    tfReportField.setText(controller.getDeletedCustomerList());
                }
            }
        });
        btExit.setText("Back to Main Screen");
        btExit.setOnAction((event) -> {
            controller.handleExitAction(event);
        });
    }

    public Parent getView(){
        return bpMainBorderPane;
    }
}
