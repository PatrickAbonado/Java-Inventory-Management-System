package controller;

import abonado.inventorymanagementsystem.InventoryManagementSystemDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/** This class controls the functionality of the Add Part page. Parts are instantiated
 as either In House or Outsourced objects. Two copies are made of the newly added part.
 One copy is stored on the All Parts list and another copy is stored on either an In House or
 Outsourced parts list for access to their private class members.*/
public class AddPartController extends Inventory implements Initializable {

    public RadioButton addPartInHouseInHouse;
    public RadioButton addPartInHouseOutsourced;
    public TextField addPartInHouseInventoryText;
    public TextField addPartInHouseIDText;
    public TextField addPartInHousePriceText;
    public TextField addPartInHouseNameText;
    public TextField addPartInHouseMinText;
    public TextField addPartInHouseMaxText;
    public Button addPartInHouseSave;
    public Button addPartInHouseCancel;
    public Label addPartId;
    public TextField addPartIdText;
    public boolean addPartIsInHouse = true;
    public Label addPartLabel;


/** This method initializes the Add Part page gui features and
 the current next part id.
 * @param url The url provides paths for the Add Part object.
 * @param resourceBundle The resources are what is used to localize the Add Part features and functionalities.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addPartInHouseIDText.setText(String.valueOf(getPartId()));
    }

    /** This method controls the save button feature on the Add Part page. This method
     gathers input from text fields and stores the data as their selected type on an All Parts
     list and either an In House or Outsourced list.
     The action event is the pressing of the Save button.
     * @param actionEvent Save button
     * RUNTIME ERROR:
     Data that did not match the required type for the input text box created errors that caused the
    program to crash. To fix this error, I implemented try catch blocks that caught the exceptions and
     prompted the user with information for correct data type entry .
     * FUTURE ENHANCEMENT:
     A future enhancement would be having the option to save potential
     changes for later review before committing to the change.*/
    @FXML
    protected void onAddPartInHouseSaveClick(ActionEvent actionEvent) throws IOException {
        int newPartId = getPartId();

        int stock, min, max, machineId;
        double price;
        String name, companyName;


        try {
            price = Double.parseDouble(addPartInHousePriceText.getText());
        }
        catch (NumberFormatException nfe){
            catchPriceExceptionMessage();
            return;
        }

        try {
            stock = Integer.parseInt(addPartInHouseInventoryText.getText());
        }
        catch(NumberFormatException nfe){
            catchStockExceptionMessage();
            return;
        }

        try{
            min = Integer.parseInt(addPartInHouseMinText.getText());
        }
        catch(NumberFormatException nfe){
            catchMinExceptionMessage();
            return;
        }

        try{
            max = Integer.parseInt(addPartInHouseMaxText.getText());
        }
        catch (NumberFormatException nfe){
            catchMaxExceptionMessage();
            return;
        }

        try{
            name = addPartInHouseNameText.getText();
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

        if(addPartIsInHouse){

            try{
                machineId = Integer.parseInt(addPartIdText.getText());
            }
            catch (NumberFormatException nfe){
                catchMachineIdExceptionMessage();
                return;
            }


            getAllParts().add(new InHouse(newPartId, name,
                        price, stock, min, max, machineId));

            getAllInHouse().add(new InHouse(newPartId, name, price,
                    stock, min, max, machineId));


            setPartId(++newPartId);
            }
        else
        {
            try{
                companyName = addPartIdText.getText();
                if (companyName.length() == 0){
                    throw new Exception();
                }
            }
            catch(Exception e){
                catchCompanyNameExceptionMessage();
                return;
            }

            getAllParts().add(new Outsourced(newPartId, name, price,
                    stock, min, max, companyName));

            getAllOutsourced().add(new Outsourced(newPartId, name, price,
                    stock, min, max, companyName));

            setPartId(++newPartId);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /** This method controls the cancel button feature on this page. This method
     prevents the saving of any form with inputted data the user chooses not
     to save to the system.
     The actionEvent is the pressing of the cancel button.
     * @param actionEvent Cancel button
     * LOGICAL ERROR:
     A logical error occurred when I had copied and pasted the wrong
     page location where the location to the main menu would have
     been loaded from. I fixed this issue by applying the correct path
     to the location field.
     * FUTURE ENHANCEMENT:
     A cancel option that allows you to go back or view canceled forms
     with the fields populated with the data that was present when the cancel
     button was clicked.*/
    @FXML
    protected void onAddPartInHouseCancelClick(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /** This method controls behaviors related to the clicking of
     the Machine ID radio button. This button modifies a boolean variable that
     associates the new Part object as an In House type part.
     The actionEvent is the pressing of the "In House" labeled radio button.
     * @param actionEvent In House radio button
     * LOGICAL ERROR:
     A logical error occurred when the button was clicked but the
     "Outsourced" label remained unchanged to the "In House" label.
     I fixed this error by setting the label text to change when this button
     clicked.
     * FUTURE ENHANCEMENT:
     A future enhancement would be an output list of all current
     "In House" parts currently held within the inventory after the
     button is clicked.*/
    @FXML
    protected void onAddPartInHouseInHouseClick(ActionEvent actionEvent) {

        addPartId.setText("Machine ID");

        addPartIsInHouse = true;
    }

    /** This method controls the "Outsourced" button functionality. This button
     modifies a boolean variable that associates the new Part object as an Outsourced type part.
     The actionEvent is the pressing of the radio button labeled
     "Outsourced."
     * @param actionEvent Outsourced radio button
     * LOGICAL ERROR:
     The entered data was saved as the wrong type.  This was fixed by
     correcting the boolean variable check for "In House" or "Outsourced" update.
     * FUTURE ENHANCEMENT:
     A provided list on the page of the current "Outsourced" inventory
     present in the system presented after clicking the button.
     */
    @FXML
    protected void onAddPartInHouseOutsourcedClick(ActionEvent actionEvent) {

        addPartId.setText("Company ID");

        addPartIsInHouse = false;
    }
}
