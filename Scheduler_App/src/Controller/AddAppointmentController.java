package Controller;

import DAO.*;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * This controller class manages the addition of new appointments.
 * It handles user interactions, validation, and inserts appointment data into the database.
 *
 * @author      Troy Jones
 */
public class AddAppointmentController implements Initializable {
    public TextField AppointmentID, AppointmentTitle, AppointmentDetails, AppointmentLocation;
    public ComboBox<String> AppointmentType;
    public DatePicker AppointmentStartDate, AppointmentEndDate;
    public ComboBox<LocalTime> AppointmentStartTime, AppointmentEndTime;
    public ComboBox<Users> SEL_UserID;
    public ComboBox<Customers> SEL_CustomerID;
    public ComboBox<Contacts> SEL_ContactID;
    public Button Save_Button, Cancel_Button;
    public Label Notification_Time;
    private final ObservableList<String> Appointment_Type_Menu = FXCollections.observableArrayList("Planning Session", "De-Briefing", "Execution", "Monitor & Control");
    private static final int lengthMax = 65;


    private void initializeTimeZoneDetails() {
        ZoneId EST = ZoneId.of("America/New_York");
        ZonedDateTime CurrentTime_ET = ZonedDateTime.now(EST);
        LocalTime currentTimeInEST = CurrentTime_ET.toLocalTime();
        LocalTime LatestTimeSlot = LocalTime.of(22, 0);
        ZoneId UserTimeZone = ZoneId.systemDefault();
        ZonedDateTime UserLatestTimeSlot = CurrentTime_ET.toLocalDate().atTime(LatestTimeSlot).atZone(EST).withZoneSameInstant(UserTimeZone);

        DateTimeFormatter DateTime = DateTimeFormatter.ofPattern("HH:mm a");
        Notification_Time.setText("The current time in EST is: " + currentTimeInEST.format(DateTime)
                + "\n"
                + "Appointments must be scheduled before " + LatestTimeSlot.format(DateTime) + " Eastern Time or " + UserLatestTimeSlot.format(DateTime) + " which is your local time.");
    }

    private void initializeComboBoxes() {
        initializeApptTypeComboBox();
        initializeUserIdComboBox();
        initializeCustIdComboBox();
        initializeContactIdComboBox();
    }

    private void initializeApptTypeComboBox() {
        AppointmentType.setItems(Appointment_Type_Menu);
        AppointmentType.setPromptText("Select a Type");
    }

    private void initializeUserIdComboBox() {
        ObservableList<Users> All_Users = Users_Database.getAllUsers();
        SEL_UserID.setItems(All_Users);
        SEL_UserID.setPromptText("Select a User ID");
    }

    private void initializeCustIdComboBox() {
        ObservableList<Customers> All_Customers = Customers_Database.getList_Of_Customers();
        SEL_CustomerID.setItems(All_Customers);
        SEL_CustomerID.setPromptText("Select a Customer ID");
    }

    private void initializeContactIdComboBox() {
        ObservableList<Contacts> All_Contacts = Contacts_Database.getList_Of_Contacts();
        SEL_ContactID.setItems(All_Contacts);
        SEL_ContactID.setPromptText("Select a Contact ID");
    }

    private void initializeDatePickers() {
        AppointmentStartDate.setValue(LocalDate.now());
        AppointmentEndDate.setValue(LocalDate.now());
    }

    private void initializeTimeComboBoxes() {
        ZoneId EST = ZoneId.of("America/New_York");
        ObservableList<LocalTime> validTimes = generateValidTimes(EST);
        AppointmentStartTime.setItems(validTimes);
        AppointmentEndTime.setItems(validTimes);
        AppointmentStartTime.getSelectionModel().select(LocalTime.of(8, 0));
        AppointmentEndTime.getSelectionModel().select(LocalTime.of(8, 0));
    }

    private ObservableList<LocalTime> generateValidTimes(ZoneId EST) {
        LocalTime ET_Start = LocalTime.of(8, 0);
        LocalTime ET_End = LocalTime.of(22, 0);
        LocalTime LocalStartTime = LocalTime.of(8, 0);
        LocalTime LocalEndTime = LocalTime.of(21, 30);

        ObservableList<LocalTime> Available_Times = FXCollections.observableArrayList();
        while (LocalStartTime.isBefore(LocalEndTime.plusSeconds(1))) {
            ZonedDateTime TimeZone = ZonedDateTime.of(LocalDate.now(), LocalStartTime, ZoneId.systemDefault());
            ZonedDateTime TimeZone_ET = TimeZone.withZoneSameInstant(EST);
            LocalTime EasternTime = TimeZone_ET.toLocalTime();

            if (!EasternTime.isBefore(ET_Start) && !EasternTime.isAfter(ET_End)) {
                Available_Times.add(LocalStartTime);
            }
            LocalStartTime = LocalStartTime.plusMinutes(30);
        }
        return Available_Times;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeTimeZoneDetails();
        initializeComboBoxes();
        initializeDatePickers();
        initializeTimeComboBoxes();
    }


