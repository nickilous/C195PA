package SchedulingApp.ViewController;

import SchedulingApp.AppState.State;
import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Models.Address;
import SchedulingApp.Models.City;
import SchedulingApp.Models.Country;
import SchedulingApp.Models.Customer;
import SchedulingApp.Views.AddCustomerView;
import javafx.collections.ListChangeListener;

import java.util.List;

public class AddCustomerViewController {
    public AddCustomerViewController(){

    }
    /**
     * Handles the save button from the add customer view
     * address the new customer and address to the observable list in the State
     * @param customer customer to be saved
     * @param address address to be saved
     */
    public void handleSave(Customer customer, Address address){
        //Address must be listed first so that when the customer is added the address id exists
        State.addAddress(address);
        State.addCustomer(customer);
    }
}
