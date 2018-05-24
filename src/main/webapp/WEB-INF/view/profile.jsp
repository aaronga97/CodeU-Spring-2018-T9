<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>


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

      <h1>User Profile Pages</h1>

      <% String profilePageUserName = (String) request.getAttribute("username");
      String currentUser = (String) request.getSession().getAttribute("user");

      if (profilePageUserName == "") { %>
          <p> Sorry, this username does not exist. Please pick a profile page to navigate to by typing users/username in the URL.
          </p>
      <% } else {
        String capitalizeFirstLetterName = profilePageUserName.substring(0, 1).toUpperCase() + profilePageUserName.substring(1);
        if (currentUser != null && ((String) currentUser).equals(profilePageUserName)) { %>
                    <a>Welcome to your page!</a>
        <% } else { %>
        <a>Welcome to <%= capitalizeFirstLetterName %>'s Page!</a>
      <% }
      } %>

    </div>
  </div>
</body>
</html>
