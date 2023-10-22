package dunbar.c195pa.DAO;

import dunbar.c195pa.Main.JDBC;
import dunbar.c195pa.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**The AppointmentQuery class is an abstract class that holds all methods necessary for performing appointment creation,
 * update, and retrieval to and from the database. */
public abstract class AppointmentQuery {
    /**A list of all appointments in the database*/
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    /**A list of all appointments related to a specific customer*/
    private static ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
    /**A list of all appointments on a particular date*/
    private static ObservableList<Appointment> appointmentsOnDate = FXCollections.observableArrayList();

    /**Get method for the appointments list
     * @return An Observable List of all appointments*/
    public static ObservableList<Appointment> getAppointments(){
        return appointments;
    }

    /**Creates a new appointment in the database.
     * @param contactID The ID number of the contact associated with the appointment
     * @param description A description of the appointment
     * @param end The end datetime of the appointment
     * @param customerID The ID number of the customer associated with the appointment
     * @param location The location for the appointment
     * @param start The start datetime for the appointment
     * @param title The title of the appointment
     * @param type The type of appointment
     * @param userID The ID number of the user associated with the appointment
     * @return Returns an integer representing the number of rows affected by the insert operation*/
    public static int insert(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) throws SQLException {
        Timestamp startTime = Timestamp.valueOf(start);
        Timestamp endTime = Timestamp.valueOf(end);

        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, startTime);
        ps.setTimestamp(6, endTime);
        ps.setInt(7, customerID);
        ps.setInt(8, userID);
        ps.setInt(9, contactID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    /**Updates an existing appointment in the database, based off of Appointment ID.
     * @param contactID The ID number of the contact associated with the appointment
     * @param description A description of the appointment
     * @param end The end datetime of the appointment
     * @param customerID The ID number of the customer associated with the appointment
     * @param location The location for the appointment
     * @param start The start datetime for the appointment
     * @param title The title of the appointment
     * @param type The type of appointment
     * @param userID The ID number of the user associated with the appointment
     * @param id The ID of the appointment being updated*/
    public static void update(int id,String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) throws SQLException {
        Timestamp startTime = Timestamp.valueOf(start);
        Timestamp endTime = Timestamp.valueOf(end);

        String sql = "UPDATE Appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, startTime);
        ps.setTimestamp(6, endTime);
        ps.setInt(7, customerID);
        ps.setInt(8, userID);
        ps.setInt(9, contactID);
        ps.setInt(10,id);
        ps.executeUpdate();
    }

    /**Removes an appointment from the database.
     * @param appointmentID The ID number of the appointment to be removed
     * @return Returns an integer representing the number of rows affected by the delete operation.*/
    public static int delete(int appointmentID) throws SQLException {
        String sql = "DELETE FROM Appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**Retrieves all appointments from the database, generates an Appointment object for each one, and stores the
     * Appointments in the appointments list.
     * @return Returns the appointments list.*/
    public static ObservableList<Appointment> selectAll() throws SQLException {
        appointments.clear();
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM Appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
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
            int contactID = rs.getInt("Contact_ID");
            Appointment appointment = new Appointment(appointmentID, title, description, location, contactID, type, start, end, userID, customerID);
            appointments.add(appointment);
        }
        return appointments;
    }
    /**Retrieves all appointments for a specific Customer ID, generates an Appointment object for each one,
     * and stores it in the customerAppointments list.
     * @param selectedCustomerID The ID number of the customer to pull all appointments for
     * @return Returns the customerAppointments list*/
    public static ObservableList<Appointment> getCustomerAppointments(int selectedCustomerID) throws SQLException {
        customerAppointments.clear();
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM Appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, selectedCustomerID);
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
            int contactID = rs.getInt("Contact_ID");
            if (start.isAfter(LocalDate.now().atStartOfDay())) {
                Appointment appointment = new Appointment(appointmentID, title, description, location, contactID, type, start, end, userID, customerID);
                customerAppointments.add(appointment);
            }
        }
        return customerAppointments;
    }

    /**Retrieves all appointments on a range of three days centered on a given date (to ensure that there are none left
     * out due to timezone related issues), generates an Appointment object for each, and stores them in the
     * appointmentsOnDate list.
     * @param selectedDate The date representing the center of the date range.
     * @return Returns the appointmentsOnDate list*/
    public static ObservableList<Appointment> getAppointmentsOnDate (LocalDate selectedDate) throws SQLException {
        appointmentsOnDate.clear();
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM Appointments WHERE DATE(Start) BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, selectedDate.minusDays(1).toString());
        ps.setString(2, selectedDate.plusDays(1).toString());
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            if(!start.toLocalDate().equals(selectedDate)){
                continue;
            }
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            Appointment appointment = new Appointment(appointmentID, title, description, location, contactID, type, start, end, userID, customerID);
            appointmentsOnDate.add(appointment);
        }
        return appointmentsOnDate;
    }
}
