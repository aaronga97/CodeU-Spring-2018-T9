<%-- This navbar has the added functionality of linking a user to their profile page
once they are logged in and adds a link to the site's activity feed --%>

<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>

<nav>
	<%
	String username = (String) request.getSession().getAttribute("user");
	User user = UserStore.getInstance().getUser(username);
	%>
	<a id="navTitle" href="/">CodeU Chat App</a>
	<a href="/conversations">Conversations</a>
	<% if(username != null) { %>
		<a href="/users/<%= username %>" > <%= username %>'s Profile</a>
	<% } else { %>
		<a href="/login">Login</a>
	<% } %>
	<a href="/about.jsp">About</a>
	<a href="/activityfeed">ActivityFeed</a>
	<% if(user != null && user.isAdmin()) {%>
		<a href="/admin" > Admin Page</a>
	<% } %>
	<% if(user != null) {%>
    		<a href="/logout" > Logout</a>
    	<% } %>
</nav>
