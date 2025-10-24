package main.com.banking.Controller;

import main.com.banking.service.Bank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Bank bank;

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please enter both username and password.");
            return;
        }

        // Simple authentication (for demo)
        if (username.equals("admin") && password.equals("1234")) {
            loadMainMenu();
        } else {
            System.out.println("Invalid login credentials.");
        }
    }

    private void loadMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();

            // Pass bank instance to main menu controller
            MainMenuController controller = loader.getController();
            controller.setBank(bank);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Clutch Mandem Bank - Main Menu");
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

