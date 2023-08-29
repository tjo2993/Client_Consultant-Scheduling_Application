package Controller;

import DAO.Appointments_Database;
import DAO.Contacts_Database;
import DAO.Customers_Database;
import DAO.Users_Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.*;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This controller class manages the modification of appointment information.
 * It handles user interactions and updates appointment data accordingly.
 *
 * @author      Troy Jones
 */
public class ModifyAppointmentController implements Initializable {
    @FXML public TextField Appointment_ID, Appointment_Title, Appointment_Description, Appointment_Location;
    @FXML public ComboBox<Contacts> Contact_ID;
    @FXML public ComboBox<Customers> Customer_ID;
    @FXML public ComboBox<Users> User_ID;
    @FXML public ComboBox<String> Appointment_Type;
    @FXML public ComboBox<LocalTime> Appointment_Start_Time, Appointment_End_Time;
    @FXML public DatePicker Appointment_Start_Date, Appointment_End_Date;
    @FXML public Button Save_Appointment, Cancel_Appointment_Changes;
    private final ObservableList<String> Appointment_Type_Menu = FXCollections.observableArrayList("Planning Session", "De-Briefing", "Execution", "Monitor & Control");
    public Label Business_Hours;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId estZone = ZoneId.of("America/New_York");
        ZonedDateTime estNow = ZonedDateTime.now(estZone);
        LocalTime currentTimeInEST = estNow.toLocalTime();
        LocalTime latestAllowableTimeInEST = LocalTime.of(22, 0);
        ZoneId userZone = ZoneId.systemDefault();
        ZonedDateTime latestAllowableTimeInUserZone = estNow.toLocalDate().atTime(latestAllowableTimeInEST).atZone(estZone).withZoneSameInstant(userZone);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        Business_Hours.setText("The current time is " + currentTimeInEST.format(timeFormatter) + " EST." +
                "\nThe latest you can schedule an appointment is " + latestAllowableTimeInEST.format(timeFormatter) + " EST or " +
                latestAllowableTimeInUserZone.format(timeFormatter) + " your time.");

        Appointments selectedModifyAppointment = MainScreenController.getModifyAppointment();

        Appointment_ID.setText(String.valueOf(selectedModifyAppointment.getAppointment_ID()));
        Appointment_Title.setText(selectedModifyAppointment.getAppointment_Title());
        Appointment_Description.setText(selectedModifyAppointment.getAppointment_Description());
        Appointment_Location.setText(selectedModifyAppointment.getAppointment_Location());
        Appointment_Type.setItems(Appointment_Type_Menu);
        Appointment_Type.getSelectionModel().select(selectedModifyAppointment.getAppointment_Type());
        Appointment_Start_Date.setValue(selectedModifyAppointment.getAppointment_Start().toLocalDate());
        Appointment_End_Date.setValue(selectedModifyAppointment.getAppointment_Start().toLocalDate());

        List<LocalTime> timeOptions = TimeSelection();
        Appointment_Start_Time.setItems(FXCollections.observableArrayList(timeOptions));
        Appointment_End_Time.setItems(FXCollections.observableArrayList(timeOptions));
        Appointment_Start_Time.setValue(selectedModifyAppointment.getAppointment_Start().toLocalTime());
        Appointment_End_Time.setValue(selectedModifyAppointment.getAppointment_End().toLocalTime());

