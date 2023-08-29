package Controller;

import DAO.Countries_Database;
import DAO.Customers_Database;
import DAO.FLD_Database;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Model.Countries;
import Model.First_Divisions;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This controller class manages the addition of new customer information.
 * It handles user interactions and inserts customer data into the database.
 *
 * @author      Troy Jones
 */
public class AddCustomerController implements Initializable {

    public TextField Customer_ID, Customer_Name, Customer_Phone, Customer_Address, Customer_ZIP_Code;

    public ComboBox<Countries> Customer_Country;

    public ComboBox<First_Divisions> Customer_Division_Group;

    public Button Save_New_Customer, Cancel_Add_Customer;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCountryComboBox();
        setupDivisionComboBox();
    }

    private void setupCountryComboBox() {
        ObservableList<Countries> allCountries = Countries_Database.getList_Of_Countries();
        Customer_Country.setItems(allCountries);
        Customer_Country.setPromptText("Country");
    }

    private void setupDivisionComboBox() {

        Customer_Division_Group.setPromptText("State/Province");
    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        try {
            if (areFieldsValid()) {
                String customerName = Customer_Name.getText();
                String address = Customer_Address.getText();
                String postalCode = Customer_ZIP_Code.getText();
                String phoneNumber = Customer_Phone.getText();
                int divisionId = Customer_Division_Group.getSelectionModel().getSelectedItem().getDivision_id();

                Customers_Database.Add_New_Customer(customerName, address, postalCode, phoneNumber, divisionId);
                MainScreenLoad(actionEvent);
            }
        } catch (Exception e) {
            showAlert("Warning Message:", "A selection must be made in the drop down menu!");
        }
    }

    private boolean areFieldsValid() {
        Map<TextField, ValidationData> fieldValidations = Map.of(
                Customer_Name, new ValidationData("Full Name", 35),
                Customer_Phone, new ValidationData("Phone number", 15),
                Customer_Address, new ValidationData("Address", 125),
                Customer_ZIP_Code, new ValidationData("Zip Code", 10)
        );

        for (Map.Entry<TextField, ValidationData> entry : fieldValidations.entrySet()) {
            TextField field = entry.getKey();
            ValidationData validationData = entry.getValue();
            if (!validateTextField(field, validationData.fieldDescription, validationData.maxLength)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unused")
    public void Select_Division(MouseEvent actionEvent) {
        Countries Country_Chosen = Customer_Country.getValue();
        if (Country_Chosen != null) {
            switch (Country_Chosen.toString()) {
                case "U.S" -> Customer_Division_Group.setItems(FLD_Database.getUSA_Divisions());
                case "Canada" -> Customer_Division_Group.setItems(FLD_Database.getCanada_Divisions());
                case "UK" -> Customer_Division_Group.setItems(FLD_Database.getUK_Divisions());
            }
        } else {
            showAlert("Select a country first", "Please select a country to continue");
        }
    }
    private boolean validateTextField(TextField textField, String fieldDescription, int CharacterLimit) {
        String content = textField.getText();


        if (content.isEmpty()) {
            showAlert(fieldDescription + " Warning", fieldDescription + " must not be blank.");
            return false;
        }

        if (content.length() > CharacterLimit) {
            showAlert("Excessive characters!", fieldDescription + " must be " + CharacterLimit + " characters or less. The current length is " + content.length());
            return false;
        }

        if ("Full Name".equals(fieldDescription) && !content.matches("^[a-zA-Z ]+$")) {
            showAlert("Invalid Name", fieldDescription + " must only contain letters. Numbers and special characters are not allowed.");
            return false;
        }

        // Specific validation for phone numbers
        if ("Phone number".equals(fieldDescription) && !content.matches("^[0-9-]*$")) {
            showAlert("Invalid Format", fieldDescription + " must only contain numbers and hyphens.");
            return false;
        }

        if ("Zip Code".equals(fieldDescription) && !content.matches("^[0-9]+$")) {
            showAlert("Invalid Format", fieldDescription + " must only contain numbers.");
            return false;
        }

        return true;
    }

    private static class ValidationData {
        String fieldDescription;
        int maxLength;

        ValidationData(String fieldDescription, int maxLength) {
            this.fieldDescription = fieldDescription;
            this.maxLength = maxLength;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void MainScreenLoad(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }





}
