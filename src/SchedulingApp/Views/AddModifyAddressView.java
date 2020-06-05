package SchedulingApp.Views;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.AddressFieldsEmptyException;
import SchedulingApp.Models.Address;
import SchedulingApp.Models.City;
import SchedulingApp.Models.Country;
import SchedulingApp.Models.Customer;
import SchedulingApp.ViewController.AddAddressViewController;
import SchedulingApp.ViewController.MainViewController;
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

public class AddModifyAddressView {
    AnchorPane mainAnchorPane = new AnchorPane();
    public Parent getView(){
        return mainAnchorPane;
    }

    AddAddressViewController controller;

    GridPane gpAddress = new GridPane();
    BorderPane bpAddress = new BorderPane();

    TableView<Address> tvAddress = new TableView();
    TableColumn address = new TableColumn("Address");
    TableColumn address2 = new TableColumn("Address 2");
    TableColumn city = new TableColumn("City");
    TableColumn country = new TableColumn("Country");

    Label lblNewAddress = new Label();
    Label lblAddress = new Label();
    Label lblAddress2 = new Label();
    Label lblPhone = new Label();
    Label lblCity = new Label();
    Label lblCountry = new Label();
    Label lblPostalCode = new Label();

    TextField tfAddress = new TextField();
    TextField tfAddress2 = new TextField();
    TextField tfPhone = new TextField();
    TextField tfPostalCode = new TextField();

    ComboBox cbCity = new ComboBox();
    ComboBox cbCountry = new ComboBox();

    Button btNext = new Button();
    Button btAddAddress = new Button();

    public AddModifyAddressView(AddAddressViewController controller){
        this.controller = controller;
        setupLabels();
        setupComboBoxes();
        setupTV();
        setupButtons();
        setupLayout();
    }
    public void setupLayout(){
        gpAddress.add(lblNewAddress,0,0);
        gpAddress.add(lblAddress,0,1);
        gpAddress.add(lblAddress2,0,2);
        gpAddress.add(lblCity,0,3);
        gpAddress.add(lblCountry,0,4);
        gpAddress.add(lblPostalCode,0,5);
        gpAddress.add(lblPhone,0,6);

        gpAddress.add(tfAddress,1,1);
        gpAddress.add(tfAddress2,1,2);
        gpAddress.add(cbCity,1,3);
        gpAddress.add(cbCountry,1,4);
        gpAddress.add(tfPostalCode,1,5);
        gpAddress.add(tfPhone,1,6);
        gpAddress.add(btAddAddress,0,7,2,1);

        bpAddress.setLeft(gpAddress);
        bpAddress.setCenter(tvAddress);
        bpAddress.setBottom(btNext);

        mainAnchorPane.getChildren().add(bpAddress);
    }
    public void setupButtons(){
        btNext.setText("Next");
        btAddAddress.setText("Add Address");

        btNext.setOnAction((event) -> {
            //TODO: create a confirmation view
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Customer To Save");
            alert.setHeaderText("Customer Name: " + controller.getCustomer().getCustomerName());
            alert.setContentText("Address: " + controller.getSelectedAddress().getAddress() + "\n" +
                    "Address2: " + controller.getSelectedAddress().getAddress2());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
                controller.handleSaveCustomer();
                MainViewController controller = new MainViewController();
                MainView mainView = new MainView(controller);
                Parent mainViewParent = mainView.getView();
                Scene mainViewScene = new Scene(mainViewParent);
                Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
                winAddProduct.setTitle("Main Screen");
                winAddProduct.setScene(mainViewScene);
                winAddProduct.show();

            } else {
                // ... user chose CANCEL or closed the dialog
            }
        });

        btAddAddress.setOnAction((event) -> {
            //TODO: add address to address list and database
            try {
                Address.isAddressValid(tfAddress.getText(), tfPostalCode.getText(), tfPhone.getText());
                Address address = new Address();
                address.setAddress(tfAddress.getText());
                address.setAddress2(tfAddress2.getText());
                address.setPostalCode(tfPostalCode.getText());
                address.setPhone(tfPhone.getText());
                address.setCityId(controller.getSelectedCity().getCityId());
                address.setCountryId(controller.getSelectedCountry().getCountryId());
                address.setCountry(controller.getSelectedCountry().getCountry());

                controller.handleAddAddress(address);
            } catch (AddressFieldsEmptyException ex){
                System.out.println(ex.getMessage());
            } catch (NullPointerException ex){
                System.out.println(ex.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("City not selected");
                alert.setHeaderText("Error: City Not Selected");
                alert.setContentText("Please Select a City");
                alert.showAndWait();
            }
        });
    }
    public void setupLabels(){
        lblNewAddress.setText("Add New Address:");
        lblAddress.setText("Address:");
        lblAddress2.setText("Address 2:");
        lblCity.setText("City:");
        lblCountry.setText("Country:");
        lblPhone.setText("Phone:");
        lblPostalCode.setText("Postal Code:");
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
    }
    public void setupComboBoxes(){


        cbCity.setCellFactory(lv -> new AddModifyAddressView.CityCell());
        cbCity.setButtonCell(new AddModifyAddressView.CityCell());

        cbCity.valueProperty().addListener((obs, oldValue, newValue) ->{
            //TODO: Listen for city combo box change
            controller.cityComboBoxListener((City) newValue);
        });

        cbCountry.setCellFactory(lv -> new AddModifyAddressView.CountryCell());
        cbCountry.setButtonCell(new AddModifyAddressView.CountryCell());

        cbCountry.valueProperty().addListener((obs, oldValue, newValue) ->{
            //TODO: listen for country combo box change

            controller.countryComboBoxListener((Country) newValue);
            if(!controller.getFilteredCities().isEmpty()) {
                cbCity.getItems().addAll(controller.getFilteredCities());
                cbCity.setValue(controller.getFilteredCities().get(0));
                cbCity.setDisable(false);
            } else {
                cbCity.setValue(null);
                cbCity.getItems().clear();
                cbCity.getSelectionModel().clearSelection();
                cbCity.setDisable(true);
            }
        });

        cbCountry.getItems().addAll(State.getCountries());
        cbCountry.setValue(State.getCountries().get(0));


    }
    public static class CityCell extends ListCell<City> {
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
}
