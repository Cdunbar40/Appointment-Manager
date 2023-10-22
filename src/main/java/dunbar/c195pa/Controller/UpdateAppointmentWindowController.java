package dunbar.c195pa.Controller;
import dunbar.c195pa.DAO.AppointmentQuery;
import dunbar.c195pa.DAO.ContactQuery;
import dunbar.c195pa.DAO.CustomerQuery;
import dunbar.c195pa.DAO.UserQuery;
import dunbar.c195pa.Main.StageHandler;
import dunbar.c195pa.Main.TimeMachine;
import dunbar.c195pa.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**The UpdateAppointmentWindowController controls the interface used to update existing appointments in the database*/
public class UpdateAppointmentWindowController implements Initializable {
    private Stage stage;

    private Parent scene;
    /**Contains a list of all Contact Names as strings*/
    ObservableList<String> contactNames = FXCollections.observableArrayList();

    @FXML
    private TextField appointmentIDField;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> contactCB;

    @FXML
    private TextField customerField;

    @FXML
    private TextField descField;

    @FXML
    private DatePicker endDatePick;

    @FXML
    private ComboBox<LocalTime> endTimeCB;

    @FXML
    private TextField locationField;

    @FXML
    private DatePicker startDatePick;

    @FXML
    private ComboBox<LocalTime> startTimeCB;

    @FXML
    private TextField titleField;

    @FXML
    private TextField typeField;

    @FXML
    private Button updateButton;

    @FXML
    private TextField userField;

    /**If the New Appointment window was opened from the main window, returns to the main window when the cancel button
     *  is pressed. If the New Appointment window was opened from the Customer Appointments window, closes the
     *  window when the cancel button is pressed. */
    @FXML
    void cancel(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        if(stage == StageHandler.getSecondaryStage()){
            stage.close();
        }
        else {
            scene = FXMLLoader.load((getClass().getResource("/dunbar/c195pa/MainWindow.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    /**Validates the fields in the new appointment window, and commits the appointment update to the database when the
     * save button is pressed. If the New Appointment window was opened from the main window, returns to the main window
     * when complete. If the New Appointment window was opened from the Customer Appointments window, closes the
     * window when complete.*/
    @FXML
    void saveAppointment(ActionEvent event) throws IOException, SQLException {
        int id = Integer.parseInt(appointmentIDField.getText());
        String title;
        String description;
        String location;
        String type;
        LocalDate startDate;
        LocalTime startTime;
        LocalDate endDate;
        LocalTime endTime;
        int customerID;
        int userID;
        String contact;
        try {
            title = titleField.getText();
            if (title.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception noTitle) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a title");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try {
            description = descField.getText();
            if (description.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception noDesc) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a description");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try {
            location = locationField.getText();
            if (location.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception noLocation) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a location");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try {
            type = typeField.getText();
            if (type.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception noType) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a type");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try {
            startDate = startDatePick.getValue();
            startTime = startTimeCB.getValue();
            endDate = endDatePick.getValue();
            endTime = endTimeCB.getValue();
            if (startDate.equals(null) || startTime.equals(null) || endDate.equals(null) || endTime.equals(null)) {
                throw new Exception();
            }
        } catch (Exception noAppointment) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment Start/End Date/Time");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try {
            customerID = Integer.parseInt(customerField.getText());
            if (customerID < 1 || !CustomerQuery.findCustomer(customerID)) {
                throw new Exception();
            }
        } catch (Exception badID){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid Customer ID");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try {
            userID = Integer.parseInt(userField.getText());
            if (userID < 1 || !UserQuery.findUser(userID)) {
                throw new Exception();
            }
        } catch (Exception badID){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid User ID");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try {
            contact = contactCB.getValue();
            if (contact.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception noContact){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a contact");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        if (!TimeMachine.checkAvailability(startDate, startTime, endDate, endTime, id)) {
            return;
        }
        LocalDateTime startDT = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(endDate, endTime);
        int contactID = ContactQuery.getContactID(contact);
        AppointmentQuery.update(id, title, description, location, type, startDT, endDT, customerID, userID, contactID);

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        if(stage == StageHandler.getSecondaryStage()){
            stage.close();
        }
        else {
            scene = FXMLLoader.load((getClass().getResource("/dunbar/c195pa/MainWindow.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    /**Populates the update appointment fields with the selected appointment's current information
     * @param selectedAppointment Appointment to be updated*/
    public void loadAppointment(Appointment selectedAppointment) {
        appointmentIDField.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        titleField.setText(selectedAppointment.getTitle());
        descField.setText(selectedAppointment.getDescription());
        locationField.setText(selectedAppointment.getLocation());
        typeField.setText(selectedAppointment.getType());
        customerField.setText(String.valueOf(selectedAppointment.getCustomerID()));
        userField.setText(String.valueOf(selectedAppointment.getUserID()));
        contactCB.setValue(selectedAppointment.getContactName());
        startDatePick.setValue(selectedAppointment.getStart().toLocalDate());
        startTimeCB.setValue(selectedAppointment.getStart().toLocalTime());
        endDatePick.setValue(selectedAppointment.getEnd().toLocalDate());
        endTimeCB.setValue(selectedAppointment.getEnd().toLocalTime());
    }

    /**Populates the options for the Contact, Start Time, and End Time combo boxes.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCB.setItems(ContactQuery.getAllContactNames());
        startTimeCB.setItems(TimeMachine.getAvailableStartTimes());
        endTimeCB.setItems(TimeMachine.getAvailableEndTimes());
    }
}

