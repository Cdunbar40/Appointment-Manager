package dunbar.c195pa.Controller;
import dunbar.c195pa.DAO.AppointmentQuery;
import dunbar.c195pa.Model.Appointment;
import dunbar.c195pa.Model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/** The CustomerAppointmentsWindowController controls the interface used to display all appointments for a particular
 * customer, as selected in the CustomerInfoWindow. Allows adding, updating, and deleting of appointments for the specific
 * customer without needing to return to the main appointments window.*/
public class CustomerAppointmentsWindowController {
    private Stage stage;
    private Parent scene;
    /** Holds the customer ID of the customer selected in the Customer Info Window*/
    private int selectedCustomerID;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private TableColumn<?, ?> appointmentIDCol;

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private Button closeButton;

    @FXML
    private TableColumn<?, ?> contactCol;

    @FXML
    private Label customerAppointments;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private TableColumn<?, ?> descCol;

    @FXML
    private TableColumn<?, ?> endCol;

    @FXML
    private TableColumn<?, ?> startCol;

    @FXML
    private TableColumn<?, ?> titleCol;

    @FXML
    private TableColumn<?, ?> typeCol;
    @FXML
    private TableColumn<?, ?> locationCol;

    @FXML
    private Button updateAppointmentButton;

    @FXML
    private TableColumn<?, ?> userIDCol;

    /**Closes the customer appointments window when the close button is pressed.*/
    @FXML
    void closeWindow(ActionEvent event) {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**Deletes the selected appointment when the delete button is pressed. If the delete button is pressed with no appointment
     * selected, displays an error. Refreshes the table to reflect the updated results after the appointment is deleted. */
    @FXML
    void deleteAppointment(ActionEvent event) throws SQLException {
        try {
            Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
            String deletedAppointment = "Appointment " + selectedAppointment.getAppointmentID();

            //Establishes a Yes/No confirmation button when a part is selected and the Delete button is pressed
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + deletedAppointment + "?");
            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            confirmation.getButtonTypes().setAll(yesButton, noButton);
            Optional<ButtonType> result = confirmation.showAndWait();

            //If yes is selected, deletes the part.
            if(result.isPresent() && result.get() == yesButton) {
                if (AppointmentQuery.delete(selectedAppointment.getAppointmentID()) > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, deletedAppointment + " has been deleted");
                    alert.show();
                }
            }
            //Flags an error if the Delete button is pressed without a part selected
        } catch (NullPointerException noItemSelected) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an Appointment from the table to delete");
            alert.show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        appointmentsTable.getSelectionModel().clearSelection();
        refreshTable();
    }

    /** Opens the Add Appointment window when the add button is pressed, prepopulating the Customer_ID field with the
     * current customer ID.*/
    @FXML
    void toAddAppointmentWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/dunbar/c195pa/NewAppointmentWindow.fxml"));
        loader.load();
        NewAppointmentWindowController nawController = loader.getController();
        nawController.loadFromCustomer(selectedCustomerID);

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Opens the Update Appointment window for the selected appointment when the update button is pressed.*/
    @FXML
    void toUpdateAppointmentWindow(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dunbar/c195pa/UpdateAppointmentWindow.fxml"));
            loader.load();
            UpdateAppointmentWindowController uawController = loader.getController();
            uawController.loadAppointment(appointmentsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException noItemSelected) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please select an Appointment to modify");
            alert.show();
        }
    }
    /**Loads all appointments for the customer selected in the Customer Info Window into the
     * customerAppointments table.
     * @param selectedCustomer Customer selected in the previous window*/
    public void loadCustomerAppointments(Customer selectedCustomer) throws SQLException {
        selectedCustomerID = selectedCustomer.getCustomerID();
        refreshTable();
        if(selectedCustomer.getCustomerName().endsWith("s")){
            customerAppointments.setText(selectedCustomer.getCustomerName() + "' Appointments");
        }
        else {
            customerAppointments.setText(selectedCustomer.getCustomerName() + "'s Appointments");
        }
    }
    /**Refreshes the customerAppointments table with updated values from the database.*/
    public void refreshTable() throws SQLException {
        ObservableList<Appointment> selectedCustomerAppointments = AppointmentQuery.getCustomerAppointments(selectedCustomerID);
        appointmentsTable.setItems(selectedCustomerAppointments);
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }
}
