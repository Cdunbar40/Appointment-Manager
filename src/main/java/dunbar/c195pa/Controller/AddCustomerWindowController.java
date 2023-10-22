package dunbar.c195pa.Controller;
import dunbar.c195pa.DAO.CountryQuery;
import dunbar.c195pa.DAO.CustomerQuery;
import dunbar.c195pa.DAO.DivisionQuery;
import dunbar.c195pa.Model.Country;
import dunbar.c195pa.Model.FLDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** The AddCustomerWindowController controls the interface used add new customers to the database. */
public class AddCustomerWindowController implements Initializable {
    private Stage stage;

    private Parent scene;
    /** A list containing all countries that may be assigned to a customer */
    private ObservableList<Country> allCountries = CountryQuery.getCountries();
    /** A list containing all divisions that may be assigned to a customer */
    private ObservableList<FLDivision> allDivisions = DivisionQuery.getDivisions();
    /** A list containing all divisions that may be assigned to a customer, but as Strings instead of FLDivision objects */
    private ObservableList<String> divNames = FXCollections.observableArrayList();

    @FXML
    private Button addCustomerButton;

    @FXML
    private TextField addressField;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> countryCB;

    @FXML
    private TextField customerIDField;

    @FXML
    private ComboBox<String> divCB;

    @FXML
    private TextField nameField;

    @FXML
    private TextField pCodeField;

    @FXML
    private TextField phoneField;

    /** Filters the selectable first level divisions for the divCB combo box based off of the country selected in the countryCB combo box.
     * Includes a lambda expression that populates the divNames Observable list with the FLDivision division String based off of the
     * selected countries ID. This consolidates what otherwise would have to be a multi-line for each block or enhanced for loop into a single line.
     */
    public void filterDivs(){
        divNames.clear();
        String selectedCountry = countryCB.getValue();
        int selectedCountryID = 0;
        for (Country country : allCountries) {
            if (selectedCountry.equals(country.getCountry())) {
                selectedCountryID = country.getCountryID();
                break;
            }
        }
        if (selectedCountryID != 0) {
            int finalSelectedCountryID = selectedCountryID;
            allDivisions.stream().filter(divName -> (divName.getCountryID() == finalSelectedCountryID)).map(FLDivision::getDivision).forEach(divNames::add);
        }
        divCB.setItems(divNames);
    }

    /**Loads the specified scene for a new window.
     * @param event An action even for a button press in the GUI
     * @param newAddress The package address for the desired FXML file*/
    private void changeWindowButton (ActionEvent event, String newAddress) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load((getClass().getResource(newAddress)));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Returns to the Customer window when the cancel button is pressed.*/
    @FXML
    void returnToCustomerWindow(ActionEvent event) throws IOException {
        changeWindowButton(event, "/dunbar/c195pa/CustomerInfoWindow.fxml");
    }

    /**Checks the form fields for valid inputs, then commits an insert for a new customer into the database using the
     * provided information when the save button is pressed. Once complete, returns to the Customer Window.*/
    @FXML
    void saveNewCustomer(ActionEvent event) throws IOException, SQLException {
        String name;
        String phone;
        String address;
        String postal;
        String division;

        try {
            name = nameField.getText();
            if(name.isEmpty()){
                throw new Exception();
            }
        }catch (Exception noName){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a name");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try{
            phone = phoneField.getText();
            if(phone.isEmpty()){
                throw new Exception();
            }
        }catch (Exception noNumber){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a phone number");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try{
            division = divCB.getValue();
            if(division.isEmpty()){
                throw new Exception();
            }
        }catch (Exception noDiv){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a State/Province");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try{
            address = addressField.getText();
            if(address.isEmpty()){
                throw new Exception();
            }
        }catch (Exception noAddress){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter an address");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        try{
            postal = pCodeField.getText();
            if(postal.isEmpty()){
                throw new Exception();
            }
        }catch (Exception noPost){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a postal code");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
            return;
        }
        int divID = DivisionQuery.findDivision(division);
        CustomerQuery.addNewCustomer(name, address, postal, phone, divID);
        changeWindowButton(event, "/dunbar/c195pa/CustomerInfoWindow.fxml");
    }
    /** On open, loads the country combo box with the available country options. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCB.setItems(CountryQuery.getCountryNames());
    }
}
