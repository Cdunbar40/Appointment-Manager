package dunbar.c195pa.Model;

/**Stores information for the current user of the application*/
public class User {
    /**Unique ID for the current user*/
    private int userID;
    /**Username of the current user*/
    private String userName;

    /**User constructor
     * @param userID The current user ID
     * @param userName the current username*/
    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    /**Getter method for the user ID
     * @return The user ID*/
    public int getUserID() {
        return userID;
    }

    /**Getter method for the user name.
     * @return The user name*/
    public String getUserName() {
        return userName;
    }
}
