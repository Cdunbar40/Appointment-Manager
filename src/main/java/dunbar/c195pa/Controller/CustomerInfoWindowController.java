package dunbar.c195pa.Controller;
import dunbar.c195pa.DAO.CustomerQuery;
import dunbar.c195pa.Main.JDBC;
import dunbar.c195pa.Main.Main;
import dunbar.c195pa.Main.StageHandler;
import dunbar.c195pa.Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**The CustomerInfoWindowController controls the interface used to display information about all existing customers.
 * It includes buttons to add, update, and delete customers, as well as to view all appointments for a specific selected
 * customer. */
public class CustomerInfoWindowController implements Initializable {

    private Stage stage;

    private Parent scene;
    @FXML
    private Button addCustomerButton;

    @FXML
    private TableColumn<?, ?> addressCol;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<?, ?> countryCol;

    @FXML
    private TableColumn<?, ?> customerIDCol;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private TableColumn<?, ?> divCol;

    @FXML
    private Button exitButton;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> pCodeCol;

    @FXML
    private TableColumn<?, ?> phoneCol;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button viewAppointmentButton;

    /**Loads the specified scene for a new window.
     * @param event An action even for a button press in the GUI
     * @param newAddress The package address for the desired FXML file*/
    private void changeWindowButton (ActionEvent event, String newAddress) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load((getClass().getResource(newAddress)));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Closes all windows when the exit button is pressed.*/
    @FXML
    void closeAll(ActionEvent event) {
        stage = StageHandler.getSecondaryStage();
        if(stage.isShowing()){
            stage.close();
        }
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
        JDBC.closeConnection();
        System.exit(0);
    }

    /**Deletes a selected customer from the database, as well as all appointments associated with that customer, when the
     * delete button is pressed. Displays a confirmation prompt before deleting, and an information prompt after deletion.
     * Refreshed the customer table after deletion to ensurethat the information is up-to-date.*/
    @FXML
    void deleteCustomer(ActionEvent event) {
        try {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            String deletedCustomer = "Customer: " + selectedCustomer.getCustomerName();

            //Establishes a Yes/No confirmation button when a part is selected and the Delete button is pressed
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + deletedCustomer + "?");
            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            confirmation.getButtonTypes().setAll(yesButton, noButton);
            Optional<ButtonType> result = confirmation.showAndWait();

            //If yes is selected, deletes the part.
            if(result.isPresent() && result.get() == yesButton) {
                if (CustomerQuery.delete(selectedCustomer.getCustomerID()) > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, deletedCustomer + " has been deleted");
                    alert.show();
                }
            }
            //Flags an error if the Delete button is pressed without a part selected
        } catch (NullPointerException noItemSelected) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer from the table to delete");
            alert.show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        customerTable.getSelectionModel().clearSelection();
        refreshCustomers();
    }

    /**Opens a new CustomerAppointments window for the selected customer when the View Appointments button is pressed. */
    @FXML
    void openAppointments(ActionEvent event) throws IOException, SQLException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dunbar/c195pa/CustomerAppointmentsWindow.fxml"));
            Parent root = fxmlLoader.load();
            CustomerAppointmentsWindowController caController = fxmlLoader.getController();
            caController.loadCustomerAppointments(customerTable.getSelectionModel().getSelectedItem());
            Scene newScene = new Scene(root);
            Stage newStage = StageHandler.getSecondaryStage();
            newStage.setTitle("");
            newStage.setScene(newScene);
            newStage.show();
        }
        catch(NullPointerException noItemSelected) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please select a Customer");
            alert.show();
        }
    }

    /**Returns to the Main window when the Back button is pressed. */
    @FXML
    void returnToMain(ActionEvent event) throws IOException {
        changeWindowButton(event, "/dunbar/c195pa/MainWindow.fxml");
    }

    /**Opens the Add Customer window when the Add button is pressed.*/
    @FXML
    void toAddCustomerWindow(ActionEvent event) throws IOException {
        changeWindowButton(event, "/dunbar/c195pa/AddCustomerWindow.fxml");
    }

    /**Opens the Update Customer window when the Update button is pressed and a customer is selected. Invokes the
     * UpdateCustomerWindowController loadCustomer method to populate the customer information. */
    @FXML
    void toUpdateWindow(ActionEvent event) throws IOException {
        try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/dunbar/c195pa/UpdateCustomerWindow.fxml"));
        loader.load();
        UpdateCustomerWindowController ucwController = loader.getController();
        ucwController.loadCustomer(customerTable.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
        }
        catch(NullPointerException noItemSelected) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please select a Customer to update");
            alert.show();
        }
    }

    /**Sets the information to be displayed in the customerTable on window open.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshCustomers();
    }

    /**Refreshes the customerTable with the latest information from the database.*/
    public void refreshCustomers(){
        try {
            customerTable.setItems(CustomerQuery.selectAllCustomers());
            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            pCodeCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            divCol.setCellValueFactory(new PropertyValueFactory<>("division"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
