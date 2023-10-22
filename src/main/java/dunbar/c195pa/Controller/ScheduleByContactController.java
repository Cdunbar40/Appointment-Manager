package dunbar.c195pa.Controller;
import dunbar.c195pa.DAO.ContactQuery;
import dunbar.c195pa.DAO.ReportsQuery;
import dunbar.c195pa.Model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**The ScheduleByContactController controls the interface used to display the Schedule By Contact report*/
public class ScheduleByContactController implements Initializable {
    private Stage stage;
    @FXML
    private TableColumn<?, ?> appointmentIDCol;

    @FXML
    private Button closeButton;

    @FXML
    private ComboBox<String> contactCB;

    @FXML
    private TableColumn<?, ?> customerIDCol;

    @FXML
    private TableColumn<?, ?> descCol;

    @FXML
    private TableColumn<?, ?> endCol;

    @FXML
    private TableView<Appointment> scheduleTable;

    @FXML
    private TableColumn<?, ?> startCol;

    @FXML
    private TableColumn<?, ?> titleCol;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private Label windowLabel;

    /**Generates and displays a schedule of appointments for the contact selected in the Contact combobox when a contact
     * is selected. */
    @FXML
    void generateSchedule(ActionEvent event) throws SQLException {
        String contact = contactCB.getValue();
        windowLabel.setText("Schedule By Contact: " + contact);
        int id = ContactQuery.getContactID(contact);
        scheduleTable.setItems(ReportsQuery.getScheduleByContact(id));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    /**Closes the window when the close button is selected*/
    @FXML
    void closeWindow(ActionEvent event) {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**Populates the Contacts combobox when the window is opened. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCB.setItems(ContactQuery.getAllContactNames());
    }
}
