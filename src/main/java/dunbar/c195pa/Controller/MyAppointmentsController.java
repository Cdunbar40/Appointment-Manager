package dunbar.c195pa.Controller;
import dunbar.c195pa.DAO.ReportsQuery;
import dunbar.c195pa.DAO.UserQuery;
import dunbar.c195pa.Model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**The MyAppointmentsController controls the window used to display the My Appointments report. */
public class MyAppointmentsController implements Initializable {

    private Stage stage;

    @FXML
    private TableColumn<?, ?> appointmentIDCol;

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private Button closeButton;

    @FXML
    private TableColumn<?, ?> contactCol;

    @FXML
    private TableColumn<?, ?> customerIDCol;

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
    private Label userAppointments;

    /**Closes the window when the close button is pressed */
    @FXML
    void closeWindow(ActionEvent event) {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**Populates the appointmentsTable with all appointments associated with the id of the current User. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appointmentsTable.setItems(ReportsQuery.getMyAppointments(UserQuery.activeUser.getUserID()));
            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
