package model;

import java.time.LocalDate;

public class BorrowRecord {
    private int recordId;
    private int userId;
    private int bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // null if not returned
    private String status;        // BORROWING | RETURNED | OVERDUE

    // Display helpers (joined from query)
    private String userFullName;
    private String bookTitle;

    public BorrowRecord() {}

    // Getters
    public int getRecordId()            { return recordId; }
    public int getUserId()              { return userId; }
    public int getBookId()              { return bookId; }
    public LocalDate getBorrowDate()    { return borrowDate; }
    public LocalDate getDueDate()       { return dueDate; }
    public LocalDate getReturnDate()    { return returnDate; }
    public String getStatus()           { return status; }
    public String getUserFullName()     { return userFullName; }
    public String getBookTitle()        { return bookTitle; }

    // Computed display status: if BORROWING and past due → show OVERDUE
    public String getDisplayStatus() {
        if ("BORROWING".equals(status) && dueDate != null && LocalDate.now().isAfter(dueDate)) {
            return "OVERDUE";
        }
        return status;
    }

    // Setters
    public void setRecordId(int v)              { this.recordId = v; }
    public void setUserId(int v)                { this.userId = v; }
    public void setBookId(int v)                { this.bookId = v; }
    public void setBorrowDate(LocalDate v)      { this.borrowDate = v; }
    public void setDueDate(LocalDate v)         { this.dueDate = v; }
    public void setReturnDate(LocalDate v)      { this.returnDate = v; }
    public void setStatus(String v)             { this.status = v; }
    public void setUserFullName(String v)       { this.userFullName = v; }
    public void setBookTitle(String v)          { this.bookTitle = v; }
}
