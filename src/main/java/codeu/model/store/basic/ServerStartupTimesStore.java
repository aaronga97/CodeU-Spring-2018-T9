package codeu.model.store.basic;

import codeu.model.data.ServerStartupTimes;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from
 * and saves to PersistentStorageAgent. It's a singleton so all servlet classes can access
 * the same instance.
 */

public class ServerStartupTimesStore {

  /** Singleton instance of ServerStartupTimesStore. */
  private static ServerStartupTimesStore instance;

  /**
   * Returns the singleton instance of ServerStartupTimesStore that should be shared between all
   * servlet classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static ServerStartupTimesStore getInstance() {
    if (instance == null) {
      instance = new ServerStartupTimesStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static ServerStartupTimesStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new ServerStartupTimesStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Activities from and saving Activities
   * to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory instance of ServerStartupTimes */
  private ServerStartupTimes serverStartupTimes;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private ServerStartupTimesStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
  }

  /** Return the serverStartupTime found in this  */
  public ServerStartupTimes getServerStartupTimes() {
    return serverStartupTimes;
  }


  /** Update the activity feed's instance of serverStartupTimes. */
  public void updateServerStartupTimes(ServerStartupTimes serverStartupTimes) {
    persistentStorageAgent.writeThrough(serverStartupTimes);
  }

  /** Sets the List of Activities stored by this ActivityStore. */
  public void setServerStartupTimes(ServerStartupTimes serverStartupTimes) {
    this.serverStartupTimes = serverStartupTimes;
  }
}
