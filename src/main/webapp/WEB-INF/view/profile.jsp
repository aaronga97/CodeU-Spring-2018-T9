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
      String profileUser = (String) request.getAttribute("profilePage");
      User thisUser = userStore.getUser(profileUser);

      if (profileUser.equals("")) { %>
        <h1>User does not exist.</h1>

      <% } else if (currentUser != null && currentUser.equals(profileUser)) { %>
        <h2>Welcome to your page!</h2>
        <% /** Gets the bio of this user to display on their profile page */ %>
        <% String profilePageBio = thisUser.getBio(); %>
        <h3>About Me</h3>
        <a> <%= profilePageBio %> </a>
        <br/>
        <br/>

        <% /** Gives current user a form that allows them to edit their own bio */ %>
        <a> Edit your bio here! (only you can see this) </a>
        <form action="/users/<%= currentUser %>" method="POST">
            <input type="text" name="bio" value="<%= thisUser.getBio() %>" >
            <br/>
            <button type="submit">Submit</button>
        </form>

      <% } else { %>
        <h2>Welcome to <%= profileUser %>'s Page!</h2>
        <h3>About Me</h3>
        <% /** Gets the bio of this user to display on their profile page */ %>
        <% String profilePageBio = thisUser.getBio(); %>
        <a> <%= profilePageBio %> </a>
      <% }
      %>

    </div>
  </div>
</body>
</html>
