package controller;

import abonado.inventorymanagementsystem.InventoryManagementSystemDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class controls the Add Product form page functionality. Products are instantiated as objects of the
 Product class. These Product objects possess an Associated Parts List. The Add Part form stores data for
 Product attributes and adds Part objects to the new Product object's Associated Parts list from a list of all
 available parts.*/
public class AddProductController extends Inventory implements Initializable {
    public Button addProductSearchButton;
    private ObservableList<Part> addProductFormNewAssociatedPartsList = FXCollections.observableArrayList();
    public TableColumn addProductFormChooseIDCol;
    public TableColumn addProductFormChooseNameCol;
    public TableColumn addProductFormChooseInventoryCol;
    public TableColumn addProductFormSearchPriceCol;
    public TableColumn addProductFormAssociatedIDCol;
    public TableColumn addProductFormAssociatedNameCol;
    public TableColumn addProductFormAssociatedInventoryCol;
    public TableColumn addProductFormAssociatedPriceCol;
    public Button addProductFormAdd;
    public Button addProductFormSave;
    public Button addProductFormCancel;
    public Button addProductFormRemove;
    public TextField addProductFormSearchText;
    public TextField addProductFormId;
    public TextField addProductFormName;
    public TextField addProductFormStock;
    public TextField addProductFormPrice;
    public TextField addProductFormMax;
    public TextField addProductFormMin;
    public TableView addProductFormAllParts;
    public TableView addProductFormAssociatedPartsTable;

    /** This method initializes the Add Product page gui features and
     the current next part id.
     * @param url The url provides paths for the Add Product object.
     * @param resourceBundle The resources are what is used to localize the Add Product features and functionalities.
     * LOGICAL ERROR:
     Applying the wrong All Products list to set the table populated the text fields with unexpected data. This was
    fixed by entering the correct call for the All Parts list.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addProductFormId.setText(String.valueOf(getProductId()));
        addProductFormAllParts.setItems(getAllParts());
        addProductFormChooseIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductFormChooseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductFormChooseInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductFormSearchPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProductFormAssociatedPartsTable.setItems(addProductFormNewAssociatedPartsList);
        addProductFormAssociatedIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductFormAssociatedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductFormAssociatedInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductFormAssociatedPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /** This method controls the search functionality for the
     current available parts list on the Add Product form. Character input
     is taken from the user in the search text field and is checked for whether
     it is contained in either the object id or name in each object on the list.
     The matching items are output on the table.  If no match is found the user is
     prompted with a "no match" message.
     The actionEvent is either the pressing of the Search button
     or Enter when the search text field is selected.
     * @param actionEvent Enter, Search button
     * RUNTIME ERROR:
     This error occurred when no input was provided in the search text field.
     I fixed this error by adding code to check for the presence of input
     and to show the full list of available parts when no input was provided but
    an action event occurred.
     * FUTURE ENHANCEMENT:
     The omission of the need for the pressing of the Enter or Search buttons as
     a result of an auto filtering feature of data when any data is inputted in the
     search text field that matches stored table data.*/
    @FXML
    protected void onAddProductPartSearch(ActionEvent actionEvent){

        boolean found = false;
        boolean searchEntered = false;

        ObservableList<Part> partSearchResults = FXCollections.observableArrayList();

        String input = addProductFormSearchText.getText();

        for (int i = 0; i < getAllParts().size(); ++i) {
            if(String.valueOf(getAllParts().get(i).getId()).contains(addProductFormSearchText.getText())){
                partSearchResults.add(getAllParts().get(i));
                addProductFormAllParts.setItems(partSearchResults);
                found = true;
            }
            else if (getAllParts().get(i).getName().toLowerCase().contains(addProductFormSearchText.getText().toLowerCase())){
                partSearchResults.add(getAllParts().get(i));
                addProductFormAllParts.setItems(partSearchResults);
                found = true;
            }
        }
        if (found){
            return;
        }

        if(addProductFormSearchText.getText().length() > 0){
            searchEntered = true;
        }
        if (searchEntered){
            Alert alertSearchPartNotFound = new Alert(Alert.AlertType.INFORMATION);
            alertSearchPartNotFound.setTitle("NOT FOUND");
            alertSearchPartNotFound.setContentText("SEARCH ENTRY NOT FOUND");
            alertSearchPartNotFound.showAndWait();
            addProductFormAllParts.setItems(getAllParts());
        }
    }

