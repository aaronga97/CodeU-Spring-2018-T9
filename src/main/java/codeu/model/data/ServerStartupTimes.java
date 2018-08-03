package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.time.Duration;

/**
  * A Class containing two instants in time: A reference instant that is used to determine
  * if at least 24 hours has passed since the last 'day' the app was active, and a 'current' instance
  * that contains the first instant the server was started in the current session.
  * In the server startup listener when at least 24 hours has passed between currentServerStartupTime
  * and referenceServerStartupTime, currentServerStartupTime becomes the new referenceServerStartuptime.
  */

public class ServerStartupTimes {

  /** the unique ID of the singular ServerStartupTimes object **/
  private final UUID serverStartupTimesId;

  /** the reference instant used to determine when at least 24 hours (a 'day') has passed in the app */
  private Instant referenceServerStartupTime;

  /** the first instant that the server was started up for the current session */
  private Instant currentServerStartupTime;

  /**
    * Constructs a new ServerStartupTime
    * @param serverStartupTimesId the unique Id of the serverStartupTimes instance
    * @param referenceServerStartupTime the last instant the server was started up before 'today'
    * @param currentServerStartupTime the first instant that the server was started up for the current session
    */

  public ServerStartupTimes(UUID serverStartupTimesId, Instant referenceServerStartupTime, Instant currentServerStartupTime) {
    this.serverStartupTimesId = serverStartupTimesId;
    this.referenceServerStartupTime = referenceServerStartupTime;
    this.currentServerStartupTime = currentServerStartupTime;
  }

  /**
    * Used to check if 24 hours has passed between the currentServerStartupTime
    * and referenceServerStartupTime fields.
    */

  public boolean compareStartupTimes24() {

    long timePassed = Duration.between(referenceServerStartupTime, currentServerStartupTime).toHours();

    return timePassed >= 24;

  }

  /** Setter for the referenceServerStartupTime attribute */
  public void setReferenceServerStartupTime(Instant newReferenceServerStartupTime) {
    this.referenceServerStartupTime = newReferenceServerStartupTime;
  }

  /** Setter for the currentServerxStartupTime attribute */
  public void setCurrentServerStartupTime(Instant newCurrentServerStartupTime) {
    this.currentServerStartupTime = newCurrentServerStartupTime;
  }

  /** Returns the unique id of the ServerStartupTimes object */
  public UUID getServerStartupTimesId() {
    return serverStartupTimesId;
  }

  /** Returns the referenceServerStartupTime attribute */
  public Instant getReferenceServerStartupTime() {
    return referenceServerStartupTime;
  }

  /** Returns the currentServerStartupTime attribute */
  public Instant getCurrentServerStartupTime() {
    return currentServerStartupTime;
  }

}
