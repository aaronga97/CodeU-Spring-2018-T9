package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Activity;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
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

	  /** Store class that gives access to Users. */
  private UserStore userStore;

	 /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

	/** Store class that gives access to Conversations. */
	private ActivityStore activityStore;

  /**
   * Set up state for handling conversation-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
		setMessageStore(MessageStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
		setActivityStore(ActivityStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

	/**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		//Conversation conversation = conversationStore.getActFeedConversation();

    //UUID conversationId = conversation.getId();

    List<Activity> activities = activityStore.getActivities();

    request.setAttribute("activities", activities);

		request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);


	}

}