    /** This method controls the functionality of the Add button in
     the Add Product form. This method adds parts from the available parts list
     to the Product's associated parts list.
     The actionEvent is the pressing of the Add button.
     * @param actionEvent Add button
     * RUNTIME ERROR:
     An NullPointerException error occurred when no row had been selected. I fixed
    this error by adding a check to ensure a valid object was present or the method was
    exited.*/
    @FXML
    protected void onAddProductFormAdd(ActionEvent actionEvent) throws IOException {

        Part selectedAdd = (Part)addProductFormAllParts.getSelectionModel().getSelectedItem();

        if(checkIfPartSelected(selectedAdd)){
            return;
        }

        addProductFormNewAssociatedPartsList.add(selectedAdd);
    }

    /** This method controls the functionality of the remove button
     under the associated parts table. This method removes parts from
     the Product's Associated Parts list.
     The actionEvent is the pressing of the Remove button.
     * @param actionEvent Remove button
     * LOGICAL ERROR:
     An updated list of associated parts was not output after a part was
     removed but the part was removed from the product's list of
     associated parts. To fix this error I created a separate list that
     was initialized with the current associated parts to the product
     that could be manipulated through the Remove and Add button selections.
    When saved the newly created list was associated to the product over the
    old associated list.
     * FUTURE ENHANCEMENT:
     A limited list of recently removed parts could be presented as part
     of the available functions for pressing the Remove button.*/
    @FXML
    protected void onAddProductFormRemove(ActionEvent actionEvent){

        Part selectedRemoved = (Part)addProductFormAssociatedPartsTable.getSelectionModel().getSelectedItem();

        if(checkIfPartSelected(selectedRemoved)){
            return;
        }

        Alert alertConfirmMenuDeletePart = new Alert(Alert.AlertType.CONFIRMATION,("Press OK to confirm DELETE"));
        Optional<ButtonType> choice = alertConfirmMenuDeletePart.showAndWait();

        if(choice.isPresent() && choice.get() == ButtonType.OK){
            addProductFormNewAssociatedPartsList.remove(selectedRemoved);
        }
    }

    /** This method controls the Save button functionality of this form. This method
     saves new Product objects to the All Products list.
     The actionEvent is the pressing of the Save button after entering
     data in the required fields.
     * @param actionEvent Save button
     * LOGICAL ERROR:
     Omitting data entry into the name field resulted in the storing of a product
     without a valid name. To fix this error I applied a check that ensured the
     length of the input data was greater than 0 in order for the product
     to be saved to the inventory.
     * FUTURE ENHANCEMENT:
     The option to save form data to a separate file or folder that can
     be sent through any communications medium or stored onto a cloud account
     for later review.*/
    @FXML
    protected void onAddProductFormSave(ActionEvent actionEvent) throws IOException {

        int newProductID = getProductId();

        int stock, min, max;
        double price;
        String name;

        try{
            price =  Double.parseDouble(addProductFormPrice.getText());
        }
        catch (NumberFormatException nfe){
            catchPriceExceptionMessage();
            return;
        }

        try{
            stock = Integer.parseInt(addProductFormStock.getText());
        }
        catch(NumberFormatException nfe){
            catchStockExceptionMessage();
            return;
        }

        try{
            min = Integer.parseInt(addProductFormMin.getText());
        }
        catch(NumberFormatException nfe){
            catchMinExceptionMessage();
            return;
        }

        try{
            max = Integer.parseInt(addProductFormMax.getText());
        }
        catch (NumberFormatException nfe){
            catchMaxExceptionMessage();
            return;
        }

        try{
            name = addProductFormName.getText();
            checkName(name);
            checkMinMax(min, max);
            checkStock(stock, min, max);
        }
        catch(Exception e)
        {
            Alert failCheckAlert = new Alert(Alert.AlertType.ERROR);
            failCheckAlert.setTitle("INVALID ENTRY");
            failCheckAlert.setContentText(e.getMessage());
            failCheckAlert.showAndWait();
            return;
        }

        getAllProducts().add(new Product (newProductID, name,
                price, stock, min, max));

        for(int i = 0; i < getAllProducts().size(); ++i){

            if(getAllProducts().get(i).getId() == newProductID){

                getAllProducts().get(i).setAssociatedPartsList(addProductFormNewAssociatedPartsList);
            }
        }

        setProductId(++newProductID);

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /** This method controls the Cancel button functionality on the Add Product form.
     This method prevents the saving of any form with inputted data the user chooses
     not to save to the system.
     The actionEvent is the pressing of the Cancel button.
     * @param actionEvent Add button
     * LOGICAL ERROR:
     After hitting the Cancel button I remained on the same form.
     To fix this error I added a loader to the Main Menu to execute
     at the end of the method.
     * FUTURE ENHANCEMENT:
     A cancel button feature that allows you to view prior cancelled form history.*/
    @FXML
    protected void onAddProductFormCancel(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }
}
