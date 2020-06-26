package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.AddressFieldsEmptyException;
import SchedulingApp.Exceptions.InvalidCustomerException;
import SchedulingApp.Models.*;
import SchedulingApp.Views.MainView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

import java.util.Optional;

public class CustomerViewController {
    Customer customer = new Customer();
    Address address = new Address();
    City city = new City();
    Country country = new Country();

    boolean isModifying = false;

    public CustomerViewController(){
    }
    public CustomerViewController(Customer customer){
        isModifying = true;
        this.customer = customer;
        this.address = State.getAddressFromCustomer(customer);
        this.city = State.getCityFromCustomer(customer);
        this.country = State.getCountryFromCustomer(customer);

        this.selectedCity = city;
        this.selectedCountry = country;
    }
    private City selectedCity;
    private Country selectedCountry;

    FilteredList<City> filteredCityData = new FilteredList<>(State.getCities(), c -> true);
    ObservableList<City> filteredCities = FXCollections.observableArrayList();


    public Customer getCustomer(){
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public boolean isModifying() {
        return isModifying;
    }


    public void setSelectedAddress(Address address){
        this.address = address;
        customer.setAddressId(address.getAddressId());
        customer.setAddress(address.getAddress());
        customer.setAddress2(address.getAddress2());
        customer.setPhone(address.getPhone());
        customer.setPostalCode(address.getPostalCode());
        customer.setCityId(address.getCityId());
        customer.setCity(address.getCity());
        customer.setCountryId(address.getCountryId());
        customer.setCountry(address.getCountry());
    }
    public Address getSelectedAddress(){return address;}
    public City getSelectedCity() {
        return selectedCity;
    }
    public Country getSelectedCountry() {
        return selectedCountry;
    }
    public ObservableList getFilteredCities(){return filteredCities;}

    /**
     * Sets the selected city from the combo box
     * @param selectedCity from combo box
     */
    public void cityComboBoxListener(City selectedCity){
        this.selectedCity = selectedCity;
    }

    /**
     * Sets the selected country from the combo box
     * @param selectedCountry from combo box
     */
    public void countryComboBoxListener(Country selectedCountry){
        filteredCityData.setPredicate(city ->{
            if (city.getCountryId() ==  selectedCountry.getCountryId()){
                return true;
            }
            return false;
        });
        this.selectedCountry = selectedCountry;
        filteredCities.clear();
        filteredCities.addAll(filteredCityData);
    }

    /**
     * Saves Finished customer to the state and which saves it
     * to the database through the state add listener
     */
    public void handleSaveCustomer(Event event){
        try {
            Customer.isValidInput(customer);

            customer.setAddress(address.getAddress());
            customer.setAddress2(address.getAddress2());
            customer.setPostalCode(address.getPostalCode());
            customer.setPhone(address.getPhone());
            customer.setAddressId(address.getAddressId());
            customer.setCityId(address.getCityId());
            customer.setCity(address.getCity());
            customer.setCountryId(address.getCountryId());
            customer.setCountry(address.getCountry());


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Customer To Save");
            alert.setHeaderText("Customer Name: " + getCustomer().getCustomerName());
            alert.setContentText("Address: " + getSelectedAddress().getAddress() + "\n" +
                    "Address2: " + getSelectedAddress().getAddress2());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if(!isModifying) {
                    State.addCustomer(customer);
                } else {
                    State.updateCustomer(customer);
                }
                MainViewController controller = new MainViewController();
                MainView mainView = new MainView(controller);
                Parent mainViewParent = mainView.getView();
                Scene mainViewScene = new Scene(mainViewParent);
                Stage winAddProduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
                winAddProduct.setTitle("Main Screen");
                winAddProduct.setScene(mainViewScene);
                winAddProduct.show();
            }
        } catch (InvalidCustomerException ex){
            System.out.println(ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Adding Customer");
            alert.setHeaderText("There is an empty field.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        } catch (NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Adding Customer");
            alert.setHeaderText("No address for customer.");
            alert.setContentText("Please select an address for the Customer");
            alert.showAndWait();
        }
    }

    /**
     * Saves address to the state which then saves it to the database through the
     * add item listener
     */
    public void handleAddAddress(Event event) {
        try {
            Address.isAddressValid(address.getAddress(), address.getPostalCode(), address.getPhone());
            address.setCityId(selectedCity.getCityId());
            address.setCity(selectedCity.getCity());
            address.setCountryId(selectedCountry.getCountryId());
            address.setCountry(selectedCountry.getCountry());

            State.addAddress(address);
        } catch (AddressFieldsEmptyException ex) {
            System.out.println(ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Adding Address");
            alert.setHeaderText("There is an empty field.");
            alert.setContentText(ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("City not selected");
            alert.setHeaderText("Error: City Not Selected");
            alert.setContentText("Please Select a City");
            alert.showAndWait();
        }
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
