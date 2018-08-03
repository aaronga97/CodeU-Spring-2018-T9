package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Activity;
import codeu.model.data.Activity.ActivityType;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ActivityStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ActivityFeedServletTest {

  private ActivityFeedServlet activityFeedServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ActivityStore mockActivityStore;

  @Before
  public void setup() {
    activityFeedServlet = new ActivityFeedServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp")).thenReturn(mockRequestDispatcher);

    mockActivityStore = Mockito.mock(ActivityStore.class);
    activityFeedServlet.setActivityStore(mockActivityStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
      Mockito.when(mockSession.getAttribute("user")).thenReturn("testusername");

    List<Activity> fakeActivityList = new ArrayList<>();
    Activity fakeActivity =  new Activity(UUID.randomUUID(), 0, Instant.now(), "test_activity", UUID.randomUUID(), "test_username",
    ActivityType.REGISTRATION, null, null, new double[4], 0);
    fakeActivityList.add(fakeActivity);

    Mockito.when(mockActivityStore.getActivities()).thenReturn(fakeActivityList);

    activityFeedServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("activities", fakeActivityList);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPostSearch() throws IOException, ServletException {
    List<Activity> fakeActivityList = new ArrayList<>();
    Activity fakeActivity =  new Activity(UUID.randomUUID(), 0, Instant.now(), "test_activity", UUID.randomUUID(), "test_username",
    ActivityType.REGISTRATION, null, null, new double[4], 0);
    fakeActivityList.add(fakeActivity);

    Mockito.when(mockRequest.getParameter("searchQuery")).thenReturn("test_activity");
    Mockito.when(mockActivityStore.getUserActivities("test_activity")).thenReturn(fakeActivityList);

    Mockito.when(mockRequest.getParameter("checked")).thenReturn("undefined");
    Mockito.when(mockRequest.getParameter("sortingStyle")).thenReturn("undefined");


    activityFeedServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);

  }

  @Test
  public void testDoPostUncleanedSearchQuery() throws IOException, ServletException {
    List<Activity> fakeActivityList = new ArrayList<>();
    Activity fakeActivity =  new Activity(UUID.randomUUID(), 0, Instant.now(), "test_activity", UUID.randomUUID(), "test_username",
    ActivityType.REGISTRATION, null, null, new double[4], 0);
    fakeActivityList.add(fakeActivity);

    Mockito.when(mockRequest.getParameter("searchQuery")).thenReturn("<h1> test_activity <h1>");
    Mockito.when(mockActivityStore.getUserActivities("test_activity")).thenReturn(fakeActivityList);
    Mockito.when(mockRequest.getParameter("checked")).thenReturn("recent");

    Mockito.when(mockRequest.getParameter("checked")).thenReturn("undefined");
    Mockito.when(mockRequest.getParameter("sortingStyle")).thenReturn("undefined");

    activityFeedServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);

  }

}
