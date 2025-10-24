package main.com.banking.Controller;

import main.com.banking.service.Bank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainMenuController {
    private Bank bank;

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @FXML
    private void handleCustomerManagement() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerRegistration.fxml"));
            Parent root = loader.load();

            CustomerRegistrationController controller = loader.getController();
            controller.setBank(bank);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Customer Registration - Clutch Mandem Bank");
            stage.show();

            closeCurrentWindow();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot open customer registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAccountManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccountManagement.fxml"));
            Parent root = loader.load();

            AccountManagementController controller = loader.getController();
            controller.setBank(bank);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Account Management - Clutch Mandem Bank");
            stage.show();

            closeCurrentWindow();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot open account management: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Transaction.fxml"));
            Parent root = loader.load();

            TransactionController controller = loader.getController();
            controller.setBank(bank);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Transactions - Clutch Mandem Bank");
            stage.show();

            closeCurrentWindow();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot open transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Reports.fxml"));
            Parent root = loader.load();

            ReportsController controller = loader.getController();
            controller.setBank(bank);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Reports - Clutch Mandem Bank");
            stage.show();

            closeCurrentWindow();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot open reports: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setBank(bank);

            Stage stage = (Stage) getCurrentStage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Clutch Mandem Bank - Login");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) getCurrentStage();
        if (stage != null) {
            stage.close();
        }
    }

    private javafx.stage.Window getCurrentStage() {
        return javafx.stage.Window.getWindows().stream()
                .filter(javafx.stage.Window::isShowing)
                .findFirst()
                .orElse(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}