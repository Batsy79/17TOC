/**
 * The "Driver" of the program. Provides the bridge between the Interface, Core and Database components of the TOC
 * 
 * @author  Pedro Alves
 * @version 23 August 2016
 */
public class TOCManager {
    private TOCDatabase db;
    private TOCMember member;
    private TOCItem item;
    private TOCItem[] transactions;
    
    // Counts how many items are currently being processed in the transaction
    private int transactionCount = 0;
    
    /**
     * The constructor. Will set up the TOCDatabase object which will allow us to communicate with the SQL database
     */
    public TOCManager() {
        db = new TOCDatabase();
        member = new TOCMember(0,"No Member",false);
        item = new TOCItem("0","No Item", 0.0, 0);
        transactions = new TOCItem[30];
        
        // Call to create the database
        //db.firstSetup();
    }

    /**
     * Adds the current item to the transactions list
     */
    public void addItemToTransaction() {
        transactions[transactionCount] = item;
        transactionCount++;
    }
    
    /**
     * Finalises the current transaction and stores the information in the database
     */
    public void finaliseTransaction() {
        for(int i=0; i<transactions.length; i++) {
            if(transactions[i] != null) {
                db.addTransaction(member.getPmKeys(), transactions[i].getBarcode());
                transactions[i] = null;
            }
        }
        transactionCount = 0;
    }
    
    /**
     * A temporary function that will create a String representing a member's tab
     * 
     * @return              A string representation of the bill
     */
    public String getBill() {
        String bill = db.findTransactions(getMemberPmKeys());
        
        return bill;
    }
    
    /**
     * Handle a new user
     * 
     * @param   userPmKeys  The new user's pmKeys
     * @return              A boolean stating if the member was found and updated
     */
    public boolean changeMember(int userPmKeys) {
        if(db.memberExists(userPmKeys)) {
            String name = db.getMemberName(userPmKeys);
            boolean admin = db.memberIsAdmin(userPmKeys);
            
            member.updateMember(userPmKeys,name,admin);
            return true;
        }else {
            return false;
        }
    }
    
     /**
     * Checks to make sure the current admin is not removing themselves
     * 
     * @param pmKeys the member's pmKeys to be updated
     */
    public void updateAdmin(int pmKeys) {
        if(pmKeys != getMemberPmKeys())
            db.updateAdmin(pmKeys);
    }
    
    /**
     * Adds a new member to the database. Will check if the current member has the rights to modify the database
     * 
     * @param   pmKeys  the new member's pmKeys
     * @param   name    the new member's name
     * @param   admin   if the new member is an admin
     */
    public void newMember(int pmKeys, String name, boolean admin) {
        if(getMemberAdmin()) {
            db.addMember(pmKeys,name,admin);
        }
    }
    
    /**
     * Will remove member from the databse. Will not modify the transactions that member has made and will keep their
     * bills stored in the transactions table. The database function this calls should check if the members first exists
     * This function will not allow the user to remove a member with the same pmkeys as them (ie. themselves)
     * 
     * @param   pmKeys  the member's pmKeys
     */
    public void removeMember(int pmKeys) {
        if(pmKeys != getMemberPmKeys()) {
            db.removeMember(pmKeys);
        }
    }
    
    /**
     * Get the current member's pmKeys
     * 
     * @return          The current member's pmKeys
     */
    public int getMemberPmKeys() {
        return member.getPmKeys();
    }   
    
    /**
     * Get the current member's name
     * 
     * @return          The current member's name
     */
    public String getMemberName() {
        return member.getName();
    }
    
    /**
     * Get the current member's admin status
     * 
     * @return          The current member's admin flag
     */
    public boolean getMemberAdmin() {
        return member.isAdmin();
    }
    
    /**
     * Handle a new item
     * 
     * @param   barcode     the barcode of the item to allow us to search the database
     * @return              a boolean representing if the item was found or not
     */
    public boolean changeItem(String barcode) {
        if(db.itemExists(barcode)) {
            String name = db.getItemName(barcode);
            double cost = db.getItemCost(barcode);
            int stock = db.getItemStock(barcode);
            
            item.updateItem(barcode,name,cost,stock);
            return true;
        }else {
            return false;
        }
    }
    
    /** Receives the values for the cost update and sends them to the database manager
     * 
     * @param cost the updated cost
     * @param barcode the barcode of the item being updated
     */
    public void updateCost(double cost) {
        db.updateCost(cost,getItemBarcode());
    }
    
    /**
     * Receives the barcode of the item to be removed
     * 
     * @param barcode the barcode of the item to be removed
     */
    public void removeItem(String barcode) {
        db.removeItem(barcode);
    }
    
    /**
     * Adds a new item to the database. Will check if the current member has the rights to modify the database
     * 
     * @param   barcode     the item's barcode
     * @param   name        the item's name
     * @param   cost        the item's cost
     * @param   stock       the item's stock
     */
    public void newItem(String barcode, String name, double cost, int stock) {
        if(getMemberAdmin()) {
            db.addItem(barcode,name,cost,stock);
        }
    }
        
    /**
     * Get the current item's barcode
     * 
     * @return                  the item's barcode
     */
    public String getItemBarcode() {
        return item.getBarcode();
    }
    
    /**
     * Get the current item's name
     * 
     * @return                  the item's name
     */
    public String getItemName() {
        return item.getName();
    }
    
    /**
     * Get the current item's cost
     * 
     * @return                  the item's cost
     */
    public double getItemCost() {
        return item.getCost();
    }
    
    /**
     * Get the current item's sotck
     * 
     * @return                 the current stock of the item
     */
    public int getItemStock() {
        return item.getStock();
    }
}