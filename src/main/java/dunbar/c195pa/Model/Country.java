package dunbar.c195pa.Model;

/**Contains all data members necessary to store Country information from the database.*/
public class Country {
    /**Unique country ID. */
    private int countryID;
    /**Country name*/
    private String country;

    /**Getter method for the country ID.
     * @return Returns the country ID. */
    public int getCountryID() {
        return countryID;
    }

    /**Getter method for the country name.
     * @return Returns the country name. */
    public String getCountry() {
        return country;
    }

    /**Country constructor.
     * @param country Name of the country
     * @param countryID Country ID*/
    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }
}
