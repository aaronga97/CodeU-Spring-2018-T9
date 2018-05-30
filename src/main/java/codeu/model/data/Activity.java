package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

/** Class representing an activity, which is used to log/track 
 * significant/trending data and present this information on the
 * activity feed.
 */

public class Activity {
	
		
	private final UUID activityId;

	/** Used to determine the all time popularity of an activity. */
	private final int allTimeCount;
	/** Will be used to determine the day to day popularity of an activity. */
	//private final int dailyCount;

	/**  Creation time of the activity */
	private final Instant creation;	
	
	/** Used to display an appropriate message about the activity to the activity feed */
	private final String message;

	/** 
	 * User ID and username of the user most relevant to the activity.
	 * For instance, the username of the user who initially created the conversation.	
	 */
	private final UUID userId;
	private final String username;
	
	/**
	 * Constructs a new Activity.
	 *
	 * @param activityId the ID of this activity
	 * @param creation the creation time of this activity
	 * @param message the message associated this activity
	 * @param userID the id of the user associated with this activity
	 * @param username the username of the user associated with this activity
	 * @param 
	 */
	
	public Activity(UUID activityId, Instant creation, String message, UUID userId, String username) {
		this.activityId = activityId;
		this.allTimeCount = 0;
		this.creation = creation;
		this.message = message;
		this.userId = userId;
		this.username = username;
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
	
}
 
