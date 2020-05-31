package SchedulingApp;

import SchedulingApp.AppState.ScheduleLogger;
import SchedulingApp.AppState.State;
import SchedulingApp.DataBase.DBConnection;
import SchedulingApp.ViewController.LoginViewController;
import SchedulingApp.Views.LoginView;
import SchedulingApp.Views.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{



        LoginViewController loginViewController = new LoginViewController();
        LoginView loginView = new LoginView(loginViewController);
        Scene scene = new Scene(loginView.getView());

        /*MainView mainView = new MainView();
        Scene scene = new Scene(mainView.getView());*/

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        /**
         * Lambda: This creates a DBConnection which would normally hang the loading of the UI.
         * By passing the lambda into and new thread the loading of the app shouldn't hang
         * and will create a separate thread for the DBConnection to be made on.
         */
        new Thread(() -> {
            DBConnection.makeConnection();
            State.loadAddresses();
            State.loadCountries();
            State.loadCities();
            State.loadCustomers();
        }).start();


        ScheduleLogger.init();
        launch(args);
        DBConnection.closeConnection();
    }
}
