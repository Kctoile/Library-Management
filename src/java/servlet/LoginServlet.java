package servlet;

import dao.DBConnection;
import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Already logged in → redirect to home
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            redirectByRole((User) session.getAttribute("user"), resp);
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userDAO.findByUsername(username.trim());
            if (user != null && user.getPasswordHash().equals(DBConnection.hash(password))) {
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
                redirectByRole(user, resp);
            } else {
                req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
                req.setAttribute("username", username); // preserve input
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException("Login error", e);
        }
    }

    private void redirectByRole(User user, HttpServletResponse resp) throws IOException {
        if ("ADMIN".equals(user.getRole())) {
            resp.sendRedirect(resp.encodeRedirectURL("../admin/books"));
        } else {
            resp.sendRedirect(resp.encodeRedirectURL("../student/books"));
        }
    }
}
