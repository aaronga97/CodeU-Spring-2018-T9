<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.Activity" %>
<%@ page import="codeu.model.data.Activity.ActivityType" %>
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

			String authorUrl = "/users/";
			authorUrl += author;

			if (activity.getActivityType() == ActivityType.REGISTRATION) {


	%>

				<li><strong> <a href=<%= authorUrl %> > <%= author%></a>: </strong><%= activity.getMessage() %></li>

	<%
			} else if (activity.getActivityType() == ActivityType.CONVERSATION) {
				String conversation = activity.getConversationName();

				String conversationUrl = "/chat/";
				conversationUrl += conversation;

	%>

				<li><strong> <a href=<%= authorUrl %> > <%= author%></a>: </strong><%= activity.getMessage() %> <strong> <a href=<%= conversationUrl %> > conversation </a> </strong>! </li>

	<%
		}
	}
	%>

		</ul>
	</div>
	<hr/>

  <form action="/activityfeed" method= "POST">
		<input type="text" name="searchQuery">

		<button type = "submit">Search User</button>
	</form>


  </div>

</body>
</html>
