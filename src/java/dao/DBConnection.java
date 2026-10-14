package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * ponytail: single static connection helper — sufficient for assignment scope.
 * Upgrade to connection pool (HikariCP) when load testing matters.
 */
public class DBConnection {

    private static final String URL      = "jdbc:sqlserver://localhost:1433;databaseName=LibraryManagement;encrypt=false";
    private static final String USER     = "sa";
    private static final String PASSWORD = "123"; // Change to match your SQL Server setup

    // S1118: utility class — hide implicit public constructor
    private DBConnection() {}

    // S4925: Class.forName() removed — JDBC 4.0+ auto-loads drivers via ServiceLoader

    // S112: narrowed throws to SQLException
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * SHA-256 hash for password storage.
     * S4790: MD5 replaced with SHA-256 (not cryptographically weak).
     * S4719: StandardCharsets.UTF_8 replaces string literal "UTF-8".
     */
    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // S4790 fix
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8)); // S4719 fix
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) { // S112 fix: specific exception
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
