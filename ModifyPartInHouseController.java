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

/** This class controls the Modify Part In House page functionalities. This class is where
 users modify parts that are of In House type. The text fields are populated with the
 corresponding In House data and the In House radio button is initially selected. This page
 is used to modify parts currently available in inventory.*/
public class ModifyPartInHouseController extends Inventory implements Initializable {
    public RadioButton modifyPartInHouseInHouse;
    public RadioButton modifyPartInHouseOutsourced;
    public TextField modifyPartInHouseInventoryText;
    public TextField modifyPartInHouseIDText;
    public TextField modifyPartInHousePriceText;
    public TextField modifyPartInHouseNameText;
    public TextField modifyPartInHouseMinText;
    public TextField modifyPartInHouseMachineIdText;
    public TextField modifyPartInHouseMaxText;
    public Button modifyPartInHouseSave;
    public Button modifyPartInHouseCancel;
    public Label inOrOutLabel;
    public boolean modifyPartInHouseIsInHouse = true;
    private static Part modifyPartInHousePartToModify;

    /** This method copies the selected Part object from the Main Menu and sets the object to
     a designated class private variable.
     The modifyPartInHousePartToModify is the selected Main Menu Part object stored when the user
     selects a part to modify from the parts table and clicks Modify on the Parts side of the Main menu.
     * @param modifyPartInHousePartToModify Part object*/
    public static void setModifyPartInHousePartToModify(Part modifyPartInHousePartToModify) {
        ModifyPartInHouseController.modifyPartInHousePartToModify = modifyPartInHousePartToModify;
    }

