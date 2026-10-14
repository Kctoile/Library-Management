package servlet;

import dao.BookDAO;
import model.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Year;
import java.util.List;

public class BookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Book book = bookDAO.findById(id);
                if (book == null) { resp.sendError(404); return; }
                req.setAttribute("book", book);
                req.setAttribute("pageTitle", "Sửa sách");
                req.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").forward(req, resp);

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                try {
                    bookDAO.delete(id);
                    setFlash(req, "success", "Xóa sách thành công.");
                } catch (IllegalStateException e) {
                    setFlash(req, "error", e.getMessage());
                }
                resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/admin/books"));

            } else {
                // List with optional search/filter
                String keyword      = req.getParameter("keyword");
                String category     = req.getParameter("category");
                String availableStr = req.getParameter("availableOnly");
                boolean availableOnly = "1".equals(availableStr);

                List<Book> books = bookDAO.findAll(keyword, category, availableOnly);
                req.setAttribute("books",       books);
                req.setAttribute("keyword",     keyword);
                req.setAttribute("category",    category);
                req.setAttribute("availableOnly", availableOnly);
                req.setAttribute("categories",  bookDAO.findAllCategories());
                req.setAttribute("pageTitle",   "Quản lý sách");
                transferFlash(req);
                req.getRequestDispatcher("/WEB-INF/views/admin/book-list.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                Book book = bindAndValidate(req);
                if (book == null) { forwardFormWithErrors(req, resp, null); return; }
                bookDAO.insert(book);
                setFlash(req, "success", "Thêm sách thành công.");
                resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/admin/books"));

            } else if ("update".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Book book = bindAndValidate(req);
                if (book == null) {
                    book = bookDAO.findById(id); // reload for re-display
                    forwardFormWithErrors(req, resp, book);
                    return;
                }
                book.setBookId(id);
                bookDAO.update(book);
                setFlash(req, "success", "Cập nhật sách thành công.");
                resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/admin/books"));
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // Returns a Book if valid, null if validation failed (sets req attributes for errors)
    private Book bindAndValidate(HttpServletRequest req) {
        String title    = trim(req.getParameter("title"));
        String author   = trim(req.getParameter("author"));
        String category = trim(req.getParameter("category"));
        String qtyStr   = trim(req.getParameter("quantity"));
        String yearStr  = trim(req.getParameter("publishedYear"));

        StringBuilder err = new StringBuilder();
        if (title.isEmpty())    err.append("Tên sách không được để trống. ");
        if (author.isEmpty())   err.append("Tác giả không được để trống. ");
        if (category.isEmpty()) err.append("Thể loại không được để trống. ");

        int qty = 0, year = 0;
        try { qty = Integer.parseInt(qtyStr); if (qty < 0) err.append("Số lượng phải >= 0. "); }
        catch (NumberFormatException e) { err.append("Số lượng không hợp lệ. "); }

        int currentYear = Year.now().getValue();
        try { year = Integer.parseInt(yearStr);
              if (year < 1000 || year > currentYear) err.append("Năm xuất bản phải từ 1000 đến " + currentYear + ". "); }
        catch (NumberFormatException e) { err.append("Năm xuất bản không hợp lệ. "); }

        if (!err.isEmpty()) {
            req.setAttribute("error", err.toString().trim());
            // Preserve user input
            req.setAttribute("inputTitle",   title);
            req.setAttribute("inputAuthor",  author);
            req.setAttribute("inputCategory",category);
            req.setAttribute("inputQty",     qtyStr);
            req.setAttribute("inputYear",    yearStr);
            return null;
        }

        Book b = new Book();
        b.setTitle(title); b.setAuthor(author); b.setCategory(category);
        b.setQuantity(qty); b.setPublishedYear(year);
        return b;
    }

    private void forwardFormWithErrors(HttpServletRequest req, HttpServletResponse resp, Book book)
            throws ServletException, IOException {
        if (book != null) req.setAttribute("book", book);
        req.setAttribute("pageTitle", book != null ? "Sửa sách" : "Thêm sách");
        req.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").forward(req, resp);
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }

    private void setFlash(HttpServletRequest req, String type, String msg) {
        req.getSession().setAttribute("flash_type", type);
        req.getSession().setAttribute("flash_msg",  msg);
    }

    private void transferFlash(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return;
        req.setAttribute("flash_type", s.getAttribute("flash_type"));
        req.setAttribute("flash_msg",  s.getAttribute("flash_msg"));
        s.removeAttribute("flash_type");
        s.removeAttribute("flash_msg");
    }
}
