
package Model;

import java.time.LocalDateTime;


public class ContactSchedule {
    private String Contact_Name;
    private String Appointment_Title;
    private String Appointment_Description;
    private int Appointment_ID;
    private String Appointment_Type;
    private LocalDateTime Appointment_Start;
    private LocalDateTime Appointment_End;
    private int Customer_ID;


    public ContactSchedule(String Contact_Name, String Appointment_Title, String Appointment_Description, int Appointment_ID, String Appointment_Type, LocalDateTime Appointment_Start, LocalDateTime Appointment_End, int Customer_ID) {
        this.Contact_Name = Contact_Name;
        this.Appointment_Title = Appointment_Title;
        this.Appointment_Description = Appointment_Description;
        this.Appointment_ID = Appointment_ID;
        this.Appointment_Type = Appointment_Type;
        this.Appointment_Start = Appointment_Start;
        this.Appointment_End = Appointment_End;
        this.Customer_ID = Customer_ID;
    }

    public String getContact_Name() {

        return Contact_Name;
    }

    public void setContact_Name(String Contact_Name) {

        this.Contact_Name = Contact_Name;
    }

    public String getAppointment_Title() {

        return Appointment_Title;
    }

    public void setAppointment_Title(String Appointment_Title) {

        this.Appointment_Title = Appointment_Title;
    }

    public String getAppointment_Description() {

        return Appointment_Description;
    }

    public void setAppointment_Description(String Appointment_Description) {

        this.Appointment_Description = Appointment_Description;
    }

    public int getAppointment_ID() {

        return Appointment_ID;
    }

    public void setAppointment_ID(int Appointment_ID) {

        this.Appointment_ID = Appointment_ID;
    }

    public String getAppointment_Type() {

        return Appointment_Type;
    }

    public void setAppointment_Type(String Appointment_Type) {

        this.Appointment_Type = Appointment_Type;
    }

    public LocalDateTime getAppointment_Start() {

        return Appointment_Start;
    }

    public void setAppointment_Start(LocalDateTime Appointment_Start) {

        this.Appointment_Start = Appointment_Start;
    }

    public LocalDateTime getAppointment_End() {

        return Appointment_End;
    }

    public void setAppointment_End(LocalDateTime Appointment_End) {

        this.Appointment_End = Appointment_End;
    }

    public int getCustomer_ID() {

        return Customer_ID;
    }

    public void setCustomer_ID(int Customer_ID) {

        this.Customer_ID = Customer_ID;
    }
}

