package dao;

import model.BorrowRecord;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDAO {

    /** All records — for ADMIN view */
    public List<BorrowRecord> findAll() throws Exception {
        return query("SELECT r.RecordID, r.UserID, r.BookID, r.BorrowDate, r.DueDate, r.ReturnDate, r.Status," +
                     " u.FullName, b.Title FROM BorrowRecords r" +
                     " JOIN Users u ON r.UserID = u.UserID" +
                     " JOIN Books b ON r.BookID = b.BookID" +
                     " ORDER BY r.BorrowDate DESC", -1);
    }

    /** Records for one user — for STUDENT view (always uses userId from session, never URL) */
    public List<BorrowRecord> findByUserId(int userId) throws Exception {
        return query("SELECT r.RecordID, r.UserID, r.BookID, r.BorrowDate, r.DueDate, r.ReturnDate, r.Status," +
                     " u.FullName, b.Title FROM BorrowRecords r" +
                     " JOIN Users u ON r.UserID = u.UserID" +
                     " JOIN Books b ON r.BookID = b.BookID" +
                     " WHERE r.UserID = ? ORDER BY r.BorrowDate DESC", userId);
    }

    public void insert(BorrowRecord record) throws Exception {
        String sql = "INSERT INTO BorrowRecords (UserID, BookID, BorrowDate, DueDate, Status) VALUES (?,?,?,?,'BORROWING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, record.getUserId());
            ps.setInt(2, record.getBookId());
            ps.setDate(3, Date.valueOf(record.getBorrowDate()));
            ps.setDate(4, Date.valueOf(record.getDueDate()));
            ps.executeUpdate();
        }
    }

    private List<BorrowRecord> query(String sql, int userId) throws Exception {
        List<BorrowRecord> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userId >= 0) ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BorrowRecord r = new BorrowRecord();
                    r.setRecordId(rs.getInt("RecordID"));
                    r.setUserId(rs.getInt("UserID"));
                    r.setBookId(rs.getInt("BookID"));
                    r.setBorrowDate(rs.getDate("BorrowDate").toLocalDate());
                    r.setDueDate(rs.getDate("DueDate").toLocalDate());
                    Date ret = rs.getDate("ReturnDate");
                    if (ret != null) r.setReturnDate(ret.toLocalDate());
                    r.setStatus(rs.getString("Status"));
                    r.setUserFullName(rs.getString("FullName"));
                    r.setBookTitle(rs.getString("Title"));
                    list.add(r);
                }
            }
        }
        return list;
    }
}