        Set_Values_For_Customers(Customer_ID, Customers_Database.getList_Of_Customers(), selectedModifyAppointment.getCustomer_ID());
        Set_Values_For_Users(User_ID, Users_Database.getAllUsers(), selectedModifyAppointment.getUser_ID());
        Set_Values_For_Contacts(Contact_ID, Contacts_Database.getList_Of_Contacts(), selectedModifyAppointment.getContact_ID());
    }

    private void Set_Values_For_Customers(ComboBox<Customers> comboBox, List<Customers> customers, int idToMatch) {
        comboBox.setItems(FXCollections.observableArrayList(customers));
        for (Customers customer : customers) {
            if (getIdFromCustomer(customer) == idToMatch) {
                comboBox.setValue(customer);
                break;
            }
        }
    }

    private int getIdFromCustomer(Customers customer) {

        return customer.getCustomer_ID();
    }

    private void Set_Values_For_Users(ComboBox<Users> comboBox, List<Users> users, int idToMatch) {
        comboBox.setItems(FXCollections.observableArrayList(users));
        for (Users user : users) {
            if (getIdFromUser(user) == idToMatch) {
                comboBox.setValue(user);
                break;
            }
        }
    }

    private int getIdFromUser(Users user) {

        return user.getUser_ID();
    }

    private void Set_Values_For_Contacts(ComboBox<Contacts> comboBox, List<Contacts> contacts, int idToMatch) {
        comboBox.setItems(FXCollections.observableArrayList(contacts));
        for (Contacts contact : contacts) {
            if (getIdFromContact(contact) == idToMatch) {
                comboBox.setValue(contact);
                break;
            }
        }
    }

    private int getIdFromContact(Contacts contact) {

        return contact.getContact_ID();
    }

    public void Save_Appointment(ActionEvent actionEvent) {
        try {

            int id = Integer.parseInt(Appointment_ID.getText());
            String title = Appointment_Title.getText();
            String description = Appointment_Description.getText();
            String location = Appointment_Location.getText();
            String appointment_Type = Appointment_Type.getSelectionModel().getSelectedItem();

            LocalDateTime start = LocalDateTime.of(Appointment_Start_Date.getValue(), Appointment_Start_Time.getSelectionModel().getSelectedItem());
            LocalDateTime end = LocalDateTime.of(Appointment_End_Date.getValue(), Appointment_End_Time.getSelectionModel().getSelectedItem());

            int customerId = Customer_ID.getSelectionModel().getSelectedItem().getCustomer_ID();
            int userId = User_ID.getSelectionModel().getSelectedItem().getUser_ID();
            int contactId = Contact_ID.getSelectionModel().getSelectedItem().getContact_ID();

            if (!Appointment_Inside_Business_Hours(start, end)) {
                showAlert(Alert.AlertType.WARNING, "Appointment Outside EST Business Hours", "Appointment cannot be saved - appointment is outside EST business hours");
                return;
            }

            if (Check_Appointment_Overlap(id, start, end)) {
                return;
            }

            if (!validateFields(title, description, location, appointment_Type, start, end)) {
                return;
            }

            Appointments_Database.Change_Appointment(id, title, description, location, appointment_Type, start, end, customerId, userId, contactId);
            Main_Menu_Return(actionEvent);

        } catch (Exception x) {
            showAlert(Alert.AlertType.INFORMATION, "Nothing Selected Yet", "You cannot leave drop-down's empty.");
        }
    }

    private boolean Appointment_Inside_Business_Hours(LocalDateTime start, LocalDateTime end) {
        ZoneId zoneEST = ZoneId.of("America/New_York");
        LocalTime open = LocalTime.of(8, 0);
        LocalTime close = LocalTime.of(22, 0);

        ZonedDateTime zonedStart = ZonedDateTime.of(start, ZoneId.systemDefault()).withZoneSameInstant(zoneEST);
        ZonedDateTime zonedEnd = ZonedDateTime.of(end, ZoneId.systemDefault()).withZoneSameInstant(zoneEST);

        LocalTime openEST = zonedStart.toLocalTime();
        LocalTime closeEST = zonedEnd.toLocalTime();

        return !(openEST.isBefore(open) || openEST.isAfter(close) || closeEST.isBefore(open) || closeEST.isAfter(close));
    }

    private boolean Check_Appointment_Overlap(int id, LocalDateTime start, LocalDateTime end) {
        for (Appointments a : Appointments_Database.getEveryAppointment()) {
            if (a.getCustomer_ID() == Customer_ID.getValue().getCustomer_ID() && id != a.getAppointment_ID()) {
                if (start.isBefore(a.getAppointment_End()) && end.isAfter(a.getAppointment_Start())) {
                    showAlert(Alert.AlertType.WARNING, "Appointment Overlap", "Appointment cannot be saved - there is an overlap with an existing appointment");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validateFields(String title, String description, String location, String appointment_Type, LocalDateTime start, LocalDateTime end) {

        if (title.isEmpty() || !title.matches("^[A-Za-z0-9 ]+$")|| title.length() > 20) {
            showAlert(Alert.AlertType.ERROR, "Title Error", "Title cannot be left blank or contain special characters.");
            return false;
        }

        if (description.isEmpty() || !description.matches("[A-Za-z0-9 .,:!\"'()-]+")|| description.length() > 50) {
            showAlert(Alert.AlertType.ERROR, "Description Error", "Description can only contain letters, numbers, and punctuation characters.");
            return false;
        }

        if (location.isEmpty() || !location.matches("^[A-Za-z0-9 ]+$")|| location.length() > 20) {
            showAlert(Alert.AlertType.ERROR, "Location Error", "Location cannot be left blank or contain special characters.");
            return false;
        }

        if (start.isAfter(end) || end.isBefore(start)) {
            showAlert(Alert.AlertType.ERROR, "Time Error", "Start time/date cannot be after/before the end time/date.");
            return false;
        }

        if (appointment_Type == null || appointment_Type.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Type Error", "Type must be selected.");
            return false;
        }

        return true;
    }

    private List<LocalTime> TimeSelection() {
        List<LocalTime> selectTime = new ArrayList<>();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);
        
        int minuteIntervals = 30;
        
        while (!start.isAfter(end)) {
            selectTime.add(start);
            start = start.plusMinutes(minuteIntervals);
        }
        return selectTime;
    }

    public void Cancel_Appointment_Change(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    private void Main_Menu_Return(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType appointment_Type, String title, String message) {
        Alert alert = new Alert(appointment_Type, message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
