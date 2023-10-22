package dunbar.c195pa.Controller;
import dunbar.c195pa.DAO.AppointmentQuery;
import dunbar.c195pa.DAO.UserQuery;
import dunbar.c195pa.Model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**The LoginController controls the interface used for logging into the application, including name/password validation,
 * access logging, and localization.*/
public class LoginController implements Initializable {
    @FXML
    private Button loginButton;

    @FXML
    private Label pw;

    @FXML
    private PasswordField pwText;

    @FXML
    private Label userName;

    @FXML
    private TextField userText;

    @FXML
    private Label zoneID;

    /**Points to the correct properties files for the login window*/
    private ResourceBundle rb = ResourceBundle.getBundle("dunbar/c195pa/localization/login", Locale.getDefault());

    /**Checks that the username and password fields are not empty, then invokes the UserQuery validateUser method
     * when the login button is pressed. If successful, opens the main window. Invokes the log method to track all login
     * attempts and their success.*/
    @FXML
    void login(ActionEvent event) throws IOException {
        String userName;
        String password;
        boolean noapp = true;
        try{
            userName = userText.getText();
            password = pwText.getText();
            if(userName.isEmpty() || password.isEmpty()){
                throw new Exception();
            }
        } catch(Exception emptyField){
            //Assuming that a login with an empty field does not count as an attempt.
            Alert alert = new Alert(Alert.AlertType.ERROR,  rb.getString("emptyfield"));
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try{
            if(!UserQuery.validateUser(userName,password)){
                log(false, userName);
                throw new Exception();
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception invalidUser) {
            Alert alert = new Alert(Alert.AlertType.ERROR, rb.getString("usernotfound"));
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        log(true, userName);
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load((getClass().getResource("/dunbar/c195pa/MainWindow.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();

        for(Appointment appointment: AppointmentQuery.getAppointments()){
            if(appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(15)) && appointment.getStart().isAfter(LocalDateTime.now())){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Upcoming appointment within 15 minutes: Appointment " + appointment.getAppointmentID() + " at " + appointment.getStart().toLocalTime());
                alert1.show();
                noapp = false;
                break;
            }
        }
        if(noapp){
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "No upcoming appointments within 15 minutes.");
            alert1.show();
        }

    }

    /**Changes the display language of the window and any error popups based on the default language settings of the
     * users machine.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")){
            pw.setText(rb.getString("password"));
            userName.setText(rb.getString("username"));
            loginButton.setText(rb.getString("login"));
        }
        zoneID.setText(String.valueOf(ZoneId.systemDefault()));
    }

    /**Generates and appends user info, timestamp of attempt, and attempt result to a text file (login_activity.txt)
     *  every time a login is attempted.
     *  @param success boolean indicating the success of the login attempt
     *  @param user String containing the information in the username field when the login was attempted*/
    public void log(boolean success, String user) throws IOException {
        String filename ="login_activity.txt";
        String outcome;
        FileWriter fwriter = new FileWriter(filename, true);
        LocalDateTime logged = LocalDateTime.now();
        PrintWriter outputFile = new PrintWriter(fwriter);
        if(success){
            outcome = "Success";
        }
        else{
            outcome = "Failure";
        }
        outputFile.println("Login attempt by " + user +  " on: " + Timestamp.valueOf(logged) + ", " + "Result: " + outcome);
        outputFile.close();
    }
}

