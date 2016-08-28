import java.util.*;

/**
 * A temporary class to prototype the TOC while the GUI is being build
 * 
 * @author  Pedro Alves
 * @version 23 August 2016
 */
public class TOCGui {
    // The communicator between the Interface, Core and Database components
    private TOCManager manager;
    
    /**
     * The entry point of the toc program. Will start up the GUI and wait for the user input
     * 
     * @param   args            console commands (not used)
     */
    public static void main(String[] args) {
        TOCGui gui = new TOCGui();
        gui.start();
    }
    
    // ------------------------------------------------- Helper Functions -------------------------------------------------
    /**
     * Prints to System.out a string of characters and ends with a \n char
     * 
     * @param   text            the string to print
     */
    private void printLine(String text) {
        System.out.println(text);
    }
    
    /**
     * Gets the input of the user through the scanner object. Will not check if the input is valid so make sure to
     * check after this function returns the input
     * 
     * @return                  a String with the input of the user. Will remove whitespace before and after the sentence
     * @see                     getInput(int characterLoc)
     */
    private String getInput() {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine().trim();
        
        return input;
    }
    
    /**
     * Gets the input of the user through the scanner object. Differs from the other getInput function as this will only
     * get a character at a certain location in the sentence. Will not check if the input is valid so make sure to
     * check after this function returns the input.
     * 
     * @param   characterLoc    the location of the character in the sentence
     * @return                  the character at the specified location in the sentence. The character will be uppercase
     * @see                     getInput()
     */
    private char getInput(int characterLoc) {
        Scanner scan = new Scanner(System.in);
        char input = scan.nextLine().trim().toUpperCase().charAt(characterLoc);
        
        return input;
    }
    
    // -------------------------------------------------- Menu Functions --------------------------------------------------
    /**
     * Will prompt the user for input so we can call relevant functions
     * 
     * @return                  a character representation of the function the user chose
     */
    private char menu() {
        printLine("------------------------------");
        printLine("Welcome " + manager.getMemberName() + " - " + manager.getMemberPmKeys());
        printLine("Choose and option: ");
        printLine("(G)et Member Details");
        printLine("(D)etails of an item");
        printLine("(B)uy items");
        printLine("(S)ee bill");
        printLine("(A)dd Member");
        printLine("(R)emove Member");
        printLine("(U)pdate admin status");
        printLine("(N)ew Item");
        printLine("(E)xile Item");
        printLine("(C)hange item cost");
        printLine("(Q)uit");
        printLine("------------------------------");
        
        // Get the user's decision
        char choice = ' ';
        boolean ok = false;
        while(!ok) {
            System.out.print("Selection: ");
            choice = getInput(0);
            ok = (choice == 'G' || choice == 'D' || choice == 'B' || choice == 'Q' || choice == 'A' || choice == 'R' || choice == 'U' || choice == 'N' || choice == 'S' || choice == 'C' || choice == 'E');
            if(!ok) {
                printLine("Please type g,G,d,D,b,B,s,S,a,A,r,R,n,N,q,Q,u,U,c,C,e,E");
                printLine("");
            }
        }
        
        return choice;
    }
    
    /**
     * The starting screen of the TOC. Will get the user's details and allow them to choose what actions they would like to
     * perform
     */
    private void start() {
        int pmkeys = 0;
        char choice = ' ';
        String input = " ";
        
        manager = new TOCManager();
        boolean loop = true;
        printLine("17 DIVISION TOC");
        
        // Waits until a valid pmkeys is passed and then allows the user to 'log in'
        while(loop) {
            input = getInput();
            try {
                pmkeys = Integer.valueOf(input);
            }catch(NumberFormatException e) {
                pmkeys = 0;
            }
            
            if(manager.changeMember(pmkeys)) {
                loop = false;
            }else {
                printLine("");
                printLine("Member does not exist");
                System.out.print("Type a valid pmkeys: ");
            }
        }
        
        // Presents the main menu after user logged in
        while(choice != 'Q') {
            printLine("");
            choice = menu();
            switch(choice) {
                case 'G':
                    printMember();
                    break;
                case 'D':
                    getItemDetails();
                    break;
                case 'B':
                    startTransaction();
                    break;
                case 'S':
                    printBill();
                    break;
                case 'A':
                    addNewMember();
                    break;
                case 'R':
                    removeMember();
                    break;
                case 'U':
                    updateAdmin();
                    break;
                case 'N':
                    addNewItem();
                    break;
                case 'E':
                    removeItem();
                    break;
                case 'C':
                    updateCost();
                    break;
                case 'Q':
                    break;
            }
        }
    }
    
    // -------------------------------------------- Actions Called By The Menu --------------------------------------------
    /**
     * Shows the user the details of the current member that logged into the TOC
     */
    private void printMember() {
        printLine("PMKEYS: " + manager.getMemberPmKeys());
        printLine("NAME:   " + manager.getMemberName());
        printLine(" ");
    }
    
    /**
     * Asks the manager to add a new member to the database
     */
    private void addNewMember() {
        // Check if the user requesting to modify database is an admin
        if(manager.getMemberAdmin()) {
            String input = "";
            
            // These lines where we ask for user input should be received by either a prompt or a form in the proper GUI
            // Ask for pmKeys
            System.out.print("pmKeys of member: ");
            int pmkeys = 0;
            input = getInput();
            try {
                pmkeys = Integer.valueOf(input);
            }catch(NumberFormatException e) {
                pmkeys = 0;
            }
            
            // Ask for name
            System.out.print("Name of member: ");
            String name = getInput();
            
            // Ask if member is an admin
            System.out.print("Is the member an admin (1 for yes, 0 for no): ");
            boolean isAdmin = false;
            int admin = 0;
            input = getInput();
            try {
                admin = Integer.valueOf(input);
            }catch(NumberFormatException e) {
                admin = 0;
            }
            if(admin == 1) {
                isAdmin = true;
            }
            
            manager.newMember(pmkeys,name,isAdmin);
            printLine("");
        }else {
            printLine("Sorry you must be an admin");
        }
    }
    
