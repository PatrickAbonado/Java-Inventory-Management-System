package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/** This class contains the methods for accessing, manipulating, and prompting the
 information about the different objects in the application.*/
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<InHouse> allInHouse = FXCollections.observableArrayList();
    private static ObservableList<Outsourced> allOutsourced = FXCollections.observableArrayList();
    private static int partId = 1;
    private static int productId = 1000;

    /** This method returns the Part object ID for instantiated Part objects
     * @return Part ID
     */
    public static int getPartId() {
        return partId;
    }

    /**
     * @param partID the part ID to set
     */
    public static void setPartId(int partID) {
        Inventory.partId = partID;
    }

    /** This method checks if a Part object is null.
     * @param part the selected Part object to check
     * @return boolean partNotSelected
     */
    public static boolean checkIfPartSelected(Part part){

        boolean partNotSelected = part == null;

        if(partNotSelected){

            Alert partNotSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            partNotSelectedAlert.setTitle("ENTRY NOT DETECTED");
            partNotSelectedAlert.setContentText("SELECTION NOT DETECTED");
            partNotSelectedAlert.showAndWait();
        }

        return partNotSelected;
    }

    /** This method checks if a Product object is null
     * @param product the selected Product object to check
     * @return boolean productNotSelected
     */
    public static boolean checkIfProductSelected(Product product){

        boolean productNotSelected = product == null;

        if(productNotSelected){

            Alert partNotSelectedAlert = new Alert(Alert.AlertType.INFORMATION);
            partNotSelectedAlert.setTitle("ENTRY NOT DETECTED");
            partNotSelectedAlert.setContentText("SELECTION NOT DETECTED");
            partNotSelectedAlert.showAndWait();
        }

        return productNotSelected;
    }

    /**
     * @return The product ID
     */
    public static int getProductId() {
        return productId;
    }

    /**
     * @param productId The product ID to set
     */
    public static void setProductId(int productId) {
        Inventory.productId = productId;
    }

    /** This method adds Part objects to the All Parts list.
     * @param newPart The new Part to add
     * */
    public void addPart(Part newPart){
        allParts.add(newPart);
    }

    /** This method adds Product objects to the All Products list.
     * @param newProduct The new product to add
     * */
    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /** This method lookups up parts on the All Parts list by part ID.
     * @param partId The part ID to lookup
     * @return Return the part matching the part ID
     * */
    public Part lookupPart(int partId) {
        return allParts.get(partId);
    }

    /** This method looks up products on the all Products list by product ID.
     * @param productId The product ID to lookup
     * @return Return the product with the matching product ID
     * */
    public Product lookupProduct(int productId){
        return allProducts.get(productId);
    }

    /** This method looks up parts on the All Parts list by part name.
     * @param partName The part name to look up
     * @return Return the allParts list with the matching part name
     * */
    public ObservableList<Part> lookupPart(String partName) {
        return allParts;
    }

    /** This method looks up products on the All Products list by product name.
     * @param productName The product name to look up
     * @return Return the all product list with the matching product name
     * */
    public ObservableList<Product> lookupProduct(String productName){
        return allProducts;
    }

    /** This method replaces objects by index on the All Parts list.
     * @param index The index to be updated
     * @param selectedPart The part to be set in the index
     * */
    public void updatePart(int index, Part selectedPart){
        getAllParts().set(index, selectedPart);
    }

    /** This method replaces objects by index on the All Products list.
     * @param index The index to be updated
     * @param newProduct The producted to be updated set in the index
     * */
    public void updateProduct(int index, Product newProduct){
        getAllProducts().set(index, newProduct);
    }

    /** This method deletes parts on the All Part list by object.
     * @param selectedPart The part to be deleted
     * @return Return the boolean value of whether it was removed
     * */
    public boolean deletePart(Part selectedPart){
        return getAllParts().remove(selectedPart);
    }

    /** This method deletes parts on the All Products list by object.
     * @param selectedProduct The product to be deleted
     * @return Return the boolean value of whether it was removed
     * */
    public boolean deleteProduct(Product selectedProduct){
       return getAllProducts().remove(selectedProduct);
    }

    /**
     * @return Return the All Parts list.
     * */
    public static ObservableList<Part> getAllParts(){return allParts;}

    /**
     * @return Return the All Products list.
     * */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    /**
     * @return Return the In House parts list
     * */
    public static ObservableList<InHouse> getAllInHouse(){return allInHouse;}

    /**
     * @return Return the Outsourced parts list
     **/
    public static ObservableList<Outsourced> getAllOutsourced() {return allOutsourced;}

    /** This throws an Exception if the Min value is greater than the Max value.
     * @param min The min object attribute
     * @param max The max object attribute
     * @throws Exception The condition message to output
     **/
    public void checkMinMax(int min, int max) throws Exception{

        if (min > max){

            throw new Exception("MAX MUST BE LARGER THAN MIN\n *** NO PARTS SAVED ***");
        }
    }

    /** This method throws an exception if Stock value is greater than the Max value
     or the Stock value is less than the Min value.
     * @param stock The object's stock attribute
     * @param min The object's min attribute
     * @param max The object's max attribute
     * @throws Exception The condition message to output
     * */
    public void checkStock(int stock, int min, int max) throws Exception{

        if(stock > max || stock < min){

            throw new Exception("INVENTORY VALUE MUST BE BETWEEN MIN AND MAX VALUES\n *** NO PARTS SAVED ***");
        }
    }

    /** This method throws an exception if the name entered has no characters in it.
     * @param name The name to check
     * @throws Exception The message to output if condition is met
     * */
    public void checkName(String name) throws Exception{

        if(name.length() == 0) {

            throw new Exception("NAME FIELD MUST HAVE CHARACTER INPUT");
        }
    }

    /** This method is the message displayed when the Price value entered is an invalid input type.*/
    public void catchPriceExceptionMessage(){

        Alert alertInvalidEntry = new Alert(Alert.AlertType.ERROR);
        alertInvalidEntry.setTitle("INVALID ENTRY");
        alertInvalidEntry.setContentText("PRICE/COST REQUIRES VALID NUMBER INPUT");
        alertInvalidEntry.showAndWait();
    }

    /** This method is the message displayed when the Stock value entered is an invalid input type.*/
    public void catchStockExceptionMessage(){

        Alert alertInvalidEntry = new Alert(Alert.AlertType.ERROR);
        alertInvalidEntry.setTitle("INVALID ENTRY");
        alertInvalidEntry.setContentText("INVENTORY ENTRY REQUIRES VALID NUMBER INPUT");
        alertInvalidEntry.showAndWait();
    }

    /** This method is the message displayed when the Min value entered is an invalid input type.*/
    public void catchMinExceptionMessage(){

        Alert alertInvalidEntry = new Alert(Alert.AlertType.ERROR);
        alertInvalidEntry.setTitle("INVALID ENTRY");
        alertInvalidEntry.setContentText("MIN ENTRY REQUIRES VALID NUMBER INPUT");
        alertInvalidEntry.showAndWait();
    }

    /** This method is the message displayed when the Max value entered is an invalid input type.*/
    public void catchMaxExceptionMessage(){

        Alert alertInvalidEntry = new Alert(Alert.AlertType.ERROR);
        alertInvalidEntry.setTitle("INVALID ENTRY");
        alertInvalidEntry.setContentText("MAX ENTRY REQUIRES VALID NUMBER INPUT");
        alertInvalidEntry.showAndWait();
    }

    /** This method is the message displayed when the Name value entered has no character input.*/
    public void catchCompanyNameExceptionMessage(){

        Alert alertInvalidEntry = new Alert(Alert.AlertType.ERROR);
        alertInvalidEntry.setTitle("INVALID ENTRY");
        alertInvalidEntry.setContentText("COMPANY NAME REQUIRES CHARACTER INPUT");
        alertInvalidEntry.showAndWait();
    }

    /** This method is the message displayed when the Machine ID value entered is an invalid input type.*/
    public void catchMachineIdExceptionMessage(){

        Alert alertInvalidEntry = new Alert(Alert.AlertType.ERROR);
        alertInvalidEntry.setTitle("INVALID ENTRY");
        alertInvalidEntry.setContentText("MACHINE ID ENTRY REQUIRES VALID NUMBER INPUT");
        alertInvalidEntry.showAndWait();
    }
}
