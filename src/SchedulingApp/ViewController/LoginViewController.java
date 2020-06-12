package SchedulingApp.ViewController;


import SchedulingApp.AppState.ScheduleLogger;
import SchedulingApp.AppState.State;
import SchedulingApp.DataBase.DBConnection;
import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Exceptions.UserFieldsEmptyException;
import SchedulingApp.Exceptions.UserNotValidException;
import SchedulingApp.Models.User;
import SchedulingApp.Views.MainView;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


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
            DataBaseManager.validateLogin(user);
            State.setUser(user);
            State.loadAppointments(user);
            State.loadUpComingAppointments(user);
            State.logInAppointmentNotification();
        } catch (UserFieldsEmptyException | UserNotValidException ex) {
            loginViewLogger.log(Level.INFO, ex.getMessage());
            throw ex;
        }
        loginViewLogger.log(Level.INFO, "User logged in");
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
