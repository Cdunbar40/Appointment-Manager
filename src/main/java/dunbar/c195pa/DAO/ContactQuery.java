package dunbar.c195pa.DAO;
import dunbar.c195pa.Main.JDBC;
import dunbar.c195pa.Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**The ContactQuery class is an abstract class containing all methods necessary to access the Contacts table in the
 * database. */
public abstract class ContactQuery {
    /**A list of all contacts.*/
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    /**A list of all contact names.*/
    private static ObservableList<String> contactNames = FXCollections.observableArrayList();

    /**Retrieves all contacts from the database, generates a Contact object for each, and stores them in the allContacts
     * list.*/
    public static void selectAllContacts() throws SQLException {
        String sql = "Select Contact_ID, Contact_Name FROM Contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int contactID = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            Contact contact = new Contact(contactID, name);
            allContacts.add(contact);
        }
    }
    /**Getter method for allContacts
     * @return Returns the all contacts list.*/
    public static ObservableList<Contact> getAllContacts(){
        return allContacts;
    }
    /**Extracts the name field of each contact and stores it in the contactNames list.
     * @return Returns the contactNames list*/
    public static ObservableList<String> getAllContactNames(){
        contactNames.clear();
        for (Contact contact:allContacts){
            contactNames.add(contact.getName());
        }
        return contactNames;
    }
    /**Retrieves the contact ID of a specific contact based off of contact name.
     * @param contactName Name of the contact
     * @return Returns the Contact ID if present, otherwise returns 0.*/
    public static int getContactID(String contactName){
        for(Contact contact:allContacts){
            if(contact.getName() == contactName){
                return contact.getContactID();
            }
        }
        return 0;
    }
}
