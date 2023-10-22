package dunbar.c195pa.Controller;
import dunbar.c195pa.DAO.ReportsQuery;
import dunbar.c195pa.Model.ReportBlock;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**The AppointmentsByTypeWindowController controls the window used to display the Appointments by type report.*/
public class AppointmentsByTypeWindowController implements Initializable {

    private Stage stage;

    @FXML
    private TableView<ReportBlock> appointmentTable;

    @FXML
    private Button closeButton;

    @FXML
    private TableColumn<?, ?> monthCol;

    @FXML
    private TableColumn<?, ?> numAppCol;

    @FXML
    private TableColumn<?, ?> typeCol;

    /**Closes the report window when the close button is pressed.*/
    @FXML
    void closeWindow(ActionEvent event) {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /** Invokes the ReportsQuery getAppointmentsByType method to query the database and assigns the information for
     * display in the ui. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appointmentTable.setItems(ReportsQuery.getAppointmentsByType());
            monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
            numAppCol.setCellValueFactory(new PropertyValueFactory<>("count"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
