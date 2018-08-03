<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.Activity" %>
<%@ page import="codeu.model.data.Utils" %>
<%@ page import="codeu.model.data.Activity.ActivityType" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<%
	List<Activity> activities = (List<Activity>) request.getAttribute("activities");
	request.getSession().setAttribute("activities", activities );
	String checked = (String) request.getAttribute("checked");
%>

<!DOCTYPE html>
<html>
<head>
	<title>Activity Feed</title>
	<link rel="stylesheet" href="/css/main.css" type="text/css">

	<%@include file= "chatbox.jsp"%>

	<script type="text/javascript">

		function callServlet(methodType) {
			document.getElementById("sortingForm").action="/activityfeed";
			document.getElementById("sortingForm").method = methodType;
			document.getElementById("sortingForm").submit();
		}


	</script>


</head>
<body onload= "scrollChat()">

	<%@include file= "navbar.jsp"%>

	<div id="container">
 		<h1>Activity Feed
			<a href="" style="float: right">&#8635;</a></h1>

		<div>
			<form id="sortingForm" action="/activityfeed" method= "POST">
					<input type="radio" id="recencySort" name="sortingStyle" onclick="callServlet('POST')" value="recent" <%= checked != null && checked.equals("recent")? "checked":""%> > Recent
					<input type="radio" id="popularitySort" name="sortingStyle" onclick="callServlet('POST')" value="popular" <%= checked != null && checked.equals("popular")? "checked":""%> > Popular
					<input type="radio" id="trendSort" name="sortingStyle" onclick="callServlet('POST')" value="trend" <%= checked != null && checked.equals("trend")? "checked":""%> > Trending
					<input type="radio" id="dailySort" name="sortingStyle" onclick="callServlet('POST')" value="daily" <%= checked != null && checked.equals("daily")? "checked":""%> > Popular this Week
					<input type="radio" id="todaySort" name="sortingStyle" onclick="callServlet('POST')" value="today" <%= checked != null && checked.equals("today")? "checked":""%> > Popular Today
			</form>

		</div>

	<div id="chat">
		<ul>
	<%
		for (Activity activity : activities) {
			String author = activity.getUsername();

			String authorUrl = "/users/";
			authorUrl += author;

			String activityHour = Utils.getTime(activity.getCreationTime());
			activityHour = activityHour.charAt(0) + activityHour.substring(1, activityHour.length()).toLowerCase();

			if (activity.getActivityType() == ActivityType.REGISTRATION) {


	%>

				<li><strong> <a href=<%= authorUrl %> > <%= author%></a>: </strong><%= activity.getMessage() %> <small><sub> <%= activityHour %></sub></small> </li>

	<%
			} else if (activity.getActivityType() == ActivityType.CONVERSATION) {
				String conversation = activity.getConversationName();

				String conversationUrl = "/chat/";
				conversationUrl += conversation;
				String conversationName = activity.getConversationName();
				String messageCount = "" + (activity.getAllTimeCount() - 1);
	%>

				<li><strong> <a href=<%= authorUrl %> > <%= author%></a>: </strong><%= activity.getMessage() %> <strong> <a href=<%= conversationUrl %> >conversation</a></strong>(<%= conversationName%>)<small><sub> <%= activityHour %> | message count: <%= messageCount %></sub></small> </li>

	<%
		}
	}
	%>

		</ul>
	</div>
	<hr/>

  <form action="/activityfeed" method= "POST">
		<input type="text" name="searchQuery">
		<input type="hidden" name="checked" value=<%= checked%> >
		<button type = "submit">Search User</button>
	</form>


  </div>

</body>
</html>
