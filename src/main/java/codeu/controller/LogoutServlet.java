package codeu.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Servlet class responsible for logging out a user. */
public class LogoutServlet extends HttpServlet {
/**
 * This function fires when a user requests the /logout URL.*/
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getSession().invalidate();

        response.sendRedirect("/");
    }
}
