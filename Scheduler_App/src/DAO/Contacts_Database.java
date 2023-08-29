package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Contacts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides a method to retrieve information about contacts from the database.
 */
public class Contacts_Database {


    public static ObservableList<Contacts> getList_Of_Contacts() {
        ObservableList<Contacts> EveryContact = FXCollections.observableArrayList();

        String all_contacts = """
        SELECT *
        FROM contacts
        """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(all_contacts);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int Contact_ID = rs.getInt("Contact_ID");
                String Contact_Name = rs.getString("Contact_Name");


                Contacts All_Contacts = new Contacts(Contact_ID, Contact_Name);
                EveryContact.add(All_Contacts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return EveryContact;
    }
}
