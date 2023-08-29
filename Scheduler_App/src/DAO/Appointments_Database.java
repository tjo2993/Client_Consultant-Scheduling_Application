package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Appointments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class Appointments_Database {

    public static ObservableList<Appointments> getEveryAppointment() {
        ObservableList<Appointments> Every_Appointment = FXCollections.observableArrayList();
        String every_appointment = """
               SELECT *
               FROM client_schedule.appointments;
               """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(every_appointment);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Appointments appointment = Appointment_Results(rs);
                Every_Appointment.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Every_Appointment;
    }

    private static Appointments Appointment_Results(ResultSet rs) throws SQLException {
        int Appointment_ID = rs.getInt("Appointment_ID");
        String Appointment_Title = rs.getString("Title");
        String Appointment_Description = rs.getString("Description");
        String Appointment_Location = rs.getString("Location");
        String Appointment_Type = rs.getString("Type");
        LocalDateTime Appointment_Start = rs.getTimestamp("Start").toLocalDateTime();
        LocalDateTime Appointment_End = rs.getTimestamp("End").toLocalDateTime();
        int customer_ID = rs.getInt("Customer_ID");
        int user_ID = rs.getInt("User_ID");
        int contact_ID = rs.getInt("Contact_ID");
        return new Appointments(Appointment_ID, Appointment_Title, Appointment_Description, Appointment_Location, Appointment_Type, Appointment_Start, Appointment_End, customer_ID, user_ID, contact_ID);
    }

    public static ObservableList<Appointments> getAppointments_By_Month() {
        ObservableList<Appointments> AppointmentsByMonth = FXCollections.observableArrayList();

        String monthly_appointments = """
        SELECT *
        FROM client_schedule.appointments
        WHERE MONTH(Start) = MONTH(CURRENT_DATE())
        AND YEAR(Start) = YEAR(CURRENT_DATE());
        """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(monthly_appointments);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Appointments appointment = Appointment_Results(rs);
                AppointmentsByMonth.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return AppointmentsByMonth;
    }

    public static ObservableList<Appointments> getAppointments_By_Week() {
        ObservableList<Appointments> AppointmentsByWeek = FXCollections.observableArrayList();

        String AppointmentBy_Week = """
        SELECT *
        FROM client_schedule.appointments
        WHERE YEARWEEK(Start) = YEARWEEK(CURRENT_DATE())
        AND MONTH(Start) = MONTH(CURRENT_DATE());
    """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(AppointmentBy_Week);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Appointments appointment = Appointment_Results(rs);
                AppointmentsByWeek.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return AppointmentsByWeek;
    }

    public static ObservableList<Appointments> Upcoming_Appointments() {
        ObservableList<Appointments> upcomingAppointments = FXCollections.observableArrayList();

        String upcoming_appointments = """
        SELECT *
        FROM client_schedule.appointments
        WHERE Start >= NOW()
        AND Start <= NOW() + INTERVAL 15 MINUTE;
    """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(upcoming_appointments);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Appointments appointment = Appointment_Results(rs);
                upcomingAppointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return upcomingAppointments;
    }

    public static void Remove_Appointment(int Appointment_ID) {
        String remove_appointment = """
        DELETE FROM client_schedule.appointments
        WHERE Appointment_ID = ?;
    """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(remove_appointment)
        ) {
            ps.setInt(1, Appointment_ID);
            ps.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void Add_Appointment(String appointment_title, String appointment_description, String appointment_location, String appointment_type, LocalDateTime appointment_start, LocalDateTime appointment_end, int customer_ID, int user_ID, int contact_ID) {
        String Add_Appointment = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement ps = Set_Statement_Values(Add_Appointment, appointment_title, appointment_description, appointment_location, appointment_type, appointment_start, appointment_end, customer_ID, user_ID, contact_ID)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static PreparedStatement Set_Statement_Values(String sql, Object... values) throws SQLException {
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);

        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof String) {
                ps.setString(i + 1, (String) values[i]);
            } else if (values[i] instanceof LocalDateTime) {
                ps.setTimestamp(i + 1, Timestamp.valueOf((LocalDateTime) values[i]));
            } else if (values[i] instanceof Integer) {
                ps.setInt(i + 1, (Integer) values[i]);
            }
        }

        return ps;
    }

    public static void Change_Appointment(
            int Appointment_ID, String Appointment_Title, String Appointment_Description, String Appointment_Location,
            String Appointment_Type, LocalDateTime Appointment_Start, LocalDateTime Appointment_End,
            int Customer_ID, int User_ID, int Contact_ID) {

        String updateQuery = Update_Search();

        try (PreparedStatement ps = DBConnection.connection.prepareStatement(updateQuery)) {
            populatePreparedStatement(ps, Appointment_ID, Appointment_Title, Appointment_Description, Appointment_Location, Appointment_Type, Appointment_Start, Appointment_End, Customer_ID, User_ID, Contact_ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String Update_Search() {
        return "UPDATE client_schedule.appointments SET " +
                "Title = ?, " +
                "Description = ?, " +
                "Location = ?, " +
                "Type = ?, " +
                "Start = ?, " +
                "End = ?, " +
                "Customer_ID = ?, " +
                "User_ID = ?, " +
                "Contact_ID = ? " +
                "WHERE Appointment_ID = ?";
    }

    private static void populatePreparedStatement(
            PreparedStatement ps, int Appointment_ID, String Appointment_Title, String Appointment_Description, String Appointment_Location,
            String Appointment_Type, LocalDateTime appointment_Start, LocalDateTime appointment_End,
            int Customer_ID, int User_ID, int Contact_ID) throws SQLException {

        ps.setString(1, Appointment_Title);
        ps.setString(2, Appointment_Description);
        ps.setString(3, Appointment_Location);
        ps.setString(4, Appointment_Type);
        ps.setTimestamp(5, Timestamp.valueOf(appointment_Start));
        ps.setTimestamp(6, Timestamp.valueOf(appointment_End));
        ps.setInt(7, Customer_ID);
        ps.setInt(8, User_ID);
        ps.setInt(9, Contact_ID);
        ps.setInt(10, Appointment_ID);
    }

}
