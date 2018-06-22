package codeu.controller;

import codeu.model.data.Activity;
import codeu.model.store.basic.ActivityStore;

import java.util.List;
import java.util.UUID;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet class responsible for the activity feed page. */

public class ActivityFeedServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ActivityStore activityStore;

  /**
   * Set up state for handling conversation-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setActivityStore(ActivityStore.getInstance());
  }

  /**
   * Sets the ActivityStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setActivityStore(ActivityStore activityStore) {
    this.activityStore = activityStore;
  }

  /**
   *This function fires when a user requests the /activityfeed URL. It forwards the
   *request to activityfeed.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    List<Activity> activities = activityStore.getActivities();

    request.setAttribute("activities", activities);

    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);


  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    String username = (String) request.getParameter("searchQuery");

    List<Activity> activities = activityStore.getUserActivities(username);

    request.setAttribute("activities", activities);

    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
  }

}
