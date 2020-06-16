package SchedulingApp.Exceptions;

public class OverlappingAppointmentException extends Exception{
    public OverlappingAppointmentException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
