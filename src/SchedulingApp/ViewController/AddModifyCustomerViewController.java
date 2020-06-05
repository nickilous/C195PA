package SchedulingApp.ViewController;

import SchedulingApp.Models.Customer;

public class AddModifyCustomerViewController {
    private Customer customer = null;


    public AddModifyCustomerViewController() {
    }

    public AddModifyCustomerViewController(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
