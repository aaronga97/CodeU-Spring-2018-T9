package codeu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet class responsible for the activity feed page. */ 

public class ActivityFeedServlet extends HttpServlet {
	
	/**
	*This function fires when a user requests the /activityfeed URL. It forwards the
	*request to activityfeed.jsp.
	*/
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
	}

}
