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
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <%@include file= "chatbox.jsp"%>
</head>
<body onload="scrollChat()">

  <%@include file= "navbar.jsp"%>

  <script src="http://code.jquery.com/jquery-latest.min.js"></script>

  <!-- Check for nulls, etc when it works -->
  <p hidden id="conversationID"><%= conversation.getId() %></p>
  <p hidden id="messagesSize"><%= messages.size() %></p>

  <script>
      //Put this in a separate js file once it works
      var conversationID = $("#conversationID").text();
      var messagesSize = $("#messagesSize").text();

      $(document).on("click", "#refreshButton", function () {
          var newMessagesCounter = 0;
          $.get("/ajaxTest/" + conversationID + "/" + messagesSize, function (responseJson) {
              var $ul = $("<ul>").appendTo($("#chat"));
              $.each(responseJson, function(index, message) {
                  $("<li>").text(message.content).appendTo($ul);
                  newMessagesCounter++;
              });
          });
          //Update messagesSize to the pastSize + theSize of new added messages
          $("#messagesSize").text(newMessagesCounter + + messagesSize);
      });
  </script>

  <body>
  <!-- AJAX TEST ZONE -->
  <h3>Ajax Test</h3>

  <button id="refreshButton">Refresh with Ajax</button>

  <!-- AJAX TEST ZONE -->

  <div id="container">

    <h1><%= conversation.getTitle() %>

      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="chat">

      <ul>

    <%
      for (Message message : messages) {
        String author = UserStore.getInstance().getUser(message.getAuthorId()).getName();

        String url = "/users/";
        url += author;
    %>

        <li><strong> <a href=<%= url %> > <%= author%></a>: </strong> <%= message.getContent() %></li>

    <%
      }
     %>

        <!-- Insert here new array with messages from ajax and add the ones not added yet -->


      </ul>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" method="POST">
        <input type="text" name="message" id="sendMessage">
        <br/>
        <button type="submit">Send</button>
    </form>
    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>

    <hr/>

  </div>

</body>

<script>
    document.getElementById("sendMessage").focus();
</script>

</html>
