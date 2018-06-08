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
<%@ page import="java.util.Map" %>

<!DOCTYPE html>

<% Map<String, String> adminStatsMap = (Map<String, String>) request.getAttribute("adminStatsMap");
   String userName = adminStatsMap.get("lastUserName");
   String userTime = adminStatsMap.get("lastUserTime");
   String convName = adminStatsMap.get("lastConversationName");
   String convTime = adminStatsMap.get("lastConversationTime");
   String messageName = adminStatsMap.get("lastMessageContent");
   String messageTime = adminStatsMap.get("lastMessageTime");
   String messageUser = adminStatsMap.get("lastMessageUser"); %>

<html>
<head>
  <title>Admin page</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@include file= "navbar.jsp"%>

  <div id="container">
    <h1>Admin Page</h1>

      <p> <b>Total users: </b> <%= adminStatsMap.get("userSize")%></p>
      <p> <b>Total conversations: </b> <%= adminStatsMap.get("convSize")%></p>
      <p> <b>Total messages: </b> <%= adminStatsMap.get("messageSize")%></p>

      <p> <b>Last User created: </b> <a href="/users/<%= userName %>"><%= userName %></a> at <%= userTime %></p>
      <p> <b>Last Conversation created: </b> <a href="/chat/<%= convName %>"><%= convName %></a> at <%= convTime %></p>
      <p> <b>Last Message sent: </b> "<%= messageName %>" by <a href="/users/<%= messageUser %>"> <%= messageUser %></a> at <%= messageTime %></p>

    <br>

    <form action="/admin" method="POST">
        <p> <b>Make someone admin:<b> </p>
        <input type="text" name="toBeAdminUser" value="" placeholder="username" >
        <br/>
        <button type="submit">Submit</button>
    </form>

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } else if(request.getAttribute("success") != null){%>
        <h2 style="color:green"><%= request.getAttribute("success") %></h2>
    <% } %>

      <br><br><br>
  </div>
</body>
</html>
