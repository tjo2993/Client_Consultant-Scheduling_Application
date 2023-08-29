package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Customers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This abstract class provides methods to retrieve, add, modify, and delete customer data from the database.
 */
public abstract class Customers_Database {

    public static ObservableList<Customers> getList_Of_Customers() {
        ObservableList<Customers> Every_Customer = FXCollections.observableArrayList();

        String selectAllQuery = """
        SELECT *
        FROM customers
        """;

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(selectAllQuery);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customers customer = Customer_Results(rs);
                Every_Customer.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Every_Customer;
    }

    private static Customers Customer_Results(ResultSet rs) throws SQLException {
        int Customer_ID = rs.getInt("Customer_ID");
        String Customer_Name = rs.getString("Customer_Name");
        String Customer_Address = rs.getString("Address");
        String Customer_ZIP_Code = rs.getString("Postal_Code");
        String Customer_Phone = rs.getString("Phone");
        int Division_ID = rs.getInt("Division_ID");

        return new Customers(Customer_ID, Customer_Name, Customer_Address, Customer_ZIP_Code, Customer_Phone, Division_ID);
    }

    public static void Add_New_Customer(String Customer_Name, String Customer_Address, String Customer_ZIP, String Customer_Phone, int Division_ID) {
        String insertQuery = """
        INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID)
        VALUES(?, ?, ?, ?, ?);
        """;

        try (PreparedStatement ps = DBConnection.connection.prepareStatement(insertQuery)) {
            Populate_PS_Customer(ps, Customer_Name, Customer_Address, Customer_ZIP, Customer_Phone, Division_ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void Populate_PS_Customer(PreparedStatement ps, String Customer_Name, String Customer_Address, String Customer_ZIP, String Customer_Phone, int Division_ID) throws SQLException {
        ps.setString(1, Customer_Name);
        ps.setString(2, Customer_Address);
        ps.setString(3, Customer_ZIP);
        ps.setString(4, Customer_Phone);
        ps.setInt(5, Division_ID);
    }


    public static void Change_Customer_Info(String Customer_Name, String Customer_Address, String Customer_ZIP, String Customer_Phone, int Division_ID, int Customer_ID) {
        String customerInfo = """
        UPDATE client_schedule.customers
        SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?
        WHERE Customer_ID = ?;
        """;

        try (PreparedStatement ps = DBConnection.connection.prepareStatement(customerInfo)) {
            Populate_PS(ps, Customer_Name, Customer_Address, Customer_ZIP, Customer_Phone, Division_ID, Customer_ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void Populate_PS(PreparedStatement ps, String Customer_Name, String Customer_Address, String Customer_ZIP, String Customer_Phone, int Division_ID, int Customer_ID) throws SQLException {
        ps.setString(1, Customer_Name);
        ps.setString(2, Customer_Address);
        ps.setString(3, Customer_ZIP);
        ps.setString(4, Customer_Phone);
        ps.setInt(5, Division_ID);
        ps.setInt(6, Customer_ID);
    }

    public static void Remove_Customer(int Customer_ID) {
        String deleteAppointmentsQuery = """
        DELETE FROM client_schedule.appointments
        WHERE Customer_ID = ?
        """;
        String deleteCustomersQuery = """
        DELETE FROM client_schedule.customers
        WHERE Customer_ID = ?
        """;

        try (
                PreparedStatement psAppointments = DBConnection.connection.prepareStatement(deleteAppointmentsQuery);
                PreparedStatement psCustomers = DBConnection.connection.prepareStatement(deleteCustomersQuery)
        ) {
            psAppointments.setInt(1, Customer_ID);
            psAppointments.executeUpdate();

            psCustomers.setInt(1, Customer_ID);
            psCustomers.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
