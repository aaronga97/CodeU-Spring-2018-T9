package codeu.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class LogoutServletTest {
    private LogoutServlet logoutServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;

    @Before
    public void setup() {
        logoutServlet = new LogoutServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/logout.jsp"))
                .thenReturn(mockRequestDispatcher);
    }

    @Test
    public void testDoGet_logout() throws IOException, ServletException {


        HttpSession mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        logoutServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockSession).invalidate();
        Mockito.verify(mockResponse).sendRedirect("/");
    }
}
