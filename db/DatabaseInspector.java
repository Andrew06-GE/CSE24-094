package main.com.banking.db;

import java.sql.*;

public class DatabaseInspector {

    public static void printDatabaseInfo() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            System.out.println("=== DATABASE INFORMATION ===");
            System.out.println("Database: " + metaData.getDatabaseProductName());
            System.out.println("Version: " + metaData.getDatabaseProductVersion());
            System.out.println("URL: " + metaData.getURL());

            // List all tables
            System.out.println("\n=== TABLES ===");
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            boolean hasTables = false;

            while (tables.next()) {
                hasTables = true;
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);

                // List columns for each table
                ResultSet columns = metaData.getColumns(null, null, tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    System.out.println("  - " + columnName + " (" + columnType + ")");
                }
                columns.close();
            }
            tables.close();

            if (!hasTables) {
                System.out.println("No tables found in database");
            }

        } catch (SQLException e) {
            System.err.println("Error inspecting database: " + e.getMessage());
        }
    }

    public static void printTableData(String tableName) {
        String sql = "SELECT * FROM " + tableName;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== " + tableName.toUpperCase() + " DATA ===");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();
            System.out.println("-".repeat(columnCount * 15));

            // Print data
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    System.out.print((value != null ? value : "NULL") + "\t");
                }
                System.out.println();
            }

            if (!hasData) {
                System.out.println("No data found in table: " + tableName);
            }

        } catch (SQLException e) {
            System.err.println("Error reading table " + tableName + ": " + e.getMessage());
        }
    }

    public static void printAllData() {
        printTableData("customers");
        printTableData("accounts");
        printTableData("transactions");
    }
}