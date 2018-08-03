package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Activity;
import codeu.model.data.ServerStartupTimes;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.basic.ServerStartupTimesStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.time.temporal.ChronoUnit;

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

      ServerStartupTimesStore serverStartupTimesStore = ServerStartupTimesStore.getInstance();

      /** Get the time for when the server first started up */
      Instant currentServerStartupTime = Instant.now();

      List<User> users = PersistentStorageAgent.getInstance().loadUsers();
      UserStore.getInstance().setUsers(users);

      List<Conversation> conversations = PersistentStorageAgent.getInstance().loadConversations();
      ConversationStore.getInstance().setConversations(conversations);

      ServerStartupTimes serverStartupTimes = PersistentStorageAgent.getInstance().loadServerStartupTimes();
      serverStartupTimesStore.setServerStartupTimes(serverStartupTimes);

      UUID serverStartupTimesId = serverStartupTimes.getServerStartupTimesId();

      /** If the ServerStartupTimes in ServerStartupTimes Store hasn't been initialized,
        * this will initialize it. Should only ever happen once.
        */

      if (serverStartupTimesId == null) {
        ServerStartupTimes initServerStartupTimes = new ServerStartupTimes(UUID.randomUUID(), currentServerStartupTime, currentServerStartupTime);
        serverStartupTimesStore.setServerStartupTimes(initServerStartupTimes);
        PersistentStorageAgent.getInstance().writeThrough(initServerStartupTimes);
        serverStartupTimes = initServerStartupTimes;
      }

      /** Check to see if at least a 'day' has passed, if it has load Activities in a special
       * way that shifts the weekly popularity variables and reevalutes every item's trending
       * count.
       */

       serverStartupTimes.setCurrentServerStartupTime(currentServerStartupTime);
       serverStartupTimesStore.updateServerStartupTimes(serverStartupTimes);

       List<Activity> activities;
       ServerStartupTimes originalServerStartupTimes = serverStartupTimesStore.getServerStartupTimes();

       System.out.println(originalServerStartupTimes.compareStartupTimes24());

      if (originalServerStartupTimes.compareStartupTimes24()) {
        activities = PersistentStorageAgent.getInstance().loadActivities(true);
        UUID originalId = originalServerStartupTimes.getServerStartupTimesId();
        ServerStartupTimes newServerStartupTimes = new ServerStartupTimes(originalId, currentServerStartupTime, currentServerStartupTime);
        serverStartupTimesStore.setServerStartupTimes(newServerStartupTimes);
        PersistentStorageAgent.getInstance().writeThrough(newServerStartupTimes);

      } else {
        activities = PersistentStorageAgent.getInstance().loadActivities(false);
      }


      ActivityStore.getInstance().setActivities(activities);

      UserStore userStore = UserStore.getInstance();
      String username = "admin";
      /** Check if admin exists, if not create it */
      if(!userStore.isUserRegistered(username)){
        String password = "admin";
        String email = "team9chatapp@gmail.com";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(UUID.randomUUID(), username, hashedPassword, email, Instant.now(), true);
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
