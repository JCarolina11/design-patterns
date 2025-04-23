package com.example.patterns_banking.repositories;

import com.example.patterns_banking.models.Account;
import com.example.patterns_banking.models.Customer;

import java.sql.*;

public class AccountRepository {

    private static final String DB_URL = "jdbc:h2:mem:patterns-banking";
    private static final String DB_USER = "sa";
    private static final String DB_PW = "";

    private static final String CREATE_TABLE_SQL =
        "CREATE TABLE accounts (" +
        "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
        "number VARCHAR(255) NOT NULL UNIQUE, " +
        "type VARCHAR(50) NOT NULL, " +
        "balance DECIMAL(19,2), " +
        "is_active BOOLEAN DEFAULT TRUE" +
        ");";

    private static final String INSERT_SQL = "INSERT INTO accounts (number, type, balance, is_active) VALUES (?, ?, ?, ?)";

    private static AccountRepository instance;

    private AccountRepository() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
    }

    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    public Account save(Account account) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, account.getNumber());
                pstmt.setString(2, account.getType().name());
                pstmt.setBigDecimal(3, account.getBalance());
                pstmt.setBoolean(4, account.getIsActive());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating account failed, no rows affected.");
                }

                try(ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        account.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating account failed, no ID obtained.");
                    }
                }
            }

            return account;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account", e);
        }
    }
}
