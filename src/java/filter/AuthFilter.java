package filter;

import model.User;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * AuthFilter — enforces authentication and role-based access on every request.
 *
 * Public paths (no login required): /login, /css, /images, static resources.
 * ADMIN paths: /admin/*
 * STUDENT paths: /student/*
 */
public class AuthFilter implements Filter {

    // Paths that need no authentication
    private static final Set<String> PUBLIC_PATHS = Set.of("/login", "/index.html");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        // Allow public paths and static resources
        if (isPublic(path)) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // Not logged in → redirect to login
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Role-based access
        if (path.startsWith("/admin") && !"ADMIN".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này.");
            return;
        }
        if (path.startsWith("/student") && !"STUDENT".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này.");
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean isPublic(String path) {
        for (String p : PUBLIC_PATHS) {
            if (path.equals(p)) return true;
        }
        // Static resources
        return path.startsWith("/css") || path.startsWith("/images") || path.startsWith("/js");
    }
}
