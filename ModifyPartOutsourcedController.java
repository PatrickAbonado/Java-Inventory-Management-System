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

/** This class controls the Modify Part Outsourced page. This page's text fields are populated
 with data from Outsourced part objects and the Outsourced radio button is initially selected.
 This page is used to modify parts currently available in inventory.*/
public class ModifyPartOutsourcedController extends Inventory implements Initializable {
    public RadioButton modifyPartOutsourcedInHouse;
    public RadioButton modifyPartOutsourcedOutsourced;
    public TextField modifyPartOutsourcedInventoryText;
    public TextField modifyPartOutsourcedIDText;
    public TextField modifyPartOutsourcedPriceText;
    public TextField modifyPartOutsourcedNameText;
    public TextField modifyPartOutsourcedMinText;
    public TextField modifyPartOutsourcedCompanyNameText;
    public TextField modifyPartOutsourcedMaxText;
    public Button modifyPartOutsourcedSave;
    public Button modifyPartOutsourcedCancel;
    public boolean modifyPartOutsourcedIsOutsourced = true;
    public Label forInOutLabel;
    private static Part modifyPartOutsourcedToModify;

    /** This method copies the selected Part object from the Main Menu to a designated class private
     variable.
     The modifyPartOutsourcedToModify is the selected Main Menu Part object stored when the user
     selects a part to modify from the Parts table on the Parts side of the Main menu.
     * @param modifyPartOutsourcedToModify Part object*/
    public static void setModifyPartOutsourcedToModify(Part modifyPartOutsourcedToModify) {
        ModifyPartOutsourcedController.modifyPartOutsourcedToModify = modifyPartOutsourcedToModify;
    }

