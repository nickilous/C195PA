package SchedulingApp.ViewController;

import SchedulingApp.Models.Customer;

public class MainViewController {
    private Customer customer;
    public void setCustomer(Customer customer){
        this.customer = customer;
    }
    public Customer getCustomer(){
        return customer;
    }
}
