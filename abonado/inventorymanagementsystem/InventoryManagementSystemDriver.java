package abonado.inventorymanagementsystem;




import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

/** This class launches the application and loads the main menu.*/
public class InventoryManagementSystemDriver extends Application {

    /** This method applies start up dimensions and loads the main menu from the provided fxml file.
     The stage is the main menu.
     @param stage Main Menu
      * ERROR:
     NONE
      * FUTURE ENHANCEMENT:
     NONE*/
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystemDriver.class.getResource("/view/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 440);
        stage.setScene(scene);
        stage.show();

    }

    /** This method launches the application.
     * @param args
     * ERROR:
     NONE
     * FUTURE ENHANCEMENT:
     NONE*/
    public static void main(String[] args) {

        launch();

    }
}