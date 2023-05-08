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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class controls the features on the Main Menu page. This page connects
 to all the main features of the application. From this page users can add or
 delete parts and products or make selections by searching through either a list
 of products or parts to modify.*/
public class MainMenuController extends Inventory implements Initializable {
    public AnchorPane menuMainWindow;
    public AnchorPane menuProductsWindow;
    public TableView menuProductsTable;
    public TableColumn menuProductIdCol;
    public TableColumn menuProductNameCol;
    public TableColumn menuProductInventoryCol;
    public TableColumn menuProductPriceCol;
    public Button menuProductsAdd;
    public Button menuProductsModify;
    public Button menuProductsDelete;
    public TableView menuPartsTable;
    public TableColumn menuPartIDCol;
    public TableColumn menuPartNameCol;
    public TableColumn menuPartInventoryCol;
    public TableColumn menuPartUtPriceCol;
    public Button menuPartsAdd;
    public Button menuPartsModify;
    public Button menuPartsDelete;
    public Button menuExit;
    public TextField menuProductSearch;
    public TextField menuPartSearch;
    public Button menuPartSearchButton;
    public Button menuProductSearchButton;


    /** This method initializes the Main Menu page gui features.
     * @param url The url provides paths for the Main Menu object.
     * @param resourceBundle The resources are what is used to localize the Main Menu features and functionalities.
     * LOGICAL ERROR:
     Switching the lists to set the tables caused the form to be populated with the wrong data for each table.
     To fix this I entered the correct table into the correct table set function.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        menuPartsTable.setItems(getAllParts());
        menuProductsTable.setItems(getAllProducts());

        menuPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        menuPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        menuPartUtPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        menuProductIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        menuProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        menuProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        menuProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** This method controls the search functionality for the
     current available parts list on the Main Menu page. Character input
     is taken from the user in the search text field and is checked for
     whether it is contained in either the object id or name on the list.
     The matching items are output on the table. If no match is found the user is
     prompted with a "no match" message.
     The actionEvent is the pressing of either the Search button
     or Enter when the text field is selected.
     * @param actionEvent Enter, Search button
     * LOGIC ERROR:
     After finding a match the method execution continued also outputting the
     "not found" message information. To fix this I added a boolean variable update if a
     match was found. The variable would be checked in a conditional statement that exited the
     method prior to the execution of the "not found" code if updated to true.
     * FUTURE ENHANCEMENT:
     A search option that provides users with the ability to customize search
     preferences for all attributes, and create multiple search tables for output.*/
    @FXML
    protected void onMainMenuPartSearch(ActionEvent actionEvent){

        boolean searchEntered = false;
        boolean found = false;

        ObservableList<Part> mainMenuPartsSearchResults = FXCollections.observableArrayList();

        for (int i = 0; i < getAllParts().size(); ++i) {
            if(String.valueOf(getAllParts().get(i).getId()).contains(menuPartSearch.getText())){
                mainMenuPartsSearchResults.add(getAllParts().get(i));
                menuPartsTable.setItems(mainMenuPartsSearchResults);
                found = true;


            }
            else if (getAllParts().get(i).getName().toLowerCase().contains(menuPartSearch.getText().toLowerCase())){
                mainMenuPartsSearchResults.add(getAllParts().get(i));
                menuPartsTable.setItems(mainMenuPartsSearchResults);
                found = true;

            }
        }

        if(found){
            return;
        }

        if(menuPartSearch.getText().length() > 0){
            searchEntered = true;
        }

        if (searchEntered){
            Alert alertSearchPartNotFound = new Alert(Alert.AlertType.INFORMATION);
            alertSearchPartNotFound.setTitle("NOT FOUND");
            alertSearchPartNotFound.setContentText("SEARCH ENTRY NOT FOUND");
            alertSearchPartNotFound.showAndWait();
            menuPartsTable.setItems(getAllParts());

        }

    }

