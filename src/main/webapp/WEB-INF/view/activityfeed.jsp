<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
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
		for (Message message : messages) {
	%>

		<li><%= message.getContent() %></li>

	<%
		}
	%>

		</ul>
	</div>	
	<hr/>  	
  </div>
	
</body>
</html>
