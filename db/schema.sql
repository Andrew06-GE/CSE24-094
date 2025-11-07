-- Customers table
CREATE TABLE customers (
                                         customer_id VARCHAR(50) PRIMARY KEY,
    first_name VARCHAR(100),
    surname VARCHAR(100),
    company_name VARCHAR(200),
    address VARCHAR(500) NOT NULL,
    date_of_birth DATE,
    is_corporate BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Accounts table
CREATE TABLE accounts (
                                        account_number VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    branch VARCHAR(100) NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    employer_name VARCHAR(200),
    employer_address VARCHAR(500),
    opened_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
    );

-- Transactions table
CREATE TABLE transactions (
                                            transaction_id VARCHAR(50) PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(500),
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
    );