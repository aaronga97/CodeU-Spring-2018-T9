package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewPasswordServlet extends HttpServlet {
    /** Store class that gives access to Users. */
    private UserStore userStore;

    @Override
    public void init() throws ServletException {
        super.init();
        setUserStore(UserStore.getInstance());
    }

    /**
     * Sets the UserStore used by this servlet. This function provides a common setup method for use
     * by the test framework or the servlet's init() function.
     */
    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/view/newPassword.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String newPassword = request.getParameter("newPassword");
        String reTypedNewPassword = request.getParameter("reTypedNewPassword");
        String username =request.getParameter("usernameVerification");

        if (!userStore.isUserRegistered(username)) {
            request.setAttribute("error", "That username was not found.");
            request.getRequestDispatcher("/WEB-INF/view/newPassword.jsp").forward(request, response);
            return;
        }
        if (!newPassword.equals(reTypedNewPassword)) {
            request.setAttribute("error", "Passwords must match.");
            request.getRequestDispatcher("/WEB-INF/view/newPassword.jsp").forward(request, response);
            return;
        }
        String hashedPassword = BCrypt.hashpw(reTypedNewPassword, BCrypt.gensalt());
        User user = userStore.getUser(request.getParameter("usernameVerification"));
        user.setPasswordHash(hashedPassword);

        response.sendRedirect("/login");
    }

}
