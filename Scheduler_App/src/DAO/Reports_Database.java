package DAO;

import Model.ContactSchedule;
import Model.CustomersByContact;
import Model.MonthlyAppointmentsByType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class Reports_Database {

    public static ObservableList<MonthlyAppointmentsByType> getMonthlyAppointmentsByType() {
        ObservableList<MonthlyAppointmentsByType> reportList = FXCollections.observableArrayList();
        String Monthly_Appointments =
        """
        SELECT appt.Type,
               MONTHNAME(appt.Start) AS Month,
               COUNT(*) AS TotalAppointments
        FROM client_schedule.appointments appt
        GROUP BY appt.Type, MONTH(appt.Start)
        ORDER BY MONTH(appt.Start), appt.Type;
        """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(Monthly_Appointments);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                MonthlyAppointmentsByType report = Monthly_Appointment_Results(rs);
                reportList.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reportList;
    }

    private static MonthlyAppointmentsByType Monthly_Appointment_Results(ResultSet rs) throws SQLException {
        String Appointment_Type = rs.getString("Type");
        String Appointment_Month = rs.getString("Month");
        int Total_Appointments = rs.getInt("TotalAppointments");
        return new MonthlyAppointmentsByType(Appointment_Type, Appointment_Month, Total_Appointments);
    }

    public static ObservableList<ContactSchedule> getContactSchedule() {
        ObservableList<ContactSchedule> reportItems = FXCollections.observableArrayList();

        String contact_schedule =
        """
        SELECT
            co.Contact_Name,
            ap.Title,
            ap.Description,
            ap.Appointment_ID,
            ap.Type,
            ap.Start,
            ap.End,
            ap.Customer_ID
        FROM client_schedule.appointments ap
        JOIN client_schedule.contacts co ON ap.Contact_ID = co.Contact_ID
        ORDER BY co.Contact_Name, ap.Start;
        """;

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(contact_schedule);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reportItems.add(Contact_Schedule_Results(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return reportItems;
    }

    private static ContactSchedule Contact_Schedule_Results(ResultSet rs) throws SQLException {
        String Contact_Name = rs.getString("Contact_Name");
        String Appointment_Title = rs.getString("Title");
        String Appointment_Description = rs.getString("Description");
        int Appointment_ID = rs.getInt("Appointment_ID");
        String Appointment_Type = rs.getString("Type");
        LocalDateTime Appointment_Start = convertToLocalDateTime(rs.getTimestamp("Start"));
        LocalDateTime Appointment_End = convertToLocalDateTime(rs.getTimestamp("End"));
        int Customer_ID = rs.getInt("Customer_ID");

        return new ContactSchedule(Contact_Name, Appointment_Title, Appointment_Description, Appointment_ID, Appointment_Type, Appointment_Start, Appointment_End, Customer_ID);
    }

    private static LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public static ObservableList<CustomersByContact> getCustomersByContact() {
        ObservableList<CustomersByContact> reportItems = FXCollections.observableArrayList();

        String Customers_By_Contact =
                """
                SELECT
                    cnt.Contact_ID,
                    cnt.Contact_Name,
                    cnt.Email,
                    country.Country,
                    COUNT(DISTINCT app.Customer_ID) AS customerCount
                FROM client_schedule.contacts cnt
                JOIN client_schedule.appointments app ON cnt.Contact_ID = app.Contact_ID
                JOIN client_schedule.customers cust ON app.Customer_ID = cust.Customer_ID
                JOIN client_schedule.first_level_divisions fld ON cust.Division_ID = fld.Division_ID
                JOIN client_schedule.countries country ON fld.Country_ID = country.Country_ID
                GROUP BY cnt.Contact_ID, cnt.Contact_Name, country.Country, cnt.Email;;
                """;

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(Customers_By_Contact);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reportItems.add(Customers_By_Contact_Results(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return reportItems;
    }

    private static CustomersByContact Customers_By_Contact_Results(ResultSet rs) throws SQLException {
        int Contact_ID = rs.getInt("Contact_ID");
        String Contact_Name = rs.getString("Contact_Name");
        String Contact_Email = rs.getString("Email");
        int Customer_Count = rs.getInt("CustomerCount");
        String Customer_Country = rs.getString("Country");
        return new CustomersByContact(Contact_ID, Contact_Name, Contact_Email, Customer_Count, Customer_Country);
    }

}
