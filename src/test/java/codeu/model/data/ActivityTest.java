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
    double[] dailyPopularity = {4.0, 2.0, 10.0, 8.0};
    double zScore = -1;

    Activity activity = new Activity(activityId, allTimeCount, creation, message, userId, username, type, conversationId, conversationName, dailyPopularity, zScore);
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
    Assert.assertEquals(dailyPopularity[3], activity.getPopularityToday(), 0.0001);
    Assert.assertEquals(dailyPopularity, activity.getDailyPopularity());
    Assert.assertEquals(zScore, activity.getZScore(), 0.0001);
    Assert.assertEquals(6.0, activity.calculateMean(), 0.0001);
    activity.setZScore();
    Assert.assertEquals(0.63246, activity.getZScore(), 0.0001);

    activity.increaseDailyPopularity();
    Assert.assertEquals(9.0, activity.getPopularityToday(), 0.0001);



  }
}
