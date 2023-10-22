package dunbar.c195pa.DAO;

import dunbar.c195pa.Main.JDBC;
import dunbar.c195pa.Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**The CountryQuery class is an abstract class containing all methods necessary to access the Country table in the
 * database. */
public abstract class CountryQuery {
    /**A list of all country options in the database*/
    private static ObservableList<Country> countries = FXCollections.observableArrayList();
    /**A list of all country names in the database*/
    private static ObservableList<String> countryNames = FXCollections.observableArrayList();

    /**Retrieves each country from the database, generates a Country object for each, and stores them in the countries
     * list. Also extracts the country names and saves them in the countryNames list.*/
    public static void selectAllCountries() throws SQLException {
        String sql = "SELECT Country_ID, Country FROM Countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int countryID = rs.getInt("Country_ID");
            String country = rs.getString("Country");
            Country newCountry = new Country(countryID, country);
            countries.add(newCountry);
            countryNames.add(country);
        }
    }
    /**Getter method for the countries list.
     * @return Returns the countries ObservableList*/
    public static ObservableList<Country> getCountries(){return countries;}
    /**Getter method for the countryNames list.
     * @return Returns the countryNames ObservableList*/
    public static ObservableList<String> getCountryNames(){return countryNames;}

}

