package main.com.banking.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:./database/bankdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;
    private static boolean driverLoaded = false;
    private static boolean schemaInitialized = false;

    static {
        initializeDriver();
    }

    private static void initializeDriver() {
        try {
            Class.forName("org.h2.Driver");
            driverLoaded = true;
            System.out.println("✓ H2 Database Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ H2 Driver not found.");
            driverLoaded = false;
        }
    }

    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            throw new SQLException("H2 Driver not available.");
        }

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (!schemaInitialized) {
                initializeSchema();
            }
        }
        return connection;
    }

    private static void initializeSchema() {
        if (!driverLoaded) return;

        try (Statement stmt = connection.createStatement()) {

            // Use IF NOT EXISTS to handle tables that already exist
            stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "customer_id VARCHAR(50) PRIMARY KEY, " +
                    "first_name VARCHAR(100), " +
                    "surname VARCHAR(100), " +
                    "company_name VARCHAR(200), " +
                    "address VARCHAR(500) NOT NULL, " +
                    "date_of_birth DATE, " +
                    "is_corporate BOOLEAN NOT NULL DEFAULT FALSE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_number VARCHAR(50) PRIMARY KEY, " +
                    "customer_id VARCHAR(50) NOT NULL, " +
                    "account_type VARCHAR(20) NOT NULL, " +
                    "branch VARCHAR(100) NOT NULL, " +
                    "balance DECIMAL(15,2) NOT NULL DEFAULT 0.00, " +
                    "employer_name VARCHAR(200), " +
                    "employer_address VARCHAR(500), " +
                    "opened_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "transaction_id VARCHAR(50) PRIMARY KEY, " +
                    "account_number VARCHAR(50) NOT NULL, " +
                    "transaction_type VARCHAR(20) NOT NULL, " +
                    "amount DECIMAL(15,2) NOT NULL, " +
                    "balance_after DECIMAL(15,2) NOT NULL, " +
                    "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "description VARCHAR(500), " +
                    "FOREIGN KEY (account_number) REFERENCES accounts(account_number))");

            schemaInitialized = true;
            System.out.println("✓ Database schema ready");
            System.out.println("✓ Database file: ./database/bankdb.mv.db");

        } catch (SQLException e) {
            // If tables already exist, that's fine - just log it
            if (e.getMessage().contains("already exists")) {
                System.out.println("✓ Database tables already exist - ready to use");
                schemaInitialized = true;
            } else {
                System.err.println("✗ Database schema error: " + e.getMessage());
            }
        }
    }

    public static boolean isDriverAvailable() {
        return driverLoaded;
    }

    public static void testConnection() {
        if (!driverLoaded) {
            System.out.println("✗ H2 Driver not available");
            return;
        }

        try {
            Connection conn = getConnection();
            System.out.println("✓ Database connection test: SUCCESS");
            System.out.println("✓ Data will persist in: ./database/bankdb.mv.db");
            System.out.println("✓ Database is ready for operations");
            conn.close();
        } catch (SQLException e) {
            System.out.println("✗ Database connection test: FAILED - " + e.getMessage());
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}