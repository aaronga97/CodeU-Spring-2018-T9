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
import codeu.model.data.Utils;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet class responsible for the login page. */
public class AdminServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;
  private MessageStore messageStore;
  private ConversationStore conversationStore;

  /**
   * Set up state for handling login-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
  }

  /**
   * Sets the UserStore, MessageStore, and Conversation Store used by this servlet. This
   * function provides a common setup method for use by the test framework or the servlet's
   * init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  void setMessageStore(MessageStore messageStore) { this.messageStore = messageStore; }
  void setConversationStore(ConversationStore conversationStore) { this.conversationStore = conversationStore; }

  /**
   * Gets the correct stats from datastore and stores them in a map, simulating a json file
   */
  public void addStats(Map<String, String> map) {
    // Retrieve sizes of each Datastore
    Integer userSize = userStore.countTotalUsers();
    Integer messageSize = messageStore.countTotalMessages();
    Integer convSize = conversationStore.countTotalConversations();


    // Adds them to the map
    map.put("userSize", userSize.toString());
    map.put("messageSize", messageSize.toString());
    map.put("convSize", convSize.toString());

    // Adds latest Message
    Message lastMessage = messageStore.getLastMessageIndex();
    if(lastMessage == null) {
      //If there's no latestMessage send empty attributes
      map.put("lastMessageContent", "");
      map.put("lastMessageTime", "");
      map.put("lastMessageUser", "");
    } else {
      //else set body and time of creation
      String lastMessageContent = lastMessage.getContent();
      String lastMessageTime = Utils.getTime(lastMessage.getCreationTime());
      String lastMessageUser = userStore.getUser(lastMessage.getAuthorId()).getName();

      map.put("lastMessageContent", lastMessageContent);
      map.put("lastMessageTime", lastMessageTime);
      map.put("lastMessageUser", lastMessageUser);

      System.out.println(lastMessageContent);
      System.out.println(lastMessageTime);
      System.out.println(lastMessageUser);
    }

    // Adds latest Conversation
    Conversation lastConversation = conversationStore.getLastConversationIndex();
    if(lastConversation == null) {
      //If there's no lastConversation send empty attributes
      map.put("lastConversationName", "");
      map.put("lastConversationTime", "");
    } else {
      //else set body and time of creation
      String lastConversationTitle= lastConversation.getTitle();
      String lastConversationTime = Utils.getTime(lastConversation.getCreationTime());

      map.put("lastConversationName", lastConversationTitle);
      map.put("lastConversationTime", lastConversationTime);

      System.out.println(lastConversationTitle);
      System.out.println(lastConversationTime);
    }

    // Adds latest User
    User lastUser = userStore.getLastUserIndex();
    if(lastUser == null) {
      //If there's no latestUser send empty attributes
      map.put("lastUserName", "");
      map.put("lastUserTime", "");
    } else {
      //else set name and time of creation
      String lastUserName = lastUser.getName();
      String lastUserTime = Utils.getTime(lastUser.getCreationTime());

      map.put("lastUserName", lastUserName);
      map.put("lastUserTime", lastUserTime);

      System.out.println(lastUserName);
      System.out.println(lastUserTime);
    }

  }

  /**
   * This function fires when a user requests the /admin URL. It simply forwards the request to
   * admin.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    Map<String, String> adminStatsMap = new HashMap<>();

    String username = (String) request.getSession().getAttribute("user");


    if (username == null) {
      // user is not logged in, don't give them access
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them access admin
      System.out.println("User not found: " + username);
      response.sendRedirect("/login");
      return;
    }

    if(user.isAdmin()){
      // user is in admin list, give access to admin site, and send map as attribute
      addStats(adminStatsMap);
      request.setAttribute("adminStatsMap", adminStatsMap);
      adminStatsMap.forEach((key,value) -> System.out.println(key + " = " + value));
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
      return;
    }

    // user isn't in admin list, redirect him to root
    response.sendRedirect("/");

  }

  /**
   *  Gets an entry made by an admin username, cleans it and if conditions are method
   *  that entry (username) acquires admin privileges.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

      //Get username submitted & clean user entry
      String toBeAdmin = request.getParameter("toBeAdminUser");
      toBeAdmin = Jsoup.clean(toBeAdmin, Whitelist.none());

      User user = userStore.getUser(toBeAdmin);

      if(user == null) {
        //If user doesn't exist send error message
        request.setAttribute("error", "User " + toBeAdmin + " doesn't exist.");
        request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        return;
      } else if(user.isAdmin()) {
        //If user is already an admin, send error message
        request.setAttribute("error", "User " + toBeAdmin + " is already an admin.");
        request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        return;
      } else {
        //User exists and it's not an admin, set admin -> true, send sucess message
        user.setAdmin(true);
        userStore.updateUser(user);
        request.setAttribute("success", toBeAdmin + " is now an admin!");
        request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        return;
      }

  }
}
