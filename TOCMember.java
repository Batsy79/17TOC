/**
 * An object that has all the details of the current member
 * 
 * @author  Pedro Alves
 * @version 23 August 2016
 */
public class TOCMember {
    private int pmKeys;
    private String name;
    private boolean admin;
    
    /**
     * The constructor. Set the member's details initially
     */
    public TOCMember(int newPmKeys, String newName, boolean newAdmin) {
        pmKeys = newPmKeys;
        name = newName;
        admin = newAdmin;
    }
    
    // ------------------------------------------- Getters and Setters -------------------------------------------
    /**
     * Update the user details when a new user has logged in. Called by TOCManager usually
     * 
     * @param   userPmKeys  The new user's pmKeys
     * @param   userName    The new user's name
     * @param   userAdmin   The new user's admin flag
     */
    public void updateMember(int userPmKeys, String userName, boolean userAdmin) {
        pmKeys = userPmKeys;
        name = userName;
        admin = userAdmin;
    }
    
    /**
     * Gets the member's pmKeys
     * 
     * @return          a member's pmkeys
     */
    public int getPmKeys() {
        return pmKeys;
    }
    
    /**
     * Gets the member's name
     * 
     * @return          a member's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the member's admin flag
     * 
     * @return          the admin flag (true or false)
     */
    public boolean isAdmin() {
        return admin;
    }
}