    private void showAlert(Alert.AlertType appointment_Type, String title, String message) {
        Alert alert = new Alert(appointment_Type, message);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private boolean Outside_Business_Hours_Check(ZonedDateTime StartTime, ZonedDateTime EndTime) {
        ZoneId EasternTimeZone = ZoneId.of("America/New_York");
        LocalTime Business_Open = LocalTime.of(8, 0);
        LocalTime Business_Close = LocalTime.of(22, 0);

        ZonedDateTime ET_Start = StartTime.withZoneSameInstant(EasternTimeZone);
        ZonedDateTime ET_End = EndTime.withZoneSameInstant(EasternTimeZone);

        LocalTime OpenInEasternTime = ET_Start.toLocalTime();
        LocalTime CloseInEasternTime = ET_End.toLocalTime();

        return OpenInEasternTime.isBefore(Business_Open) || OpenInEasternTime.isAfter(Business_Close) || CloseInEasternTime.isBefore(Business_Open) || CloseInEasternTime.isAfter(Business_Close);
    }

    private boolean Appointment_Overlap_Check(LocalDateTime LocalStartTime, LocalDateTime LocalEndTime, int Customer_ID) {
        for (Appointments everyAppointment : Appointments_Database.getEveryAppointment()) {
            if (everyAppointment.getCustomer_ID() == Customer_ID) {
                LocalDateTime appointmentStartTime = everyAppointment.getAppointment_Start();
                LocalDateTime appointmentEndTime = everyAppointment.getAppointment_End();

                if (LocalStartTime.isBefore(appointmentEndTime) && LocalEndTime.isAfter(appointmentStartTime)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean Invalid_Text_Check(TextField field) {

        if (field.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", field.getId().split("TextField")[0] + " cannot be left blank.");
            return true;
        } else if (field.getText().length() > AddAppointmentController.lengthMax) {
            showAlert(Alert.AlertType.INFORMATION, "Excessive characters!", field.getId().split("TextField")[0] + " must be " + AddAppointmentController.lengthMax + " characters or less. The current length is " + field.getText().length());
            return true;
        }
        return false;
    }

    public void Save_Button(ActionEvent actionEvent) {
        try {
            String appointmentTitle = AppointmentTitle.getText();
            String appointmentDetails = AppointmentDetails.getText();
            String appointmentLocation = AppointmentLocation.getText();
            String appointment_Type = AppointmentType.getSelectionModel().getSelectedItem();
            LocalDate appointmentStartDate = AppointmentStartDate.getValue();
            LocalTime appointmentStartTime = AppointmentStartTime.getSelectionModel().getSelectedItem();
            LocalDateTime localStart = LocalDateTime.of(appointmentStartDate, appointmentStartTime);
            LocalDate appointmentEndDate = AppointmentEndDate.getValue();
            LocalTime appointmentEndTime = AppointmentEndTime.getSelectionModel().getSelectedItem();
            LocalDateTime localEnd = LocalDateTime.of(appointmentEndDate, appointmentEndTime);

            int Select_CustomerID = SEL_CustomerID.getSelectionModel().getSelectedItem().getCustomer_ID();
            int Select_UserID = SEL_UserID.getSelectionModel().getSelectedItem().getUser_ID();
            int Select_ContactID = SEL_ContactID.getSelectionModel().getSelectedItem().getContact_ID();

            ZonedDateTime timeZoneStart = ZonedDateTime.of(localStart, ZoneId.systemDefault());
            ZonedDateTime timeZoneEnd = ZonedDateTime.of(localEnd, ZoneId.systemDefault());

            if (Outside_Business_Hours_Check(timeZoneStart, timeZoneEnd)) {
                showAlert(Alert.AlertType.WARNING, "Appointment Outside EST Business Hours", "Appointment cannot be saved - appointment is outside EST business hours");
                return;
            }

            if (Appointment_Overlap_Check(localStart, localEnd, Select_CustomerID)) {
                showAlert(Alert.AlertType.WARNING, "Appointment Overlap", "Appointment cannot be saved - there is an overlap with an existing appointment");
                return;
            }

            if (Invalid_Text_Check(AppointmentTitle)) return;
            if (Invalid_Text_Check(AppointmentDetails)) return;
            if (Invalid_Text_Check(AppointmentLocation)) return;
            if (localStart.isAfter(localEnd) || localEnd.isBefore(localStart) || appointmentStartDate.isAfter(appointmentEndDate) || appointmentEndDate.isBefore(appointmentStartDate)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Date/Time", "Check your date and time settings. They seem to be invalid.");
                return;
            }
            if (AppointmentType.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Type not selected", "Type must be selected.");
                return;
            }

            Appointments_Database.Add_Appointment(appointmentTitle, appointmentDetails, appointmentLocation, appointment_Type, localStart, localEnd, Select_CustomerID, Select_UserID, Select_ContactID);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();

        } catch (Exception x) {
            showAlert(Alert.AlertType.INFORMATION, "Nothing Selected Yet", "You cannot leave drop-down's empty.");
        }
    }

    public void Cancel_Button(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

}
