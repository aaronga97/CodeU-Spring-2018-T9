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
            <% /** Iterates through any incoming pal requests for user to accept/decline */
            List<String> incomingRequests = currUser.getIncomingRequests();
            if (incomingRequests.size() > 0) { %>
                <p> You have some notifications! </p>
            <%
            }
            for (String requester: incomingRequests) {
                 /** Lists the incoming requests for current user to accept or decline */ %>
                 <p> <%= requester %> has sent you a pal request: </p>
                 <form action="/users/<%= profileUser %>" method="POST">
                   <button type="submit" name="accept" value="<%= requester %>"> Accept </button>
                   <button type="submit" name="decline" value="<%= requester %>"> Decline </button>
                   <br/>
                 </form>
            <% } %>
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
              <a href="/chat/<%= conversationName %>"> Send a direct message to <%= profileUser %> </a>
            <%
            } else {
                if (currUser.sentPalRequest(profileUser)) {
                    /** States that the current user has already sent this profile user a pal request */ %>
                    <p> Pal Request sent! </p>
                    <%
                } else if (profUser.sentPalRequest(currentUser)) {
                    /** Gives current user the option to accept or decline request on this user profile page */ %>
                    <p> <%= profileUser %> has sent you a pal request: </p>
                      <form action="/users/<%= profileUser %>" method="POST">
                        <button type="submit" name="accept" value="<%= profileUser %>"> Accept </button>
                        <button type="submit" name="decline" value="<%= profileUser %>"> Decline </button>
                        <br/>
                      </form>
                    <%
                } else {
                    /** Gives current user a button to request this user as a pal */ %>
                       <form action="/users/<%= profileUser %>" method="POST">
                         <button type="submit" name="requestPal" value="<%= currentUser %>"> Request Pal </button>
                         <br/>
                       </form>
                    <%
                }
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

</body>
</html>