    /** This method controls the Add part button underneath the Parts table. This method routes users to
     the Add Part page by loading the page's fxml file with the provided startup dimensions.
     The actionEvent is the pressing of the Add button underneath the Parts table on the Main Menu.
     * @param actionEvent Add button
     * RUNTIME ERROR:
     Neglecting or misspelling the correct path to the fxml page resulted in and InvocationTargetException error.
    To fix this I accurately spelled the file name and included the full path after the resources directory.
     * FUTURE ENHANCEMENT:
     An option could be offered that adds parts to products from the add part page.*/
    @FXML
    protected void onMenuPartAddClick(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/AddPart.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 575, 425);
        stage.setScene(scene);
        stage.show();
    }

    /** This method controls the Modify Parts button underneath the Parts table. This method
     compares the selected part to elements in the In House and Outsourced lists. If a part's ID is
     matched with either list the user is routed to a Modify Part form with the part data populated
     in text fields and the part type radio button selected.
     The actionEvent is the pressing of the Modify button.
     * @param actionEvent Modify button
     * LOGICAL ERROR:
     I had copy and pasted the same code twice resulting in being routed to the In House page rather
     than the Outsourced page. I fixed this problem by changing the fxml path to the Outsourced page.
     * FUTURE ENHANCEMENTS:
     Right-clicking a part row with the mouse will provide an option to modifying the part from the
     right-click menu.
     */
    @FXML
    protected void onMenuPartModifyClick(ActionEvent actionEvent) throws IOException {

        FXMLLoader loadInHouse;
        FXMLLoader loadOutsourced;

        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();

        Part selectedMenuPart = (Part)menuPartsTable.getSelectionModel().getSelectedItem();

        if(checkIfPartSelected(selectedMenuPart)){
            return;
        }

        for(int i = 0; i < getAllInHouse().size(); ++i){

            if(selectedMenuPart.getId() == getAllInHouse().get(i).getId()){

                ModifyPartInHouseController.setModifyPartInHousePartToModify(getAllInHouse().get(i));

                loadInHouse = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/ModifyPartInHouse.fxml"));
                Scene scene = new Scene(loadInHouse.load(), 575, 425);
                stage.setScene(scene);
            }
        }

        for(int i = 0; i < getAllOutsourced().size(); ++i){

            if(getAllOutsourced().get(i).getId() == selectedMenuPart.getId()){

                ModifyPartOutsourcedController.setModifyPartOutsourcedToModify(getAllOutsourced().get(i));

                loadOutsourced = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/ModifyPartOutsourced.fxml"));
                Scene scene = new Scene(loadOutsourced.load(), 575, 425);
                stage.setScene(scene);
            }
        }

        stage.show();

    }

    /** This method controls the Delete button underneath the Part table on the Main Menu.
     This method compares selected parts by their IDs and deletes the matching parts on their
     list.
     The actionEvent is the clicking of the Delete button.
     * @param actionEvent Delete button
     * RUNTIME ERROR:
     Initially I had used index values to search for items to remove from the parts lists but
     with the way that the indexes are modified by the remove method this led to a NullPointerException error
     when multiple items were on the list and deleted. To fix this error I switched to the comparison
     of the part IDs witch always results in a matching result if parts are present in the system.
     * FUTURE ENHANCEMENT:
     An option to view a history of deleted items is available for users.
     */
    @FXML
    protected void onMenuPartDeleteClick(ActionEvent actionEvent) throws IOException {

        int totalAssociated = 0;

        Part selectedMenuPart = (Part)menuPartsTable.getSelectionModel().getSelectedItem();

        if(checkIfPartSelected(selectedMenuPart)){
            return;
        }

        for(int i = 0; i < getAllProducts().size(); ++i){

            for(int j = 0; j < getAllProducts().get(i).getAssociatedParts().size(); ++j){

                if(selectedMenuPart.getId() == getAllProducts().get(i).getAssociatedParts().get(j).getId()){
                    totalAssociated = getAllProducts().get(i).getAssociatedParts().size();
                }
            }
        }

        if (totalAssociated > 0){

            Alert alertErrorPartAssociated = new Alert(Alert.AlertType.ERROR);
            alertErrorPartAssociated.setTitle("CANNOT DELETE");
            alertErrorPartAssociated.setContentText("PART ASSOCIATED WITH PRODUCT\n*** PART NOT DELETED ***");
            alertErrorPartAssociated.showAndWait();

            return;
        }

        Alert alertConfirmMenuDeletePart = new Alert(Alert.AlertType.CONFIRMATION,("PRESS OK TO CONFIRM DELETE"));
        Optional<ButtonType> choice = alertConfirmMenuDeletePart.showAndWait();

        if(choice.isPresent() && choice.get() == ButtonType.OK){

            for (int i = 0; i < getAllInHouse().size(); ++i){

                if(getAllInHouse().get(i).getId() == selectedMenuPart.getId()){
                    getAllInHouse().remove(i);
                }
            }

            for (int i = 0; i < getAllOutsourced().size(); ++i){

                if(getAllOutsourced().get(i).getId() == selectedMenuPart.getId()){

                    getAllOutsourced().remove(i);
                }
            }

            for (int i = 0; i < getAllParts().size(); ++i){

                if(getAllParts().get(i).getId() == selectedMenuPart.getId()){

                    deletePart(getAllParts().get(i));
                }
            }
        }
    }

    /** This method controls the Add button underneath the Products table. This method
     routes users to the Add Product form page by loading the fxml file with applied dimensions.
     The actionEvent is the clicking of the Add button.
     * @param actionEvent Add button
     * RUNTIME ERROR:
     Neglecting or misspelling the correct path to the fxml page resulted in this error. To fix
     this I accurately spelled the file name and included the full path after resources directory.
     * FUTURE ENHANCEMENTS:
     Voice selection of button.*/
    @FXML
    protected void onMenuProductAddClick(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/AddProductForm.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 800, 625);
        stage.setScene(scene);
        stage.show();
    }

    /** This method controls the Modify product button on the Main Menu. This method sets a copy
     of the selected product for comparison and routes users to the Modify Product form.
     The actionEvent is the clicking of the Modify button underneath the Product table.
     * @param actionEvent Modify button
     * LOGICAL ERROR:
     Prior to using ID for matching between the All Products list and the selected product,
     After deletion of a product if no product was matched the form was populated with null values.
     * FUTURE ENHANCEMENTS:
     The option to modify the Associated Parts on the selected product could be made available for users.*/
    @FXML
    protected void onMenuProductModifyClick(ActionEvent actionEvent) throws IOException {

        Product selectedMenuProduct = (Product)menuProductsTable.getSelectionModel().getSelectedItem();

        if(checkIfProductSelected(selectedMenuProduct)){
            return;
        }

        ModifyProductController.setProductToModify(selectedMenuProduct);


        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/ModifyProductForm.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 800, 625);
        stage.setScene(scene);
        stage.show();
    }

    /** This method controls the Delete button underneath the Product table. This method compares the
     selected product's ID with all element ID's on the All Products list. When a match is found, that element is
     deleted from the list using the remove function. Before deletion this method checks to confirm that
     there are no associated parts present on the selected product's list. If found the product is not deleted
     and the user is prompted.
     The actionEvent is the clicking on the Delete button.
     * @param actionEvent Delete button
     * RUNTIME ERROR:
     When no selection is made and the Delete button is clicked a NullPointerException error would occur.
     To fix this error I created a check that ensures that the selected object created if null would
     exit the method with a message prompt for the user indicating no selection was detected.
     * FUTURE ENHANCEMENTS:
     An option to view a history of deleted products is output upon deletion with an option to recover
     a limited number of deleted forms for the option of recovery.*/
    @FXML
    protected void onMenuProductDeleteClick(ActionEvent actionEvent) throws IOException {

        int totalAssociated = 0;

        Product selectedMenuProduct = (Product)menuProductsTable.getSelectionModel().getSelectedItem();

        if(checkIfProductSelected(selectedMenuProduct)){
            return;
        }

        totalAssociated = selectedMenuProduct.getAssociatedParts().size();

        if (totalAssociated > 0){
            Alert alertErrorPartAssociated = new Alert(Alert.AlertType.ERROR);
            alertErrorPartAssociated.setTitle("CANNOT DELETE");
            alertErrorPartAssociated.setContentText("PRODUCT ASSOCIATED WITH PART\n*** PRODUCT NOT DELETED ***");
            alertErrorPartAssociated.showAndWait();
            return;
        }

        Alert alertConfirmMenuDeleteProduct = new Alert(Alert.AlertType.CONFIRMATION,("PRESS OK TO CONFIRM DELETE"));
        Optional<ButtonType> choice = alertConfirmMenuDeleteProduct.showAndWait();

        if(choice.isPresent() && choice.get() == ButtonType.OK){
            deleteProduct(selectedMenuProduct);
        }

    }

    /** This method controls the Exit button on the lower right-hand corner of the Main Menu
     form page. This method prompts the user with confirmation of the button selection and
     exits the program after the user as confirmed the decision to exit.
     The actionEvent is the clicking of Exit button.
     * @param actionEvent Exit button*/
    @FXML
    protected void onMenuExitClick(ActionEvent actionEvent){

        Alert alertConfirmMenuDeleteProduct = new Alert(Alert.AlertType.CONFIRMATION,("PRESS OK TO CONFIRM EXIT"));
        Optional<ButtonType> choice = alertConfirmMenuDeleteProduct.showAndWait();

        if(choice.isPresent() && choice.get() == ButtonType.OK){

            System.exit(0);
        }
    }

    /** This method controls the search button and text field above the Product table.
     This method takes user input and checks if any object in the product list contains
     matching elements. If no matches are found the user is prompted with a message.
     The actionEvent is the clicking of the Search button or pressing Enter on the keyboard
     while the search text field has been selected.
     * @param actionEvent Enter, Search button
     * LOGICAL ERROR:
     After finding a match the program would still show the "not found" message. To fix this error
     I added a conditional statement that if a match was found the method should be exited.
     * FUTURE ENHANCEMENT:
     A search option that provides users with the ability to customize search
     preferences for all attributes, and create multiple search tables for output.*/
    @FXML
    protected void onMainMenuProductSearch(ActionEvent actionEvent){

        boolean searchEntered = false;
        boolean found = false;

        ObservableList<Product> mainMenuProductSearchResults = FXCollections.observableArrayList();

        for (int i = 0; i < getAllProducts().size(); ++i) {

            if(String.valueOf(getAllProducts().get(i).getId()).contains(menuProductSearch.getText())){

                mainMenuProductSearchResults.add(getAllProducts().get(i));

                menuProductsTable.setItems(mainMenuProductSearchResults);

                found = true;
            }
            else if (getAllProducts().get(i).getName().toLowerCase().contains(menuProductSearch.getText().toLowerCase())){

                mainMenuProductSearchResults.add(getAllProducts().get(i));

                menuProductsTable.setItems(mainMenuProductSearchResults);

                found = true;
            }
        }

        if (found){

            return;
        }

        if(menuProductSearch.getText().length() > 0){

            searchEntered = true;
        }

        if (searchEntered){

            Alert alertSearchPartNotFound = new Alert(Alert.AlertType.INFORMATION);
            alertSearchPartNotFound.setTitle("NOT FOUND");
            alertSearchPartNotFound.setContentText("SEARCH ENTRY NOT FOUND");
            alertSearchPartNotFound.showAndWait();
            menuProductsTable.setItems(getAllProducts());
        }
    }
}