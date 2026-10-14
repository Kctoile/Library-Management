package model;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String role; // "ADMIN" | "STUDENT"

    public User() {}

    public User(int userId, String username, String passwordHash, String fullName, String role) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
    }

    public int getUserId()             { return userId; }
    public String getUsername()        { return username; }
    public String getPasswordHash()    { return passwordHash; }
    public String getFullName()        { return fullName; }
    public String getRole()            { return role; }

    public void setUserId(int v)          { this.userId = v; }
    public void setUsername(String v)     { this.username = v; }
    public void setPasswordHash(String v) { this.passwordHash = v; }
    public void setFullName(String v)     { this.fullName = v; }
    public void setRole(String v)         { this.role = v; }
}
