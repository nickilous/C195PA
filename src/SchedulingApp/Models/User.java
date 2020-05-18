package SchedulingApp.Models;


import SchedulingApp.Exceptions.UserFieldsEmptyException;

import java.sql.Date;
import java.sql.Timestamp;

public class User {
    String userName;
    String password;
    int userID;
    int active;
    Date createDate;
    User createdBy;
    Timestamp lastUpdate;
    User lastUpdatedBy;

    public User(){
        this.userName = "";
        this.password = "";
    }
    public User(String userName, String password) throws UserFieldsEmptyException {
        if(!userName.isEmpty() && !password.isEmpty()) {
            this.userName = userName;
            this.password = password;
        } else {
            throw new UserFieldsEmptyException("User Name or Password was empty");
        }
    }

    public String getUserName(){
        return this.userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
