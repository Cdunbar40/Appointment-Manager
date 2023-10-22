package dunbar.c195pa.Model;

import dunbar.c195pa.DAO.CountryQuery;
import dunbar.c195pa.DAO.DivisionQuery;

/**Contains all data members necessary to retrieve and store appointments in the database*/
public class Customer {
    /**Unique customer ID*/
    private int customerID;
    /**Customer name*/
    private String customerName;
    /**Customer address*/
    private String address;
    /**Customer postal code*/
    private String postal;
    /**Customer phone number*/
    private String phone;
    /**First level division ID for the customer (ie, ID for the state/province as applicable)*/
    private int divID;
    /**Name of the first level division for the customer. */
    private String division;
    /**Name of the Country the customer lives in. */
    private String country;

    /**Getter method for the customerID.
     * @return Returns the customer ID.*/
    public int getCustomerID() {
        return customerID;
    }

    /**Setter method for the customer ID.*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**Getter method for the customer name.
     * @return The customer name. */
    public String getCustomerName() {
        return customerName;
    }

    /**Getter method for the customer's address.
     * @return The customer's address.*/
    public String getAddress() {
        return address;
    }

    /**Getter method for the customer's postal code.
    * @return The customer's postal code.*/
    public String getPostal() {
        return postal;
    }

    /**Getter method for the customer's phone number.
     * @return The customer's phone number.*/
    public String getPhone() {
        return phone;
    }

    /**Getter method for the customer's division.
     * @return The customer's division.*/
    public String getDivision(){
        return division;
    }

    /**Getter method for the customer's country.
     * @return The customer's country.*/
    public String getCountry() {
        return country;
    }

    /**Customer constructor. Also determines the division and country name based off of the division ID.
     * @param customerID   The ID of the customer.
     * @param customerName The name of the customer.
     * @param address      The address of the customer.
     * @param postal       The postal code of the customer.
     * @param phone        The phone number of the customer.
     * @param divID        The ID of the first level division.*/
    public Customer(int customerID, String customerName, String address, String postal, String phone, int divID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divID = divID;

        for(FLDivision div:DivisionQuery.getDivisions()){
            if(div.getDivID() == divID){
                division = div.getDivision();
                for(Country testCountry: CountryQuery.getCountries()){
                    if(div.getCountryID() == testCountry.getCountryID()){
                        country = testCountry.getCountry();
                    }
                }
            }
        }
    }
}
