// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Assert;

public class AdminServletTest {

  private AdminServlet adminServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private HttpSession mockSession;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    adminServlet = new AdminServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockUserStore = Mockito.mock(UserStore.class);
    adminServlet.setUserStore(mockUserStore);
  }

  /**
   * Tests that when you DON'T get a user from the session (user=null),
   * checks that user is redirected to /login.
   */
  @Test
  public void testDoGet_NullUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  /**
   * Tests that when you get a user from the session and the user DOES NOT exist
   * in our database, checks that user is redirected to /login.
   */
  @Test
  public void testDoGet_FakeUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("fake_username");
    Mockito.when(mockUserStore.getUser("fake_username")).thenReturn(null);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/login");
  }


  /** Tests that when user is a valid admin to forward the request into ./admin */
  @Test
  public void testDoGet_ValidAdminUser() throws IOException, ServletException {
    User admin_user =
        new User(
            UUID.randomUUID(),
            "admin_username",
            "admin_password",
            Instant.now(),
            true);

    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin_username");
    Mockito.when(mockUserStore.getUser("admin_username")).thenReturn(admin_user);

    boolean b = admin_user.isAdmin();
    Assert.assertEquals(b, true);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  /**
   * Tests that when you get a user from the session and the user DOES exist but
   * is NOT in admin list checks that user is redirected to /.
   */
  @Test
  public void testDoGet_NotAdminUser() throws IOException, ServletException {
    User not_admin_user =
        new User(
            UUID.randomUUID(),
            "not_admin_username",
            "not_admin_password",
            Instant.now(),
            false);

    Mockito.when(mockSession.getAttribute("user")).thenReturn("not_admin_username");
    Mockito.when(mockUserStore.getUser("not_admin_username")).thenReturn(not_admin_user);

    boolean b = not_admin_user.isAdmin();
    Assert.assertEquals(b, false);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/");
  }

  /** Test when they want to make a user admin but the user doesn't exist */
  @Test
  public void testDoPost_UserDoesNotExist() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("toBeAdminUser")).thenReturn("unexistentUser");
    Mockito.when(mockUserStore.getUser("unexistentUser")).thenReturn(null);

    adminServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
        .setAttribute("error", "User unexistentUser doesn't exist.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  /** Test when they want to make a user admin but the user is already admin */
  @Test
  public void testDoPost_UserIsAlreadyAdmin() throws IOException, ServletException {
    User admin_user =
        new User(
            UUID.randomUUID(),
            "admin_username",
            "admin_password",
            Instant.now(),
            true);

    Mockito.when(mockRequest.getParameter("toBeAdminUser")).thenReturn("admin_username");
    Mockito.when(mockUserStore.getUser("admin_username")).thenReturn(admin_user);

    boolean b = admin_user.isAdmin();
    Assert.assertEquals(b, true);

    adminServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
        .setAttribute("error", "User admin_username is already an admin.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

}
