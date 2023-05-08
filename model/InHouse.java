package model;

/** This class contains the attributes and methods for objects of the InHouse type.*/
public class InHouse extends Part {
    private int machineId;


    /** This is the constructor for InHouse objects.*/
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){

        super(id,name,price,stock,min,max);
        this.machineId = machineId;
    }

    /**
     * @param machineId Machine ID to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * @return  Returns machineId
     */
    public int getMachineId() {
        return machineId;
    }



}
