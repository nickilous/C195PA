package SchedulingApp.Views;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.AddressFieldsEmptyException;
import SchedulingApp.Models.Address;
import SchedulingApp.Models.City;
import SchedulingApp.Models.Country;
import SchedulingApp.Models.Customer;
import SchedulingApp.ViewController.CustomerViewController;
import SchedulingApp.ViewController.MainViewController;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;

public class CustomerView {
    AnchorPane mainAnchorPane = new AnchorPane();
    public Parent getView(){
        return mainAnchorPane;
    }

    CustomerViewController controller;

    GridPane gpAddress = new GridPane();
    BorderPane bpAddress = new BorderPane();

    TableView<Address> tvAddress = new TableView();
    TableColumn address = new TableColumn("Address");
    TableColumn address2 = new TableColumn("Address 2");
    TableColumn city = new TableColumn("City");
    TableColumn country = new TableColumn("Country");

    Label lblNewAddress = new Label();
    Label lblNewCustomer = new Label();
    Label lblCustomerName = new Label();
    Label lblAddress = new Label();
    Label lblAddress2 = new Label();
    Label lblPhone = new Label();
    Label lblCity = new Label();
    Label lblCountry = new Label();
    Label lblPostalCode = new Label();

    TextField tfCustomerName = new TextField();
    TextField tfAddress = new TextField();
    TextField tfAddress2 = new TextField();
    TextField tfPhone = new TextField();
    TextField tfPostalCode = new TextField();

    ComboBox cbCity = new ComboBox();
    ComboBox cbCountry = new ComboBox();

    Button btNext = new Button();
    Button btAddAddress = new Button();

    public CustomerView(CustomerViewController controller){
        this.controller = controller;
        setupLabels();
        setupTextFields();
        setupComboBoxes();
        setupTV();
        setupButtons();
        setupLayout();
    }
    public void setupLayout(){
        gpAddress.add(lblNewCustomer,0,0, 2, 1);
        gpAddress.add(lblCustomerName, 0 , 1);
        gpAddress.add(lblNewAddress, 0 , 2, 2, 1);
        gpAddress.add(lblAddress,0,3);
        gpAddress.add(lblAddress2,0,4);
        gpAddress.add(lblCity,0,5);
        gpAddress.add(lblCountry,0,6);
        gpAddress.add(lblPostalCode,0,7);
        gpAddress.add(lblPhone,0,8);

        gpAddress.add(tfCustomerName, 1, 1);
        gpAddress.add(tfAddress,1,3);
        gpAddress.add(tfAddress2,1,4);
        gpAddress.add(cbCity,1,5);
        gpAddress.add(cbCountry,1,6);
        gpAddress.add(tfPostalCode,1,7);
        gpAddress.add(tfPhone,1,8);
        gpAddress.add(btAddAddress,0,9,2,1);

        bpAddress.setLeft(gpAddress);
        bpAddress.setCenter(tvAddress);
        bpAddress.setBottom(btNext);

        mainAnchorPane.getChildren().add(bpAddress);
    }
    public void setupButtons(){
        btNext.setText("Next");
        btAddAddress.setText("Add Address");

        btNext.setOnAction((event) -> {
            controller.handleSaveCustomer(event);
        });

        btAddAddress.setOnAction((event) -> {
            controller.handleAddAddress(event);
        });
    }
    public void setupLabels(){
        lblNewAddress.setText("If address isn't in the table, \n" +
                "1. fill address fields \n" +
                "2. click add to add address to table \n" +
                "3. select new address from table and click Next:");
        lblNewCustomer.setText("Enter or Change Customer Name:");
        lblCustomerName.setText("Customer Name:");
        lblAddress.setText("Address:");
        lblAddress2.setText("Address 2:");
        lblCity.setText("City:");
        lblCountry.setText("Country:");
        lblPhone.setText("Phone:");
        lblPostalCode.setText("Postal Code:");
    }

    public void setupTextFields(){
        tfCustomerName.setText(controller.getCustomer().getCustomerName());
        tfAddress.setText(controller.getSelectedAddress().getAddress());
        tfAddress2.setText(controller.getSelectedAddress().getAddress2());
        tfPhone.setText(controller.getSelectedAddress().getPhone());
        tfPostalCode.setText(controller.getSelectedAddress().getPostalCode());

        controller.getCustomer().customerNameProperty().bind(tfCustomerName.textProperty());
        controller.getAddress().addressProperty().bind(tfAddress.textProperty());
        controller.getAddress().address2Property().bind(tfAddress2.textProperty());
        controller.getAddress().phoneProperty().bind(tfPhone.textProperty());
        controller.getAddress().postalCodeProperty().bind(tfPostalCode.textProperty());
    }
    public void setupTV(){
        tvAddress.setPrefWidth(400);
        tvAddress.getColumns().addAll( address, address2, city, country);
        tvAddress.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if (newSelection != null) {
                controller.setSelectedAddress(newSelection);
            }
        });
        setupTvCol();
    }
    public void setupTvCol(){
        address.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        address2.setCellValueFactory(new PropertyValueFactory<Customer, String>("address2"));
        city.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
        country.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));

        tvAddress.setItems(State.getAddresses());
        if(controller.isModifying()){
            tvAddress.getSelectionModel().select(controller.getAddress());
        }
    }
    public void setupComboBoxes(){


        cbCity.setCellFactory(lv -> new CustomerViewController.CityCell());
        cbCity.setButtonCell(new CustomerViewController.CityCell());

        cbCity.valueProperty().addListener((obs, oldValue, newValue) ->{
            controller.cityComboBoxListener((City) newValue);
        });

        cbCountry.setCellFactory(lv -> new CustomerViewController.CountryCell());
        cbCountry.setButtonCell(new CustomerViewController.CountryCell());

        cbCountry.valueProperty().addListener((obs, oldValue, newValue) ->{
            controller.countryComboBoxListener((Country) newValue);
            if(!controller.getFilteredCities().isEmpty()) {
                cbCity.getItems().addAll(controller.getFilteredCities());
                if(controller.isModifying()) {
                    cbCity.setValue(controller.getSelectedCity());
                } else {
                    cbCity.setValue(controller.getFilteredCities().get(0));
                }
                cbCity.setDisable(false);
            } else {
                cbCity.setValue(null);
                cbCity.getItems().clear();
                cbCity.getSelectionModel().clearSelection();
                cbCity.setDisable(true);
            }
        });

        cbCountry.getItems().addAll(State.getCountries());
        if(controller.isModifying()){
            cbCountry.setValue(controller.getSelectedCountry());
        } else {
            cbCountry.setValue(State.getCountries().get(0));
        }

    }

}
