package main.com.banking;

import main.com.banking.service.Bank;
import main.com.banking.db.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Bank bank;

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("=== Clutch Mandem Bank System ===");

            // Test database connection
            DatabaseConnection.testConnection();


            bank = new Bank();
            System.out.println("✓ Using Database-backed Bank Service");

            // Load login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            // Pass bank to controller
            main.com.banking.Controller.LoginController controller = loader.getController();
            controller.setBank(bank);

            primaryStage.setTitle("Clutch Mandem Bank - Login");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();

            System.out.println("✓ Application started successfully");

        } catch (Exception e) {
            System.err.println("✗ Error starting application: " + e.getMessage());
            e.printStackTrace();

            // Simple error fallback
            showErrorDialog("Startup Error", "Failed to start application: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
        System.out.println("Application stopped");
    }

    private void showErrorDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}