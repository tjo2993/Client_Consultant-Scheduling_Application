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
import javafx.stage.Stage;
import Model.Countries;
import Model.Customers;
import Model.First_Divisions;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller class is responsible for managing the modification of customer information.
 * It handles user interactions and updates customer data accordingly.
 * Upon exit the user is sent back to the main menu.
 *
 * @author      Troy Jones
 */
public class ModifyCustomerController implements Initializable {
    public TextField Customer_ID, Customer_Name, Customer_Phone, Customer_Address, Customer_Postal_Code;

    public ComboBox<Countries> Customer_Country;
    public ComboBox<First_Divisions> Customer_Division;

    public Button Save_Customer_Changes, Cancel_Customer_Changes;

    private First_Divisions Select_A_Division;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Customers selectedModifyCustomer = MainScreenController.getModifyCustomer();
        ObservableList<Countries> countries = Countries_Database.getList_Of_Countries();

        try {
            Customer_ID.setText(String.valueOf(selectedModifyCustomer.getCustomer_ID()));
            Customer_Name.setText(selectedModifyCustomer.getCustomer_Name());
            Customer_Phone.setText(selectedModifyCustomer.getCustomer_Phone());
            Customer_Address.setText(selectedModifyCustomer.getCustomer_Address());
            Customer_Postal_Code.setText(selectedModifyCustomer.getCustomer_ZIP());

            for (First_Divisions fld : FLD_Database.getAll_Divisions()) {
                if (selectedModifyCustomer.getDivision_ID() == fld.getDivision_id()) {
                    this.Select_A_Division = fld;
                    break;
                }
            }
            Customer_Division.setValue(Select_A_Division);

            int divisionID = Select_A_Division.getDivision_id();
            Countries selectedCountry;
            if (divisionID <= 54) {
                selectedCountry = countries.get(0);
            } else if (divisionID >= 60 && divisionID <= 72) {
                selectedCountry = countries.get(2);
            } else if (divisionID >= 101 && divisionID <= 104) {
                selectedCountry = countries.get(1);
            } else {
                throw new Exception("Invalid division ID");
            }
            Customer_Country.setItems(countries);
            Customer_Country.setValue(selectedCountry);

            switch (selectedCountry.toString()) {
                case "U.S" -> Customer_Division.setItems(FLD_Database.getUSA_Divisions());
                case "UK" -> Customer_Division.setItems(FLD_Database.getUK_Divisions());
                case "Canada" -> Customer_Division.setItems(FLD_Database.getCanada_Divisions());
                default -> throw new Exception("Invalid country name");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void Save_Customer_Changes(ActionEvent actionEvent) {
        try {
            int customerId = Integer.parseInt(Customer_ID.getText());
            String customerName = Customer_Name.getText();
            String address = Customer_Address.getText();
            String postalCode = Customer_Postal_Code.getText();
            String phoneNumber = Customer_Phone.getText();
            int divisionId = Customer_Division.getSelectionModel().getSelectedItem().getDivision_id();

            if (Check_Fields(customerName, address, postalCode, phoneNumber)) {
                Customers_Database.Change_Customer_Info(customerName, address, postalCode, phoneNumber, divisionId, customerId);
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Warning", "Drop down menu's cannot be unselected");
        }
    }

    private boolean Check_Fields(String Customer_Name, String Customer_Address, String Customer_ZIP, String Customer_Phone) {

        // Name checks
        if (Customer_Name.isEmpty() || !Customer_Name.matches("^[A-Za-z ]+$") || Customer_Name.length() > 25) {
            showAlert(Alert.AlertType.ERROR, "Name Error", "Name can only contain letters and whitespace, cannot be left blank, and must be 25 characters or less.");
            return false;
        }

        // Phone number checks
        if (Customer_Phone.isEmpty() || !Customer_Phone.matches("^[0-9()-]+$") || Customer_Phone.length() > 15) {
            showAlert(Alert.AlertType.ERROR, "Phone Number Error", "Phone number must contain numbers, parentheses, or hyphens, cannot be left blank, and be 15 digits or less.");
            return false;
        }

        // Address checks
        if (Customer_Address.isEmpty() || !Customer_Address.matches("^[A-Za-z0-9 ,.-]+$") || Customer_Address.length() > 125) {
            showAlert(Alert.AlertType.ERROR, "Address Error", "Address cannot be left blank, must only contain letters, numbers, spaces, commas, periods, or hyphens, and be no more than 125 characters.");
            return false;
        }

        // Postal code checks
        if (Customer_ZIP.isEmpty() || !Customer_ZIP.matches("^[0-9-]+$") || Customer_ZIP.length() > 15) {
            showAlert(Alert.AlertType.ERROR, "Postal Code Error", "Postal code cannot be left blank, must only contain numbers or hyphens, and be no more than 15 characters.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType appointment_Type, String title, String content) {
        Alert alert = new Alert(appointment_Type, content);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public void Cancel_Changes(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void Division_Selected() {
        Countries selectedCountry = Customer_Country.getValue();

        if (selectedCountry == null) {
            showCountryNotSelectedAlert();
            return;
        }

        ObservableList<First_Divisions> divisions = switch (selectedCountry.toString()) {
            case "U.S" -> FLD_Database.getUSA_Divisions();
            case "Canada" -> FLD_Database.getCanada_Divisions();
            case "UK" -> FLD_Database.getUK_Divisions();
            default -> null;
        };

        if (divisions != null) {
            Customer_Division.setItems(divisions);
        }
    }

    private void showCountryNotSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have not selected a country yet.");
        alert.setTitle("No Country Selected");
        alert.showAndWait();
    }
}
