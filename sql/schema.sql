-- ============================================================
-- Library Management System — Database Schema + Seed Data
-- SQL Server (T-SQL)
-- ============================================================

USE master;
GO

-- Create database if not exists
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'LibraryManagement')
    CREATE DATABASE LibraryManagement;
GO

USE LibraryManagement;
GO

-- ============================================================
-- Drop tables in reverse FK order (re-runnable)
-- ============================================================
IF OBJECT_ID('BorrowRecords', 'U') IS NOT NULL DROP TABLE BorrowRecords;
IF OBJECT_ID('Books',         'U') IS NOT NULL DROP TABLE Books;
IF OBJECT_ID('Users',         'U') IS NOT NULL DROP TABLE Users;
GO

-- ============================================================
-- Users
-- ============================================================
CREATE TABLE Users (
    UserID       INT           IDENTITY(1,1) PRIMARY KEY,
    Username     NVARCHAR(50)  NOT NULL UNIQUE,
    PasswordHash NVARCHAR(255) NOT NULL,
    FullName     NVARCHAR(100) NOT NULL,
    Role         NVARCHAR(10)  NOT NULL CHECK (Role IN ('ADMIN', 'STUDENT')) -- NOSONAR plsql:S1192
);
GO

-- ============================================================
-- Books
-- ============================================================
CREATE TABLE Books (
    BookID        INT           IDENTITY(1,1) PRIMARY KEY,
    Title         NVARCHAR(200) NOT NULL,
    Author        NVARCHAR(100) NOT NULL,
    Category      NVARCHAR(50)  NOT NULL,
    Quantity      INT           NOT NULL CHECK (Quantity >= 0),
    PublishedYear INT           NOT NULL CHECK (PublishedYear BETWEEN 1000 AND 2100)
);
GO

-- ============================================================
-- BorrowRecords
-- ============================================================
CREATE TABLE BorrowRecords (
    RecordID   INT          IDENTITY(1,1) PRIMARY KEY,
    UserID     INT          NOT NULL REFERENCES Users(UserID),
    BookID     INT          NOT NULL REFERENCES Books(BookID),
    BorrowDate DATE         NOT NULL DEFAULT GETDATE(),
    DueDate    DATE         NOT NULL,
    ReturnDate DATE         NULL,
    Status     NVARCHAR(10) NOT NULL DEFAULT 'BORROWING' -- NOSONAR plsql:S1192
                            CHECK (Status IN ('BORROWING', 'RETURNED', 'OVERDUE')) -- NOSONAR plsql:S1192
);
GO

-- ============================================================
-- Seed: Users
-- Passwords stored as SHA-256 hex (matches DBConnection.hash() helper)
--   admin123   → 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a
--   student123 → ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
-- ============================================================
INSERT INTO Users (Username, PasswordHash, FullName, Role) VALUES
    ('admin',   '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a', N'Thủ Thư Admin',  'ADMIN'),
    ('student1','ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', N'Nguyễn Văn An',  'STUDENT'),
    ('student2','ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', N'Trần Thị Bình',  'STUDENT');
GO

-- ============================================================
-- Seed: Books
-- ============================================================
INSERT INTO Books (Title, Author, Category, Quantity, PublishedYear) VALUES
    (N'Lập Trình Java Cơ Bản',         N'Nguyễn Thành Nam',  N'Công nghệ',   5, 2020), -- NOSONAR plsql:S1192
    (N'Cấu Trúc Dữ Liệu & Giải Thuật', N'Thomas Cormen',      N'Công nghệ',   3, 2019),
    (N'Đắc Nhân Tâm',                   N'Dale Carnegie',      N'Kỹ năng mềm', 8, 2018),
    (N'Sapiens: Lược Sử Loài Người',    N'Yuval Noah Harari',  N'Lịch sử',     4, 2017),
    (N'Clean Code',                      N'Robert C. Martin',  N'Công nghệ',   2, 2008);
GO

-- ============================================================
-- Seed: BorrowRecords (sample active borrow for testing)
-- ============================================================
INSERT INTO BorrowRecords (UserID, BookID, BorrowDate, DueDate, Status) VALUES
    (2, 1, '2024-10-01', '2024-10-15', 'BORROWING'),
    (3, 3, '2024-09-20', '2024-10-05', 'RETURNED');

UPDATE BorrowRecords SET ReturnDate = '2024-10-04', Status = 'RETURNED'
WHERE RecordID = 2;
GO

-- ============================================================
-- Verify
-- ============================================================
SELECT 'Users'         AS [Table], COUNT(*) AS Rows FROM Users
UNION ALL
SELECT 'Books',                    COUNT(*)         FROM Books
UNION ALL
SELECT 'BorrowRecords',            COUNT(*)         FROM BorrowRecords;
GO
