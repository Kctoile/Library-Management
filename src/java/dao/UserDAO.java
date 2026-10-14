package dao;

import model.User;
import java.sql.*;

public class UserDAO {

    public User findByUsername(String username) throws Exception {
        String sql = "SELECT UserID, Username, PasswordHash, FullName, Role FROM Users WHERE Username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("UserID"));
                    u.setUsername(rs.getString("Username"));
                    u.setPasswordHash(rs.getString("PasswordHash"));
                    u.setFullName(rs.getString("FullName"));
                    u.setRole(rs.getString("Role"));
                    return u;
                }
            }
        }
        return null;
    }
}
