package dunbar.c195pa.Controller;
import dunbar.c195pa.DAO.AppointmentQuery;
import dunbar.c195pa.DAO.UserQuery;
import dunbar.c195pa.Main.JDBC;
import dunbar.c195pa.Main.Main;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import java.util.ResourceBundle;

/**The MainWindowController controls the interface used to display appointments. It includes a custom greeting message
 * based off of the user, a display of all appointments for the next week or month (toggleable), navigation buttons for
 * other windows, a report generation button with three options, and an alert field that displays when an appointment is
 * scheduled within 15 minutes of login.*/
public class MainWindowController implements Initializable {
    private Stage stage;
    private Parent scene;

    @FXML
    private Label alertLabel;

    @FXML
    private TableColumn<Appointment, String> appointmentContactCol;

    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerCol;

    @FXML
    private TableColumn<Appointment, String> appointmentDescCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentEndCol;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIDCol;

    @FXML
    private TableColumn<Appointment, String> appointmentLocationCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> appointmentStartCol;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, String> appointmentTitleCol;

    @FXML
    private TableColumn<Appointment, String> appointmentTypeCol;

    @FXML
    private TableColumn<Appointment, Integer> appointmentUserCol;

    @FXML
    private Button changeAppointmentButton;

    @FXML
    private Button customerInfoButton;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private ToggleGroup displayRange;

    @FXML
    private Button exitButton;

    @FXML
    private Button getReportButton;

    @FXML
    private Label greetingLabel;

    @FXML
    private RadioButton monthViewButton;

    @FXML
    private Button newAppointmentButton;

    @FXML
    private ComboBox<String> reportCB;

    @FXML
    private RadioButton weekViewButton;

    @FXML
    private RadioButton displayAllButton;

