package dunbar.c195pa.Main;
import javafx.stage.Stage;

/**Manages stage generation and tracking for the program. Allows the use of multiple windows.*/
public class StageHandler {
    /**Generates the primary window for the program.*/
    private static Stage primaryStage = new Stage();
    /**Generates the secondary window for the program*/
    private static Stage secondaryStage = new Stage();
    /**Getter method for the primary stage.
     * @return Returns the primaryStage*/
    public static Stage getPrimaryStage(){
        return primaryStage;
    }
    /**Getter method for the secondaryStage.
     * @return Returns the secondaryStage.*/
    public static Stage getSecondaryStage(){
        return secondaryStage;
    }
}
