package main.com.banking;

import main.com.banking.service.Bank;
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
            System.out.println("Starting Clutch Mandem Bank Application...");


            String fxmlPath = "/fxml/Login.fxml";
            System.out.println("Looking for FXML at: " + fxmlPath);

            if (getClass().getResource(fxmlPath) == null) {
                System.err.println("ERROR: FXML file not found at: " + fxmlPath);
                System.err.println("Available resources:");

                // Test different possible paths
                testPath("/fxml/Login.fxml");
                testPath("/fxml/login.fxml");
                testPath("fxml/Login.fxml");
                testPath("fxml/login.fxml");

                return;
            } else {
                System.out.println("SUCCESS: FXML file found!");
            }

            bank = new Bank();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            main.com.banking.Controller.LoginController controller = loader.getController();
            controller.setBank(bank);

            primaryStage.setTitle("Clutch Mandem Bank - Login");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.setResizable(false);
            primaryStage.show();

            System.out.println("Application started successfully!");

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void testPath(String path) {
        if (getClass().getResource(path) != null) {
            System.out.println("FOUND: " + path);
        } else {
            System.out.println("NOT FOUND: " + path);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}