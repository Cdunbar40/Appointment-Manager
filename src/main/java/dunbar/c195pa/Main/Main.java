package dunbar.c195pa.Main;

import dunbar.c195pa.DAO.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**Begins the application, loading the login window and initializing the DB connection*/
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dunbar/c195pa/loginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = StageHandler.getPrimaryStage();
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**Establishes the database connection, retrieves information from the database that will not change while the application
     * is running (contacts, countries, and first level divisions), and launches the application.*/
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
        ContactQuery.selectAllContacts();
        DivisionQuery.selectAllDivisions();
        CountryQuery.selectAllCountries();
        launch();
    }

}
