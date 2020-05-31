package SchedulingApp.Views;

import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Exceptions.UserFieldsEmptyException;
import SchedulingApp.Models.Customer;
import SchedulingApp.ViewController.AddAddressViewController;
import SchedulingApp.ViewController.AddCustomerViewController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddCustomerView {
    private static Customer customer = new Customer(DataBaseManager.getNextId("customer"));

    AnchorPane mainAnchorPane = new AnchorPane();
    public Parent getView(){
        return mainAnchorPane;
    }

    AddCustomerViewController controller = new AddCustomerViewController();

    Label lblName = new Label();
    TextField tfName = new TextField();

    Button btNext = new Button();
    GridPane gpCustomer = new GridPane();

    public AddCustomerView(AddCustomerViewController controller){
        this.controller = controller;
        setupLabels();
        setupButton();
        setupGridPane();
    }
    public void setupLabels(){
        lblName.setText("Enter Name:");
    }

    public void setupGridPane(){
        gpCustomer.add(lblName, 0,0);
        gpCustomer.add(tfName, 1, 0);
        gpCustomer.add(btNext,0,2,2,1);
        mainAnchorPane.getChildren().add(gpCustomer);
    }


    private void setupButton(){
        btNext.setText("Next");
        btNext.setOnAction((event) -> {
            try {
                Customer.isCustomerValid(tfName.getText());
                customer.setCustomerName(tfName.getText());

                AddAddressView addAddressView = new AddAddressView(new AddAddressViewController(customer));
                Parent mainViewParent = addAddressView.getView();
                Scene mainViewScene = new Scene(mainViewParent);
                Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
                winAddProduct.setTitle("Add Customer Address");
                winAddProduct.setScene(mainViewScene);
                winAddProduct.show();

            } catch (UserFieldsEmptyException ex){
                //TODO: Alert to the fields being empty
            }
        });
    }
}
