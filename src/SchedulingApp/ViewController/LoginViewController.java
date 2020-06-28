package SchedulingApp.ViewController;


import SchedulingApp.AppState.ScheduleLogger;
import SchedulingApp.AppState.State;
import SchedulingApp.DataBase.DBConnection;
import SchedulingApp.DataBase.DataBaseManager;
import SchedulingApp.Exceptions.UserFieldsEmptyException;
import SchedulingApp.Exceptions.UserNotValidException;
import SchedulingApp.Models.User;
import javafx.application.Platform;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


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
            /**
             * Lambda: Loads the appoinments in a different thread this allows the main thread to keep loading
             * while the appointments are loading.
             */
            new Thread(() -> {
                State.loadAppointments(user);
                State.loadUpComingAppointments(user);
                State.logInAppointmentNotification();
                State.addListListeners();
            }).start();
        } catch (UserFieldsEmptyException | UserNotValidException ex) {
            loginViewLogger.log(Level.INFO, ex.getMessage());
            throw ex;
        }
        loginViewLogger.log(Level.INFO, "User " + userName +" logged in");
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
                    } catch (SQLException ex) {
                        ex.printStackTrace();
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
