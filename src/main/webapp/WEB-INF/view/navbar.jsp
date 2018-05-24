<%-- This navbar has the added functionality of linking a user to their profile page 
once they are logged in and adds a link to the site's activity feed --%>

<nav>
	<a id="navTitle" href="/">CodeU Chat App</a>
	<a href="/conversations">Conversations</a>
	<% if(request.getSession().getAttribute("user") != null) { %>
		<a href="users/<%=request.getSession().getAttribute("user")%>" > <%= request.getSession()
		.getAttribute("user") %> profile</a>
	<% } else { %>
		<a href="/login">Login</a>
	<% } %>
	<a href="/about.jsp">About</a>
	<a href="/activityfeed">ActivityFeed</a>
</nav>
