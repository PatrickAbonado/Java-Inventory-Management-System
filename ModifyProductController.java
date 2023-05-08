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
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class controls the Modify Product form page. This page is used to modify and save products
 selected from the Main Menu Products table in the Products section of the Main Menu.*/
public class ModifyProductController extends Inventory implements Initializable {

    public Button modifyProductPartSearchButton;

    public TableColumn modifyProductFormChooseIDCol;
    public TableColumn modifyProductFormChooseNameCol;
    public TableColumn modifyProductFormChooseInventoryCol;
    public TableColumn modifyProductFormSearchPriceCol;
    public TableColumn modifyProductFormAssociatedIDCol;
    public TableColumn modifyProductFormAssociatedNameCol;
    public TableColumn modifyProductFormAssociatedInventoryCol;
    public TableColumn modifyProductFormAssociatedPriceCol;
    public Button modifyProductFormAdd;
    public Button modifyProductFormSave;
    public Button modifyProductFormCancel;
    public Button modifyProductFormRemove;
    public TextField modifyProductFormPartSearchText;
    public TextField modifyProductFormId;
    public TextField modifyProductFormName;
    public TextField modifyProductFormInventory;
    public TextField modifyProductFormPrice;
    public TextField modifyProductFormMax;
    public TextField modifyProductFormMin;
    public TableView modifyProductFormChooseTable;
    public TableView modifyProductFormAssociatedTable;
    ObservableList<Part> modifyFormNewAssociatedPartsList = FXCollections.observableArrayList();
    private static Product productToModify;

    /** This method copies the selected Product object from the Main Menu to a designated class
     private variable.
     The product is the selected Main Menu Product object stored when the user
     selects a Product to modify from the Products table on the Products side of the Main menu.
     * @param product Product object*/
    public static void setProductToModify(Product product){

        productToModify = product;
    }

    /** This method initializes the Modify Product form page gui features and populates data
     in the form text fields with the attributes of the selected Product from the Main Menu.
     * @param url The url provides paths for the Modify Product object.
     * @param resourceBundle The resources are what is used to localize the Modify Product features and functionalities.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (int i = 0; i < getAllProducts().size(); ++i){

            if(getAllProducts().get(i).getId() == productToModify.getId()){

                modifyProductFormId.setText(String.valueOf(getAllProducts().get(i).getId()));
                modifyProductFormName.setText(getAllProducts().get(i).getName());
                modifyProductFormInventory.setText(String.valueOf(getAllProducts().get(i).getStock()));
                modifyProductFormPrice.setText(String.valueOf(getAllProducts().get(i).getPrice()));
                modifyProductFormMax.setText(String.valueOf(getAllProducts().get(i).getMax()));
                modifyProductFormMin.setText(String.valueOf(getAllProducts().get(i).getMin()));

                for(int j = 0; j < getAllProducts().get(i).getAssociatedParts().size(); ++j){

                    modifyFormNewAssociatedPartsList.add(getAllProducts().get(i).getAssociatedParts().get(j));
                }
            }
        }

        modifyProductFormChooseTable.setItems(getAllParts());

        modifyProductFormChooseIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductFormChooseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductFormChooseInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductFormSearchPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProductFormAssociatedTable.setItems(modifyFormNewAssociatedPartsList);

        modifyProductFormAssociatedIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductFormAssociatedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductFormAssociatedInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductFormAssociatedPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** This method controls the search functionality for the
     current available parts list on the Modify Product form. Character input
     is taken from the user in the search text field and is checked for whether
     it is contained in either the object ID or name in each object on the list.
     The matching items are output on the table. If no match is found the user is
     prompted with a "no match" message.
     The actionEvent is either the pressing of the Search button
     or Enter when the text field is selected.
     * @param actionEvent Enter, Search button
     * RUNTIME ERROR:
     This error occurred when no input was provided in the search text field.
     I fixed this error by adding code to check for the presence of input
     and to show the full list when no input was provided but an action event
     occurred.
     * FUTURE ENHANCEMENT:
     The omission of the need for the pressing of the Enter or Search buttons as
     a result of an auto filtering feature of data when any input is presented
     that matches stored table data.*/
    @FXML
    protected void onModifyProductPartSearch(ActionEvent actionEvent){

        boolean found = false;
        boolean searchEntered = false;

        ObservableList<Part> partSearchResults = FXCollections.observableArrayList();

        String input = modifyProductFormPartSearchText.getText();

        for (int i = 0; i < getAllParts().size(); ++i) {

            if(String.valueOf(getAllParts().get(i).getId()).contains(input)){

                partSearchResults.add(getAllParts().get(i));

                modifyProductFormChooseTable.setItems(partSearchResults);

                found = true;
            }
            else if (getAllParts().get(i).getName().toLowerCase().contains(input.toLowerCase())){

                partSearchResults.add(getAllParts().get(i));

                modifyProductFormChooseTable.setItems(partSearchResults);

                found = true;
            }
        }

        if (found){

            return;
        }

        if(modifyProductFormPartSearchText.getText().length() > 0){

            searchEntered = true;
        }

        if (searchEntered){

            Alert alertSearchPartNotFound = new Alert(Alert.AlertType.INFORMATION);
            alertSearchPartNotFound.setTitle("NOT FOUND");
            alertSearchPartNotFound.setContentText("SEARCH ENTRY NOT FOUND");
            alertSearchPartNotFound.showAndWait();
            modifyProductFormChooseTable.setItems(getAllParts());
        }
    }

