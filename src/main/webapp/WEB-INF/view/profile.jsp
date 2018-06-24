<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.time.Instant" %>
<%
/** Gets the UserStore instance to access all users. */
UserStore userStore = UserStore.getInstance();
%>

<!DOCTYPE html>
<html>
<head>
  <title>User Profile Page</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@include file= "navbar.jsp"%>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <%
      String currentUser = (String) request.getSession().getAttribute("user");
      User currUser = userStore.getUser(currentUser);
      String profileUser = (String) request.getAttribute("profilePage");
      User profUser = userStore.getUser(profileUser);

      if (profileUser.equals("")) { %>
        <h1>User does not exist.</h1>

      <% } else {
        if (currentUser != null && currentUser.equals(profileUser)) { %>
            <h1 style="color:dodgerblue">Welcome to your page!</h1>
      <% } else if (currentUser != null) { %>
            <h1 style="color:dodgerblue">Welcome to <%= profileUser %>'s Page!</h1>

            <%
            /** If the current user logged in is already pals with the profile user page they are viewing, then allow her/him to message and view their private conversation. Else, allow them to request this person as a pal. */
            if (currUser.isPal(profileUser)) {
              String conversationName = "../chat/";
              String firstUser = currentUser;
              String secondUser = profileUser;

              if (firstUser.compareTo(secondUser) > 0) {
                String temp = firstUser;
                firstUser = secondUser;
                secondUser = temp;
              }
              conversationName = conversationName + firstUser + "-" +secondUser;
              %>
              <a href="/chat/<%= conversationName %>"> View Conversation with <%= profileUser %> </a>
            <%
            } else {
              System.out.println("Not friends yet");
              %>
              <button onclick="requestPal()"> Request Pal </button>
              <%
            }
            %>

        <% } %>
        <% /** Gets the bio of this user to display on their profile page */ %>
            <% String profilePageBio = profUser.getBio(); %>
            <h2 style="color:skyblue">About Me</h2>
            <a> <%= profilePageBio %> </a>
            <br/>
            <br/>
        <% if (currentUser != null && currentUser.equals(profileUser)) { %>
            <% /** Gives current user a form that allows them to edit their own bio */ %>
                <a> Edit your bio here! (only you can see this) </a>
                <form action="/users/<%= currentUser %>" method="POST">
                    <input type="text" name="bio" value="<%= profUser.getBio() %>" >
                    <br/>
                <button type="submit">Submit</button>
                </form>
         <% } %>
         <h2 style="color:skyblue"> <%= profileUser %>'s Sent Messages </h2>
         <% List<Message> userMessages = (List) request.getAttribute("messages");
            for (Message m: userMessages) { %>
                <a> <strong> <%= m.getTime() %> </strong> : <%= m.getContent() %> </a>
                <br/>
            <% } %>
      <% } %>


    </div>
  </div>

  <script>
  function requestPal() {
      document.write("Sent Pal Request!");

      // NEED TO ADD JAVA CODE HERE :(

      /** Adds profile user to list of outgoing requests of current user. */
      currUser.addOutgoingRequest(profileUser);

      /** Adds current user to list of incoming requests to profile user. */
      profUser.addIncomingRequest(currentUser);
  }
  </script>

</body>
</html>
