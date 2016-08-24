/**
 * An object that holds the details of the item that has been scanned
 * 
 * @author  Pedro Alves
 * @version 23 August 2016
 */
public class TOCItem {
    private String barcode;
    private String name;
    private double cost;
    private int stock;
    
    /**
     * The constructore. Set the item's details initially
     */
    public TOCItem(String newBarcode, String newName, double newCost, int newStock) {
        barcode = newBarcode;
        name = newName;
        cost = newCost;
        stock = newStock;
    }
    
    // ------------------------------------------- Getters and Setters -------------------------------------------
    /**
     * Update the item details when it is scanned. Usually called by TOCManager
     * 
     * @param   itemBarcode     the item's barcode
     * @param   itemName        the item's name
     * @param   itemCost        the item's cost
     * @param   itemStock       the current stock of the item
     */
    public void updateItem(String itemBarcode, String itemName, double itemCost, int itemStock) {
        barcode = itemBarcode;
        name = itemName;
        cost = itemCost;
        stock = itemStock;
    }
    
    /**
     * Get the item's barcode
     * 
     * @return                  the item's barcode
     */
    public String getBarcode() {
        return barcode;
    }
    
    /**
     * Get the item's name
     * 
     * @return                  the item's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the item's cost
     * 
     * @return                  the item's cost
     */
    public double getCost() {
        return cost;
    }
    
    /**
     * Get the item's sotck
     * 
     * @return                 the current stock of the item
     */
    public int getStock() {
        return stock;
    }
}
