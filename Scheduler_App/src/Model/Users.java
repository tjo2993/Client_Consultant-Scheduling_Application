package Model;

/**
 * The Users class represents user information in the application.
 * Each user has a unique user ID, a username, and a password.
 */
public class Users {
    private int User_ID;
    private String userName;
    private String password;

    /**
     * Constructs a new Users object with the specified user details.
     *
     * @param User_ID   The unique ID of the user.
     * @param userName The username of the user.
     * @param password The password of the user.
     */
    public Users(int User_ID, String userName, String password) {
        this.User_ID = User_ID;
        this.userName = userName;
        this.password = password;
    }
    /**
     * Returns a string representation of the user's username.
     *
     * @return The username of the user as a string.
     */
    @Override
    public String toString(){

        return (userName);
    }
    /**
     * Returns the user's unique ID.
     *
     * @return The user's unique ID.
     */
    public int getUser_ID() {

        return User_ID;
    }
    /**
     * Sets the user's unique ID.
     *
     * @param user_ID The new unique ID for the user.
     */
    public void setUser_ID(int user_ID) {

        this.User_ID = user_ID;
    }
    /**
     * Returns the username of the user.
     *
     * @return The username of the user.
     */
    public String getUserName() {

        return userName;
    }
    /**
     * Sets the username of the user.
     *
     * @param userName The new username for the user.
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }
    /**
     * Returns the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {

        return password;
    }
    /**
     * Sets the password of the user.
     *
     * @param password The new password for the user.
     */
    public void setPassword(String password) {

        this.password = password;
    }
}
