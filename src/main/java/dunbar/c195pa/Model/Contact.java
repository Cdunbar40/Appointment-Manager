package dunbar.c195pa.Model;

/**Contains all data members necessary to store Contact information from the database. */
public class Contact {
    /**Unique contact Id*/
    private int contactID;
    /**Name of contact*/
    private String name;

    /**Contact constructor.
     * @param contactID ID of contact
     * @param name Name of contact*/
    public Contact (int contactID, String name){
        this.contactID = contactID;
        this.name = name;
    }

    /**Getter method for the contact ID.
     * @return Returns the contact ID.*/
    public int getContactID() {
        return contactID;
    }

    /**Getter method for the contact name.
     * @return Returns the contact name. */
    public String getName() {
        return name;
    }
}
