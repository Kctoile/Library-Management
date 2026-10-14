package model;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String category;
    private int quantity;
    private int publishedYear;

    public Book() {}

    public Book(int bookId, String title, String author, String category, int quantity, int publishedYear) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.publishedYear = publishedYear;
    }

    public int getBookId()          { return bookId; }
    public String getTitle()        { return title; }
    public String getAuthor()       { return author; }
    public String getCategory()     { return category; }
    public int getQuantity()        { return quantity; }
    public int getPublishedYear()   { return publishedYear; }

    public void setBookId(int v)          { this.bookId = v; }
    public void setTitle(String v)        { this.title = v; }
    public void setAuthor(String v)       { this.author = v; }
    public void setCategory(String v)     { this.category = v; }
    public void setQuantity(int v)        { this.quantity = v; }
    public void setPublishedYear(int v)   { this.publishedYear = v; }
}
