package codeu.model.store.basic;

import codeu.model.data.Activity;
import codeu.model.data.Activity.ActivityType;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from
 * and saves to PersistentStorageAgent. It's a singleton so all servlet classes can access
 * the same instance.
 */

public class ActivityStore {

  /** Singleton instance of ActivityStore. */
  private static ActivityStore instance;

  /**
   * Returns the singleton instance of ActivityStore that should be shared between all
   * servlet classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static ActivityStore getInstance() {
    if (instance == null) {
      instance = new ActivityStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static ActivityStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new ActivityStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Activities from and saving Activities
   * to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Activities. */
  private List<Activity> activities = new ArrayList<>();

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private ActivityStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
  }

  /** Add a new activity to the current set of activities known to the application. */
  public void addActivity(Activity activity) {
    activities.add(activity);
    persistentStorageAgent.writeThrough(activity);
  }

  /** Return the list of activities found in this ActivityStore */
  public List<Activity> getActivities() {
    return activities;
  }

  /** Find and return the Activity with the given Id, mainly used for testing. */
  public Activity getActivityWithId(UUID id) {
    for (Activity activity : activities) {
      if(activity.getActivityId().equals(id)) {
        return activity;
      }
    }
    return null;
  }

  /** Find and return all activities associated with a username (case-insensitive). */
  public List<Activity> getUserActivities(String username) {
    List<Activity> activities = new ArrayList<>();

    username = username.toLowerCase();

    for (Activity activity: this.activities) {
      if (activity.getUsername().toLowerCase().equals(username)) {
        activities.add(activity);
      }
    }

    return activities;
  }

  /** Find and return activity associated with a conversationName */
  public Activity getActivityWithConversationName(String conversationName) {

    for (Activity activity: this.activities) {
      if (activity.getActivityType() != ActivityType.REGISTRATION && activity.getConversationName().equals(conversationName)) {
        return activity;
      }
    }
    return null;
  }

  /** Update an existing Activity. */
  public void updateActivity(Activity activity) {
    persistentStorageAgent.writeThrough(activity);
  }

  /** Sets the List of Activities stored by this ActivityStore. */
  public void setActivities(List<Activity> activities) {
    this.activities = activities;
  }
}
