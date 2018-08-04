package codeu.controller;

import codeu.model.data.Activity;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ForgotPasswordServletTest {
    private ForgotPasswordServlet forgotPasswordServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;

    @Before
    public void setup() throws IOException {
        forgotPasswordServlet = new ForgotPasswordServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/forgotPassword.jsp"))
                .thenReturn(mockRequestDispatcher);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        forgotPasswordServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
  public void testDoPost_EmailNotFound() throws IOException, ServletException {
        Mockito.when(mockRequest.getParameter("email")).thenReturn("lee@gmail.com");

        UserStore mockUserStore = Mockito.mock(UserStore.class);
        Mockito.when(mockUserStore.isEmailRegistered("lee@gmail.com")).thenReturn(false);
        forgotPasswordServlet.setUserStore(mockUserStore);

        forgotPasswordServlet.doPost(mockRequest, mockResponse);
        Mockito.verify(mockRequest).setAttribute("error", "That email is not linked to an account");
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}
