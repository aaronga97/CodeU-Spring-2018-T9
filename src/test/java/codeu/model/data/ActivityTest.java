package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class ConversationTest {

  @Test
  public void testCreate() {
    UUID activityId = UUID.randomUUID();
		Instant creation = Instant.now();
		String message = "Test_Message";
		UUID userID = UUID.randomUUID();
    String username = "Test_Username";

    Activity activity = new Activity(activityId, creation, message, userId, username);

    Assert.assertEquals(activityID, activity.getActivityId());
		Assert.assertEquals(0, activity.getAllTimeCount());
    Assert.assertEquals(creation, activity.getCreationTime());
    Assert.assertEquals(message, activity.getMessage());
    Assert.assertEquals(userID, activity.getUserId());
		Assert.assertEquals(username, activity.getUsername());
  }
}
