package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.io.Serializable;

/** Class representing an activity, which is used to log/track
 * significant/trending data and present this information on the
 * activity feed.
 */

public class Activity implements Serializable {

  /**
   * Enum used to give a "type" to an activity object,
   * by which one can determine what action the activity object is associated with.
   */

  public enum ActivityType {CONVERSATION, REGISTRATION}

  private final UUID activityId;

  /** Used to determine the all time popularity of an activity. */
  private int allTimeCount;
  /** Will be used to determine the day to day popularity of an activity. */
  //private final int dailyCount;

  /**  Creation time of the activity */
  private final Instant creation;

  /** Used to display an appropriate message about the activity to the activity feed */
  private String message;

  /**
   * User ID and username of the user most relevant to the activity.
   * For instance, the username of the user who initially created the conversation.
   */
  private final UUID userId;
  private final String username;

  /**
   * Declares whether it is an activity object for a conversation or for a user registration
   * 'C' is used for a conversation while 'R' is used for a registration.
   * May eventually be changed to an enum.
   */
  private final ActivityType type;

  /*
   * Gives the ID of the conversation this activity is associated with, null if not associated
   * with any conversation.
   */

  private final UUID conversationId;

  /**
   * Gives the name of the conversation this activity is associated with, null if not associated
   * with any conversation.
   */

  private final String conversationName;

  /**
   * Gives the day by day popularity of an activity object
   * The third index in the array represents the current 'day',
   * while every other index represents the previous 3 'days'
   */

  private double[] dailyPopularity;

  /**
   * Gives the z-score of an activty, which represents
   * how many standard deviations its popularity for the day (3rd index of dailyPopularity),
   * is above or below it's mean popularity for the last 4 'days' (all indexes of dailyPopularity)
   * Used to determine an activity's trend
   */

  private double zScore;

  /**
   * Constructs a new Activity.
   *
   * @param activityId the ID of this activity
   * @param allTimeCount the count of this activity
   * @param creation the creation time of this activity
   * @param message the message associated this activity
   * @param userId the id of the user associated with this activity
   * @param username the username of the user associated with this activity
   * @param
   */
  public Activity(UUID activityId, int allTimeCount, Instant creation, String message, UUID userId, String username, ActivityType type, UUID conversationId, String conversationName, double[] dailyPopularity, double zScore) {
    this.activityId = activityId;
    this.allTimeCount = allTimeCount;
    this.creation = creation;
    this.message = message;
    this.userId = userId;
    this.username = username;
    this.type = type;
    this.conversationId = conversationId;
    this.conversationName = conversationName;
    this.dailyPopularity = dailyPopularity;
    this.zScore = zScore;

  }

  /** Returns the ID of this activity. */
  public UUID getActivityId() {
    return activityId;
  }

  /** Returns the all time count of this activity */
  public int getAllTimeCount() {
    return allTimeCount;
  }

  /** Returns the creation time of this activity */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the message associated with the activity */
  public String getMessage() {
    return message;
  }

  /** Returns the user id of the user associated with the activity */
  public UUID getUserId() {
    return userId;
  }

  /** Returns the username of the user associated with the activity */
  public String getUsername() {
    return username;
  }

  /** Returns the type of activity **/
  public ActivityType getActivityType() {
    return type;
  }

  /** Returns the id of the conversation associated with activity, if n/a returns null */
  public UUID getConversationId() {
    return conversationId;
  }

  /** Returns the name of the conversation associated with activity, if n/a returns null */
  public String getConversationName() {
    return conversationName;
  }

  /** Returns 'today's' popularity count (third index in dailyPopularity)*/
  public double getPopularityToday() {
    return dailyPopularity[3];
  }

  public double[] getDailyPopularity() {
    return dailyPopularity;
  }

  /** Returns the zScore of an Activity */
  public double getZScore() {
    return zScore;
  }

  /** Increases the allTimeCount field of an Activity by 1 */
  public void increaseAllTimeCount() {
    allTimeCount += 1;
  }

  /** Increases the popularity count for the current day (3rd index of dailyPopularity) */
  public void increaseDailyPopularity() {
    this.dailyPopularity[3] += 1;
  }

  /** Sets the zScore for an activity object based on its dailyPopularity */
  public void setZScore() {
    if (this.calculateStandardDeviation() != 0) {
      this.zScore = this.calculateZScore();
    } else {
      this.zScore = 0;
    }
  }

  /** Setter for the message field of an Activity */
  public void setMessage(String newMessage) {
    message = newMessage;
  }

  /** Calculates the mean of the dailyPopularity array field */
  public double calculateMean() {
    double sum = 0.0;
      for(double num : dailyPopularity) {
        sum += num;
      }

      double mean = sum/dailyPopularity.length;

      return mean;
  }

  /** calculates the standard deviation of the dailyPopularity array */
  private double calculateStandardDeviation() {
    double mean = this.calculateMean();
    double standardDeviation = 0.0;

    for(double num: dailyPopularity) {
      standardDeviation += Math.pow(num - mean, 2);
    }
    System.out.println(Math.sqrt(standardDeviation/dailyPopularity.length));
    System.out.println(mean);
    return Math.sqrt(standardDeviation/dailyPopularity.length);
  }

  /** Calculates the ZScore of the dailyPopularity array field */
  private double calculateZScore() {
    double zScore = (this.getPopularityToday() - this.calculateMean())/this.calculateStandardDeviation();
    return zScore;
  }
}
