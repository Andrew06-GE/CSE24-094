package main.com.banking.Controller;

import main.com.banking.service.Bank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import java.time.LocalDate;

public class CustomerRegistrationController {

    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private TextField addressField;
    @FXML private DatePicker dateOfBirthPicker;
    @FXML private TextField companyNameField;
    @FXML private TextField companyAddressField;
    @FXML private RadioButton individualRadio;
    @FXML private RadioButton corporateRadio;

    private ToggleGroup customerTypeGroup;
    private Bank bank;

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @FXML
    private void initialize() {
        customerTypeGroup = new ToggleGroup();
        individualRadio.setToggleGroup(customerTypeGroup);
        corporateRadio.setToggleGroup(customerTypeGroup);
        individualRadio.setSelected(true);
        toggleCustomerFields();
    }

    @FXML
    private void toggleCustomerFields() {
        boolean isIndividual = individualRadio.isSelected();
        firstNameField.setDisable(!isIndividual);
        surnameField.setDisable(!isIndividual);
        dateOfBirthPicker.setDisable(!isIndividual);
        companyNameField.setDisable(isIndividual);
        companyAddressField.setDisable(isIndividual);

        // Clear fields when switching types
        if (isIndividual) {
            companyNameField.clear();
            companyAddressField.clear();
        } else {
            firstNameField.clear();
            surnameField.clear();
            dateOfBirthPicker.setValue(null);
        }
    }

    @FXML
    private void handleRegisterIndividual() {
        try {
            String firstName = firstNameField.getText().trim();
            String surname = surnameField.getText().trim();
            String address = addressField.getText().trim();
            LocalDate dateOfBirth = dateOfBirthPicker.getValue();

            // Validation
            if (firstName.isEmpty()) {
                throw new IllegalArgumentException("First name is required");
            }
            if (surname.isEmpty()) {
                throw new IllegalArgumentException("Surname is required");
            }
            if (address.isEmpty()) {
                throw new IllegalArgumentException("Address is required");
            }
            if (dateOfBirth == null) {
                throw new IllegalArgumentException("Date of birth is required");
            }
            if (dateOfBirth.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }

            bank.registerIndividualCustomer(firstName, surname, address, dateOfBirth);
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Individual customer registered successfully!\n" +
                            "Name: " + firstName + " " + surname + "\n" +
                            "Address: " + address + "\n" +
                            "Date of Birth: " + dateOfBirth);
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", e.getMessage());
        }
    }

    @FXML
    private void handleRegisterCorporate() {
        try {
            String companyName = companyNameField.getText().trim();
            String companyAddress = companyAddressField.getText().trim();

            // Validation
            if (companyName.isEmpty()) {
                throw new IllegalArgumentException("Company name is required");
            }
            if (companyAddress.isEmpty()) {
                throw new IllegalArgumentException("Company address is required");
            }

            bank.registerCorporateCustomer(companyName, companyAddress);
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Corporate customer registered successfully!\n" +
                            "Company: " + companyName + "\n" +
                            "Address: " + companyAddress);
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", e.getMessage());
        }
    }

    @FXML
    private void handleBackToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setBank(bank);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Clutch Mandem Bank - Main Menu");
            stage.show();

            closeCurrentWindow();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot open main menu: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    private void clearFields() {
        firstNameField.clear();
        surnameField.clear();
        addressField.clear();
        dateOfBirthPicker.setValue(null);
        companyNameField.clear();
        companyAddressField.clear();
        individualRadio.setSelected(true);
        toggleCustomerFields();
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}