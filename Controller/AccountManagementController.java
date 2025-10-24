package main.com.banking.Controller;

import main.com.banking.model.Account;
import main.com.banking.service.Bank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.math.BigDecimal;

public class AccountManagementController {
    @FXML private TextArea outputArea;

    private Bank bank;

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @FXML
    private void handleOpenSavingsAccount() {
        try {
            String customerId = showInputDialog("Enter Customer ID:");
            if (customerId == null || customerId.isEmpty()) return;


            if (bank.getCustomer(customerId) == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Customer not found: " + customerId);
                return;
            }

            String branch = showInputDialog("Enter Branch:");
            if (branch == null || branch.isEmpty()) return;

            String amountStr = showInputDialog("Enter Initial Deposit:");
            if (amountStr == null || amountStr.isEmpty()) return;

            BigDecimal amount = new BigDecimal(amountStr);


            Account account = bank.openSavingsAccount(customerId, branch, amount);

            outputArea.appendText("Savings Account Opened:\n");
            outputArea.appendText("   Account: " + account.getAccountNumber() + "\n");
            outputArea.appendText("   Customer: " + account.getOwner().getDisplayName() + "\n");
            outputArea.appendText("   Customer ID: " + account.getOwner().getCustomerId() + "\n");
            outputArea.appendText("   Balance: BWP " + account.getBalance() + "\n\n");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenInvestmentAccount() {
        try {
            String customerId = showInputDialog("Enter Customer ID:");
            if (customerId == null || customerId.isEmpty()) return;

            // Use getCustomer instead of findCustomerById
            if (bank.getCustomer(customerId) == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Customer not found: " + customerId);
                return;
            }

            String branch = showInputDialog("Enter Branch:");
            if (branch == null || branch.isEmpty()) return;

            String amountStr = showInputDialog("Enter Initial Deposit (min BWP 500):");
            if (amountStr == null || amountStr.isEmpty()) return;

            BigDecimal amount = new BigDecimal(amountStr);


            Account account = bank.openInvestmentAccount(customerId, branch, amount);

            outputArea.appendText("Investment Account Opened:\n");
            outputArea.appendText("   Account: " + account.getAccountNumber() + "\n");
            outputArea.appendText("   Customer: " + account.getOwner().getDisplayName() + "\n");
            outputArea.appendText("   Customer ID: " + account.getOwner().getCustomerId() + "\n");
            outputArea.appendText("   Balance: BWP " + account.getBalance() + "\n\n");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenChequeAccount() {
        try {
            String customerId = showInputDialog("Enter Customer ID :");
            if (customerId == null || customerId.isEmpty()) return;


            if (bank.getCustomer(customerId) == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Customer not found: " + customerId);
                return;
            }

            String branch = showInputDialog("Enter Branch:");
            if (branch == null || branch.isEmpty()) return;

            String amountStr = showInputDialog("Enter Initial Deposit:");
            if (amountStr == null || amountStr.isEmpty()) return;

            String employer = showInputDialog("Enter Employer Name:");
            if (employer == null || employer.isEmpty()) return;

            BigDecimal amount = new BigDecimal(amountStr);


            Account account = bank.openChequeAccount(customerId, branch, amount, employer, "");

            outputArea.appendText("Cheque Account Opened:\n");
            outputArea.appendText("   Account: " + account.getAccountNumber() + "\n");
            outputArea.appendText("   Customer: " + account.getOwner().getDisplayName() + "\n");
            outputArea.appendText("   Customer ID: " + account.getOwner().getCustomerId() + "\n");
            outputArea.appendText("   Employer: " + employer + "\n");
            outputArea.appendText("   Balance: BWP " + account.getBalance() + "\n\n");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewCustomerAccounts() {
        try {
            String customerId = showInputDialog("Enter Customer ID:");
            if (customerId == null || customerId.isEmpty()) return;


            String accountsInfo = bank.viewCustomerAccounts(customerId);
            outputArea.appendText(accountsInfo + "\n");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setBank(bank);

            Stage stage = (Stage) outputArea.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot return to main menu: " + e.getMessage());
        }
    }

    private String showInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        return dialog.showAndWait().orElse("");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}