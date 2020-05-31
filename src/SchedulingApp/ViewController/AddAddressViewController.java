package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.Models.Address;
import SchedulingApp.Models.City;
import SchedulingApp.Models.Country;
import SchedulingApp.Models.Customer;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddAddressViewController {
    private Customer customer = new Customer();

    private City selectedCity;
    private Country selectedCountry;

    private ObservableList<City> cities = FXCollections.observableArrayList();
    private ObservableList<Country> countries = FXCollections.observableArrayList();

    public AddAddressViewController(Customer customer){
        this.customer = customer;
    }

    public City getSelectedCity() {
        return selectedCity;
    }
    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public ObservableList<City> getCities() {
        return cities;
    }


    public void setCountries(ObservableList<Country> countries) {
        this.countries = countries;
    }

    public void cityComboBoxListener(City selectedCity){
        this.selectedCity = selectedCity;
    }
    public void countryComboBoxListener(Country selectedCountry){
        this.selectedCountry = selectedCountry;
        cities = State.getCitiesByCountry(selectedCountry.getCountryId());
    }

    public void handleAddAddress(Address address){
        State.addAddress(address);
    }

}
