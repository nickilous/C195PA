package SchedulingApp;

import SchedulingApp.AppState.ScheduleLogger;
import SchedulingApp.DataBase.DBConnection;
import SchedulingApp.ViewController.LoginViewController;
import SchedulingApp.Views.LoginView;
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

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        /**
         * Lambda: This creates a DBConnection which would normally hang the loading of the UI.
         * By passing the lambda into and new thread the loading of the app shouldn't hang
         * and will create a separate thread for the DBConnection to be made on.
         */
        new Thread(() -> DBConnection.makeConnection()).start();
        ScheduleLogger.init();
        launch(args);
        DBConnection.closeConnection();
    }
}
