package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/** This class contains the attributes and methods for objects of the Product type. Product
 inherits its attributes from the Part class.*/
public class Product extends Part{
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** This is the constructor of Product objects.*/
    public Product(int id, String name, double price, int stock, int min, int max) {

        super(id, name, price, stock, min, max);

    }

    /** This method adds a part to the Associated Parts list.
     * @param part The part to add to the list
     * */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /** This method deletes a selected Part from the Associated Parts list.
     * @param selectedAssociatedPart The part to delete from the Associated Parts list
     * */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){

        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return Return the Associated Parts list
     * */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    /**
     * @param list The associated parts list to set
     * */
    public void setAssociatedPartsList(ObservableList<Part> list){
        this.associatedParts = list;
    }
}
