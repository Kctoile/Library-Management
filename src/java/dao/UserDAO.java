package dao;

import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDAO {

    public User findByUsername(String username) throws Exception {
        String sql = "SELECT UserID, Username, PasswordHash, FullName, Role "
                + "FROM Users WHERE Username = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public User findById(int userId) throws Exception {
        String sql = "SELECT UserID, Username, PasswordHash, FullName, Role "
                + "FROM Users WHERE UserID = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public void insert(User user) throws Exception {
        String sql = "INSERT INTO Users (Username, PasswordHash, FullName, Role) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setUserId(keys.getInt(1));
                }
            }
        }
    }

    public boolean existsByUsername(String username) throws Exception {
        return findByUsername(username) != null;
    }

    private User mapRow(ResultSet rs) throws Exception {
        User u = new User();
        u.setUserId(rs.getInt("UserID"));
        u.setUsername(rs.getString("Username"));
        u.setPasswordHash(rs.getString("PasswordHash"));
        u.setFullName(rs.getString("FullName"));
        u.setRole(rs.getString("Role"));
        return u;
    }
}
