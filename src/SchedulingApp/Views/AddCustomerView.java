package SchedulingApp.Views;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.UserFieldsEmptyException;
import SchedulingApp.Models.Address;
import SchedulingApp.Models.City;
import SchedulingApp.Models.Country;
import SchedulingApp.Models.Customer;
import SchedulingApp.ViewController.AddCustomerViewController;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class AddCustomerView {
    AnchorPane mainAnchorPane = new AnchorPane();
    public Parent getView(){
        return mainAnchorPane;
    }

    Customer customer = new Customer();
    AddCustomerViewController controller = new AddCustomerViewController();

    City selectedCity = null;
    Country selectedCountry = null;

    Label lblName = new Label();
    Label lblAddress = new Label();
    Label lblAddress2 = new Label();
    Label lblPhone = new Label();
    Label lblCity = new Label();
    Label lblCountry = new Label();
    Label lblPostalCode = new Label();

    TextField tfName = new TextField();
    TextField tfAddress = new TextField();
    TextField tfAddress2 = new TextField();
    TextField tfPhone = new TextField();
    TextField tfPostalCode = new TextField();

    ComboBox cbCity = new ComboBox();
    ComboBox cbCountry = new ComboBox();

    Button btSave = new Button();

    GridPane gpAddress = new GridPane();

    public AddCustomerView(AddCustomerViewController controller){
        this.controller = controller;
        setupLabels();
        setupComboBoxes();
        setupButton();
        setupVBox();
    }
    public void setupLabels(){
        lblName.setText("Name:");
        lblAddress.setText("Address:");
        lblAddress2.setText("Address 2:");
        lblCity.setText("City:");
        lblCountry.setText("Country:");
        lblPhone.setText("Phone:");
        lblPostalCode.setText("Postal Code:");
    }
    public void setupComboBoxes(){
        cbCity.getItems().addAll(State.getCities());
        cbCity.setCellFactory(lv -> new CityCell());
        cbCity.setButtonCell(new CityCell());
        //cbCity.setDisable(true);
        cbCity.valueProperty().addListener((obs, oldValue, newValue) ->{
            //TODO: Listen for city combo box change
            selectedCity = (City) newValue;
            controller.cityComboBoxListener();
        });
        cbCountry.getItems().addAll(State.getCountries());
        cbCountry.setCellFactory(lv -> new CountryCell());
        cbCountry.setButtonCell(new CountryCell());
        //cbCountry.setDisable(true);
        cbCountry.valueProperty().addListener((obs, oldValue, newValue) ->{
            //TODO: listen for country combo box change
            selectedCountry = (Country) newValue;
            controller.countryComboBoxListener();
        });
        cbCountry.setValue(State.getCountries().get(0));
    }
    public void setupVBox(){

        gpAddress.add(lblName, 0,0);
        gpAddress.add(tfName, 1, 0);

        gpAddress.add(lblAddress, 0, 1);
        gpAddress.add(tfAddress, 1, 1);

        gpAddress.add(lblAddress2,0,2);
        gpAddress.add(tfAddress2, 1, 2);

        gpAddress.add(lblPostalCode, 0, 3);
        gpAddress.add(tfPostalCode, 1, 3);

        gpAddress.add(lblCity, 0, 4);
        gpAddress.add(cbCity, 1, 4);

        gpAddress.add(lblCountry, 0, 5);
        gpAddress.add(cbCountry, 1, 5);

        gpAddress.add(lblPhone, 0, 6);
        gpAddress.add(tfPhone, 1,6);

        gpAddress.add(btSave,0,7,2,1);

        mainAnchorPane.getChildren().add(gpAddress);
    }
    public static class CityCell extends ListCell<City>{
        @Override
        protected void updateItem(City city, boolean empty) {
            super.updateItem(city, empty);
            if(empty){
                setText(null);
            } else {
                setText(city.getCity());
            }
        }
    }
    public static class CountryCell extends ListCell<Country> {
        @Override
        public void updateItem(Country country, boolean empty) {
            super.updateItem(country, empty);
            if (empty) {
                setText(null);
            } else {
                setText(country.getCountry());
            }
        }
    }

    private void setupButton(){
        btSave.setText("Save");
        btSave.setOnAction((event) -> {
            try {
                Customer.isCustomerValid(tfName.getText(),
                        tfAddress.getText(),
                        selectedCity.getCity(),
                        selectedCountry.getCountry(),
                        tfPostalCode.getText(),
                        tfPhone.getText());
                Customer customer = new Customer();
                Address address = new Address();

                customer.setCustomerName(tfName.getText());
                customer.setAddressId(State.getAddresses().size() + 1);

                address.setAddress(tfAddress.getText());
                address.setAddress2(tfAddress2.getText());
                address.setPostalCode(tfPostalCode.getText());
                address.setPhone(tfPhone.getText());
                address.setCityId(selectedCity.getCityId());

                controller.handleSave(customer, address);
            } catch (UserFieldsEmptyException ex){
                //TODO: Alert to the fields being empty
            }
        });
    }
}
