package Controller;

import DAO.Reports_Database;
import Model.ContactSchedule;
import Model.MonthlyAppointmentsByType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Model.CustomersByContact;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;

public class ReportsController implements Initializable {


    @FXML public TableView<MonthlyAppointmentsByType> MonthlyAppointmentsByType;
    @FXML public TableColumn<MonthlyAppointmentsByType, Integer> Appointment_Month;
    @FXML public TableColumn<MonthlyAppointmentsByType, String> Report_Type;
    @FXML public TableColumn<MonthlyAppointmentsByType, Integer> Appointment_Total_Monthly;

    @FXML public TableView<ContactSchedule> ContactScheduleTable;
    @FXML public TableColumn<MonthlyAppointmentsByType, Integer> Appointment_ID;
    @FXML public TableColumn<MonthlyAppointmentsByType, String> Appointment_Title;
    @FXML public TableColumn<MonthlyAppointmentsByType, String> Appointment_Contact;
    @FXML public TableColumn<MonthlyAppointmentsByType, String> Appointment_Type;
    @FXML public TableColumn<MonthlyAppointmentsByType, String> Appointment_Customer;
    @FXML public TableColumn<MonthlyAppointmentsByType, Timestamp> Appointment_Start_Time;
    @FXML public TableColumn<MonthlyAppointmentsByType, Timestamp> Appointment_End_Time;
    @FXML public TableColumn<MonthlyAppointmentsByType, String> Appointment_Description;

    @FXML public TableView<CustomersByContact> CustomersByContact;
    @FXML public TableColumn<MonthlyAppointmentsByType, Integer> Contact_ID;
    @FXML public TableColumn<MonthlyAppointmentsByType, String> Contact_Name;
    @FXML public TableColumn<CustomersByContact, String> Contact_Email;
    @FXML public TableColumn<MonthlyAppointmentsByType, Integer> Customer_Count;
    @FXML public TableColumn<MonthlyAppointmentsByType, String> Customer_Country;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeMonthlyAppointmentsByType();
        initializeContactScheduleReport();
        initializeCustomersByContact();
    }

    private void initializeMonthlyAppointmentsByType() {
        Report_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        Appointment_Month.setCellValueFactory(new PropertyValueFactory<>("month"));
        Appointment_Total_Monthly.setCellValueFactory(new PropertyValueFactory<>("total"));
        MonthlyAppointmentsByType.setItems(Reports_Database.getMonthlyAppointmentsByType());
    }

    private void initializeContactScheduleReport() {
        Appointment_Contact.setCellValueFactory(new PropertyValueFactory<>("Contact_Name"));
        Appointment_Title.setCellValueFactory(new PropertyValueFactory<>("Appointment_Title"));
        Appointment_Description.setCellValueFactory(new PropertyValueFactory<>("Appointment_Description"));
        Appointment_ID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        Appointment_Type.setCellValueFactory(new PropertyValueFactory<>("Appointment_Type"));
        Appointment_Start_Time.setCellValueFactory(new PropertyValueFactory<>("Appointment_Start"));
        Appointment_End_Time.setCellValueFactory(new PropertyValueFactory<>("Appointment_End"));
        Appointment_Customer.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        ContactScheduleTable.setItems(Reports_Database.getContactSchedule());
    }

    private void initializeCustomersByContact() {
        Contact_ID.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));
        Contact_Name.setCellValueFactory(new PropertyValueFactory<>("Contact_Name"));
        Contact_Email.setCellValueFactory(new PropertyValueFactory<>("Contact_Email"));
        Customer_Count.setCellValueFactory(new PropertyValueFactory<>("Customer_Count"));
        Customer_Country.setCellValueFactory(new PropertyValueFactory<>("Customer_Country"));
        CustomersByContact.setItems(Reports_Database.getCustomersByContact());
    }

    public void onActionCancel(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while returning to the main menu.");
            alert.setTitle("Error");
            alert.showAndWait();
        }
    }
}

