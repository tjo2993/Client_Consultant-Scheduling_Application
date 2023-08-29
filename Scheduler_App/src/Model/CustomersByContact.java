package Model;

/**
 * Represents a report containing customer information along with division and country details.
 */
public class CustomersByContact {
    private final int Contact_ID;
    private String Contact_Name;
    private String Contact_Email;
    private int customerCount;

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {

        this.customerCountry = customerCountry;
    }

    private String customerCountry;



    public CustomersByContact(int Contact_ID, String Contact_Name, String Contact_Email, int customerCount, String customerCountry) {
        this.Contact_ID = Contact_ID;
        this.Contact_Name = Contact_Name;
        this.Contact_Email = Contact_Email;
        this.customerCount = customerCount;
        this.customerCountry = customerCountry;
    }

    public int getContact_ID() {

        return Contact_ID;
    }


    public String getContact_Name() {

        return Contact_Name;
    }

    public void setContact_Name(String contact_Name) {

        this.Contact_Name = contact_Name;
    }

    public int getCustomerCount() {

        return customerCount;
    }

    public void setCustomerCount(int customerCount) {

        this.customerCount = customerCount;
    }

    public String getContact_Email() {
        return Contact_Email;
    }

    public void setContact_Email(String contact_Email) {
        Contact_Email = contact_Email;
    }
}
