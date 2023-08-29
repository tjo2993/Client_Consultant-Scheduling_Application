package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Users_Database {

    public static ObservableList<Users> getAllUsers() {
        ObservableList<Users> userList = FXCollections.observableArrayList();

        String All_Users = "SELECT * FROM users";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(All_Users);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                userList.add(User_Results(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    private static Users User_Results(ResultSet rs) throws SQLException {
        int User_ID = rs.getInt("User_ID");
        String Login_UserName = rs.getString("User_Name");
        String Login_Password = rs.getString("Password");

        return new Users(User_ID, Login_UserName, Login_Password);
    }

}
