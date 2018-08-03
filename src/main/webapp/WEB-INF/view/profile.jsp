<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.Utils" %>
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
    <div id="inside-container">

      <%
      String currentUser = (String) request.getSession().getAttribute("user");
      User currUser = userStore.getUser(currentUser);
      String profileUser = (String) request.getAttribute("profilePage");
      User profUser = userStore.getUser(profileUser);

      if (profileUser.equals("")) { %>
        <h1 style="color:red">User does not exist.</h1>

      <% } else {
        if (currentUser != null && currentUser.equals(profileUser)) { %>
            <h1>Welcome to your page!</h1>
            <% /** Iterates through any incoming pal requests for user to accept/decline */
            List<String> incomingRequests = currUser.getIncomingRequests();
            if (incomingRequests.size() > 0) { %>
                <h3> You have some notifications! </h3>
            <%
            }
            for (String requester: incomingRequests) {
                 /** Lists the incoming requests for current user to accept or decline */
                 String url = "../users/" + requester;
                 %>
                 <p> <a href=<%= url %> > <%= requester %></a> has sent you a pal request: </p>
                 <form action="/users/<%= profileUser %>" method="POST">
                   <button type="submit" name="accept" value="<%= requester %>"> Accept </button>
                   <button type="submit" name="decline" value="<%= requester %>"> Decline </button>
                   <br/>
                 </form>
            <% } %>

            <% /** Iterates through any outgoing pal requests for user to view */
            List<String> outgoingRequests = currUser.getOutgoingRequests();
            if (outgoingRequests.size() == 0) { %>
                <p> No outgoing requests to view. </p>
            <%
            } else { %>
                <h3> My outgoing pal requests: </h3>
                <% for (String requestee: outgoingRequests) {
                 /** Lists the outgoing requests for current user to view */
                 String url = "../users/" + requestee;
                 %>
                 <li> <a href=<%= url %> > <%= requestee %></a> </li>
             <% }
              } %>
             </br>
      <% } else { %>
            <h1>Welcome to <%= profileUser %>'s Page!</h1>
            </br>
            <%
            if (currentUser != null) {
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
                    </br>
                    </br>
                    <%
                } else {
                    if (currUser.sentPalRequest(profileUser)) {
                        /** States that the current user has already sent this profile user a pal request */ %>
                        <p> Pal Request sent! </p>
                        </br>
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
                        </br>
                        <%
                    }
                }
            }
        } %>
        <% /** Gets the bio of this user to display on their profile page */ %>
            <% String profilePageBio = profUser.getBio(); %>
            <h2>About Me</h2>
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
                <br/>
        <% } %>
        <h2> <%= profileUser %>'s Sent Messages </h2>
        <% List<Message> userMessages = (List) request.getAttribute("messages");
            for (Message m: userMessages) {
                if (!m.isPrivate()) {
            %>
                <a> <strong> <%= Utils.getTime(m.getCreationTime()) %> </strong> : <%= m.getContent() %> </a>
                <br/>
            <%
            }
             } %>

        </br>
        <h2> <%= profileUser %>'s Pals </h2>
        <% List<String> pals = (List) request.getAttribute("pals");
            for (String pal: pals) {
                String url = "../users/" + pal;
                %>
                <li> <a href=<%= url %> > <%= pal %></a> </li>
                <%
            }
        %>
      <% } %>

    </br>
    </br>
    </br>

    </div>
  </div>

</body>
</html>
