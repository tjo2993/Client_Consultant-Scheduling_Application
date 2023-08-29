package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.First_Divisions;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides methods to retrieve information about first-level divisions from the database.
 */
public class FLD_Database {

    public static ObservableList<First_Divisions> getAll_Divisions() {
        ObservableList<First_Divisions> firstDivisionsList = FXCollections.observableArrayList();
        String selectAllDivisionsQuery =
                """ 
                SELECT *
                FROM first_level_divisions
                """;


        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(selectAllDivisionsQuery);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                First_Divisions division = All_Division_Results(rs);
                firstDivisionsList.add(division);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return firstDivisionsList;
    }

    private static First_Divisions All_Division_Results(ResultSet rs) throws SQLException {
        int Division_ID = rs.getInt("Division_ID");
        String Division_Name = rs.getString("Division");
        int Country_ID = rs.getInt("Country_ID");

        return new First_Divisions(Division_ID, Division_Name, Country_ID);
    }


    public static ObservableList<First_Divisions> getCanada_Divisions() {
        ObservableList<First_Divisions> firstDivisionsList = FXCollections.observableArrayList();
        String Canada_Query =
                """
                SELECT *
                FROM first_level_divisions
                WHERE Country_ID = 3
                """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(Canada_Query);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                First_Divisions division = All_Division_Results(rs);
                firstDivisionsList.add(division);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return firstDivisionsList;
    }


    public static ObservableList<First_Divisions> getUK_Divisions() {
        ObservableList<First_Divisions> firstDivisionsList = FXCollections.observableArrayList();
        String UK_Query =
                """
                SELECT *
                FROM first_level_divisions
                WHERE Country_ID = 2
                """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(UK_Query);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                First_Divisions division = All_Division_Results(rs);
                firstDivisionsList.add(division);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return firstDivisionsList;
    }


    public static ObservableList<First_Divisions> getUSA_Divisions() {
        ObservableList<First_Divisions> firstDivisionsList = FXCollections.observableArrayList();
        String USA_Query =
                """
                SELECT *
                FROM first_level_divisions
                WHERE Country_ID = 1
                """;

        try (
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(USA_Query);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                First_Divisions division = All_Division_Results(rs);
                firstDivisionsList.add(division);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return firstDivisionsList;
    }
}
