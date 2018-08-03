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

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them create a conversation
      response.sendRedirect("/login");
      return;
    }

    List<Activity> activities = activityStore.getActivities();

    sortByRecency(activities);

    request.setAttribute("activities", activities);
    request.setAttribute("checked", "recent");

    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    List<Activity> activities = (List<Activity>) request.getSession().getAttribute("activities");
    String toggle = "Undefined";
    String sortingStyle = request.getParameter("sortingStyle") == null? "Undefined":request.getParameter("sortingStyle");

    /*If the user posts a search request that isn't an empty string, reassembles the list of activities */

    if (request.getParameter("searchQuery") != null) {
      if (!request.getParameter("searchQuery").equals("")) {
        String username = request.getParameter("searchQuery");

        String cleanedUsername = Jsoup.clean(username, Whitelist.none());

        activities = activityStore.getUserActivities(cleanedUsername);
        request.setAttribute("searchQuery", cleanedUsername);
      }

        toggle = request.getParameter("checked");

      if (toggle.equals("popular")) {
        request.setAttribute("checked", "popular");
      } else if (toggle.equals("recent")) {
        request.setAttribute("checked", "recent");
      } else if (toggle.equals("trend")) {
        request.setAttribute("checked", "trend");
      } else if (toggle.equals("daily")) {
        request.setAttribute("checked", "daily");
      } else if (toggle.equals("today")) {
        request.setAttribute("checked", "today");
      }

      }


    if(sortingStyle.equals("popular") || toggle.equals("popular")) {

      sortByPopularity(activities);
      request.setAttribute("checked", "popular");

    } else if (sortingStyle.equals("recent") || toggle.equals("recent")) {

      sortByRecency(activities);
      request.setAttribute("checked", "recent");

    } else if (sortingStyle.equals("trend") || toggle.equals("trend")) {

      sortByTrend(activities);
      request.setAttribute("checked", "trend");

    } else if (sortingStyle.equals("daily") || toggle.equals("daily")) {

      sortByDaily(activities);
      request.setAttribute("checked", "daily");

    } else if (sortingStyle.equals("today") || toggle.equals("today")) {

      sortByToday(activities);
      request.setAttribute("checked", "today");

    }

    request.setAttribute("activities", activities);

    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
  }

  /*Sorts activities by allTimeCount field*/
  void sortByPopularity(List<Activity> activities) {
    Collections.sort(activities, (one, other) -> other.getAllTimeCount() - one.getAllTimeCount());
  }

  /*Sorts activities by their popularity 'today'*/
  void sortByToday(List<Activity> activities) {
    Collections.sort(activities, (one, other) -> Double.compare(other.getPopularityToday(), one.getPopularityToday()));
  }

  /*Sorts activities by their daily average*/
  void sortByDaily(List<Activity> activities) {
    Collections.sort(activities, (one, other) -> Double.compare(other.calculateMean(), one.calculateMean()));
  }

  /*Sorts activities by their zScore field*/
  void sortByTrend(List<Activity> activities) {
    Collections.sort(activities, (one, other) -> Double.compare(other.getZScore(), one.getZScore()));
  }

  /*Sorts activities by creationTime field*/
  void sortByRecency(List<Activity> activities) {
    Collections.sort(activities, (one, other) -> other.getCreationTime().compareTo(one.getCreationTime()));

  }

}
