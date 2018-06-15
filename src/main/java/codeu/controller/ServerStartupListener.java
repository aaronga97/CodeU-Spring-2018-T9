package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.util.UUID;

/**
 * Listener class that fires when the server first starts up, before any servlet classes are
 * instantiated.
 */
public class ServerStartupListener implements ServletContextListener {

  /** Loads data from Datastore. */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      List<User> users = PersistentStorageAgent.getInstance().loadUsers();
      UserStore.getInstance().setUsers(users);

      List<Conversation> conversations = PersistentStorageAgent.getInstance().loadConversations();
      ConversationStore.getInstance().setConversations(conversations);

			Conversation actFeedConversation = PersistentStorageAgent.getInstance().loadActFeedConversation();
			ConversationStore.getInstance().setActFeedConversation(actFeedConversation);

			/**
			 * Checks to see if the actFeedConversation has been set in datastore yet.
			 * If not, creates a new activityFeedConversation and writes it through to datastore.
			 * Should only ever happen one time.
			 */

			if (ConversationStore.getInstance().getActFeedConversation().getId() == null ) {
				Conversation convo = new Conversation(UUID.randomUUID(), UUID.randomUUID(), "actFeedConversation", Instant.now(), false);
				ConversationStore.getInstance().setActFeedConversation(convo);
				PersistentStorageAgent.getInstance().actFeedWriteThrough(convo);
			}

      UserStore userStore = UserStore.getInstance();
      String username = "admin";
      /** Check if admin exists, if not create it */
      if(!userStore.isUserRegistered(username)){
        String password = "admin";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(UUID.randomUUID(), username, hashedPassword, Instant.now(), true);
        userStore.addUser(user);
      }

      List<Message> messages = PersistentStorageAgent.getInstance().loadMessages();
      MessageStore.getInstance().setMessages(messages);

    } catch (PersistentDataStoreException e) {
      System.err.println("Server didn't start correctly. An error occurred during Datastore load!");
      System.err.println("This is usually caused by loading data that's in an invalid format.");
      System.err.println("Check the stack trace to see exactly what went wrong.");
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}
}
