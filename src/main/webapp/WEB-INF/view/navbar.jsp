<%-- This navbar has the added functionality of linking a user to their profile page
once they are logged in and adds a link to the site's activity feed --%>

<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.User" %>

<nav>
	<%
	String username = (String) request.getSession().getAttribute("user");
	User user = UserStore.getInstance().getUser(username);
	%>
	<a href="/">CodeU Chat App</a>

	<% /** If user is logged in, can view profile page, About, Conversations, Activity Feed, Admin (if admin), Logout */ %>
	<% if (user != null) { %>
	    <a href="/users/<%= username %>" >Hello <%= username %>!</a>
	    <a href="/about.jsp">About</a>
        <a href="/conversations">Conversations</a>
        <a href="/activityfeed">Activity Feed</a>
        <div class="right-nav">
             <% /** If user is admin, can view Admin page */ %>
                <% if (user.isAdmin()) { %>
                    <a href="/admin">Admin</a>
            	<% } %>
            <a href="/logout">Logout</a>
        </div>
    <% }
    /** Else, can only view Login, About */
    else { %>
        <a href="/login">Login</a>
	    <a href="/about.jsp">About</a>
    <% } %>
</nav>
