package servlet;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {

            request.setAttribute("error", "Vui lòng nhập tài khoản và mật khẩu");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        try {
            User user = userDAO.findByUsername(username.trim());

            // checkpw tự lấy salt bên trong hash đã lưu
            if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {

                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                if ("ADMIN".equals(user.getRole())) {
                    response.sendRedirect(
                            response.encodeRedirectURL(request.getContextPath() + "/admin/books"));
                } else {
                    response.sendRedirect(
                            response.encodeRedirectURL(request.getContextPath() + "/student/books"));
                }
                return;
            }

            request.setAttribute("error", "Tài khoản hoặc mật khẩu không chính xác");
            request.setAttribute("inputUsername", username.trim());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
