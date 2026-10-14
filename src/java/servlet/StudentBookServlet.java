package servlet;

import dao.BookDAO;
import model.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class StudentBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("detail".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Book book = bookDAO.findById(id);
                if (book == null) { resp.sendError(404); return; }
                req.setAttribute("book", book);
                req.setAttribute("pageTitle", book.getTitle());
                req.getRequestDispatcher("/WEB-INF/views/student/book-detail.jsp").forward(req, resp);

            } else {
                String keyword      = req.getParameter("keyword");
                String category     = req.getParameter("category");

                List<Book> books = bookDAO.findAll(keyword, category, false);
                req.setAttribute("books",      books);
                req.setAttribute("keyword",    keyword);
                req.setAttribute("category",   category);
                req.setAttribute("categories", bookDAO.findAllCategories());
                req.setAttribute("pageTitle",  "Tìm kiếm sách");
                req.getRequestDispatcher("/WEB-INF/views/student/book-list.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
