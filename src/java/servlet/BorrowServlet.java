package servlet;

import dao.BookDAO;
import dao.BorrowRecordDAO;
import dao.UserDAO;
import model.BorrowRecord;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class BorrowServlet extends HttpServlet {

    private final BorrowRecordDAO borrowDAO = new BorrowRecordDAO();
    private final BookDAO         bookDAO   = new BookDAO();
    private final UserDAO         userDAO   = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("add".equals(action)) {
                req.setAttribute("books", bookDAO.findAll(null, null, false));
                // ponytail: reuse UserDAO — no separate findAll needed for assignment scope
                req.setAttribute("pageTitle", "Tạo giao dịch mượn");
                req.getRequestDispatcher("/WEB-INF/views/admin/borrow-form.jsp").forward(req, resp);
            } else {
                List<BorrowRecord> records = borrowDAO.findAll();
                req.setAttribute("records",   records);
                req.setAttribute("pageTitle", "Giao dịch mượn sách");
                transferFlash(req);
                req.getRequestDispatcher("/WEB-INF/views/admin/borrow-list.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            int userId = Integer.parseInt(req.getParameter("userId"));
            String dueDateStr = req.getParameter("dueDate");

            StringBuilder err = new StringBuilder();
            if (dueDateStr == null || dueDateStr.isBlank()) err.append("Vui lòng chọn ngày đến hạn. ");

            LocalDate dueDate = null;
            try {
                dueDate = LocalDate.parse(dueDateStr);
                if (!dueDate.isAfter(LocalDate.now())) err.append("Ngày đến hạn phải sau ngày hôm nay. ");
            } catch (Exception e) {
                err.append("Ngày đến hạn không hợp lệ. ");
            }

            // Check available quantity
            model.Book book = bookDAO.findById(bookId);
            if (book == null || book.getQuantity() <= 0) err.append("Sách không còn có thể mượn. ");

            if (!err.isEmpty()) {
                req.setAttribute("error", err.toString().trim());
                req.setAttribute("books", bookDAO.findAll(null, null, false));
                req.setAttribute("pageTitle", "Tạo giao dịch mượn");
                req.getRequestDispatcher("/WEB-INF/views/admin/borrow-form.jsp").forward(req, resp);
                return;
            }

            BorrowRecord record = new BorrowRecord();
            record.setUserId(userId);
            record.setBookId(bookId);
            record.setBorrowDate(LocalDate.now());
            record.setDueDate(dueDate);
            borrowDAO.insert(record);

            setFlash(req, "success", "Tạo giao dịch mượn thành công.");
            resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/admin/borrows"));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

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
