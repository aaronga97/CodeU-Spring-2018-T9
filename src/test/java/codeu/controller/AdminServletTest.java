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

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

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
  private MessageStore mockMessageStore;
  private ConversationStore mockConversationStore;

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

    mockMessageStore = Mockito.mock(MessageStore.class);
    adminServlet.setMessageStore(mockMessageStore);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    adminServlet.setConversationStore(mockConversationStore);
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

  /** Tests that when user is a valid admin to forward the request into ./admin ,
   *  with correct attributes in the adminStatsMap as well.
   */
  @Test
  public void testDoGet_ValidAdminUser() throws IOException, ServletException {
    //Initialize every fake objects/attributes needed, admin_user, fakeMap, fakeUserList
    //fakeMessageList, and fakeConversationList
    UUID fakeConversationId = UUID.randomUUID();

    User admin_user =
        new User(
            UUID.randomUUID(),
            "admin_username",
            "admin_password", "team9chatapp@gmail.com",
            Instant.now(),
            true);

    List<User> fakeUserList = new ArrayList<>();
    List<Message> fakeMessageList = new ArrayList<>();
    List<Conversation> fakeConversationList = new ArrayList<>();

    Map<String, String> fakeMap = new HashMap<String, String>();

    //Fill fakeLists with 1 instance each
    fakeUserList.add(admin_user);

    fakeMessageList.add(
            new Message(
                    UUID.randomUUID(),
                    fakeConversationId,
                    UUID.randomUUID(),
                    "test message",
                    Instant.now(),
                    false));

    fakeConversationList.add(
            new Conversation(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    "test_conversation",
                    Instant.now(),
                    false));

    Integer userSize = fakeMessageList.size();
    Integer messageSize = fakeMessageList.size();
    Integer convSize = fakeConversationList.size();

    //Return admin username when getUser
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin_username");
    Mockito.when(mockUserStore.getUser("admin_username")).thenReturn(admin_user);

    //Validate user is admin
    boolean b = admin_user.isAdmin();
    Assert.assertEquals(true, b);

    //Call method that adds stats
    adminServlet.addStats(fakeMap);

    //Return the size of total users, messages, conversation
    Mockito.when(mockUserStore.countTotalUsers()).thenReturn(userSize);
    Mockito.when(mockMessageStore.countTotalMessages()).thenReturn(messageSize);
    Mockito.when(mockConversationStore.countTotalConversations()).thenReturn(convSize);

    //Check that size matches expected size
    Integer expectedValue = 1;
    Assert.assertEquals(expectedValue, userSize);
    Assert.assertEquals(expectedValue, messageSize);
    Assert.assertEquals(expectedValue, convSize);

    //Insert those attributes into map
    fakeMap.put("userSize", userSize.toString());
    fakeMap.put("convSize", convSize.toString());
    fakeMap.put("messageSize", messageSize.toString());

    //Test map attributes
    Assert.assertEquals(expectedValue.toString(), fakeMap.get("userSize"));
    Assert.assertEquals(expectedValue.toString(), fakeMap.get("convSize"));
    Assert.assertEquals(expectedValue.toString(), fakeMap.get("messageSize"));

    adminServlet.doGet(mockRequest, mockResponse);

    //Verify map is sent and get request forwarded
    Mockito.verify(mockRequest).setAttribute("adminStatsMap", fakeMap);
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
            "not_admin_password", "team9chatapp@gmail.com",
            Instant.now(),
            false);

    Mockito.when(mockSession.getAttribute("user")).thenReturn("not_admin_username");
    Mockito.when(mockUserStore.getUser("not_admin_username")).thenReturn(not_admin_user);

    boolean b = not_admin_user.isAdmin();
    Assert.assertEquals(false, b);

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
            "admin_password", "team9chatapp@gmail.com",
            Instant.now(),
            true);

    Mockito.when(mockRequest.getParameter("toBeAdminUser")).thenReturn("admin_username");
    Mockito.when(mockUserStore.getUser("admin_username")).thenReturn(admin_user);

    boolean b = admin_user.isAdmin();
    Assert.assertEquals(true, b);

    adminServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest)
        .setAttribute("error", "User admin_username is already an admin.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  /** Test when they want to make user admin and he is eligible for it*/
  @Test
  public void testDoPost_MakeUserAdmin() throws IOException, ServletException {
    User soon_admin_user =
        new User(
            UUID.randomUUID(),
            "soon_admin_username",
            "soon_admin_password", "team9chatapp@gmail.com",
            Instant.now(),
            false);

    Mockito.when(mockRequest.getParameter("toBeAdminUser")).thenReturn("soon_admin_username");
    Mockito.when(mockUserStore.getUser("soon_admin_username")).thenReturn(soon_admin_user);

    boolean b = soon_admin_user.isAdmin();
    Assert.assertEquals(false, b);

    adminServlet.doPost(mockRequest, mockResponse);

    soon_admin_user.setAdmin(true);
    boolean c = soon_admin_user.isAdmin();
    Assert.assertEquals(true, c);

    Mockito.verify(mockRequest)
        .setAttribute("success", "soon_admin_username is now an admin!");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

}
