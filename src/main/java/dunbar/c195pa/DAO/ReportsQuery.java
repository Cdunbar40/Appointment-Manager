package dunbar.c195pa.DAO;

import dunbar.c195pa.Main.JDBC;
import dunbar.c195pa.Model.Appointment;
import dunbar.c195pa.Model.ReportBlock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**The ReportsQuery class is an abstract class containing all methods needed to generate the My Appointments, Appointments
 * By Type, and Schedule By Contact reports. */
public abstract class ReportsQuery {
    /**List of all appointments associated with the userID of the current logged in user. */
    private static ObservableList<Appointment> myAppointments = FXCollections.observableArrayList();
    /**List of appointment blocks based on type and month. */
    private static ObservableList<ReportBlock> appointmentsByType = FXCollections.observableArrayList();
    /**List of all appointments associated with a specific contact*/
    private static ObservableList<Appointment> scheduleByContact = FXCollections.observableArrayList();

    /**Retrieves all appointments associated with the user ID of the user currently logged into the application.
     * Generates an Appointment object for each appointment and stores them in the myAppointments list.
     * @param userID current userID
     * @return Returns the myAppointments ObservableList */
    public static ObservableList<Appointment> getMyAppointments(int userID) throws SQLException {
        myAppointments.clear();
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, Contact_ID FROM Appointments WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,userID);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int contactID = rs.getInt("Contact_ID");
            Appointment appointment = new Appointment(appointmentID, title, description, location, contactID, type, start, end, userID, customerID);
            myAppointments.add(appointment);
        }
        return myAppointments;
    }

    /**Retrieves a count of all appointments per type per month, generates a ReportBlock object for each month/type
     * combo, and stores it in the appointmentsByType list.
     * @return Returns the appointmentsByType ObservableList*/
    public static ObservableList<ReportBlock> getAppointmentsByType() throws SQLException {
        appointmentsByType.clear();
        String sql = "SELECT monthname(Start) AS month, type, COUNT(type) AS type_count FROM appointments " +
                "GROUP BY month, type " +
                "ORDER BY month";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            String month = rs.getString("month");
            String type = rs.getString("type");
            int count = rs.getInt("type_count");
            ReportBlock block = new ReportBlock(month, type, count);
            appointmentsByType.add(block);
        }
        return appointmentsByType;
    }

    /**Retrieves a list of all appointments associated with a specific contact, ordered from earliest to latest,
     *  generates an Appointment object for each, and stores them in the scheduleByContact list.
     *  @param id Contact ID of the contact of interest
     *  @return Returns the scheduleByContact ObservableList*/
    public static ObservableList<Appointment> getScheduleByContact(int id) throws SQLException {
        scheduleByContact.clear();
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID FROM Appointments WHERE Contact_ID = ? ORDER BY Start";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            Appointment appointment = new Appointment(appointmentID, title, description, location, id, type, start, end, userID, customerID);
            scheduleByContact.add(appointment);
        }
        return scheduleByContact;
    }
}
