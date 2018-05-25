<%-- This navbar has the added functionality of linking a user to their profile page
once they are logged in and adds a link to the site's activity feed --%>

<nav>
	<% String user = (String) request.getSession().getAttribute("user"); %>
	<a id="navTitle" href="/">CodeU Chat App</a>
	<a href="/conversations">Conversations</a>
	<% if(user != null) { %>
		<a href="/users/<%= user %>" > <%= user %> profile</a>
	<% } else { %>
		<a href="/login">Login</a>
	<% } %>
	<a href="/about.jsp">About</a>
	<a href="/activityfeed">ActivityFeed</a>
</nav>
