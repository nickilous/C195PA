package SchedulingApp.Views;

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

public class AddModifyCustomerView {
    AnchorPane mainAnchorPane = new AnchorPane();
    public Parent getView(){
        return mainAnchorPane;
    }

    AddCustomerViewController controller;

    Label lblName = new Label();
    TextField tfName = new TextField();

    Button btNext = new Button();
    GridPane gpCustomer = new GridPane();

    public AddModifyCustomerView(AddCustomerViewController controller){
        this.controller = controller;
        setupLabels();
        setupTextField();
        setupButton();
        setupGridPane();
    }
    public void setupLabels(){
        lblName.setText("Enter Name:");
    }
    public void setupTextField(){
        if(!(controller.getCustomer() == null)){
            tfName.setText(controller.getCustomer().getCustomerName());
        }
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
                controller.getCustomer().setCustomerName(tfName.getText());

                AddModifyAddressView addModifyAddressView = new AddModifyAddressView(new AddAddressViewController(controller.getCustomer()));
                Parent mainViewParent = addModifyAddressView.getView();
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
