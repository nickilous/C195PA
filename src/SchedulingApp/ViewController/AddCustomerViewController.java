package SchedulingApp.ViewController;

import SchedulingApp.Models.Customer;

public class AddCustomerViewController {
    private Customer customer = null;

    public AddCustomerViewController(){

    }
    public AddCustomerViewController(Customer customer){
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
