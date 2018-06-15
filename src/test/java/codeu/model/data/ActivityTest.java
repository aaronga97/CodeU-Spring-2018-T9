package codeu.model.data;

import codeu.model.data.Activity.ActivityType;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class ActivityTest {

  @Test
  public void testCreate() {

    UUID activityId = UUID.randomUUID();
    int allTimeCount = 0;
    Instant creation = Instant.now();
    String message = "Test_Message";
    UUID userId = UUID.randomUUID();
    String username = "Test_Username";
    ActivityType type = ActivityType.CONVERSATION;
    UUID conversationId = UUID.randomUUID();
    String conversationName = "Test_Conversation_Name";

    Activity activity = new Activity(activityId, allTimeCount, creation, message, userId, username, type, conversationId, conversationName);
    Assert.assertEquals(activityId, activity.getActivityId());

    Assert.assertEquals(allTimeCount, activity.getAllTimeCount());
    activity.increaseAllTimeCount();
    Assert.assertEquals(1, activity.getAllTimeCount());

    Assert.assertEquals(creation, activity.getCreationTime());
    Assert.assertEquals(message, activity.getMessage());
    Assert.assertEquals(userId, activity.getUserId());
    Assert.assertEquals(username, activity.getUsername());
    Assert.assertEquals(type, activity.getActivityType());
    Assert.assertEquals(conversationId, activity.getConversationId());
    Assert.assertEquals(conversationName, activity.getConversationName());


  }
}
