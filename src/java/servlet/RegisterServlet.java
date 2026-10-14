package servlet;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = trim(request.getParameter("username"));
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");
        String fullName = trim(request.getParameter("fullName"));

        StringBuilder err = new StringBuilder();

        if (username.isEmpty()) {
            err.append("Tài khoản không được để trống. ");
        }
        if (fullName.isEmpty()) {
            err.append("Họ tên không được để trống. ");
        }
        if (password == null || password.isEmpty()) {
            err.append("Mật khẩu không được để trống. ");
        } else if (password.length() < 6) {
            err.append("Mật khẩu phải có ít nhất 6 ký tự. ");
        } else if (!password.equals(confirm)) {
            err.append("Xác nhận mật khẩu không khớp. ");
        }

        try {
            if (err.length() == 0 && userDAO.findByUsername(username) != null) {
                err.append("Tài khoản đã tồn tại. ");
            }

            if (err.length() > 0) {
                request.setAttribute("error", err.toString().trim());
                request.setAttribute("inputUsername", username);
                request.setAttribute("inputFullName", fullName);
                request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                        .forward(request, response);
                return;
            }

            // Hash tại đây — không lưu mật khẩu gốc
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));

            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(passwordHash);
            user.setFullName(fullName);
            user.setRole("STUDENT");

            userDAO.insert(user);

            response.sendRedirect(
                    response.encodeRedirectURL(request.getContextPath() + "/login?registered=true"));

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
