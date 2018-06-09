package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
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
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;
	
	@Before
	public void setup() {
		activityFeedServlet = new ActivityFeedServlet();

		mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
		Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
		.thenReturn(mockRequestDispatcher);

		mockConversationStore = Mockito.mock(ConversationStore.class);
    activityFeedServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    activityFeedServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    activityFeedServlet.setUserStore(mockUserStore);
  }
	
	@Test
	public void testDoGet() throws IOException, ServletException {
	
		UUID fakeConversationId = UUID.randomUUID();
    Conversation fakeConversation =
        new Conversation(fakeConversationId, UUID.randomUUID(), "test_conversation", Instant.now(), false);
    Mockito.when(mockConversationStore.getActFeedConversation())
        .thenReturn(fakeConversation);
		
		List<Message> fakeMessageList = new ArrayList<>();
		
		Message fakeMessage =  new Message(UUID.randomUUID(),fakeConversationId, 
			UUID.randomUUID(), "test message", Instant.now());
    
		fakeMessageList.add(fakeMessage);
	
    Mockito.when(mockMessageStore.getMessagesInConversation(fakeConversationId))
        .thenReturn(fakeMessageList);
	
		activityFeedServlet.doGet(mockRequest, mockResponse);

		Mockito.verify(mockRequest).setAttribute("conversation", fakeConversation);
		Mockito.verify(mockRequest).setAttribute("messages", fakeMessageList);
		Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
	}
}
