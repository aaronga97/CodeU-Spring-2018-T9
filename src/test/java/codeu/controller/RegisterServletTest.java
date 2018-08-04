package codeu.controller;

import java.io.IOException;

import java.time.Instant;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import codeu.model.data.User;
import codeu.model.data.Activity;
import codeu.model.data.Activity.ActivityType;

import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ActivityStore;

public class RegisterServletTest {

  private RegisterServlet registerServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  private ActivityStore mockActivityStore;

  @Before
  public void setup() {
    registerServlet = new RegisterServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/register.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockActivityStore = Mockito.mock(ActivityStore.class);
    registerServlet.setActivityStore(mockActivityStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    registerServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_BadUsername() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("bad@$username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("testpassword1");
    Mockito.when(mockRequest.getParameter("reTypedPassword")).thenReturn("testpassword1");



    registerServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockActivityStore, Mockito.never()).addActivity(Mockito.any(Activity.class));
    Mockito.verify(mockRequest)
        .setAttribute("error", "Please enter only letters, numbers, and spaces.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_ShortUsername() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("te");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("testpassword1");



    registerServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
            .setAttribute("error", "username must be between 3 and 14 characters");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_LongUsername() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("long test password");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("testpassword1");



    registerServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
            .setAttribute("error", "username must be between 3 and 14 characters");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_BadPassword() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("test password");



    registerServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
            .setAttribute("error", "Password must be between 5 and 13 characters and contain both letters and numbers.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_ShortPassword() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("pass");



    registerServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
            .setAttribute("error", "Password must be between 5 and 13 characters and contain both letters and numbers.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_LongPassword() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("LongPassword1234");



    registerServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
            .setAttribute("error", "Password must be between 5 and 13 characters and contain both letters and numbers.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }



  @Test
  public void testDoPost_NewUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("testpassword1");
    Mockito.when(mockRequest.getParameter("reTypedPassword")).thenReturn("testpassword1");
    Mockito.when(mockRequest.getParameter("email")).thenReturn("team9ChatApp@gmail.com");

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(false);
    registerServlet.setUserStore(mockUserStore);

    registerServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    Mockito.verify(mockUserStore).addUser(userArgumentCaptor.capture());
    Assert.assertEquals("test username", userArgumentCaptor.getValue().getName());
    Assert.assertThat(
        userArgumentCaptor.getValue().getPasswordHash(), CoreMatchers.containsString("$2a$10$"));
    Assert.assertEquals(60, userArgumentCaptor.getValue().getPasswordHash().length());
    Assert.assertEquals("team9ChatApp@gmail.com",userArgumentCaptor.getValue().getEmail());

    ArgumentCaptor<Activity> activityArgumentCaptor = ArgumentCaptor.forClass(Activity.class);
    Mockito.verify(mockActivityStore).addActivity(activityArgumentCaptor.capture());

    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoPost_ExistingUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("testpassword1");
    Mockito.when(mockRequest.getParameter("reTypedPassword")).thenReturn("testpassword1");

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(true);
    registerServlet.setUserStore(mockUserStore);

    registerServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockUserStore, Mockito.never()).addUser(Mockito.any(User.class));
    Mockito.verify(mockActivityStore, Mockito.never()).addActivity(Mockito.any(Activity.class));
    Mockito.verify(mockRequest).setAttribute("error", "That username is already taken.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_PasswordsDontMatch() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("testpassword1");
    Mockito.when(mockRequest.getParameter("reTypedPassword")).thenReturn("testpassword2");

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered("test username")).thenReturn(false);
    registerServlet.setUserStore(mockUserStore);

    registerServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockUserStore, Mockito.never()).addUser(Mockito.any(User.class));
    Mockito.verify(mockActivityStore, Mockito.never()).addActivity(Mockito.any(Activity.class));
    Mockito.verify(mockRequest).setAttribute("error", "Passwords must match.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_AlreadyUsedEmail() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("test username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("testpassword1");
    Mockito.when(mockRequest.getParameter("reTypedPassword")).thenReturn("testpassword1");
    Mockito.when(mockRequest.getParameter("email")).thenReturn("team9ChatApp@gmail.com");


    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isEmailRegistered("team9ChatApp@gmail.com")).thenReturn(true);
    registerServlet.setUserStore(mockUserStore);

    registerServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockUserStore, Mockito.never()).addUser(Mockito.any(User.class));
    Mockito.verify(mockActivityStore, Mockito.never()).addActivity(Mockito.any(Activity.class));
    Mockito.verify(mockRequest).setAttribute("error", "That email is already linked to an account.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}