    /**Loads the specified scene for a new window.
     * @param event An action even for a button press in the GUI
     * @param newAddress The package address for the desired FXML file*/
    private void changeWindowButton (ActionEvent event, String newAddress) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load((getClass().getResource(newAddress)));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Deletes the selected appointment from the database. Includes a confirmation alert and a completion message.*/
    @FXML
    void deleteApp(ActionEvent event) {
        try {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, deletedAppointment + ": " + selectedAppointment.getType() + " has been deleted");
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
        //Refresh the table
        appointmentTable.getSelectionModel().clearSelection();
        if(monthViewButton.isSelected()){
            refreshTable(filterByMonth());
        }
        else {
            refreshTable(filterByWeek());
        }
        //Recheck for appointments within 15 minutes
        for(Appointment appointment:AppointmentQuery.getAppointments()){
            if(appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(15)) && appointment.getStart().isAfter(LocalDateTime.now())){
                alertLabel.setText("Upcoming Appointment Alert: Appointment " + appointment.getAppointmentID() + " at " + appointment.getStart().toLocalTime());
                break;
            }
            else {
                alertLabel.setText("Upcoming Appointment Alert: None");
            }
        }
    }

    /**Changes the appointmentsTable display to a month view when the Month radio button is selected.*/
    @FXML
    void displayByMonth(ActionEvent event) {
        refreshTable(filterByMonth());
    }

    /**Changes the appointmentsTable to display to a week view when the Week radio button is selected.*/
    @FXML
    void displayByWeek(ActionEvent event) {
        refreshTable(filterByWeek());
    }

    @FXML
    void displayAllAppointments(ActionEvent event) throws SQLException {refreshTable( AppointmentQuery.selectAll());}

    /**Opens to the selected reports window when the Generate Report button is pressed. */
    @FXML
    void getReport(ActionEvent event) throws IOException {
        if(reportCB.getValue().equals("My Appointments")){
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dunbar/c195pa/Reports/MyAppointments.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            Stage newStage = StageHandler.getSecondaryStage();
            newStage.setTitle("");
            newStage.setScene(newScene);
            newStage.show();
        }
        else if(reportCB.getValue().equals("Schedule By Contact")){
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dunbar/c195pa/Reports/ScheduleByContact.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            Stage newStage = StageHandler.getSecondaryStage();
            newStage.setTitle("");
            newStage.setScene(newScene);
            newStage.show();
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dunbar/c195pa/Reports/AppointmentsByTypeWindow.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            Stage newStage = StageHandler.getSecondaryStage();
            newStage.setTitle("");
            newStage.setScene(newScene);
            newStage.show();
        }
    }

    /**Closes the application when the Exit button is pressed*/
    @FXML
    void onActionClose(ActionEvent event) {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
        JDBC.closeConnection();
        System.exit(0);
    }

    /**Navigates to the CustomerInfo window when the Customer Information button is pressed*/
    @FXML
    void toCustomerWindow(ActionEvent event) throws IOException {
        changeWindowButton(event, "/dunbar/c195pa/CustomerInfoWindow.fxml");
    }

    /**Navigates to the new appointment window when the Create Appointment button is pressed*/
    @FXML
    void toNewAppWindow(ActionEvent event) throws IOException {
        changeWindowButton(event, "/dunbar/c195pa/NewAppointmentWindow.fxml");
    }

    /**Navigates to the Update Appointment window when the Update Appointment button is pressed and an Appointment is
     *selected.*/
    @FXML
    void toUpdateAppWindow(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dunbar/c195pa/UpdateAppointmentWindow.fxml"));
            loader.load();
            UpdateAppointmentWindowController uawController = loader.getController();
            uawController.loadAppointment(appointmentTable.getSelectionModel().getSelectedItem());

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

    /**Initializes the main window, populating the Standard Reports combobox and Appointments Table (defaulting to
     * a week view).Checks for any appointments occurring within 15 minutes and populates the alert appropriately*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> reportList = FXCollections.observableArrayList();
        reportList.add("My Appointments");
        reportList.add("Schedule By Contact");
        reportList.add("Appointments By Type");
        reportCB.setItems(reportList);
        refreshTable(filterByWeek());
        greetingLabel.setText("Hello " + UserQuery.getActiveUser().getUserName() + "!");

        for(Appointment appointment:AppointmentQuery.getAppointments()){
            if(appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(15)) && appointment.getStart().isAfter(LocalDateTime.now())){
                alertLabel.setText("Upcoming Appointment Alert: Appointment " + appointment.getAppointmentID() + " at " + appointment.getStart().toLocalTime());
                break;
            }
            else {
                alertLabel.setText("Upcoming Appointment Alert: None");
            }
        }
    }

    /**Invokes the AppointmentQuery selectAll method to get all appointments, and then filters out any that are greater
     * than 1 week away of the current day. Uses a lambda expression to filter out appointments that are greater than one
     * week away, as it is more concise than creating an equivalent enhanced for loop.
     * @return Returns the filtered list*/
    public ObservableList<Appointment> filterByWeek(){
        ObservableList<Appointment> byWeek = FXCollections.observableArrayList();
        ObservableList<Appointment> allAppointments;

        try {
            allAppointments = AppointmentQuery.selectAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(allAppointments != null){
            allAppointments.stream().filter(appointment ->
                            appointment.getStart().isAfter(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay())
                                    && appointment.getStart().isBefore(LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(23, 59, 59)))
                                    .forEach(byWeek::add);
        }
        return byWeek;
    }

    /**Invokes the AppointmentQuery selectAll method to get all appointments, and then filters out any that
     *  are greater than 1 month away of the current day. ses a lambda expression to filter out appointments that are greater than one
     *  month away, as it is more concise than creating an equivalent enhanced for loop.
     *  @return Returns the filtered list*/
    public ObservableList<Appointment> filterByMonth(){
        ObservableList<Appointment> byMonth = FXCollections.observableArrayList();
        ObservableList<Appointment> allAppointments;

        try {
            allAppointments = AppointmentQuery.selectAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(allAppointments != null){
            allAppointments.stream().filter(appointment ->
                    appointment.getStart().getMonth().equals(LocalDateTime.now().getMonth())).forEach(byMonth::add);
        }
        return byMonth;
    }
    /**Refreshes the appointments table with the most up to date entries from the database.*/
    public void refreshTable(ObservableList<Appointment> view){
        appointmentTable.setItems(view);
        appointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentUserCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }
}
