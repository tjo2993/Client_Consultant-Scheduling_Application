package Model;

import java.time.LocalDateTime;


public class Appointments {
    private final int Appointment_ID;
    private String Appointment_Title;
    private String Appointment_Description;
    private String Appointment_Location;
    private String Appointment_Type;
    private LocalDateTime Appointment_Start;
    private LocalDateTime Appointment_End;
    private int Customer_ID;
    private int User_ID;
    private int Contact_ID;


    public Appointments(int Appointment_ID, String Appointment_Title, String Appointment_Description, String Appointment_Location, String Appointment_Type, LocalDateTime Appointment_Start, LocalDateTime Appointment_End, int Customer_ID, int User_ID, int Contact_ID) {
        this.Appointment_ID = Appointment_ID;
        this.Appointment_Title = Appointment_Title;
        this.Appointment_Description = Appointment_Description;
        this.Appointment_Location = Appointment_Location;
        this.Appointment_Type = Appointment_Type;
        this.Appointment_Start = Appointment_Start;
        this.Appointment_End = Appointment_End;
        this.Customer_ID = Customer_ID;
        this.User_ID = User_ID;
        this.Contact_ID = Contact_ID;
    }

    public int getAppointment_ID() {

        return Appointment_ID;
    }


    public String getAppointment_Title() {

        return Appointment_Title;
    }

    public void setAppointment_Title(String appointment_Title) {
        this.Appointment_Title = appointment_Title;
    }

    public String getAppointment_Description() {

        return Appointment_Description;
    }

    public void setAppointment_Description(String appointment_Description) {

        this.Appointment_Description = appointment_Description;
    }

    public String getAppointment_Location() {

        return Appointment_Location;
    }

    public void setAppointment_Location(String appointment_Location) {

        this.Appointment_Location = appointment_Location;
    }

    public String getAppointment_Type() {
        return Appointment_Type;
    }

    public void setAppointment_Type(String appointment_Type) {

        this.Appointment_Type = appointment_Type;
    }

    public LocalDateTime getAppointment_Start() {

        return Appointment_Start;
    }

    public void setAppointment_Start(LocalDateTime appointment_Start) {

        this.Appointment_Start = appointment_Start;
    }

    public LocalDateTime getAppointment_End() {

        return Appointment_End;
    }

    public void setAppointment_End(LocalDateTime appointment_End) {
        this.Appointment_End = appointment_End;
    }

    public int getCustomer_ID() {

        return Customer_ID;
    }

    public void setCustomer_ID(int customer_ID) {

        this.Customer_ID = customer_ID;
    }

    public int getUser_ID() {

        return User_ID;
    }

    public int getContact_ID() {

        return Contact_ID;
    }

    @Override
    public String toString(){

        return (Appointment_Type);
    }

}
