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
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordServlet extends HttpServlet {
    /** Store class that gives access to Users. */
    private UserStore userStore;

    /** Class that enforces password restrictions. */
    // private PasswordUtils passwordUtils;

//    @Override
//    public void init() throws ServletException {
//        super.init();
//        setUserStore(UserStore.getInstance());
//    }

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
        String recipientEmail = request.getParameter("emailPrompt");

//        if (recipientEmail.equals("")) {
//            request.setAttribute("error", "Please enter your email address");
//            request.getRequestDispatcher("/WEB-INF/view/forgotPassword.jsp").forward(request, response);
//            return;
//        }
        if (userStore.isEmailRegistered(recipientEmail)) {
            Mail.sendEmail(recipientEmail);

        }
        else {
            request.setAttribute("error", "That email is  linked to an account");
            request.getRequestDispatcher("/WEB-INF/view/forgotPassword.jsp").forward(request, response);
            return;
        }
    }

}

