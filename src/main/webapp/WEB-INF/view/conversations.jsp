<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.Utils" %>
<%
/** Gets the UserStore instance to access all users. */
UserStore userStore = UserStore.getInstance();
%>

<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@include file= "navbar.jsp"%>

  <div id="container">
    <div id="inside-container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <h1>Conversations</h1>

    <%
    String currentUser = (String) request.getSession().getAttribute("user");
    User currUser = userStore.getUser(currentUser);
    if (currUser != null) { %>
      <h2>Create a new conversation</h2>
      <form action="/conversations" method="POST">
          <div class="form-group">
            <label class="form-control-label">Title:</label>
          <input type="text" name="conversationTitle" id="newConversation">
        </div>

        <button type="submit">Create</button>
      </form>

      <hr/>
    <% } %>
    <%
    List<Conversation> conversations =
      (List<Conversation>) request.getAttribute("conversations");
    if(conversations == null || conversations.isEmpty()){
    %>
      <p>Create a conversation to get started.</p>
    <%
    }
    else{
      // displays private conversations for user logged in
      %>
          <h2>Direct Messages</h2>
      <%
      List<String> pals = currUser.getPals();
      for (String pal: pals) {
          String url = Utils.getPrivateConversationURL(currentUser, pal);
          %>
          <li><a href="/chat/<%= url %>">
          Chat with <%= pal %> </a></li>
          <%
      }

      // displays public conversations for everyone
      %>
          <h2>Public Conversations</h2>
      <%
      for(Conversation conversation : conversations){
        // only display conversations that are not private
        if (!conversation.getPrivate()) {
    %>
      <li><a href="/chat/<%= conversation.getTitle() %>">
        <%= conversation.getTitle() %></a></li>
    <%
        }
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>
    <br></br>
  </div>
  </div>
</body>

<script>
    document.getElementById("newConversation").focus();
</script>

</html>
