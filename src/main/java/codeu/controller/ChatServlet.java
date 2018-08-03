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
import codeu.model.data.Activity;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ActivityStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Servlet class responsible for the chat page. */
public class ChatServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Activity. */
private ActivityStore activityStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
    setActivityStore(ActivityStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
  * Sets the ConversationStore used by this servlet. This function provides a common setup method
  * for use by the test framework or the servlet's init() function.
  */
  void setActivityStore(ActivityStore activityStore) {
    this.activityStore = activityStore;
  }

  /**
   * This function fires when a user navigates to the chat page. It gets the conversation title from
   * the URL, finds the corresponding Conversation, and fetches the messages in that Conversation.
   * It then forwards to chat.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // check if this conversationTitle was a private conversation and if it doesn't exist, reason is because
      // this was an old User before private conversations were made, so call createConversations() for both Users
      String pattern = "([^-])+[-]([^-])+";

      if (conversationTitle.matches(pattern)) {
        String namePattern1 = "(.*)-";
        Pattern p1 = Pattern.compile(namePattern1);
        Matcher m1 = p1.matcher(conversationTitle);
        String name1 = "";
        String name2 = "";
        if (m1.find()) {
          name1 = m1.group(1);
        }
        String namePattern2 = "-(.*)";
        Pattern p2 = Pattern.compile(namePattern2);
        Matcher m2 = p2.matcher(conversationTitle);
        if (m2.find()) {
          name2 = m2.group(1);
        }
        User user1 = UserStore.getInstance().getUser(name1);
        User user2 = UserStore.getInstance().getUser(name2);
        if (user1 != null && user2 != null) {
          user1.createConversations();
          user2.createConversations();
          conversation = conversationStore.getConversationWithTitle(conversationTitle);
        } else {
          // usernames in conversationTitle do not exist, so redirect to conversation list
          System.out.println("User does not exist " + conversationTitle);
          response.sendRedirect("/conversations");
          return;
        }
      } else {
        // couldn't find conversation, redirect to conversation list
        System.out.println("Conversation was null: " + conversationTitle);
        response.sendRedirect("/conversations");
        return;
      }
    }

    if (conversation.getPrivate()) {
      // if conversation is private conversation, make sure user is logged in and is one of the two users in this conversation
      String username = (String) request.getSession().getAttribute("user");
      String namePattern1 = "(.*)-";
      Pattern p1 = Pattern.compile(namePattern1);
      Matcher m1 = p1.matcher(conversationTitle);
      String name1 = "";
      String name2 = "";
      if (m1.find()) {
        name1 = m1.group(1);
      }
      String namePattern2 = "-(.*)";
      Pattern p2 = Pattern.compile(namePattern2);
      Matcher m2 = p2.matcher(conversationTitle);
      if (m2.find()) {
        name2 = m2.group(1);
      }
      if (username == null || (!username.equals(name1) && !username.equals(name2))) {
        System.out.println("Sorry, you cannot view this private conversation.");
        response.sendRedirect("/conversations");
        return;
      }
    }

    // otherwise, user logged in is one of the users in private conversation OR conversation is public and anyone can view
    UUID conversationId = conversation.getId();

    List<Message> messages = messageStore.getMessagesInConversation(conversationId);

    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messages);
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the chat page. It gets the logged-in
   * username from the session, the conversation title from the URL, and the chat message from the
   * submitted form data. It creates a new Message from that data, adds it to the model, and then
   * redirects back to the chat page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      response.sendRedirect("/conversations");
      return;
    }

    String messageContent = request.getParameter("message");

    // this removes any HTML from the message content
    String cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());

    boolean privateMessage = conversation.getPrivate();

    Message message =
        new Message(
            UUID.randomUUID(),
            conversation.getId(),
            user.getId(),
            cleanedMessageContent,
            Instant.now(),
            privateMessage);

    messageStore.addMessage(message);

    if (!conversation.getPrivate()) {
      Activity conversationActivity = activityStore.getActivityWithConversationName(conversationTitle);
      conversationActivity.increaseAllTimeCount();
      conversationActivity.increaseDailyPopularity();
      conversationActivity.setZScore();
      activityStore.updateActivity(conversationActivity);
    }

    // redirect to a GET request
    response.sendRedirect("/chat/" + conversationTitle);
  }
}
