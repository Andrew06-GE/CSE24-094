package main.com.banking.Controller;

import main.com.banking.service.Bank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.math.BigDecimal;

public class AccountManagementController {

    @FXML
    private TextArea outputArea;

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField branchField;

    @FXML
    private TextField initialBalanceField;

    @FXML
    private TextField employerNameField;

    @FXML
    private TextField employerAddressField;

    private Bank bank;

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @FXML
    private void handleOpenSavingsAccount() {
        try {
            // Get input values
            String customerId = customerIdField.getText().trim();
            String branch = branchField.getText().trim();
            String balanceText = initialBalanceField.getText().trim();

            // Validate inputs
            if (customerId.isEmpty() || branch.isEmpty() || balanceText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields: Customer ID, Branch, and Initial Balance");
                return;
            }

            BigDecimal initialBalance = new BigDecimal(balanceText);
            if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Initial balance cannot be negative");
                return;
            }

            // Create the account
            bank.openSavingsAccount(customerId, branch, initialBalance);

            outputArea.setText("SUCCESS: Savings Account Created\n" +
                    "==========================\n\n" +
                    "Customer ID: " + customerId + "\n" +
                    "Branch: " + branch + "\n" +
                    "Initial Balance: BWP " + initialBalance + "\n\n" +
                    "Account created successfully!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number for initial balance");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot create savings account: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenInvestmentAccount() {
        try {
            // Get input values
            String customerId = customerIdField.getText().trim();
            String branch = branchField.getText().trim();
            String balanceText = initialBalanceField.getText().trim();

            // Validate inputs
            if (customerId.isEmpty() || branch.isEmpty() || balanceText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields: Customer ID, Branch, and Initial Balance");
                return;
            }

            BigDecimal initialBalance = new BigDecimal(balanceText);
            if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Initial balance cannot be negative");
                return;
            }

            // Create the account
            bank.openInvestmentAccount(customerId, branch, initialBalance);

            outputArea.setText("SUCCESS: Investment Account Created\n" +
                    "=============================\n\n" +
                    "Customer ID: " + customerId + "\n" +
                    "Branch: " + branch + "\n" +
                    "Initial Balance: BWP " + initialBalance + "\n\n" +
                    "Account created successfully!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number for initial balance");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot create investment account: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenChequeAccount() {
        try {
            // Get input values
            String customerId = customerIdField.getText().trim();
            String branch = branchField.getText().trim();
            String balanceText = initialBalanceField.getText().trim();
            String employerName = employerNameField.getText().trim();
            String employerAddress = employerAddressField.getText().trim();

            // Validate inputs
            if (customerId.isEmpty() || branch.isEmpty() || balanceText.isEmpty() ||
                    employerName.isEmpty() || employerAddress.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields for cheque account");
                return;
            }

            BigDecimal initialBalance = new BigDecimal(balanceText);
            if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Initial balance cannot be negative");
                return;
            }

            // Create the account
            bank.openChequeAccount(customerId, branch, initialBalance, employerName, employerAddress);

            outputArea.setText("SUCCESS: Cheque Account Created\n" +
                    "==========================\n\n" +
                    "Customer ID: " + customerId + "\n" +
                    "Branch: " + branch + "\n" +
                    "Initial Balance: BWP " + initialBalance + "\n" +
                    "Employer: " + employerName + "\n" +
                    "Employer Address: " + employerAddress + "\n\n" +
                    "Account created successfully!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number for initial balance");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot create cheque account: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewCustomerAccounts() {
        try {
            String customerId = customerIdField.getText().trim();
            if (customerId.isEmpty()) {
                // Show all customers first
                String allCustomers = bank.viewAllCustomers();
                outputArea.setText("CUSTOMER ACCOUNTS VIEWER\n" +
                        "==========================\n\n" +
                        "Available Customers:\n" +
                        "-------------------\n" +
                        allCustomers +
                        "\n\nTo view specific customer accounts, enter Customer ID above and click this button again.");
            } else {
                // Show specific customer accounts
                String accountsReport = bank.viewCustomerAccounts(customerId);
                outputArea.setText("ACCOUNTS FOR CUSTOMER: " + customerId + "\n" +
                        "==============================\n\n" +
                        accountsReport);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot load customer accounts: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewAllCustomers() {
        try {
            String customerReport = bank.viewAllCustomers();
            outputArea.setText("ALL CUSTOMERS REPORT\n" +
                    "=====================\n\n" +
                    customerReport);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot load customers: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearOutput() {
        outputArea.clear();
    }

    @FXML
    private void handleClearInputFields() {
        customerIdField.clear();
        branchField.clear();
        initialBalanceField.clear();
        employerNameField.clear();
        employerAddressField.clear();
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

    private void closeCurrentWindow() {
        Stage stage = (Stage) outputArea.getScene().getWindow();
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