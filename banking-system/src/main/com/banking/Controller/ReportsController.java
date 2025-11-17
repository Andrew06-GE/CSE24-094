package main.com.banking.Controller;

import main.com.banking.service.Bank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReportsController {

    @FXML
    private TextArea reportArea;

    private Bank bank; // Changed from Object to Bank

    public void setBank(Bank bank) { // Changed parameter to Bank
        this.bank = bank;
    }

    @FXML
    private void handleCustomerReport() {
        try {
            String customerReport = bank.generateCustomerReport();
            reportArea.setText("CUSTOMER REPORT\n" + "=".repeat(50) + "\n\n" + customerReport);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot generate customer report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAccountsSummary() {
        try {
            String accountSummary = bank.generateAccountsSummary();
            reportArea.setText("ACCOUNTS SUMMARY REPORT\n" + "=".repeat(50) + "\n\n" + accountSummary);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot generate accounts summary: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTransactionReport() {
        try {
            String transactionReport = bank.generateTransactionReport();
            reportArea.setText("TRANSACTION REPORT\n" + "=".repeat(50) + "\n\n" + transactionReport);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot generate transaction report: " + e.getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClearReport() {
        reportArea.clear();
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) reportArea.getScene().getWindow();
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