    /** This method initializes the Modify Part In House form page gui features and populates data
     in the form text fields with the attributes of the selected Part from the Main Menu.
     * @param url The url provides paths for the Modify Part object.
     * @param resourceBundle The resources are what is used to localize the Modify Part features and functionalities.
     * RUNTIME ERROR:
     Before checks were added for Outsourced and In House parts, if an Outsourced part was directed to this page
    a NumberFormatException would occur because the machine ID requires integer input. This was fixed by adding
    conditional logic that directed user requests based on verified type.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for(int i = 0; i < getAllInHouse().size(); ++i){
            if(modifyPartInHousePartToModify.getId() == getAllInHouse().get(i).getId()){
                modifyPartInHouseIDText.setText(String.valueOf(getAllInHouse().get(i).getId()));
                modifyPartInHouseNameText.setText(getAllInHouse().get(i).getName());
                modifyPartInHouseInventoryText.setText(String.valueOf(getAllInHouse().get(i).getStock()));
                modifyPartInHousePriceText.setText(String.valueOf(getAllInHouse().get(i).getPrice()));
                modifyPartInHouseMaxText.setText(String.valueOf(getAllInHouse().get(i).getMax()));
                modifyPartInHouseMinText.setText(String.valueOf(getAllInHouse().get(i).getMin()));
                modifyPartInHouseMachineIdText.setText(String.valueOf(getAllInHouse().get(i).getMachineId()));
            }
        }

    }

    /** This method controls the Save button on the Modify Part In House form page. This saves updated
     In House part data or creates a new Outsourced part object. If a new Outsourced object is created the
     In House part list removes the object with the matching ID. The All Parts and Associated Parts lists
     are updated with the new part information through the setting of the creation of a new Part object in
     the index that matches the part ID of the object being updated.
     The actionEvent is the clicking of the Save button.
     * @param actionEvent Save button
     * LOGICAL ERROR:
     When the Max text field was set with an integer that was less than the Min field the saved
     data would show a higher minimum value than the maximum value. To fix this error I added a check
     that ensured that the number entered in the Min text field could only be saved if it was less than
     the number in the Max text field. If this condition was not satisfied the user would be prompted with a
     message indicating the error and the form was not saved.
     * FUTURE ENHANCEMENT:
     The ability to save the form for review and confirmation at a later time.*/
    @FXML
    protected void onModifyPartInHouseSaveClick(ActionEvent actionEvent) throws IOException {

        int stock, min, max, machineId;
        double price;
        String name, companyName;

        int id = modifyPartInHousePartToModify.getId();

        try{
            price = Double.parseDouble(modifyPartInHousePriceText.getText());
        }
        catch (NumberFormatException nfe){
            catchPriceExceptionMessage();
            return;
        }

        try{
            stock = Integer.parseInt(modifyPartInHouseInventoryText.getText());
        }
        catch(NumberFormatException nfe){
            catchStockExceptionMessage();
            return;
        }

        try{
            min = Integer.parseInt(modifyPartInHouseMinText.getText());
        }
        catch(NumberFormatException nfe){
            catchMinExceptionMessage();
            return;
        }

        try{
            max = Integer.parseInt(modifyPartInHouseMaxText.getText());
        }
        catch (NumberFormatException nfe){
            catchMaxExceptionMessage();
            return;
        }

        try{
            name = modifyPartInHouseNameText.getText();
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

        if(modifyPartInHouseIsInHouse){

            try{
                machineId = Integer.parseInt(modifyPartInHouseMachineIdText.getText());
            }
            catch(NumberFormatException nfe){
                catchMachineIdExceptionMessage();
                return;
            }

            for(int i = 0; i < getAllInHouse().size(); ++i){

                if(id == getAllInHouse().get(i).getId()){
                    getAllInHouse().get(i).setName(name);
                    getAllInHouse().get(i).setPrice(price);
                    getAllInHouse().get(i).setStock(stock);
                    getAllInHouse().get(i).setMin(min);
                    getAllInHouse().get(i).setMax(max);
                    getAllInHouse().get(i).setMachineId(machineId);
                }
            }

            for (int i = 0; i < getAllParts().size(); ++i){

                if(getAllParts().get(i).getId() == modifyPartInHousePartToModify.getId()){

                    updatePart(i,(new InHouse(id, name, price,
                            stock, min, max, machineId)));
                }
            }
        }

        else {
            try{
                companyName =  modifyPartInHouseMachineIdText.getText();
                if (companyName.length() == 0){
                    throw new Exception();
                }
            }
            catch(Exception e){
                catchCompanyNameExceptionMessage();
                return;
            }

                getAllOutsourced().add(new Outsourced(id, name, price,
                        stock, min, max, companyName));

                getAllInHouse().remove(modifyPartInHousePartToModify);

                for (int i = 0; i < getAllParts().size(); ++i) {

                    if (id == getAllParts().get(i).getId()) {

                        updatePart(i,(new Outsourced(id, name, price,
                                stock, min, max, companyName)));
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

    /** This method controls the cancel button on the Modify Part In House form page located on the bottom right
     side of the page. This button routes users back to the Main Menu page and does not save entered form data.
     The actionEvent is the clicking on the Cancel button.
     * @param actionEvent Cancel button*/
    @FXML
    protected void onModifyPartInHouseCancelClick(ActionEvent actionEvent) throws IOException {

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
     After the Outsourced button was selected the data was still saved with the In House save instructions which lead
     to a NumberFormatException error because Machine ID must be of Integer type. To fix the error
     I created branches for the different types to be saved if the correct boolean condition was met.
     * FUTURE ENHANCEMENT:
     Upon clicking the In House button a table is output showing all current In House parts
     available in the system.*/
    @FXML
    protected void onModifyPartInHouseInHouseClick(ActionEvent actionEvent){

        inOrOutLabel.setText("Machine ID");

        modifyPartInHouseIsInHouse = true;

    }

    /** This method controls the Outsourced button to the right of the Outsourced label.
     This button sets a boolean value that is used to direct which save branch the data
     will utilize.
     The actionEvent is the clicking of the radio button.
     * @param actionEvent Outsourced radio button
     * LOGICAL ERROR:
     After attempting to save the data as In House, the data ended up being stored as an Outsourced type
     which caused the inaccurate labeling of the attribute field and selection of radio button when the Part
     was selected for modification. To fix this error I created branches for the different types to be saved
     if a specific boolean condition occurred.
     * FUTURE ENHANCEMENT:
     Upon clicking the Outsourced button a table is output showing all current Outsourced parts
     available in the system.*/
    @FXML
    protected void onModifyPartInHouseOutsourcedClick(ActionEvent actionEvent){

        inOrOutLabel.setText("Company ID");

        modifyPartInHouseIsInHouse = false;
    }

}
