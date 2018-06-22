<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.Activity" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
List<Activity> activities = (List<Activity>) request.getAttribute("activities");
%>

<!DOCTYPE html>
<html>
<head>
	<title>Activity Feed</title>
	<link rel="stylesheet" href="/css/main.css" type="text/css">

	<%@include file= "chatbox.jsp"%>
</head>
<body onload= "scrollChat()">

	<%@include file= "navbar.jsp"%>

	<div id="container">
 		<h1>Activity Feed
			<a href="" style="float: right">&#8635;</a></h1>

	  <hr/>

	<div id="chat">
		<ul>
	<%
		for (Activity activity : activities) {
		String author = activity.getUsername();

		String url = "/users/";
		url += author;
	%>

		<li><strong> <a href=<%= url %> > <%= author%></a>: </strong><%= activity.getMessage() %></li>

	<%
		}
	%>

		</ul>
	</div>
	<hr/>

  <form action="/activityfeed" method= "POST">
		<input type="text" name="searchQuery">

		<button type = "submit">Search</button>
	</form>


  </div>

</body>
</html>
