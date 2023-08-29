package Model;


public class Customers {
    private int Customer_ID;
    private String Customer_Name;
    private String Customer_Address;
    private String Customer_ZIP;
    private String Customer_Phone;
    private int Division_ID;

    public Customers(int Customer_ID, String Customer_Name, String Customer_Address, String Customer_ZIP, String Customer_Phone, int Division_ID) {
        this.Customer_ID = Customer_ID;
        this.Customer_Name = Customer_Name;
        this.Customer_Address = Customer_Address;
        this.Customer_ZIP = Customer_ZIP;
        this.Customer_Phone = Customer_Phone;
        this.Division_ID = Division_ID;
    }

    @Override
    public String toString(){
        return "#" + (Customer_ID) + " " + Customer_Name;
    }

    public int getCustomer_ID() {

        return Customer_ID;
    }

    public void setCustomer_ID(int customer_ID) {

        this.Customer_ID = customer_ID;
    }

    public String getCustomer_Name() {

        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        this.Customer_Name = customer_Name;
    }

    public String getCustomer_Address() {

        return Customer_Address;
    }

    public void setCustomer_Address(String customer_Address) {
        this.Customer_Address = customer_Address;
    }

    public String getCustomer_ZIP() {

        return Customer_ZIP;
    }

    public void setCustomer_ZIP(String customer_ZIP) {

        this.Customer_ZIP = customer_ZIP;
    }

    public String getCustomer_Phone() {

        return Customer_Phone;
    }

    public void setCustomer_Phone(String customer_Phone) {

        this.Customer_Phone = customer_Phone;
    }

    public int getDivision_ID() {

        return Division_ID;
    }

    public void setDivision_ID(int division_ID) {

        this.Division_ID = division_ID;
    }
}
