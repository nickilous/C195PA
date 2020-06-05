package SchedulingApp.ViewController;

import SchedulingApp.Models.Customer;
import SchedulingApp.Views.AddModifyCustomerView;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainViewController {
    private Customer customer;

    public void setSelectedCustomer(Customer customer){
        this.customer = customer;
    }
    public Customer getSelectedCustomer(){
        return customer;
    }

    public void loadAddCustomerView(Event event){
        AddModifyCustomerViewController addModifyCustomerViewController = new AddModifyCustomerViewController();
        AddModifyCustomerView addModifyCustomerView = new AddModifyCustomerView(addModifyCustomerViewController);
        Parent addCustomerViewParent = addModifyCustomerView.getView();
        Scene mainViewScene = new Scene(addCustomerViewParent);
        Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
        winAddProduct.setTitle("Add Customer");
        winAddProduct.setScene(mainViewScene);
        winAddProduct.show();
    }
    public void loadModifyCustomerView(Event event){
        AddModifyCustomerViewController addModifyCustomerViewController = new AddModifyCustomerViewController(customer);
        AddModifyCustomerView addModifyCustomerView = new AddModifyCustomerView(addModifyCustomerViewController);
        Parent addCustomerViewParent = addModifyCustomerView.getView();
        Scene mainViewScene = new Scene(addCustomerViewParent);
        Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
        winAddProduct.setTitle("Modify Customer");
        winAddProduct.setScene(mainViewScene);
        winAddProduct.show();
    }
}
