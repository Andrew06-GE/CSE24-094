package main.com.banking.Controller;

import main.com.banking.service.Bank;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.math.BigDecimal;

public class TransactionController {

    @FXML private TextField accountNumberField;
    @FXML private TextField amountField;
    @FXML private RadioButton depositRadio;
    @FXML private RadioButton withdrawRadio;
    @FXML private Label statusLabel;
    @FXML private TextArea outputArea;

    private ToggleGroup transactionTypeGroup;
    private Bank bank;

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @FXML
    private void initialize() {
        transactionTypeGroup = new ToggleGroup();
        depositRadio.setToggleGroup(transactionTypeGroup);
        withdrawRadio.setToggleGroup(transactionTypeGroup);
        depositRadio.setSelected(true);
    }


    @FXML
    private void processTransaction() {
        try {
            String accountNumber = accountNumberField.getText();
            BigDecimal amount = new BigDecimal(amountField.getText());

            if (depositRadio.isSelected()) {
                bank.deposit(accountNumber, amount);
                statusLabel.setText("Deposit successful: BWP " + amount);
                outputArea.appendText("Deposited BWP " + amount + " to account: " + accountNumber + "\n");
            } else if (withdrawRadio.isSelected()) {
                bank.withdraw(accountNumber, amount);
                statusLabel.setText("Withdrawal successful: BWP " + amount);
                outputArea.appendText("Withdrew BWP " + amount + " from account: " + accountNumber + "\n");
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            outputArea.appendText("Transaction failed: " + e.getMessage() + "\n");
        }
    }


    @FXML
    private void clearFields() {
        accountNumberField.clear();
        amountField.clear();
        depositRadio.setSelected(true);
        statusLabel.setText("");
    }

    @FXML
    private void handleDeposit() {
        try {
            String accountNumber = accountNumberField.getText();
            BigDecimal amount = new BigDecimal(amountField.getText());
            bank.deposit(accountNumber, amount);
            statusLabel.setText("Deposit successful: BWP " + amount);
            outputArea.appendText("Deposited BWP " + amount + " to account: " + accountNumber + "\n");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            outputArea.appendText("Deposit failed: " + e.getMessage() + "\n");
        }
    }

    @FXML
    private void handleWithdraw() {
        try {
            String accountNumber = accountNumberField.getText();
            BigDecimal amount = new BigDecimal(amountField.getText());
            bank.withdraw(accountNumber, amount);
            statusLabel.setText("Withdrawal successful: BWP " + amount);
            outputArea.appendText("Withdrew BWP " + amount + " from account: " + accountNumber + "\n");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            outputArea.appendText("Withdrawal failed: " + e.getMessage() + "\n");
        }
    }

    @FXML
    private void handleApplyInterest() {
        try {
            bank.applyMonthlyInterestToAll();
            statusLabel.setText("Monthly interest applied to all accounts");
            outputArea.appendText("Monthly interest applied to all accounts\n");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            outputArea.appendText("Interest application failed: " + e.getMessage() + "\n");
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

    private void closeCurrentWindow() {
        Stage stage = (Stage) statusLabel.getScene().getWindow();
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