package SchedulingApp.Views;

import SchedulingApp.AppState.State;
import SchedulingApp.Models.Customer;
import SchedulingApp.ViewController.AddModifyCustomerViewController;
import SchedulingApp.ViewController.MainViewController;
import javafx.geometry.Pos;
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

    HBox toggleButtonsHBox = new HBox();

    TableView<Customer> tvCustomers = new TableView();
    TableColumn customerName = new TableColumn("Customer Name");
    TableColumn address = new TableColumn("Address");
    TableColumn address2 = new TableColumn("Address 2");
    TableColumn city = new TableColumn("City");
    TableColumn country = new TableColumn("Country");

    ToggleButton monthToggleButton = new ToggleButton();
    ToggleButton weekToggleButton = new ToggleButton();
    ToggleGroup chooseMonthWeekGroup = new ToggleGroup();

    BorderPane mainBorderPane = new BorderPane();

    TitledPane addCustomerTitlePane = new TitledPane();
    Button addCustomerButton = new Button();
    Button updateCustomerButton = new Button();
    Button deleteCustomerButton = new Button();

    TitledPane addAppointmentTitlePane = new TitledPane();
    Button addAppointmentButton = new Button();
    Button updateAppointmentButton = new Button();
    Button deleteAppointmentButton = new Button();


    public MainView(MainViewController controller){
        this.controller = controller;
        State.addListListeners();

        mainAnchorPane.getChildren().add(mainBorderPane);
        mainAnchorPane.setPrefHeight(800);
        mainAnchorPane.setPrefWidth(1200);

        mainBorderPane.setLeft(leftVBox);
        mainBorderPane.prefWidth(mainAnchorPane.getPrefWidth());
        mainBorderPane.prefHeight(mainAnchorPane.getPrefHeight());

        mainBorderPane.setTop(toggleButtonsHBox);
        mainBorderPane.setCenter(tvCustomers);


        setupTopBorderPane();
        setupLeftBorderPane();
        setupTitlePanes();
        setupTV();

        setupButtonsUI();
    }
    /*private void setupCalendarGridPane(SimpleIntegerProperty calRow, SimpleIntegerProperty calCol){
        for (int i = 0; i < calRow.getValue(); i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(120);
            row.setVgrow(Priority.ALWAYS);
            calendarGridPane.getRowConstraints().add(row);
        }
        for (int i = 0; i < calCol.getValue(); i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(10);
            col.setPrefWidth(100);
            col.setHgrow(Priority.SOMETIMES);
            calendarGridPane.getColumnConstraints().add(col);
        }
        calendarGridPane.setGridLinesVisible(true);
    }*/
    private void setupTopBorderPane(){
        weekToggleButton.setText("Week");
        monthToggleButton.setText("Month");
        weekToggleButton.setToggleGroup(chooseMonthWeekGroup);
        monthToggleButton.setToggleGroup(chooseMonthWeekGroup);
        toggleButtonsHBox.getChildren().add(weekToggleButton);
        toggleButtonsHBox.getChildren().add(monthToggleButton);
        toggleButtonsHBox.setAlignment(Pos.CENTER);
        chooseMonthWeekGroup.selectToggle(weekToggleButton);

        chooseMonthWeekGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) ->{
            //TODO
        });
    }
    private void setupTitlePanes(){
        addCustomerTitlePane.setText("Customer Controls");
        addAppointmentTitlePane.setText("Appointment Controls");

        int leftVBoxSize = leftVBox.getChildren().size();
        addCustomerTitlePane.prefWidthProperty().bind(leftVBox.widthProperty().divide(leftVBoxSize));
        addAppointmentTitlePane.prefWidthProperty().bind(leftVBox.widthProperty().divide(leftVBoxSize));

        addCustomerButtonsHBox.getChildren().add(addCustomerButton);
        addCustomerButtonsHBox.getChildren().add(updateCustomerButton);
        addCustomerButtonsHBox.getChildren().add(deleteCustomerButton);

        addAppointmentButtonsHBox.getChildren().add(addAppointmentButton);
        addAppointmentButtonsHBox.getChildren().add(updateAppointmentButton);
        addAppointmentButtonsHBox.getChildren().add(deleteAppointmentButton);

        addCustomerTitlePane.setContent(addCustomerButtonsHBox);
        addAppointmentTitlePane.setContent(addAppointmentButtonsHBox);
    }
    private void setupLeftBorderPane(){
        leftVBox.getChildren().add(addCustomerTitlePane);
        leftVBox.getChildren().add(addAppointmentTitlePane);
        leftVBox.prefWidth(184);
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

        addAppointmentButton.setOnAction((event) -> {
            //TODO
        });
        updateAppointmentButton.setOnAction((event) -> {
            //TODO
        });
        deleteAppointmentButton.setOnAction((event) -> {
            //TODO
        });

        addCustomerButton.setOnAction((event) -> {
            AddModifyCustomerViewController addModifyCustomerViewController = new AddModifyCustomerViewController();
            AddModifyCustomerView addModifyCustomerView = new AddModifyCustomerView(addModifyCustomerViewController);
            Parent addCustomerViewParent = addModifyCustomerView.getView();
            Scene mainViewScene = new Scene(addCustomerViewParent);
            Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
            winAddProduct.setTitle("Add Customer");
            winAddProduct.setScene(mainViewScene);
            winAddProduct.show();
        });
        updateCustomerButton.setOnAction((event) -> {
            AddModifyCustomerViewController addModifyCustomerViewController = new AddModifyCustomerViewController(controller.getCustomer());
            AddModifyCustomerView addModifyCustomerView = new AddModifyCustomerView(addModifyCustomerViewController);
            Parent addCustomerViewParent = addModifyCustomerView.getView();
            Scene mainViewScene = new Scene(addCustomerViewParent);
            Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
            winAddProduct.setTitle("Add Customer");
            winAddProduct.setScene(mainViewScene);
            winAddProduct.show();
        });
        deleteCustomerButton.setOnAction((event) -> {
            //TODO
        });

    }
    public void setupTV(){
        tvCustomers.setPrefWidth(400);
        tvCustomers.getColumns().addAll(customerName, address, address2, city, country);
        tvCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if (newSelection != null) {
                controller.setCustomer(newSelection);
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
    }


}

