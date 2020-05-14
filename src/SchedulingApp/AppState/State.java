package SchedulingApp.AppState;

import SchedulingApp.Models.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Locale;

public class State {
    static Locale selectedLanguage;
    static User user;

    public static void setUser(User user) {
        State.user = user;
    }
    public static User getUser() {
        return user;
    }

    public static Locale getSelectedLanguage() {
        return selectedLanguage;
    }
    public static void setSelectedLanguage(Locale selectedLanguage) {
        State.selectedLanguage = selectedLanguage;
    }
}
