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
<%@ page import="java.util.List" %>

<!DOCTYPE html>

<%
ConversationStore conversationStore = ConversationStore.getInstance();
UserStore userStore = UserStore.getInstance();
MessageStore messageStore = MessageStore.getInstance();

List <User> users;
List <Message> messages;
List <Conversation> conversations = conversationStore.getAllConversations();

Integer convSize = conversations.size();
Integer userSize;
Integer messageSize;

System.out.println("Conversations Length: " + convSize + "\n"); %>

<html>
<head>
  <title>Admin page</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>

    <a href="/conversations">Conversations</a>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <h1>Admin Page</h1>

    <p>New features coming soon...</p>


  </div>
</body>
</html>
