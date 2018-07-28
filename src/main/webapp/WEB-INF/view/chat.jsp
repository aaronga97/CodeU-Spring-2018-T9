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
<%@ page import="codeu.model.data.Utils" %>
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
      //Create interval in which each ajax call will be fired, 1000 = 1 second
      var interval = 1000;
      //Get conversation ID
      var conversationID = $("#conversationID").text();
      //Get messages shown since the last refresh/get request
      var messagesSize = $("#messagesSize").text();
      var jsonSize = 0;

      function getNewMessages() { //when refresh button is clicked call function
          //Make get request to ajaxTest servlet, and execute function with Ajax responseJson
          $.get("/ajaxTest/" + conversationID + "/" + messagesSize, function (responseJson) {
              //Select the unorderedList where I want to append new messages
              var $ul = $("#unorderedMessageList");
              jsonSize = responseJson.length;
              //Iterate through responseJson with new messages
              $.each(responseJson, function(index, message) {

                  var result = "<li><strong><a href=/users/";
                  result += message.author;
                  result += " >";
                  result += message.author;
                  result += "</a>: </strong>";
                  result += message.content;
                  result += "<small><sub> ";
                  result += message.creation;
                  result += "</sub></small></li>";

                  //Append them
                  $($ul).append(result);
              });
              //Update messagesSize to the pastSize + theSize of new added messages, at the end of ajaxRequest
              messagesSize = +messagesSize + jsonSize;
          });
          //After finishing previous ajaxCall, schedule the next one
          setTimeout(getNewMessages, interval);
      };

    //Initiate ajax call
    setTimeout(getNewMessages, interval);
  </script>

  <body>

  <div id="container">

    <h1><%= conversation.getTitle() %>

      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="chat">

      <ul id="unorderedMessageList">

    <%
      for (Message message : messages) {
        String author = UserStore.getInstance().getUser(message.getAuthorId()).getName();

        String url = "/users/";
        url += author;

        String messageHour = Utils.getTime(message.getCreationTime());
        messageHour = messageHour.substring(messageHour.length()-8);
    %>

        <li><strong> <a href=<%= url %> > <%= author%></a>: </strong> <%= message.getContent() %> <small><sub> <%= messageHour %></sub></small></li>

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
    <br></br>
  </div>

</body>

<script>
    document.getElementById("sendMessage").focus();
</script>

</html>
