package Controller;

import DAO.Appointments_Database;
import DAO.Customers_Database;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import Model.Appointments;
import Model.Customers;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.util.AbstractMap.SimpleEntry;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainScreenController implements Initializable {
    @FXML public Button Add_Customer, Modify_Customer, Delete_Customer, Add_Appointment, Delete_Appointment, Modify_Appointment, Logout_Application;
    @FXML public RadioButton View_By_Week, View_By_Month, View_All;
    @FXML public ToggleGroup Toggle_The_View;

    @FXML public Label User_Local_Time, Eastern_Local_Time;

    @FXML public TableView<Customers> Customer_List_Table;
    @FXML public TableColumn<Customers, Integer> Customer_ID;
    @FXML public TableColumn<Customers, String> Customer_Name, Customer_Address, Customer_ZIP_Code, Customer_Phone, Customer_Division;

    @FXML public TableView<Appointments> Appointment_List_Table;
    @FXML public TableColumn<Appointments, Integer> Appointment_ID, Associated_Customer_ID, Associated_User_ID;
    @FXML public TableColumn<Appointments, String> Appointment_Title, Appointment_Description, Appointment_Location, Appointment_Contact, Appointment_Type;
    @FXML public TableColumn<Appointments, LocalDateTime> Appointment_Start, Appointment_End;

    public static Customers Selected_Customer;
    public static Appointments Selected_Appointment;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Upcoming_Appointments();

        Timeline appointmentReminder = new Timeline(
                new KeyFrame(Duration.minutes(15), e -> Upcoming_Appointments())
        );
        appointmentReminder.setCycleCount(Timeline.INDEFINITE);
        appointmentReminder.play();
        setupTimeZone();
        Set_Customer_Values();
        Set_Appointment_Values();

        setupViewToggleListener();
    }

    private void setupTimeZone() {
        LocalDateTime currentLocalTime = LocalDateTime.now();
        String Time_Layout = currentLocalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        User_Local_Time.setText("The local time is: " + Time_Layout);

        ZoneId Time_ET = ZoneId.of("America/New_York");
        ZonedDateTime ET_Current = ZonedDateTime.now(Time_ET);
        String timeLayout = ET_Current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z"));
        Eastern_Local_Time.setText("The time in EST is: " + timeLayout);
    }

    private void Set_Customer_Values() {
        Customer_ID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        Customer_Name.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        Customer_Address.setCellValueFactory(new PropertyValueFactory<>("Customer_Address"));
        Customer_ZIP_Code.setCellValueFactory(new PropertyValueFactory<>("Customer_ZIP"));
        Customer_Phone.setCellValueFactory(new PropertyValueFactory<>("Customer_Phone"));
        Customer_Division.setCellValueFactory(new PropertyValueFactory<>("Division_ID"));
        Customer_List_Table.setItems(Customers_Database.getList_Of_Customers());
    }

    private void Set_Appointment_Values() {
        Appointment_ID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        Appointment_Title.setCellValueFactory(new PropertyValueFactory<>("Appointment_Title"));
        Appointment_Description.setCellValueFactory(new PropertyValueFactory<>("Appointment_Description"));
        Appointment_Location.setCellValueFactory(new PropertyValueFactory<>("Appointment_Location"));
        Appointment_Contact.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));
        Appointment_Type.setCellValueFactory(new PropertyValueFactory<>("Appointment_Type"));
        Appointment_Start.setCellValueFactory(new PropertyValueFactory<>("Appointment_Start"));
        Appointment_End.setCellValueFactory(new PropertyValueFactory<>("Appointment_End"));
        Associated_Customer_ID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        Associated_User_ID.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
        Appointment_List_Table.setItems(Appointments_Database.getEveryAppointment());
    }

    private void setupViewToggleListener() {
        Toggle_The_View.selectedToggleProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection == View_All) {
                Appointment_List_Table.setItems(Appointments_Database.getEveryAppointment());
            } else if (newSelection == View_By_Month) {
                Appointment_List_Table.setItems(Appointments_Database.getAppointments_By_Month());
            } else if (newSelection == View_By_Week) {
                Appointment_List_Table.setItems(Appointments_Database.getAppointments_By_Week());
            }
        });
    }

    private void Upcoming_Appointments() {
        ObservableList<Appointments> soonAppointments = Appointments_Database.Upcoming_Appointments();
        LocalDateTime currentTime = LocalDateTime.now();

        soonAppointments.stream()
                .map(appointment -> {
                    LocalDateTime startTime = appointment.getAppointment_Start();
                    long timeDifference = ChronoUnit.MINUTES.between(currentTime, startTime);
                    return new SimpleEntry<>(appointment, timeDifference);
                })
                .filter(entry -> entry.getValue() > 0 && entry.getValue() <= 15)
                .forEach(entry -> {
                    Appointments appointment = entry.getKey();
                    long timeDifference = entry.getValue();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    String message = "You have an upcoming appointment in " + timeDifference + " minutes.\n\n" +
                            "Details:\n" +
                            "Title: " + appointment.getAppointment_Title() + "\n" +
                            "ID: " + appointment.getAppointment_ID() + "\n" +
                            "Start Time: " + appointment.getAppointment_Start() + "\n";
                    alert.setContentText(message);

                    alert.setTitle("Upcoming Appointment Reminder");
                    alert.setHeaderText("Reminder!");
                    alert.show();
                });

        if (soonAppointments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "There are no appointments within the next 15 minutes");
            alert.setTitle("No Upcoming Appointments");
            alert.show();
        }
    }

    public void AddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddCustomer.fxml")));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Customer Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void ModifyCustomer(ActionEvent actionEvent) throws IOException {

        Selected_Customer = Customer_List_Table.getSelectionModel().getSelectedItem();

        if (Selected_Customer != null) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyCustomer.fxml")));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Edit Customer Menu");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have not selected a customer to edit yet.");
            alert.setTitle("No customer selected to edit");
            alert.showAndWait();
        }
    }

    public void DeleteCustomer() {
        Customers Chosen_Customer = Customer_List_Table.getSelectionModel().getSelectedItem();

        if (Chosen_Customer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action will delete the selected customer and any associated appointments - do you wish to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Customers_Database.Remove_Customer(Chosen_Customer.getCustomer_ID());
                Customer_List_Table.setItems(Customers_Database.getList_Of_Customers());
                Appointment_List_Table.setItems(Appointments_Database.getEveryAppointment());
                Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, "Customer " + Chosen_Customer.getCustomer_Name() + " has been deleted successfully.");
                newAlert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have not selected a customer to delete yet.");
            alert.setTitle("No Customer Selected to Delete");
            alert.showAndWait();
        }
    }

    public static Customers getModifyCustomer() {

        return Selected_Customer;
    }

    public void AddAppointment(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddAppointment.fxml")));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Appointment Menu");
        stage.setScene(scene);
        stage.show();
    }

    public void ModifyAppointment(ActionEvent actionEvent) throws IOException{

        Selected_Appointment = Appointment_List_Table.getSelectionModel().getSelectedItem();

        if (Selected_Appointment != null) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModifyAppointment.fxml")));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Edit Appointment Menu");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have not selected an appointment to edit yet.");
            alert.setTitle("No appointment selected to edit");
            alert.showAndWait();
        }
    }

    public void DeleteAppointment() {
        Appointments Chosen_Appointment = Appointment_List_Table.getSelectionModel().getSelectedItem();

        if(Chosen_Appointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action will delete the selected appointments - do you wish to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Appointments_Database.Remove_Appointment(Chosen_Appointment.getAppointment_ID());
                Appointment_List_Table.setItems(Appointments_Database.getEveryAppointment());
                Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment ID " + Chosen_Appointment.getAppointment_ID() + " which had a type of " + Chosen_Appointment.getAppointment_Type() + " has been deleted successfully.");
                newAlert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have not select an appointment to delete yet.");
            alert.setTitle("No Appointment Selected to Delete");
            alert.showAndWait();
        }
    }

    public static Appointments getModifyAppointment() {

        return Selected_Appointment;
    }

    public void Reports(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Reports.fxml")));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY(0);

        Scene scene = new Scene(root);
        stage.setTitle("Application Reports Menu");
        stage.setScene(scene);
        stage.show();
    }


    public void Logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}

