package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class ActivityTest {

  @Test
  public void testCreate() {
		UUID activityId = UUID.randomUUID();
		Instant creation = Instant.now();
		String message = "Test_Message";
		UUID userId = UUID.randomUUID();
		String username = "Test_Username";
		char type = 'T';

		Activity activity = new Activity(activityId, creation, message, userId, username, type);

		Assert.assertEquals(activityId, activity.getActivityId());
		
		Assert.assertEquals(0, activity.getAllTimeCount());
		activity.increaseAllTimeCount();
		Assert.assertEquals(1, activity.getAllTimeCount());
		
		Assert.assertEquals(creation, activity.getCreationTime());
		Assert.assertEquals(message, activity.getMessage());
		Assert.assertEquals(userId, activity.getUserId());
		Assert.assertEquals(username, activity.getUsername());
		Assert.assertEquals(type, activity.getType());
  }
}
