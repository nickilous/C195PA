package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.Models.Address;
import SchedulingApp.Models.City;
import SchedulingApp.Models.Country;
import SchedulingApp.Models.Customer;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class AddAddressViewController {
    private Customer customer;
    private Address address;

    private City selectedCity;
    private Country selectedCountry;

    FilteredList<City> filteredData = new FilteredList<>(State.getCities(), c -> true);
    ObservableList<City> filteredCities = FXCollections.observableArrayList();

    public AddAddressViewController(Customer customer){
        this.customer = customer;
    }

    public Customer getCustomer(){
        return customer;
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
        filteredData.setPredicate(city ->{
            if (city.getCountryId() ==  selectedCountry.getCountryId()){
                return true;
            }
            return false;
        });
        this.selectedCountry = selectedCountry;
        filteredCities.clear();
        filteredCities.addAll(filteredData);
    }

    /**
     * Saves Finished customer to the state and which saves it
     * to the database through the state add listener
     */
    public void handleSaveCustomer(){
        State.addCustomer(customer);
    }

    /**
     * Saves address to the state which then saves it to the database through the
     * add item listener
     * @param address new address to add to state and database
     */
    public void handleAddAddress(Address address){
        State.addAddress(address);
    }

}
