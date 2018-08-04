package codeu.controller;

import codeu.model.data.Mail;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class ForgotPasswordServlet extends HttpServlet {
    /** Store class that gives access to Users. */
    private UserStore userStore;

    /** Class that enforces password restrictions. */


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
        request.getRequestDispatcher("/WEB-INF/view/forgotPassword.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String recipientEmail = request.getParameter("email");
        Random random = new Random();
        int randomPass = random.nextInt(9000) + 1000;
        String numConvertedToString = Integer.toString(randomPass);
        String tempPass = "temporarypass"+numConvertedToString;

        if (recipientEmail.equals("")) {
            request.setAttribute("error", "Please enter your email address");
            request.getRequestDispatcher("/WEB-INF/view/forgotPassword.jsp").forward(request, response);
            return;
        }


        if (userStore.isEmailRegistered(recipientEmail)) {
            Mail.sendEmail(recipientEmail, tempPass);
            String hashedPassword = BCrypt.hashpw(tempPass, BCrypt.gensalt());
            User user = userStore.getUserByEmail(request.getParameter("email"));
            user.setPasswordHash(hashedPassword);
            request.setAttribute("success", "Email sent successfully");
            request.getRequestDispatcher("/WEB-INF/view/forgotPassword.jsp").forward(request, response);
        }
        else {
            request.setAttribute("error", "That email is not linked to an account");
            request.getRequestDispatcher("/WEB-INF/view/forgotPassword.jsp").forward(request, response);
        }

    }

}

