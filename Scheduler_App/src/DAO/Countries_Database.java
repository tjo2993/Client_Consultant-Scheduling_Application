package DAO;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Countries;
import java.sql.*;

/**
 * This class provides methods to retrieve information about countries from the database.
 */
public class Countries_Database {

    public static ObservableList<Countries> getList_Of_Countries() {
        ObservableList<Countries> Every_Country = FXCollections.observableArrayList();
        String all_countries = """
        SELECT *
        FROM countries
        """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(all_countries);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int Country_ID = rs.getInt("Country_ID");
                String Country_Name = rs.getString("Country");

                Countries country = new Countries(Country_ID, Country_Name);
                Every_Country.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Every_Country;
    }

}
