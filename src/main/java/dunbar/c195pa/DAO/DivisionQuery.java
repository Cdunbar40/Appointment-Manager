package dunbar.c195pa.DAO;
import dunbar.c195pa.Main.JDBC;
import dunbar.c195pa.Model.FLDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**The DivisionQuery class is an abstract class containing all operations needed to access the first_level_division table
 * in the database.*/
public abstract class DivisionQuery {
    /**List of all first level divisions in the database*/
    private static ObservableList<FLDivision> divisions = FXCollections.observableArrayList();
    /**List of all first level division names in the database*/
    private static ObservableList<String>divisionNames = FXCollections.observableArrayList();

    /**Retrieves all first level divisions from the database, generates a FLDivision object for each, and stores
     * them in the divisions list. Also extracts the name of each division and stores it in the divisionNames list.*/
    public static void selectAllDivisions() throws SQLException {
        String sql = "SELECT Division_ID, Division, Country_ID FROM First_Level_Divisions";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int divID = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryID = rs.getInt("Country_ID");
            FLDivision div = new FLDivision(divID, division, countryID);
            divisions.add(div);
            divisionNames.add(division);
        }
    }
    /**Getter method for the divisions list.
     * @return Returns the divisions ObservableList*/
    public static ObservableList<FLDivision> getDivisions(){return divisions;}

    /**Retrieves the division ID for a given division name.
     * @param name name of the division
     * @return Returns the division ID if it exists, otherwise returns -1;*/
    public static int findDivision(String name){
        for(FLDivision div : divisions){
            if(div.getDivision().equals(name)){
                return div.getDivID();
            }
        }
        return -1;
    }
}