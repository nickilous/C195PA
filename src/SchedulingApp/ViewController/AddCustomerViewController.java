package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Models.Address;
import SchedulingApp.Models.Customer;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class AddCustomerViewController {
    public AddCustomerViewController(){
        State.getAddresses().addListener((ListChangeListener<Address>) c -> {
            while(c.next()) {
                for (Address addedAddress : c.getAddedSubList()) {
                    DataBaseManager.saveAddress(addedAddress);
                }
            }
        });
        State.getCustomers().addListener((ListChangeListener<Customer>) c -> {
            while (c.next()) {
                for (Customer addedCustomer : c.getAddedSubList()) {
                    DataBaseManager.saveCustomer(addedCustomer);
                }
            }
        });
    }
    public void handleSave(Customer customer, Address address){
        State.addAddress(address);
        State.addCustomer(customer);
    }
    public void cityComboBoxListener(){

    }
    public void countryComboBoxListener(){

    }
}
