<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>


<!DOCTYPE html>
<html>
<head>
  <title>User Profile Page</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else { %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>User Profile Pages</h1>

      <%
      String currentUser = (String) request.getSession().getAttribute("user");
      String username = (String) request.getAttribute("profilePage");

      if (currentUser != null && currentUser.equals(username)) { %>
        <a>Welcome to your page!</a>
      <% } else { %>
        <a>Welcome to <%= username %>'s Page!</a>
      <% }
      %>

    </div>
  </div>
</body>
</html>
