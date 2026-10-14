package servlet;

import dao.BorrowRecordDAO;
import model.BorrowRecord;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class StudentBorrowServlet extends HttpServlet {

    private final BorrowRecordDAO borrowDAO = new BorrowRecordDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // IDOR protection: ALWAYS take userId from session — never from URL parameter
        User user = (User) req.getSession().getAttribute("user");
        try {
            List<BorrowRecord> records = borrowDAO.findByUserId(user.getUserId());
            req.setAttribute("records",   records);
            req.setAttribute("pageTitle", "Lịch sử mượn sách của tôi");
            req.getRequestDispatcher("/WEB-INF/views/student/borrow-list.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
