<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>

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
      String username = (String) request.getAttribute("profilePage");
      User thisUser = userStore.getUser(username);

      if (username.equals("")) { %>
        <h1>User does not exist.</h1>
      <% } else if (currentUser != null && currentUser.equals(username)) { %>
        <h1>Welcome to your page!</h1>
        <br/>
        <a> Edit your bio here! (only you can see this) </a>
        <form action="/users/<%= currentUser %>" method="POST">
            <input type="text" name="bio" value="<%= thisUser.getBio() %>" >
            <br/>
            <button type="submit">Submit</button>
        </form>
      <% } else { %>
        <h1>Welcome to <%= username %>'s Page!</h1>
        <br/>
        <a> About Me </a>
        <br/>
        <% String profilePageBio = thisUser.getBio(); %>
        <a> <%= profilePageBio %> </a>
      <% }
      %>

    </div>
  </div>
</body>
</html>
