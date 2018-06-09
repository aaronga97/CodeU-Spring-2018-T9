package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public class NewPasswordServletTest {
    private NewPasswordServlet newPasswordServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;

    @Before
    public void setup() throws IOException {
        newPasswordServlet = new NewPasswordServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/newPassword.jsp"))
                .thenReturn(mockRequestDispatcher);
    }
    @Test
    public void testDoGet() throws IOException, ServletException {
        newPasswordServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }
    /**test for when a user enters a bad password*/
    @Test
    public void testDoPost_BadPassword() throws IOException, ServletException {
        Mockito.when(mockRequest.getParameter("username")).thenReturn("yuh");

        newPasswordServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockRequest)
                .setAttribute("error", "Password must be between 5 and 12 characters and contain both letters and numbers.");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    /**test for when a user does not exist*/
    @Test
    public void testDoPost_NotAnExistingUser() throws IOException, ServletException {
        Mockito.when(mockRequest.getParameter("usernameVerification")).thenReturn("test username");

        UserStore mockUserStore = Mockito.mock(UserStore.class);
        Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(false);
        newPasswordServlet.setUserStore(mockUserStore);

        newPasswordServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockUserStore, Mockito.never()).addUser(Mockito.any(User.class));
        Mockito.verify(mockRequest).setAttribute("error", "That username was not found.");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }
    /**test for when a user does exist*/
    @Test
    public void testDoPost_ExistingUser() throws IOException, ServletException {

        User user =
                new User(
                        UUID.randomUUID(),
                        "test username",
                        "$2a$10$.e.4EEfngEXmxAO085XnYOmDntkqod0C384jOR9oagwxMnPNHaGLa",
                        Instant.now(),
                        false);

        Mockito.when(mockRequest.getParameter("usernameVerification")).thenReturn("test username");
        Mockito.when(mockRequest.getParameter("newPassword")).thenReturn("testPassword1");
        Mockito.when(mockRequest.getParameter("reTypedNewPassword")).thenReturn("testPassword1");

        UserStore mockUserStore = Mockito.mock(UserStore.class);
        Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(true);
        Mockito.when(mockUserStore.getUser("test username")).thenReturn(user);
        newPasswordServlet.setUserStore(mockUserStore);
        String mockHashedPassword = BCrypt.hashpw("testPassword1", BCrypt.gensalt());
        user.setPasswordHash(mockHashedPassword);
        Mockito.when(user.getPasswordHash().equals(mockHashedPassword)).thenReturn(true);

        newPasswordServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).sendRedirect("/login");
    }

}
