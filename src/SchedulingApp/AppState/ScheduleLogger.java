package SchedulingApp.AppState;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ScheduleLogger {
    private static FileHandler handler;
    private Logger logger;

    public static void init( ){
        try {
            handler = new FileHandler("SchedulingApp-Userlog.log", 1024 * 1024, 10, true);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger(String className){
        logger = Logger.getLogger(className);
        logger.addHandler(handler);
        return logger;
    }
}
