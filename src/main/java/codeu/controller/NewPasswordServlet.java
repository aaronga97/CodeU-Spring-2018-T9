package codeu.controller;

import codeu.model.data.PasswordUtils;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewPasswordServlet extends HttpServlet {
    /** Store class that gives access to Users. */
    private UserStore userStore;

    /** Class that enforces password restrictions. */
   // private PasswordUtils passwordUtils;

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
        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
            // user is not logged in, don't let them create a conversation
            response.sendRedirect("/login");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/view/newPassword.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String newPassword = request.getParameter("newPassword");
        String reTypedNewPassword = request.getParameter("reTypedNewPassword");
        reTypedNewPassword = Jsoup.clean(reTypedNewPassword, Whitelist.none());



        if (!newPassword.equals(reTypedNewPassword)) {
            request.setAttribute("error", "Passwords must match.");
            request.getRequestDispatcher("/WEB-INF/view/newPassword.jsp").forward(request, response);
            return;
        }

        if(!PasswordUtils.isPasswordCorrect(newPassword)){
            request.setAttribute("error", "Password must be between 5 and 13 characters and contain both letters and numbers.");
            request.getRequestDispatcher("/WEB-INF/view/newPassword.jsp").forward(request, response);
            return;
        }

        String hashedPassword = BCrypt.hashpw(reTypedNewPassword, BCrypt.gensalt());
        String username = (String) request.getSession().getAttribute("user");
        User user = userStore.getUser(username);
        user.setPasswordHash(hashedPassword);
        request.getSession().invalidate();
        user.setPasswordHash(hashedPassword);
        response.sendRedirect("/login");

    }

}
