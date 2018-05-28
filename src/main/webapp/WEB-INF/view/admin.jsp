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
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="codeu.model.store.basic.MessageStore" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<!DOCTYPE html>

<%
/** Retrieve an instance of each Datastore */
ConversationStore conversationStore = ConversationStore.getInstance();
UserStore userStore = UserStore.getInstance();
MessageStore messageStore = MessageStore.getInstance();

/** Retrieve sizes of each Datastore */
Integer convSize = conversationStore.countTotalConversations();
Integer userSize = userStore.countTotalUsers();
Integer messageSize = messageStore.countTotalMessages();
%>

<html>
<head>
  <title>Admin page</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@include file= "navbar.jsp"%>

  <div id="container">
    <h1>Admin Page</h1>

    <p> <b>Total Users: </b> <%= userSize %></p>

    <p> <b>Total Messages: </b> <%= messageSize %></p>

    <p> <b>Total Conversations: </b> <%= convSize %></p>

    <p>New features coming soon...</p>

    <form action="/admin" method="POST">
        <p> <b>Make someone admin:<b> </p>
        <input type="text" name="toBeAdminUser" value="" placeholder="username" >
        <br/>
        <button type="submit">Submit</button>
    </form>

  </div>
</body>
</html>
