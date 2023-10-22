package dunbar.c195pa.DAO;
import dunbar.c195pa.Main.JDBC;
import dunbar.c195pa.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**The CustomerQuery class is an abstract class containing all methods necessary to access the Customer table in the
 * database.*/
public abstract class CustomerQuery {
    /**List containing all customers in the database*/
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();

    /**Retrieves all customers from the database, generates a Customer object for each one, and stores them in the
     * customers list.
     * @return Returns the customers ObservableList*/
    public static ObservableList<Customer> selectAllCustomers() throws SQLException {
        customers.clear();
        String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM Customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postal = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            int divID = rs.getInt("Division_ID");
            Customer customer = new Customer(customerID, customerName, address, postal, phone, divID);
            customers.add(customer);
        }
        return customers;
    }

    /**Deletes a specified customer from the database.
     * @param customerID The ID of the customer to be deleted
     * @return Returns an integer representation of the number of rows affected by the delete operation*/
    public static int delete (int customerID) throws SQLException {
        String sql = "DELETE FROM Appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.executeUpdate();
        sql = "DELETE FROM Customers WHERE Customer_ID = ?";
        ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
    /**Searches the Customer table in the database for a given customerID to determine if the ID exists.
     * @param customerID customer ID to search for
     * @return If the ID is found returns true, otherwise returns false*/
    public static boolean findCustomer(int customerID) throws SQLException {
        String sql = "SELECT Customer_ID FROM Customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()){
            return false;
        }
        return true;
    }
    /**Adds a new customer to the database.
     * @param name The customer name
     * @param address The customer street address. Does not include country, state/province, or postal code
     * @param division The first level division ID of the customer
     * @param phone The customer phone number
     * @param postalCode The customer postal code*/
    public static void addNewCustomer(String name, String address, String postalCode, String phone, int division) throws SQLException {
        String sql = "INSERT INTO Customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, division);
        ps.executeUpdate();
    }
    /**Updates an existing customers information in the database.
     * @param name The customer name
     * @param address The customer street address. Does not include country, state/province, or postal code
     * @param division The first level division ID of the customer
     * @param phone The customer phone number
     * @param postalCode The customer postal code
     * @param id ID number of the customer to be updated*/
    public static void updateCustomer(int id, String name, String address, String postalCode, String phone, int division) throws SQLException {
        String sql = "UPDATE Customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, division);
        ps.setInt(6, id);
        ps.executeUpdate();
    }
}
