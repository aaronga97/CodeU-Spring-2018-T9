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
import codeu.model.data.AjaxMessage;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class AjaxGetMessages extends HttpServlet{

    /** Store class that gives access to Conversations. */
    private ConversationStore conversationStore;

    /** Store class that gives access to Messages. */
    private MessageStore messageStore;

    /** Store class that gives access to Users. */
    private UserStore userStore;

    /** Set up state for handling chat requests. */
    @Override
    public void init() throws ServletException {
        super.init();
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
        setUserStore(UserStore.getInstance());
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
     * Fetches latest messages from ajax call
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<Message> messageList = new ArrayList<>();
        List<Message> newMessageList = new ArrayList<>();
        List<AjaxMessage> ajaxMessageList = new ArrayList<>();

        String requestUrl = request.getRequestURI();

        //Get UUID from conversation, the 36 is the length of a UUID
        String conversationID = (String) requestUrl.substring("/ajaxTest/".length(), 36 + "/ajaxTest/".length());
        UUID conversationUUID = UUID.fromString(conversationID);

        //Get size of messages list displayed in jsp
        String lastMessagesSize = (String) requestUrl.substring(("/ajaxTest/" + conversationID + "/").length());
        Integer lastSize = Integer.parseInt(lastMessagesSize);

        messageList = messageStore.getMessagesInConversation(conversationUUID);

        System.out.println("lastSize: " + lastSize);
        System.out.println("Datastore size: " + messageList.size());

        //If size is less than actual size of messages, send the missing messages
        while(lastSize < messageList.size()){
            newMessageList.add(messageList.get(lastSize));
            lastSize++;
        }

        //Convert each message into an ajaxMessage and itt to ajaxMessageList
        for(int i = 0; i < newMessageList.size(); ++i){
          Message actualMessage = newMessageList.get(i);
          AjaxMessage convertedMessage = AjaxMessage.toAjaxMessage(actualMessage);

          ajaxMessageList.add(convertedMessage);
        }

        //Convert ajaxMessages into a json
        String messagesJson = new Gson().toJson(ajaxMessageList);
        System.out.println(messagesJson);

        //Send json with new messages to front end
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(messagesJson);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    }
}
