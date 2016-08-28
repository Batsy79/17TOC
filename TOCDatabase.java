import java.sql.*;
import java.time.*;

/**
 * This object will provide the communication with the SQL database
 * 
 * @author  Pedro Alves
 * @version 23 August 2016
 */
public class TOCDatabase {
    // The connection to the SQL database
    Connection conn = null;
    
    // -------------------------------------- Functions that communicate directly with DB --------------------------------------
    /**
     * Opens a connection to the database which is represented by the conn variable in this class' parameters. 
     * Remember to call the close function of the connection when you want to save the changes
     */
    private void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:tocDatabase.db");
        }catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.err.println("Error at TOCDatabase connect()");
            System.err.println("Something went wrong, please contact one of the TOC's admins");
            System.exit(0);
        }
    }
    
    // -------------------------------------------------- Called by the TOCManager --------------------------------------------------
    /**
     * Get a member's name from the databse that matches the given pmkeys
     * 
     * @param   pmkeys  the member's pmkeys
     * @return          the member's name (Default: "Member Not Found"
     */
    public String getMemberName(int pmkeys) {
        // Initialise the value of name to its default value
        String name = "Member Not Found";
        
        // Initialise connection to the database
        connect();
        // Check if we can find the member's name by searching their pmkeys
        ResultSet results = null;
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM MEMBERS WHERE ID = ?");
            stmt.setInt(1,pmkeys);
            results = stmt.executeQuery();
            while(results.next()) {
                name = results.getString("NAME");
            }
            stmt.close();
            conn.close(); 
        }catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.err.println("Error at TOCDatabase getMemberName(int pmkeys)");
            System.err.println("Something went wrong, please contact one of the TOC's admins");
            System.exit(0);
        }
        
        return name;
    }
    
    /**
     * Check if a member is an admin in the database
     * 
     * @param   pmkeys  the member's pmkeys
     * @return          a boolean representing if the member is an admin
     */
    public boolean memberIsAdmin(int pmkeys) {
        // Initialise the value of admin to its default value
        int admin = 0;
        
        // Initialise connection to the database
        connect();
        // Check if we can find the member's admin flag by searching their pmkeys
        ResultSet results = null;
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM MEMBERS WHERE ID = ?");
            stmt.setInt(1,pmkeys);
            results = stmt.executeQuery();
            while(results.next()) {
                admin = results.getInt("ADMIN");
            }
            stmt.close();
            conn.close(); 
        }catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.err.println("Error at TOCDatabase memberIsAdmin(int pmkeys)");
            System.err.println("Something went wrong, please contact one of the TOC's admins");
            System.exit(0);
        }
        
        if(admin == 0) {
            return false;
        }else {
            return true;
        }
    }
    
    /**
     * Check if a member exists in the database
     * 
     * @param   pmkeys  the member's pmkeys
     * @return          a boolean representing if the member was found
     */
    public boolean memberExists(int pmkeys) {
        // Initialise the value of the pmkeys (id) to its default value
        int id = 0;
        
        // Initialise connection to the database
        connect();
        // Check if we can find the member's pmkeys by searching their pmkeys
        ResultSet results = null;
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM MEMBERS WHERE ID = ?");
            stmt.setInt(1,pmkeys);
            results = stmt.executeQuery();
            while(results.next()) {
                id = results.getInt("ID");
            }
            stmt.close();
            conn.close(); 
        }catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.err.println("Error at TOCDatabase memberExists(int pmkeys)");
            System.err.println("Something went wrong, please contact one of the TOC's admins");
            System.exit(0);
        }

        if(id == 0) {
            return false;
        }else {
            return true;
        }
    }
    
    /**
     * Adds new member to the MEMBERS table. The function that calls this should check if the member that requests 
     * to modify the database has permission
     * 
     * @param   pmKeys  the new member's pmKeys
     * @param   name    the new member's name
     * @param   admin   if the new member is an admin
     */
    public void addMember(int pmKeys, String name, boolean isAdmin) {
        // Change the isAdmin to a format that fits the MEMBERS table
        int admin = isAdmin ? 1 : 0;
        
        if(!memberExists(pmKeys)) {
            // Establish a connection to the database
            connect();
            
            try{
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO MEMBERS VALUES(?,?,?)");
                stmt.setInt(1,pmKeys);
                stmt.setString(2,name);
                stmt.setInt(3,admin);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
            }catch(Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.err.println("Error at TOCDatabase addMember(int pmKeys, String name, boolean isAdmin)");
                System.err.println("Something went wrong, please contact one of the TOC's admins");
                System.exit(0);
            }
        }
   }
   
   /**
    * Removes member from the MEMBERS table. As an additional note, this will not remove the member's details from the
    * TRANSACTIONS table so their past bills will still be available. NOTE TO GORDO: Would you like it to be like this 
    * or would you like to remove the bills as well?
    * 
    * @param    pmKeys      the member's pmKeys
    */
   public void removeMember(int pmKeys) {
       // First check if the member exists and then remove them from the database
       if(memberExists(pmKeys)) {
           // Establish a connection to the database
           connect();
           
           try {
               PreparedStatement stmt = conn.prepareStatement("DELETE FROM MEMBERS WHERE ID = ?");
               stmt.setInt(1,pmKeys);
               stmt.executeUpdate();
               stmt.close();
               conn.close();
           }catch(Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.err.println("Error at TOCDatabase removeMember(int pmKeys)");
                System.err.println("Something went wrong, please contact one of the TOC's admins");
                System.exit(0);
           }
       }
   }
   
   /**
    * Check if an item exists in the database
    * 
    * @param    barcode     the barcode of the item to search for
    * @return               a boolean representing if the item exists
    */
   public boolean itemExists(String barcode) {
       // Initialise the value of the barcode to its default value
       String code = "NO_BARCODE_FOUND";
       
       // Initialise connection to the database
       connect();
       // Check if we can find the member's pmkeys by searching their pmkeys
       ResultSet results = null;
       try {
           PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ITEMS WHERE BARCODE = ?");
           stmt.setString(1,barcode);
           results = stmt.executeQuery();
           while(results.next()) {
               code = results.getString("BARCODE");
            }
            stmt.close();
            conn.close();
        }catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.err.println("Error at TOCDatabase itemExists(int barcode)");
            System.err.println("Something went wrong, please contact one of the TOC's admins");
            System.exit(0);
        }

        if(code.equals("NO_BARCODE_FOUND")) {
            return false;
        }else {
            return true;
        }
   }
   
   /**
    * Gets the name of the item that matches the barcode
    * 
    * @param    barcode     the barcode of the item
    * @return               the name of the item
    */
   public String getItemName(String barcode) {
       // Initialise the value of name to its default value
        String name = "Item Not Found";
        
        // Initialise connection to the database
        connect();
        // Check if we can find the member's name by searching their pmkeys
        ResultSet results = null;
        try{
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ITEMS WHERE BARCODE = ?");
            stmt.setString(1,barcode);
            results = stmt.executeQuery();
            while(results.next()) {
                name = results.getString("NAME");
            }
            stmt.close();
            conn.close(); 
        }catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.err.println("Error at TOCDatabase getItemName(int barcode)");
            System.err.println("Something went wrong, please contact one of the TOC's admins");
            System.exit(0);
        }
        
        return name;
   }
   
   /**
    * Gets the cost of the item that matches the barcode
    * 
    * @param    barcode     the barcode of the item
    * @return               the cost of the item
    */
   public double getItemCost(String barcode) {
       // Initialise the value of name to its default value
       double cost = 0.0;
        
       // Initialise connection to the database
       connect();
       // Check if we can find the member's name by searching their pmkeys
       ResultSet results = null;
       try{
           PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ITEMS WHERE BARCODE = ?");
           stmt.setString(1,barcode);
           results = stmt.executeQuery();
           while(results.next()) {                
               cost = results.getDouble("COST");
           }
           stmt.close();
           conn.close(); 
       }catch(Exception e) {
           System.err.println(e.getClass().getName() + ": " + e.getMessage());
           System.err.println("Error at TOCDatabase getItemCost(int barcode)");
           System.err.println("Something went wrong, please contact one of the TOC's admins");
           System.exit(0);
       }
        
       return cost;
   }
   
   /**
    * Gets the stok of the item that matches the barcode
    * 
    * @param    barcode     the barcode of the item
    * @return               the cost of the item
    */
   public int getItemStock(String barcode) {
       // Initialise the value of name to its default value
       int stock = 0;
        
       // Initialise connection to the database
       connect();
       // Check if we can find the member's name by searching their pmkeys
       ResultSet results = null;
       try{
           PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ITEMS WHERE BARCODE = ?");
           stmt.setString(1,barcode);
           results = stmt.executeQuery();
           while(results.next()) {                
               stock = results.getInt("STOCK");
           }
           stmt.close();
           conn.close(); 
       }catch(Exception e) {
           System.err.println(e.getClass().getName() + ": " + e.getMessage());
           System.err.println("Error at TOCDatabase getItemStock(int barcode)");
           System.err.println("Something went wrong, please contact one of the TOC's admins");
           System.exit(0);
       }
        
       return stock;
   }
   
   /**
     * Adds new item to the ITEMS table. The function that calls this should check if the member that requests 
     * to modify the database has permission
     * 
     * @param   barcode     the item's barcode
     * @param   name        the item's name
     * @param   cost        the item's cost
     * @param   stock       the item's stock
     */
    public void addItem(String barcode, String name, double cost, int stock) {       
        if(!itemExists(barcode)) {
            // Establish a connection to the database
            connect();
            
            try{
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO ITEMS VALUES(NULL,?,?,?,?)");
                stmt.setString(1,barcode);
                stmt.setString(2,name);
                stmt.setDouble(3,cost);
                stmt.setInt(4,stock);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
            }catch(Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.err.println("Error at TOCDatabase addItem(int barcode, String name, double cost, int stock)");
                System.err.println("Something went wrong, please contact one of the TOC's admins");
                System.exit(0);
            }
        }
   }
   
   /**
    * Adds a new transaction to the TRANSACTIONS database
    * 
    * @param    pmkeys      the pmkeys of the member making the transaction
    * @param    barcode     the barcode of the item the member
    * 
    * @return               a boolean indicating if the transaction was successful 
    */
   public boolean addTransaction(int pmkeys, String barcode) {
       // A checker if the transaction was successful (ie. there is stock available)
       boolean transactionAdded = false;
       
       // Establish a connection to the database
       connect();
       
       try{
           // Check if there is recorded stock of the item
           if(getItemStock(barcode) > 0) {
               PreparedStatement stmt = conn.prepareStatement("INSERT INTO TRANSACTIONS VALUES(NULL,?,?,?,?,?)");
               stmt.setInt(1,pmkeys);
               stmt.setString(2,barcode);
               stmt.setString(3,getItemName(barcode));
               stmt.setDouble(4,getItemCost(barcode));
               
               // Add the date and time. Maybe think of a better way to solve this
               String dateTime = "" + LocalDateTime.now();
               stmt.setString(5,dateTime);
               
               stmt.executeUpdate();
               stmt.close();
               
               transactionAdded = true;
           }
           conn.close();
        }catch(Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.err.println("Error at TOCDatabase addTransaction(int pmkeys, String barcode)");
                System.err.println("Something went wrong, please contact one of the TOC's admins");
                System.exit(0);
       }
       
       return transactionAdded;
   }
   
   /**
    * Finds every database entry in the TRANSACTIONS table that matches the member's given pmkeys and exports it to
    * a String.
    * 
    * @param    pmkeys      the pmkeys of the member
    * @return               a String representing the entries
    */
   public String findTransactions(int pmkeys) {
       // Establish a connection to the database
       connect();
       
       ResultSet results;
       String transactions = "";
       try{
           PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TRANSACTIONS WHERE PMKEYS = ?");
           stmt.setInt(1,pmkeys);
           results = stmt.executeQuery();
           while(results.next()) {                
               String itemCode = results.getString("BARCODE");
               String itemName = results.getString("NAME");
               double itemCost = results.getDouble("COST");
               String purchaseDate = results.getString("DATE");
               
               transactions = transactions + purchaseDate + " - " + itemName + " - $" + itemCost + "\n";
           }
           stmt.close();
           conn.close();
       }catch(Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.err.println("Error at TOCDatabase findTransactions(int pmkeys)");
                System.err.println("Something went wrong, please contact one of the TOC's admins");
                System.exit(0);
       }
       
       return transactions;
    }
    
    /**
     * Creates the tables that the TOC requires. They will all be initially empty. There is
     * a restriction in the name of members and items to 25 characters.
     * 
     * Initialises an admin account which will usually be the TOC representative of a division.
     * Note to Devs: Remember to modify this function to allow us to change the initial admin
     * details when a user first install it
     */
    public void firstSetup() {
        try{
            connect();
            
            Statement stmt = conn.createStatement();
            // This string will store the sql commands we want to pass
            String sql;
            
            // Create the members table
            sql = "CREATE TABLE MEMBERS("                               +
                  "ID    INT          NOT NULL,"                        +
                  "NAME  VARCHAR(25)  NOT NULL,"                        +
                  "ADMIN INT          NOT NULL,"                        +
                  "PRIMARY KEY (ID))";
            stmt.executeUpdate(sql);
            System.out.println("Members table created");
            
            // Create the items table
            sql = "CREATE TABLE ITEMS("                                 +
                  "ID       INTEGER     PRIMARY KEY    AUTOINCREMENT,"  +
                  "BARCODE  VARCHAR(30) NOT NULL,"                      +
                  "NAME     VARCHAR(25) NOT NULL,"                      +
                  "COST     REAL        NOT NULL,"                      +
                  "STOCK    INT         NOT NULL)";                      
            stmt.executeUpdate(sql);
            System.out.println("Items table created");
            
            // Create the transactions table
            sql = "CREATE TABLE TRANSACTIONS("                          +
                  "NUM      INTEGER     PRIMARY KEY     AUTOINCREMENT," +
                  "PMKEYS   INT         NOT NULL,"                      +
                  "BARCODE  VARCHAR(30) NOT NULL,"                      +
                  "NAME     VARCHAR(30) NOT NULL,"                      +
                  "COST     REAL        NOT NULL,"                      +
                  "DATE     TEXT        NOT NULL)";
            stmt.executeUpdate(sql);
            System.out.println("Transactions table created");
            
            // Add the first admin
            sql = "INSERT INTO MEMBERS  (ID,NAME,ADMIN) " + 
                  "VALUES (8618374,'Pedro Alves', 1)";
            stmt.executeUpdate(sql);
            
            // Get the admin details to test
            ResultSet results = stmt.executeQuery("SELECT * FROM MEMBERS;");
            while(results.next()) {
                int id = results.getInt("ID");
                String name = results.getString("NAME");
                System.out.println("Admin created");
                System.out.println("PMKEYS: " + id);
                System.out.println("NAME:   " + name);
            }
            
            stmt.close();
            conn.close();
            
            System.out.println("------------------------------");
            System.out.println("");
        }catch(Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.err.println("Something went wrong, please contact one of the TOC's admins");
            System.err.println("Error at TOCDatabase firstSetup()");
            System.exit(0);
        }
    }
}