    /**
     * Asks the manager to update the admin status of a member from the database
     */
    private void updateAdmin () {
                // Check if the user requesting to modify the database is an admin
        if (manager.getMemberAdmin()) {
            String input = "";
            
            // Ask for pmkeys
            System.out.print("pmKeys of member to update: ");
            int pmkeys = 0;
            input = getInput();
            try {
                pmkeys = Integer.valueOf(input);
            }catch(NumberFormatException e) {
                pmkeys = 0;
            }
            
            // Pass the details to the manager
            manager.updateAdmin(pmkeys);
            printLine("");
        }else {
            printLine("Sorry you must be an admin");
        }
    }
   
    
    /**
     * Asks the mamanger to remove member from the database
     */
    private void removeMember() {
        // Check if the user requesting to modify the database is an admin
        if(manager.getMemberAdmin()) {
            String input = "";
            
            // Ask for pmkeys
            System.out.print("pmKeys of member to remove: ");
            int pmkeys = 0;
            input = getInput();
            try {
                pmkeys = Integer.valueOf(input);
            }catch(NumberFormatException e) {
                pmkeys = 0;
            }
            
            // Pass the details to the manager
            manager.removeMember(pmkeys);
            printLine("");
        }else {
            printLine("Sorry you must be an admin");
        }
    }
    
    /**
     * Prompts the user to type the barcode of the item we want to see the details for
     */
    private void getItemDetails() {
        String input = "";
            
        // These lines where we ask for user input should be received by either a prompt or a form in the proper GUI
        // Ask for pmKeys
        System.out.print("Barcode of item: ");
        String barcode = getInput();

        if(manager.changeItem(barcode)) {
            printItem(barcode);
        }else {
            printLine("Item is not in the database");
        }
    }
    
    /**
     * Shows the user the details of an item's barcode
     * 
     * @param   barcode     the barcode of the item to show the info on
     */
    private void printItem(String barcode) {
        manager.changeItem(barcode);
        
        printLine("BARCODE: "   + manager.getItemBarcode());
        printLine("NAME:    "   + manager.getItemName());
        printLine("PRICE:   $"  + manager.getItemCost());
        printLine("STOCK:   "   + manager.getItemStock());
        printLine(" ");
    }
    
    /**
     * Asks the manager to add a new item to the database
     */
    private void addNewItem() {
        // Check if the user requesting to modify database is an admin
        if(manager.getMemberAdmin()) {
            String input = "";
            
            // These lines where we ask for user input should be received by either a prompt or a form in the proper GUI
            // Ask for pmKeys
            System.out.print("Barcode of item: ");
            String barcode = getInput();
            
            // Ask for name
            System.out.print("Name of item: ");
            String name = getInput();
            
            // Ask for cost
            System.out.print("Cost of item: ");
            double cost = 0.0;
            input = getInput();
            try {
                cost = Double.valueOf(input);
            }catch(NumberFormatException e) {
                cost = 0.0;
            }
            
            // Ask for stock
            System.out.print("Stock of item: ");
            int stock = 0;
            input = getInput();
            try {
                stock = Integer.valueOf(input);
            }catch(NumberFormatException e) {
                stock = 0;
            }
            
            manager.newItem(barcode,name,cost,stock);
            printLine("");
        }else {
            printLine("Sorry you must be an admin");
        }
    }
    
      /**
     * Asks the manager to remove an item from the databse
     */
    private void removeItem() {
        String input = "";
        
        // Ask for barcode
        System.out.print("Barcode of item to remove: ");
        String barcode = "";
        barcode = getInput();
        
        // Pass the details to the manager
        manager.removeItem(barcode);
        printLine("");
    }
    
    /**
     * Asks the manager to update the cost of an item
     */
    private void updateCost () {
                
            String input = "";
            
            // Ask for barcode of item
            System.out.print("Barcode of item to update: ");
            String barcode = getInput();
            
            // Ask for updated cost
            if(manager.changeItem(barcode)) {
                System.out.print("Updated cost: ");
                double cost = 0.0;
                input = getInput();
                try {
                    cost = Double.valueOf(input);
                }catch(NumberFormatException e) {
                    cost = 0.0;
                }
                
                // Pass the details to the manager
                manager.updateCost(cost);
            }
               
            printLine("");
    }
    
    /**
     * Gets the item barcode and adds it to the list of bought items. After the user has completed their shopping,
     * this function lets the TOCManager update the database
     */
    private void startTransaction() {
        // Controls the loop while the user is still buying
        boolean buying = true;
        String barcode = " ";
        while(buying) {
            System.out.print("Scan item (Type finish to finalise): ");
            barcode = getInput();
            if(manager.changeItem(barcode)) {
                manager.addItemToTransaction();
                printLine("" + manager.getItemName() + ": " + manager.getItemCost());
            }else if(barcode.equals("finish") || barcode.equals("Finish")) {
                manager.finaliseTransaction();
                buying = false;
            }else {
                printLine("Item not in this TOC's database");
            }
        }
    }
    
    /**
     * A temporary function that prints the user's past transactions
     */
    private void printBill() {
        printLine(manager.getBill());
    }
}