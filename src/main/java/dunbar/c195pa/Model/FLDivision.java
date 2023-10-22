package dunbar.c195pa.Model;

/**Contains all data members necessary to store First Level Division information from the database.*/
public class FLDivision {
    /**The unique first level division ID. */
    private int divID;
    /**The name of the division*/
    private String division;
    /**The country ID associated with the division*/
    private int countryID;

    /** FLDivision constructor
     * @param divID     The ID of the division.
     * @param division  The name of the division.
     * @param countryID The ID of the country associated with the division.*/
    public FLDivision(int divID, String division, int countryID) {
        this.divID = divID;
        this.division = division;
        this.countryID = countryID;
    }

    /**Getter method for the division ID.
     *@return The division ID.*/
    public int getDivID() {
        return divID;
    }

    /**Getter method for the division name.
     * @return The division name.*/
    public String getDivision() {
        return division;
    }

    /**Getter method for the country ID.
     * @return The country ID.*/
    public int getCountryID() {
        return countryID;
    }
}
