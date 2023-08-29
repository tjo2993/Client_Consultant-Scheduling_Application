package Controller;

import DAO.Users_Database;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.ResourceBundle;
import Model.Users;
import java.io.*;
import java.net.URL;
import java.time.*;
import java.util.*;

/**
 * The controller class for the login interface.
 *
 * This class handles user interactions and authentication for the login interface.
 * It manages the user's sign-in attempt, validates credentials, logs activity, and
 * controls navigation to the main menu or exiting the application.
 *
 * @author      Troy Jones
 */
public class LoginController implements Initializable {

    @FXML public Label Scheduling_Application, Sign_In_Below, User_Login, User_Password, User_Geolocation, User_Geolocation_Secondary;

    @FXML public Button Button_Sign_In, Button_Exit;

    @FXML public TextField User_Name_Field;
    @FXML public PasswordField User_Password_Field;


    ResourceBundle langBundle = ResourceBundle.getBundle("Lang");
    Locale defaultLocale = Locale.getDefault();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        langBundle = ResourceBundle.getBundle("Lang", defaultLocale);
        User_Geolocation_Secondary.setText(String.valueOf(ZoneId.systemDefault()));

        try {
            User_Geolocation.setText(langBundle.getString("UserGeolocation"));
            Scheduling_Application.setText(langBundle.getString("ApplicationTitle"));
            Sign_In_Below.setText(langBundle.getString("SignInBelow"));
            User_Login.setText(langBundle.getString("LoginUsername"));
            User_Password.setText(langBundle.getString("LoginPassword"));
            Button_Sign_In.setText(langBundle.getString("SignInAction"));
            Button_Exit.setText(langBundle.getString("CancelAction"));
        } catch (Exception e) {
            System.out.println(e + " is missing");
        }
    }


    @FXML
    public void Sign_In_Validate(ActionEvent actionEvent) {
        String Login_User = User_Name_Field.getText();
        String password = User_Password_Field.getText();

        boolean isLoginCorrect = !Correct_User(Login_User);
        boolean isPasswordCorrect = !Correct_Password(Login_User, password);

        Sign_In_Log(Login_User, password, isLoginCorrect && isPasswordCorrect);

        if (Login_User.isEmpty()) {
            displayAlert("BlankMessageTitle", "BlankMessage", "BlankLogin");
            return;
        } else if (!isLoginCorrect) {
            displayAlert("MessageTitle", "MessageHeader", "IncorrectUsername");
            return;
        }

        if (password.isEmpty()) {
            displayAlert("BlankMessageTitle", "BlankMessage", "BlankPassword");
            return;
        } else if (!isPasswordCorrect) {
            displayAlert("MessageTitle", "MessageHeader", "IncorrectPassword");
            return;
        }

        // Load the main menu if the username and password are valid
        MainScreen(actionEvent);
    }

    private void Sign_In_Log(String Login_User, String Login_Password, boolean SignIn) {
        try {
            LocalDate Current_Date = LocalDate.now();
            LocalTime Current_Time = LocalTime.now();
            LocalDateTime Current_Stamp = LocalDateTime.of(Current_Date, Current_Time);
            File file = new File("src/files/login_activity.txt");

            // Read the file content to determine the existing login count
            Scanner scanner = new Scanner(file);
            StringBuilder content = new StringBuilder();
            while (scanner.hasNext()) {
                content.append(scanner.nextLine()).append("\n");
            }
            scanner.close();

            int User_Activity = (int) content.chars().filter(ch -> ch == '\n').count();
            String attemptInfo = "Login Attempt " + (User_Activity + 1) + " || Username: " + Login_User + " || Password: " + Login_Password + " || Timestamp: " + Current_Stamp + "\n";
            FileWriter fw = new FileWriter(file, true);

            if (!SignIn) {
                fw.write("Login Failed || " + attemptInfo);
            } else {
                fw.write("Login Successful || " + attemptInfo);
            }

            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean Correct_User(String Login_User) {
        if (Login_User.isEmpty()) {
            return true;
        }
        ObservableList<Users> Is_Authorized = Users_Database.getAllUsers();
        for (Users Login : Is_Authorized) {
            if (Login_User.equals(Login.getUserName())) {
                return false;
            }
        }
        return true;
    }

    private boolean Correct_Password(String Login_User, String Login_Password) {
        if (Login_Password.isEmpty()) {
            return true;
        }
        ObservableList<Users> Authorized_Login = Users_Database.getAllUsers();
        for (Users Login : Authorized_Login) {
            if (Login_User.equals(Login.getUserName()) && Login_Password.equals(Login.getPassword())) {
                return false;
            }
        }
        return true;
    }

    private void displayAlert(String titleKey, String headerKey, String contentKey) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(langBundle.getString(titleKey));
        alert.setHeaderText(langBundle.getString(headerKey));
        alert.setContentText(langBundle.getString(contentKey));
        alert.showAndWait();
    }

    private void MainScreen(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ExitApplication() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the program?");
        Optional<ButtonType> After_Action = alert.showAndWait();
        if (After_Action.isPresent() && After_Action.get() == ButtonType.OK) {
        System.exit(0);
        }
    }
}