    /** This method controls the functionality of the Add button in
     the Modify Product form. This method adds parts from the available parts list
     to the Product's associated parts list.
     The actionEvent is the pressing of the Add button.
     * @param actionEvent Add button
     * RUNTIME ERROR:
     An error occurred when no row had been selected. I fixed this error
     by adding a check to ensure a valid object was present to continue
     or the method was exited.*/
    @FXML
    protected void onModifyProductFormAdd(ActionEvent actionEvent){

        Part selectedAdd = (Part)modifyProductFormChooseTable.getSelectionModel().getSelectedItem();

        if(checkIfPartSelected(selectedAdd)){
            return;
        }

        modifyFormNewAssociatedPartsList.add(selectedAdd);
    }

    /** This method controls the functionality of the Remove button
     under the associated parts table. This method removes parts from
     the Product's Associated Parts list.
     The actionEvent is the pressing of the Remove button.
     * @param actionEvent Remove button
     * LOGICAL ERROR:
     An updated list of associated parts was not presented after a part was
     removed but the part was removed from the product's list of
     associated parts. To fix this error I created a separate list that
     was initialized with the current associated parts to the product that is used
     for manipulation through the Remove and Add button selections. When saved
     the newly created list was associated to the product over the old associated list.
     * FUTURE ENHANCEMENT:
     A list of recently removed parts could be presented as part
     of the available functions for pressing the Remove button.*/
    @FXML
    protected void onModifyProductFormRemove(ActionEvent actionEvent){

        Part selectedRemoved = (Part)modifyProductFormAssociatedTable.getSelectionModel().getSelectedItem();

        if(checkIfPartSelected(selectedRemoved)){
            return;
        }

        Alert alertConfirmMenuDeleteProduct = new Alert(Alert.AlertType.CONFIRMATION,("PRESS OK TO CONFIRM REMOVE"));
        Optional<ButtonType> choice = alertConfirmMenuDeleteProduct.showAndWait();

        if(choice.isPresent() && choice.get() == ButtonType.OK){

            modifyFormNewAssociatedPartsList.remove(selectedRemoved);
        }
    }

    /** This method controls the Save button functionality of this form. This method
     saves modified Product objects to the All Products list.
     The actionEvent is the pressing of the Save button after entering
     data in the required fields.
     * @param actionEvent Save button
     * LOGICAL ERROR:
     Omitting data entry into the name field resulted in the storing of a product
     without a valid name. To fix this error I applied a check that ensured the
     length of the input data was greater than 0 in order for the product
     to be saved to the inventory. If no characters were present in the entry the user
    was prompted with a message indicating the error.
     * FUTURE ENHANCEMENT:
     The option to save entered data to a separate file or folder that can
     be sent through any communications medium or stored onto a cloud account
     for later review.*/
    @FXML
    protected void onModifyProductFormSave(ActionEvent actionEvent) throws IOException {

        int stock, min, max;
        double price;
        String name;

        int id = productToModify.getId();

        try{
            price = Double.parseDouble(modifyProductFormPrice.getText());
        }
        catch (NumberFormatException nfe){
            catchPriceExceptionMessage();
            return;
        }

        try{
            stock = Integer.parseInt(modifyProductFormInventory.getText());
        }
        catch(NumberFormatException nfe){
            catchStockExceptionMessage();
            return;
        }

        try{
            min = Integer.parseInt(modifyProductFormMin.getText());
        }
        catch(NumberFormatException nfe){
            catchMinExceptionMessage();
            return;
        }

        try{
            max = Integer.parseInt(modifyProductFormMax.getText());
        }
        catch (NumberFormatException nfe){
            catchMaxExceptionMessage();
            return;
        }

        try{
            name = modifyProductFormName.getText();
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

        for(int i = 0; i < getAllProducts().size(); ++i){

            if(id == getAllProducts().get(i).getId()){

                getAllProducts().get(i).setName(name);
                getAllProducts().get(i).setStock(stock);
                getAllProducts().get(i).setPrice(price);
                getAllProducts().get(i).setMax(max);
                getAllProducts().get(i).setMin(min);
                getAllProducts().get(i).setAssociatedPartsList(modifyFormNewAssociatedPartsList);
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /** This method controls the Cancel button functionality on the Modify Product form.
     This method prevents the saving of any form with inputted data the user chooses
     not to save to the system.
     The actionEvent is the pressing of the Cancel button.
     * @param actionEvent Cancel button
     * LOGICAL ERROR:
     After hitting the Cancel button I remained on the same form.
     To fix this error I added a loader to the Main Menu to execute
     at the end of the method.
     * FUTURE ENHANCEMENT:
     A cancel button feature that allows you to view prior cancelled form history.*/
    @FXML
    protected void onModifyProductFormCancel(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }
}
