package codeu.controller;

import codeu.model.data.Activity;
import codeu.model.store.basic.ActivityStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Collections;
import java.util.Comparator;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


/** Servlet class responsible for the activity feed page. */

public class ActivityFeedServlet extends HttpServlet {

  /** Store class that gives access to Activities. */
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
    request.setAttribute("checked", "recent");

    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    List<Activity> activities = (List<Activity>) request.getSession().getAttribute("activities");


    /*If the user posts a search request that isn't an empty string, reassembles the list of activities */
    if (request.getParameter("searchQuery") != null && !request.getParameter("searchQuery").equals("")) {
      String username = request.getParameter("searchQuery");

      String cleanedUsername = Jsoup.clean(username, Whitelist.none());

      activities = activityStore.getUserActivities(cleanedUsername);
      request.setAttribute("searchQuery", cleanedUsername);
    }

    /* If one of the sorting radio buttons are been toggled, sorts the list of activities*/

    if (request.getParameter("sortingStyle") != null) {

      if(request.getParameter("sortingStyle").equals("popular")) {

        /*Sorts activities by allTimeCount field*/
        Collections.sort(activities, new Comparator<Activity>() {
          public int compare(Activity one, Activity other) {
            return other.getAllTimeCount() - one.getAllTimeCount();
          }
        });

        request.setAttribute("checked", "popular");

      } else if (request.getParameter("sortingStyle").equals("recent")) {

        /*Sorts activities by creationTime field*/
        Collections.sort(activities, new Comparator<Activity>() {
          public int compare(Activity one, Activity other) {
            return other.getCreationTime().compareTo(one.getCreationTime());
          }
        });

        request.setAttribute("checked", "recent");

      }


    }

    request.setAttribute("activities", activities);

    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
  }
}