    /** This method initializes the Modify Part Outsourced form page gui features and populates
     data in the form text fields with the attributes of the selected Part from the Main Menu.
     * @param url The url provides paths for the Modify Part object.
     * @param resourceBundle The resources are what is used to localize the Modify Part features and functionalities.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for(int i = 0; i < getAllOutsourced().size(); ++i){

            if(getAllOutsourced().get(i).getId() == modifyPartOutsourcedToModify.getId()){
                modifyPartOutsourcedIDText.setText(String.valueOf(getAllOutsourced().get(i).getId()));
                modifyPartOutsourcedNameText.setText(getAllOutsourced().get(i).getName());
                modifyPartOutsourcedInventoryText.setText(String.valueOf(getAllOutsourced().get(i).getStock()));
                modifyPartOutsourcedPriceText.setText(String.valueOf(getAllOutsourced().get(i).getPrice()));
                modifyPartOutsourcedMaxText.setText(String.valueOf(getAllOutsourced().get(i).getMax()));
                modifyPartOutsourcedMinText.setText(String.valueOf(getAllOutsourced().get(i).getMin()));
                modifyPartOutsourcedCompanyNameText.setText(String.valueOf(getAllOutsourced().get(i).getCompanyName()));
            }
        }
    }

    /** This method controls the Save button on the Modify Part Outsourced form page. This saves updated
     Outsourced part data or creates a new In House part object. If a new In House object is created the
     Outsourced part list removes the object with the matching ID. The All Parts and Associated Parts
     lists are updated with the new part information through the setting of the creation of a new Part object
     in the index of the object that matches the part ID of the object being updated.
     The actionEvent is the clicking of the Save button.
     * @param actionEvent Save button
     * LOGICAL ERROR:
     When the Inventory text field was set with an integer that was greater than the Max field or less than the Min
     field the saved data would show a higher inventory value than the maximum value and a lower inventory value than the
     minimum value. To fix this error I added a check that ensured that the Inventory value was less than the Max value
    but still more than the Min value. If this condition was not satisfied the user would be prompted with a message
    indicating the error and the form was not saved.
     * FUTURE ENHANCEMENT:
     The ability to save the form for review and confirmation at a later time.*/
    @FXML
    protected void onModifyPartOutsourcedSaveClick(ActionEvent actionEvent) throws IOException {

        int stock, min, max, machineId;
        double price;
        String name, companyName;

        int id = modifyPartOutsourcedToModify.getId();

        try{
            price = Double.parseDouble(modifyPartOutsourcedPriceText.getText());
        }
        catch (NumberFormatException nfe){
            catchPriceExceptionMessage();
            return;
        }

        try{
            stock = Integer.parseInt(modifyPartOutsourcedInventoryText.getText());
        }
        catch(NumberFormatException nfe){
            catchStockExceptionMessage();
            return;
        }

        try{
            min = Integer.parseInt(modifyPartOutsourcedMinText.getText());
        }
        catch(NumberFormatException nfe){
            catchMinExceptionMessage();
            return;
        }

        try{
            max = Integer.parseInt(modifyPartOutsourcedMaxText.getText());
        }
        catch (NumberFormatException nfe){
            catchMaxExceptionMessage();
            return;
        }

        try{
            name = modifyPartOutsourcedNameText.getText();
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


        if(modifyPartOutsourcedIsOutsourced){
            try{
                companyName = modifyPartOutsourcedCompanyNameText.getText();
                if (companyName.length() == 0){
                    throw new Exception();
                }
            }
            catch(Exception e){
                catchCompanyNameExceptionMessage();
                return;
            }


            for(int i = 0; i < getAllOutsourced().size(); ++i){
                if(getAllOutsourced().get(i).getId() == id){
                    getAllOutsourced().get(i).setName(name);
                    getAllOutsourced().get(i).setPrice(price);
                    getAllOutsourced().get(i).setStock(stock);
                    getAllOutsourced().get(i).setMax(max);
                    getAllOutsourced().get(i).setMin(min);
                    getAllOutsourced().get(i).setCompanyName(companyName);
                }
            }

            for (int i = 0; i < getAllParts().size(); ++i){
                if(getAllParts().get(i).getId() == id){

                    updatePart(i,(new Outsourced(id, name, price,
                            stock, min, max, companyName)));
                }
            }
        }

        else
        {
            try{
                machineId = Integer.parseInt(modifyPartOutsourcedCompanyNameText.getText());
            }
            catch(NumberFormatException nfe){
                catchMachineIdExceptionMessage();
                return;
            }

            getAllInHouse().add(new InHouse(id, name, price,
                    stock, min, max, machineId));

            getAllOutsourced().remove(modifyPartOutsourcedToModify);

            for (int i = 0; i < getAllParts().size(); ++i){
                if(getAllParts().get(i).getId() == id){

                    updatePart(i, (new InHouse(id, name, price,
                            stock, min, max, machineId)));
                }
            }
        }


        for(int i = 0; i < getAllProducts().size(); ++i){

            for (int j = 0; j < getAllProducts().get(i).getAssociatedParts().size(); ++j) {

                for(int k = 0; k < getAllParts().size(); ++k){

                    if(getAllParts().get(k).getId() == getAllProducts().get(i).getAssociatedParts().get(j).getId()){

                        getAllProducts().get(i).getAssociatedParts().set(j, getAllParts().get(k));
                    }
                }
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /** This method controls the Cancel button on the Modify Part Outsourced form page located on the bottom
     right side of the page. This button routes users back to the Main Menu page and does not save entered form data.
     The actionEvent is the clicking on the Cancel button.
     * @param actionEvent Cancel button*/
    @FXML
    protected void onModifyPartOutsourcedCancelClick(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setScene(scene);
        stage.show();
    }

    /** This method controls the In House labeled radio button functions. When selected
     this button sets a boolean value that is used to direct which save branch the data
     will utilize.
     The actionEvent is the clicking of the radio button to the right of the In House label.
     * @param actionEvent In House radio button
     * RUNTIME ERROR:
     After the Outsourced button was selected the data was still processed through the In House save instructions
     which lead to a NumberFormatException error because Machine ID must be of Integer type. To fix the error
     I created branches for the different types to be saved if the correct boolean condition was met.
     * FUTURE ENHANCEMENT:
     Upon clicking the In House button a table is output showing all current In House parts
     available in the system.*/
    @FXML
    protected void onModifyPartOutsourcedInHouseClick(ActionEvent actionEvent){

        modifyPartOutsourcedIsOutsourced = false;

        forInOutLabel.setText("Machine ID");

    }

    /** This method controls the Outsourced labeled radio button functions. When selected
     this button sets a boolean value that is used to direct which save branch the data
     will utilize.
     The actionEvent is the clicking of the radio button to the right of the Outsourced label.
     * @param actionEvent Outsourced radio button
     * LOGIC ERROR:
     After the button was selected the data was still saved with the Outsourced save instructions which led
     to a numerical company name and an object created as the wrong type. To fix the error
     I created branches for the different types to be saved if the correct boolean condition was met.
     * FUTURE ENHANCEMENT:
     Upon clicking the Outsourced button a table is output showing all current Outsourced parts
     available in the system.*/
    @FXML
    protected void onModifyPartOutsourcedOutsourcedClick(ActionEvent actionEvent){

        modifyPartOutsourcedIsOutsourced = true;

        forInOutLabel.setText("Company Name");
    }
}
