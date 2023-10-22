package dunbar.c195pa.DAO;
import dunbar.c195pa.Main.JDBC;
import dunbar.c195pa.Model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**The UserQuery class is an abstract class that contains all methods needed to access the Users table in the database.
 * It also tracks which user is currently logged in. */
public abstract class UserQuery {
    /**Holds the information for the user that is currently logged in*/
    public static User activeUser;
    /**Searches the Users table in the database for a given userID.
     * @param userID the userID of interest
     * @return Returns true if the userID is found, false if not.*/
    public static boolean findUser(int userID) throws SQLException {
        String sql = "SELECT User_ID FROM Users WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()){
            return false;
        }
        return true;
    }
    /**Setter method for the current activeUser.
     * @param id current user ID
     * @param name current username*/
    public static void setActiveUser(int id, String name){
        activeUser = new User(id,name);
    }
    /**Getter method for the current activeUser.
     * @return Returns the activeUser User object*/
    public static User getActiveUser(){
        return activeUser;
    }

    /**Searches the users table in the database for a specific username/password combination.
     * @param name username to search
     * @param pw password to search
     * @return Returns true if the combination is valid, otherwise returns false*/
    public static boolean validateUser(String name, String pw) throws SQLException {
        String sql = "SELECT User_ID, User_Name FROM Users WHERE User_name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, pw);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()){
            return false;
        }
        else{
            int id = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            setActiveUser(id, userName);
        }
        return true;
    }
}
