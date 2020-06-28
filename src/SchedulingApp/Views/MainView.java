package SchedulingApp.Views;

import SchedulingApp.AppState.State;
import SchedulingApp.Models.Appointment;
import SchedulingApp.Models.Customer;
import SchedulingApp.ViewController.ApptViewController;
import SchedulingApp.ViewController.CustomerViewController;
import SchedulingApp.ViewController.MainViewController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {

    MainViewController controller;

    AnchorPane mainAnchorPane = new AnchorPane();
    VBox leftVBox = new VBox();

    VBox addAppointmentButtonsHBox = new VBox();
    VBox addCustomerButtonsHBox = new VBox();
    VBox utilityButtonsHBox = new VBox();


    TabPane tpCalendar = new TabPane();
    Tab tWeek = new Tab("Week");
    Tab tMonth = new Tab("Month");

    HBox hbCalendar = new HBox();
    Button btForward = new Button();
    Button btBackward = new Button();

    VBox vbCalendar = new VBox();
    Label vbCalendarLabel = new Label("Calendar Label");

    TableView<Customer> tvCustomers = new TableView();
    TableColumn customerName = new TableColumn("Customer Name");
    TableColumn address = new TableColumn("Address");
    TableColumn address2 = new TableColumn("Address 2");
    TableColumn city = new TableColumn("City");
    TableColumn country = new TableColumn("Country");

    TableView<Appointment> tvAppointments = new TableView<>();
    TableColumn title = new TableColumn("Appointment");
    TableColumn description = new TableColumn("Description");
    TableColumn start = new TableColumn("Start");
    TableColumn end = new TableColumn("End");
    TableColumn type = new TableColumn("type");

    BorderPane mainBorderPane = new BorderPane();

    TitledPane addCustomerTitlePane = new TitledPane();
    Button addCustomerButton = new Button();
    Button updateCustomerButton = new Button();
    Button deleteCustomerButton = new Button();

    TitledPane addAppointmentTitlePane = new TitledPane();
    Button addAppointmentButton = new Button();
    Button updateAppointmentButton = new Button();
    Button deleteAppointmentButton = new Button();

    TitledPane utilitiesTitlePane = new TitledPane();
    Button btPrintReport = new Button();

    BooleanProperty isWeek = new SimpleBooleanProperty();

    public MainView(MainViewController controller){
        isWeek.set(true);

        this.controller = controller;
        this.controller.isWeekProperty().bind(isWeek);

        mainAnchorPane.getChildren().add(mainBorderPane);
        //mainAnchorPane.setPrefHeight(800);
        //mainAnchorPane.setPrefWidth(1200);

        mainBorderPane.setLeft(leftVBox);
        mainBorderPane.prefWidth(mainAnchorPane.getPrefWidth());
        mainBorderPane.prefHeight(mainAnchorPane.getPrefHeight());

        mainBorderPane.setTop(new Label("Select a Customer to See Appointments"));
        mainBorderPane.setCenter(tvCustomers);

        tpCalendar.getTabs().addAll(tWeek, tMonth);



        setupLeftBorderPane();
        setupRightBoarderPane();
        setupTitlePanes();
        setupTV();
        setupButtonsUI();
    }

    private void setupTitlePanes(){
        addCustomerTitlePane.setText("Customer Controls");
        addAppointmentTitlePane.setText("Appointment Controls");
        utilitiesTitlePane.setText("Utility Controls");

        int leftVBoxSize = leftVBox.getChildren().size();
        addCustomerTitlePane.prefWidthProperty().bind(leftVBox.widthProperty().divide(leftVBoxSize));
        addAppointmentTitlePane.prefWidthProperty().bind(leftVBox.widthProperty().divide(leftVBoxSize));
        utilitiesTitlePane.prefWidthProperty().bind(leftVBox.widthProperty().divide(leftVBoxSize));

        addCustomerButtonsHBox.getChildren().add(addCustomerButton);
        addCustomerButtonsHBox.getChildren().add(updateCustomerButton);
        addCustomerButtonsHBox.getChildren().add(deleteCustomerButton);

        addAppointmentButtonsHBox.getChildren().add(addAppointmentButton);
        addAppointmentButtonsHBox.getChildren().add(updateAppointmentButton);
        addAppointmentButtonsHBox.getChildren().add(deleteAppointmentButton);

        addCustomerTitlePane.setContent(addCustomerButtonsHBox);
        addAppointmentTitlePane.setContent(addAppointmentButtonsHBox);

        utilityButtonsHBox.getChildren().add(btPrintReport);
        utilitiesTitlePane.setContent(utilityButtonsHBox);
    }
    private void setupLeftBorderPane(){
        leftVBox.getChildren().add(addCustomerTitlePane);
        leftVBox.getChildren().add(addAppointmentTitlePane);
        leftVBox.getChildren().add(utilitiesTitlePane);
        leftVBox.prefWidth(184);
    }
    private void setupRightBoarderPane(){
        tWeek.setContent(tvAppointments);
        tWeek.setOnSelectionChanged (e -> {
            if (tWeek.isSelected()) {
                isWeek.set(true);
                tWeek.setContent(tvAppointments);
                controller.triggerFilterChange();
            } else {
                tWeek.setContent(null);
            }
        });
        tMonth.setOnSelectionChanged (e ->{
            if(tMonth.isSelected()){
                isWeek.set(false);
                tMonth.setContent(tvAppointments);
                controller.triggerFilterChange();
            } else {
                tMonth.setContent(null);
            }
        });

        tpCalendar.getSelectionModel().select(tWeek);
        vbCalendarLabel.textProperty().bind(controller.calendarLabelProperty());

        hbCalendar.getChildren().addAll(btBackward, vbCalendarLabel, btForward);
        vbCalendar.getChildren().addAll(hbCalendar, tpCalendar);
        mainBorderPane.setRight(vbCalendar);
    }
    private void setupButtonsUI(){
        addAppointmentButton.setText("Add Appointment");
        updateAppointmentButton.setText("Update Appointment");
        deleteAppointmentButton.setText("Delete Appointment");
        addAppointmentButton.setMaxWidth(Double.MAX_VALUE);
        updateAppointmentButton.setMaxWidth(Double.MAX_VALUE);
        deleteAppointmentButton.setMaxWidth(Double.MAX_VALUE);

        addCustomerButton.setText("Add Customer");
        updateCustomerButton.setText("Update Customer");
        deleteCustomerButton.setText("Delete Customer");
        addCustomerButton.setMaxWidth(Double.MAX_VALUE);
        updateCustomerButton.setMaxWidth(Double.MAX_VALUE);
        deleteCustomerButton.setMaxWidth(Double.MAX_VALUE);

        btPrintReport.setText("Print Reports");
        btPrintReport.setMaxWidth(Double.MAX_VALUE);

        btForward.setText(">");
        btBackward.setText("<");

        btForward.setOnAction((event) ->{
            controller.handleForward();
        });
        btBackward.setOnAction((event) -> {
            controller.handleBackward();
        });

        addAppointmentButton.setOnAction((event) -> {
            controller.loadAddAppointmentView(event);
        });
        updateAppointmentButton.setOnAction((event) -> {
            controller.loadModifyAppointmentView(event);
        });
        deleteAppointmentButton.setOnAction((event) -> {
            controller.handleDeleteAppointment(event);
        });

        addCustomerButton.setOnAction((event) -> {
            controller.loadAddCustomerView(event);
        });
        updateCustomerButton.setOnAction((event) -> {
            controller.loadModifyCustomerView(event);
        });
        deleteCustomerButton.setOnAction((event) -> {
            controller.handleDeleteCustomer(event);
        });

        btPrintReport.setOnAction((event) ->{
            controller.loadReportView(event);
        });

    }
    public void setupTV(){
        tvCustomers.setPrefWidth(400);
        tvCustomers.getColumns().addAll(customerName, address, address2, city, country);
        tvCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if (newSelection != null) {
                controller.setSelectedCustomer(newSelection);
            }
        });
        tvAppointments.setPrefWidth(400);
        tvAppointments.getColumns().addAll(title, description, start, end, type);
        tvAppointments.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if (newSelection != null) {
                controller.setSelectedAppointment(newSelection);
            }
        });
        setupTVCol();

    }
    public Parent getView(){
        return mainAnchorPane;
    }
    public void setupTVCol() {
        customerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        address2.setCellValueFactory(new PropertyValueFactory<Customer, String>("address2"));
        city.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
        country.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));

        tvCustomers.setItems(State.getCustomers());
        tvCustomers.getSelectionModel().selectFirst();

        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        start.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        end.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));

        tvAppointments.setItems(controller.getCurrentAppointments());
    }


}

