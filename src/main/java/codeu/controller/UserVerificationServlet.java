package codeu.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import codeu.model.store.basic.UserStore;

public class UserVerificationServlet extends HttpServlet {

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
        request.getRequestDispatcher("/WEB-INF/view/userVerification.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("usernameVerification");

        if (!userStore.isUserRegistered(username)) {
            request.setAttribute("error", "That username was not found.");
            request.getRequestDispatcher("/WEB-INF/view/userVerification.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("/newPassword");
    }


}
