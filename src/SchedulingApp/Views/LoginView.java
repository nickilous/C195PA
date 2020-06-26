package SchedulingApp.Views;

import SchedulingApp.AppResources.ObservableResourceFactory;
import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.UserFieldsEmptyException;
import SchedulingApp.Exceptions.UserNotValidException;
import SchedulingApp.ViewController.LoginViewController;
import SchedulingApp.ViewController.MainViewController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Programatic UI setup and layout
 */
public class LoginView {
    LoginViewController controller;

    AnchorPane anchorPane;

    HBox hBox;
    VBox vBox;

    Label welcomeText;

    TextField userName;
    TextField password;

    Label lbUserName;
    Label lbPassword;
    Label lbLanguage;

    HBox btHBox;
    Button btLogin;
    Button btCancel;

    ComboBox<Locale>  cbLocale;

    public LoginView(LoginViewController controller){
        this.controller = controller;

        anchorPane = new AnchorPane();

        hBox = new HBox();
        vBox = new VBox();

        welcomeText = new Label();

        userName = new TextField();
        password = new TextField();

        setupComboBox();
        setupWelcomeText();
        setupButtons();
        setupLabels();
        createLayout();
    }

    private void createLayout(){

        GridPane loginGridPane = new GridPane();
        loginGridPane.setPadding(new Insets(10,10,10,10));
        loginGridPane.setAlignment(Pos.CENTER);
        loginGridPane.setVgap(10);
        loginGridPane.setHgap(10);

        loginGridPane.add(welcomeText, 0, 0, 2, 1);

        loginGridPane.add(lbUserName, 0, 1);
        loginGridPane.add(lbPassword, 0, 2);

        loginGridPane.add(userName, 1, 1);
        loginGridPane.add(password, 1, 2);

        loginGridPane.add(lbLanguage, 0,3);
        loginGridPane.add(cbLocale, 1, 3);

        loginGridPane.add(btHBox, 1, 4);

        anchorPane.getChildren().add(loginGridPane);
    }

    private void setupWelcomeText(){
        Font welcomeFont = new Font(21);
        welcomeText.setFont(welcomeFont);
        welcomeText.textProperty().bind(RESOURCE_FACTORY.getStringBinding("welcome"));
        welcomeText.setAlignment(Pos.CENTER);
    }

    private void setupLabels(){
        lbUserName = new Label();
        lbPassword = new Label();
        lbLanguage = new Label();

        lbUserName.textProperty().bind(RESOURCE_FACTORY.getStringBinding("username"));
        lbPassword.textProperty().bind(RESOURCE_FACTORY.getStringBinding("password"));
        lbLanguage.textProperty().bind(RESOURCE_FACTORY.getStringBinding("language"));
    }

    private void setupButtons(){
        btHBox = new HBox();
        btLogin = new Button("Login");
        btCancel = new Button("Cancel");

        btLogin.setOnAction((event) -> {
            try {
                controller.handleLogin(userName.getText(), password.getText());
                MainViewController controller = new MainViewController();
                MainView mainView = new MainView(controller);
                Parent mainViewParent = mainView.getView();
                Scene mainViewScene = new Scene(mainViewParent);
                Stage winAddProduct = (Stage)((Node)event.getSource()).getScene().getWindow();
                winAddProduct.setTitle("Main Screen");
                winAddProduct.setScene(mainViewScene);
                winAddProduct.centerOnScreen();
                winAddProduct.show();

            } catch (UserFieldsEmptyException | UserNotValidException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.titleProperty().bind(RESOURCE_FACTORY.getStringBinding("loginErrorTitle"));
                if (ex instanceof UserFieldsEmptyException){
                    alert.contentTextProperty().bind(RESOURCE_FACTORY.getStringBinding("loginErrorEmpty"));
                } else if( ex instanceof UserNotValidException){
                    alert.contentTextProperty().bind(RESOURCE_FACTORY.getStringBinding("loginErrorIncorrect"));
                }
                alert.show();
                System.out.println("Fields Empty");
            }
        });

        btCancel.setOnAction((event) -> {
            controller.handleCancel();
        });

        btHBox.getChildren().add(btLogin);
        btHBox.getChildren().add(btCancel);

        btHBox.setAlignment(Pos.BOTTOM_RIGHT);
    }

    private static final String RESOURCE_NAME = "SchedulingApp/login" ;
    private static final ObservableResourceFactory RESOURCE_FACTORY = new ObservableResourceFactory();
    static {
        RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME));
    }
    private void setupComboBox(){
        cbLocale = new ComboBox<>();
        cbLocale.getItems().addAll(Locale.ENGLISH, Locale.FRENCH);
        cbLocale.setValue(Locale.ENGLISH);
        State.setSelectedLanguage(cbLocale.getValue());
        cbLocale.setCellFactory(lv -> new LocaleCell());
        cbLocale.setButtonCell(new LocaleCell());

        cbLocale.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME, newValue));
                controller.comboBoxListener(newValue);
            }
        });
    }
    public static class LocaleCell extends ListCell<Locale> {
        @Override
        public void updateItem(Locale locale, boolean empty) {
            super.updateItem(locale, empty);
            if (empty) {
                setText(null);
            } else {
                setText(locale.getDisplayLanguage(locale));
            }
        }
    }

    public Parent getView(){
        return anchorPane;
    }
}
