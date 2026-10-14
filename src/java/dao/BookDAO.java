package dao;

import model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> findAll(String keyword, String category, boolean availableOnly) throws Exception {
        // Build query dynamically — ponytail: StringBuilder sufficient, no query builder needed
        StringBuilder sql = new StringBuilder(
            "SELECT BookID, Title, Author, Category, Quantity, PublishedYear FROM Books WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (LOWER(Title) LIKE ? OR LOWER(Author) LIKE ? OR LOWER(Category) LIKE ?)");
            String kw = "%" + keyword.trim().toLowerCase() + "%";
            params.add(kw); params.add(kw); params.add(kw);
        }
        if (category != null && !category.isBlank()) {
            sql.append(" AND Category = ?");
            params.add(category.trim());
        }
        if (availableOnly) {
            sql.append(" AND Quantity > 0");
        }
        sql.append(" ORDER BY Title");

        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) books.add(map(rs));
            }
        }
        return books;
    }

    public Book findById(int bookId) throws Exception {
        String sql = "SELECT BookID, Title, Author, Category, Quantity, PublishedYear FROM Books WHERE BookID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public void insert(Book book) throws Exception {
        String sql = "INSERT INTO Books (Title, Author, Category, Quantity, PublishedYear) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setInt(4, book.getQuantity());
            ps.setInt(5, book.getPublishedYear());
            ps.executeUpdate();
        }
    }

    public void update(Book book) throws Exception {
        String sql = "UPDATE Books SET Title=?, Author=?, Category=?, Quantity=?, PublishedYear=? WHERE BookID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setInt(4, book.getQuantity());
            ps.setInt(5, book.getPublishedYear());
            ps.setInt(6, book.getBookId());
            ps.executeUpdate();
        }
    }

    /**
     * Delete only if no active (BORROWING) records exist for this book.
     * @throws IllegalStateException if book has active borrows
     */
    public void delete(int bookId) throws Exception {
        String checkSql = "SELECT COUNT(*) FROM BorrowRecords WHERE BookID = ? AND Status = 'BORROWING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setInt(1, bookId);
            try (ResultSet rs = check.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    throw new IllegalStateException("Không thể xóa sách đang được mượn.");
                }
            }
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM Books WHERE BookID = ?")) {
                del.setInt(1, bookId);
                del.executeUpdate();
            }
        }
    }

    /** All distinct categories for filter dropdown */
    public List<String> findAllCategories() throws Exception {
        List<String> cats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT Category FROM Books ORDER BY Category");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) cats.add(rs.getString(1));
        }
        return cats;
    }

    private Book map(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setBookId(rs.getInt("BookID"));
        b.setTitle(rs.getString("Title"));
        b.setAuthor(rs.getString("Author"));
        b.setCategory(rs.getString("Category"));
        b.setQuantity(rs.getInt("Quantity"));
        b.setPublishedYear(rs.getInt("PublishedYear"));
        return b;
    }
}
