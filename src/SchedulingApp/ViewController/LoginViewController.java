package SchedulingApp.ViewController;


import SchedulingApp.AppState.ScheduleLogger;
import SchedulingApp.AppState.State;
import SchedulingApp.DataBase.DBConnection;
import SchedulingApp.Exceptions.UserFieldsEmptyException;
import SchedulingApp.Exceptions.UserNotValidException;
import SchedulingApp.Models.User;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginViewController {
    Logger loginViewLogger = new ScheduleLogger().getLogger(LoginViewController.class.getName());

    public void handleLogin(String userName,
                            String password) throws UserFieldsEmptyException, UserNotValidException {
        try {
            User user = new User(userName, password);
            user = validateLogin(user);
            State.setUser(user);
        } catch (UserFieldsEmptyException | UserNotValidException ex) {
            //TODO: Handle different Laguages in error dialog
            loginViewLogger.log(Level.INFO, ex.getMessage());
            throw ex;
        }
        loginViewLogger.log(Level.INFO, "User logged in");
    }
    /**
     * Searches for matching username and password in database
     * @param user
     * @return user if match found
     */
    User validateLogin(User user) throws UserNotValidException {
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
            pst.setString(1, user.getUserName());
            pst.setString(2, user.getPassword());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setUserID(rs.getInt("userId"));
            } else {
                throw new UserNotValidException("User not found in DB.");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return user;
    }
    public void handleCancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want close the program?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent((ButtonType response) -> {
                    try {
                        DBConnection.closeConnection();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    Platform.exit();
                            System.exit(0);
                        }
                );
    }
    public void comboBoxListener(Locale newLanguage){
        System.out.println(newLanguage.getLanguage());
        State.setSelectedLanguage(newLanguage);
    }
}
