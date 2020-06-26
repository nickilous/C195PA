package SchedulingApp.Models;


import SchedulingApp.Exceptions.UserFieldsEmptyException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.sql.Timestamp;

public class User {
    StringProperty userName = new SimpleStringProperty();
    StringProperty password = new SimpleStringProperty();
    SimpleIntegerProperty userID = new SimpleIntegerProperty();
    SimpleIntegerProperty active = new SimpleIntegerProperty();

    Date createDate;
    User createdBy;
    Timestamp lastUpdate;
    User lastUpdatedBy;

    public User(){
    }
    public User(String userName, String password) throws UserFieldsEmptyException {
        if(!userName.isEmpty() && !password.isEmpty()) {
            this.userName.setValue(userName);
            this.password.setValue(password);
        } else {
            throw new UserFieldsEmptyException("User Name or Password was empty");
        }
    }

    public String getUserName() {
        return userName.get();
    }
    public StringProperty userNameProperty() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getPassword() {
        return password.get();
    }
    public StringProperty passwordProperty() {
        return password;
    }
    public void setPassword(String password) {
        this.password.set(password);
    }

    public int getUserID() {
        return userID.get();
    }
    public SimpleIntegerProperty userIDProperty() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public int getActive() {
        return active.get();
    }
    public SimpleIntegerProperty activeProperty() {
        return active;
    }
    public void setActive(int active) {
        this.active.set(active);
    }
}
