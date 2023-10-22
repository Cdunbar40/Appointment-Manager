package dunbar.c195pa.Main;

import dunbar.c195pa.DAO.AppointmentQuery;
import dunbar.c195pa.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import java.sql.SQLException;
import java.time.*;
import java.util.Comparator;

/**Contains all methods used to convert and compare dates and times for the program.*/
public abstract class TimeMachine {
    /**Stores the local timezone ID*/
    private static ZoneId localID = ZoneId.systemDefault();
    /**Stores the est timezone ID*/
    private static ZoneId easternID = ZoneId.of("America/New_York");
    /**Date 1 week from when the application is launched.*/
    private static LocalDate endWeek = LocalDate.now().plusWeeks(1);
    /**Date 1 month from when the application is launched*/
    private static LocalDate endMonth = LocalDate.now().plusMonths(1);
    /**Placeholder, used for ZonedDateTimes in the local timezone*/
    private static ZonedDateTime myDT;
    /**Business open time in the eastern timezone*/
    private static ZonedDateTime hoursStartEST = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8,0), easternID);
    /**Business open time in the local timezone*/
    private static LocalTime hoursStartLocal = LocalTime.from(hoursStartEST.withZoneSameInstant(localID));
    /**Contains all possible appointment start times for a given day in the local timezone*/
    private static ObservableList<LocalTime> availableStartTimes = FXCollections.observableArrayList();
    /**Contains all possible appointment end times for a given day in the local timezone*/
    private static ObservableList<LocalTime> availableEndTimes = FXCollections.observableArrayList();

    /**Converts from local time to eastern time
     * @param local LocalDateTime to convert
     * @return Returns the ZonedDateTime for the eastern timezone*/
    public static ZonedDateTime localToEastern (LocalDateTime local) {
        myDT = local.atZone(localID);
        ZonedDateTime est = myDT.withZoneSameInstant(easternID);
        return est;
    }

    /**Generates the list of possible start and end times for a given day in the local timezone
     * @return Returns the availableStartTimes ObservableList*/
    public static ObservableList<LocalTime> getAvailableStartTimes() {
        availableStartTimes.clear();
        availableEndTimes.clear();
        int i = 0;
        for(i = 0;  i < 14; i++){
            LocalTime timeToAdd = hoursStartLocal.plusHours(i);
            availableStartTimes.add(timeToAdd);
            availableEndTimes.add(timeToAdd.plusHours(1));
        }
        availableStartTimes.sort(Comparator.naturalOrder());
        availableEndTimes.sort(Comparator.naturalOrder());
        return availableStartTimes;
    }
    /**Getter method for availableEndTimes
     * @return Returns the availableEndTimes ObservableList*/
    public static ObservableList<LocalTime> getAvailableEndTimes(){
        return availableEndTimes;
    }

    /**Checks for valid appointment start and end dates and times. Includes logical checks and existing appointment
     *overlap checks. Overloaded method - This version is used for new appointments
     * @param endDate new appointment desired end date
     * @param endTime new appointment desired end time
     * @param startDate new appointment desired start date
     * @param startTime new appointment desired start time
     * @return Returns false if the start time is in the past, if the end time is before the start time, if the
     * appointment starts within office hours but extends outside of office hours, or if the appointment overlaps with an
     * existing appointment. Otherwise, returns true. */
    public static boolean checkAvailability(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) throws SQLException {
        ZonedDateTime localStartDT = ZonedDateTime.of(startDate, startTime, localID);       //ZDT of new app start in local TZ
        ZonedDateTime localEndDT = ZonedDateTime.of(endDate, endTime, localID);             //ZDT of new app end in local TZ
        ZonedDateTime estStart = localStartDT.withZoneSameInstant(easternID);               //ZDT of new app start in EST
        ZonedDateTime estEnd = localEndDT.withZoneSameInstant(easternID);                   //ZDT of new app end in EST
        ObservableList<Appointment> appointmentsOnDate = AppointmentQuery.getAppointmentsOnDate(startDate);
        if((estStart.isBefore(ZonedDateTime.now()))){                                       //If start time is in the past
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selected Date/Time has already passed");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return false;
        }
        if(estEnd.isBefore(estStart)){                                                      //If end time is before start time
            Alert alert = new Alert(Alert.AlertType.ERROR, "End time cannot be before start time");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return false;
        }
        if((estStart.toLocalDate().isBefore(estEnd.toLocalDate()))){                        //If appointment start date is different from the end date in the eastern timezone
            Alert alert = new Alert(Alert.AlertType.ERROR, "Appointment falls outside of office hours");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return false;
        }
        for(Appointment appointment: appointmentsOnDate){
            ZonedDateTime existingAppointmentStart = TimeMachine.localToEastern(appointment.getStart());    //ZDT start of existing app in EST
            ZonedDateTime existingAppointmentEnd = TimeMachine.localToEastern(appointment.getEnd());        //ZDT end of existing app in EST
            if((estStart.isBefore(existingAppointmentStart) && estEnd.isAfter(existingAppointmentStart))    //Checking appointment overlaps
                    || (estStart.equals(existingAppointmentStart))
                    || (estStart.isAfter(existingAppointmentStart) && estStart.isBefore(existingAppointmentEnd)))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointment overlaps with Appointment ID: " + appointment.getAppointmentID());
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
                return false;
            }
        }
        return true;
    }
    /**Checks for valid appointment start and end dates and times. Includes logical checks and existing appointment
     *overlap checks.Overloaded method - This version is for appointments being updated.
     * @param endDate new appointment desired end date
     * @param endTime new appointment desired end time
     * @param startDate new appointment desired start date
     * @param startTime new appointment desired start time
     * @param selectedAppointmentID ID of the appointment being updated.
     * @return Returns false if the start time is in the past, if the end time is before the start time, if the
     * appointment starts within office hours but extends outside of office hours, or if the appointment overlaps with an
     * existing appointment. Otherwise, returns true. */
    public static boolean checkAvailability(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, int selectedAppointmentID) throws SQLException {
        ZonedDateTime localStartDT = ZonedDateTime.of(startDate, startTime, localID);       //ZDT of new app start in local TZ
        ZonedDateTime localEndDT = ZonedDateTime.of(endDate, endTime, localID);             //ZDT of new app end in local TZ
        ZonedDateTime estStart = localStartDT.withZoneSameInstant(easternID);               //ZDT of new app start in EST
        ZonedDateTime estEnd = localEndDT.withZoneSameInstant(easternID);                   //ZDT of new app end in EST
        ObservableList<Appointment> appointmentsOnDate = AppointmentQuery.getAppointmentsOnDate(startDate);
        if((estStart.isBefore(ZonedDateTime.now()))){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selected Date/Time has already passed");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return false;
        }
        if(estEnd.isBefore(estStart)){
            Alert alert = new Alert(Alert.AlertType.ERROR, "End time cannot be before start time");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return false;
        }
        if((estStart.toLocalDate().isBefore(estEnd.toLocalDate()))){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Appointment falls outside of office hours");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return false;
        }
        for(Appointment appointment: appointmentsOnDate){
            if(appointment.getAppointmentID() == selectedAppointmentID){        //Prevents errors due to an appointment overlapping with itself
                continue;
            }
            ZonedDateTime existingAppointmentStart = TimeMachine.localToEastern(appointment.getStart());    //ZDT start of existing app in EST
            ZonedDateTime existingAppointmentEnd = TimeMachine.localToEastern(appointment.getEnd());        //ZDT end of existing app in EST
            if((estStart.isBefore(existingAppointmentStart) && estEnd.isAfter(existingAppointmentStart))
                    || (estStart.equals(existingAppointmentStart))
                    || (estStart.isAfter(existingAppointmentStart) && estStart.isBefore(existingAppointmentEnd)))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointment overlaps with Appointment ID: " + appointment.getAppointmentID());
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
                return false;
            }
        }
        return true;
    }
    /**Getter method for endweek.
     * @return Returns the LocalDate endWeek*/
    public static LocalDate getEndWeek(){return endWeek;}
    /**Getter method for endMonth
     * @return Returns the LocalDate endMonth*/
    public static LocalDate getEndMonth(){return endMonth;}
}
