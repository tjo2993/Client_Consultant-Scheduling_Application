package Model;


public class Contacts {
    private int Contact_ID;
    private String Contact_Name;



    public Contacts(int Contact_ID, String Contact_Name) {
        this.Contact_ID = Contact_ID;
        this.Contact_Name = Contact_Name;

    }


    @Override
    public String toString(){
        return "#" + Contact_ID + " " + Contact_Name;
    }

    public int getContact_ID() {

        return Contact_ID;
    }

}
