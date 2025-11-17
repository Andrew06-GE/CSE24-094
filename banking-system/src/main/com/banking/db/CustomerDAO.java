package main.com.banking.db;

import main.com.banking.model.Customer;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public void saveCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (customer_id, first_name, surname, company_name, address, date_of_birth, is_corporate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerId());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getSurname());
            stmt.setString(4, customer.getCompanyName());
            stmt.setString(5, customer.getAddress());

            if (customer.getDateOfBirth() != null) {
                stmt.setDate(6, Date.valueOf(customer.getDateOfBirth()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.setBoolean(7, customer.isCorporate());
            stmt.executeUpdate();

            System.out.println("Customer saved: " + customer.getCustomerId());
        }
    }

    public Customer findCustomerById(String customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null;
    }

    public List<Customer> findAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers ORDER BY created_at DESC";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        String customerId = rs.getString("customer_id");
        String firstName = rs.getString("first_name");
        String surname = rs.getString("surname");
        String companyName = rs.getString("company_name");
        String address = rs.getString("address");
        Date dateOfBirth = rs.getDate("date_of_birth");
        boolean isCorporate = rs.getBoolean("is_corporate");

        LocalDate localDateOfBirth = dateOfBirth != null ? dateOfBirth.toLocalDate() : null;

        // Use the database constructor
        return new Customer(customerId, firstName, surname, companyName, address, localDateOfBirth, isCorporate);
    }
}