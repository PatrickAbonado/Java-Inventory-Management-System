package model;

/** This class contains the attributes and methods for objects of the InHouse type.*/
public class Outsourced extends Part{
    private String companyName;

    /** This is the constructor of InHouse objects.*/
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id,name,price,stock,min,max);
        this.companyName = companyName;
    }

    /**
     * @param companyName The company name to set
     * */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return Return the company name
     * */
    public String getCompanyName(){return companyName;}
}
