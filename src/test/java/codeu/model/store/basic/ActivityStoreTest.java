package codeu.model.store.basic;

import codeu.model.data.Activity;
import codeu.model.data.Activity.ActivityType;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ActivityStoreTest {
  private ActivityStore activityStore;
  private PersistentStorageAgent mockPersistentStorageAgent;
  private UUID activityId = UUID.randomUUID();
  private UUID activityId2 = UUID.randomUUID();

  private final Activity ACTIVITY_ONE = new Activity(activityId, 1, Instant.ofEpochMilli(1000), "activity_one", UUID.randomUUID(), "test_user1", ActivityType.CONVERSATION, UUID.randomUUID(), "test_conversation_name", new double[4], 0);
  private final Activity ACTIVITY_TWO = new Activity(activityId2, 2, Instant.ofEpochMilli(2000), "activity_two", UUID.randomUUID(), "test_user1", ActivityType.REGISTRATION, null, null, new double[4], 0);
  private final Activity ACTIVITY_THREE = new Activity(UUID.randomUUID(), 3, Instant.ofEpochMilli(3000), "activity_three", UUID.randomUUID(), "test_user2", ActivityType.CONVERSATION, UUID.randomUUID(), "test_conversation_name2", new double[4], 0);

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    activityStore = activityStore.getTestInstance(mockPersistentStorageAgent);

    final List<Activity> activityList = new ArrayList<>();
    activityList.add(ACTIVITY_ONE);
    activityList.add(ACTIVITY_TWO);
    activityList.add(ACTIVITY_THREE);
    activityStore.setActivities(activityList);
  }

  @Test
  public void testGetActivityWithId_found() {
    Activity resultActivity = activityStore.getActivityWithId(activityId);

    assertEquals(ACTIVITY_ONE, resultActivity);
  }

  @Test
  public void testGetActivityWithId_notFound() {
    Activity resultActivity = activityStore.getActivityWithId(UUID.randomUUID());

    Assert.assertNull(resultActivity);
  }

  @Test
  public void testGetUserActivities_found() {
    List<Activity> userActivities = activityStore.getUserActivities("test_user1");

    Assert.assertEquals(2, userActivities.size());
    assertEquals(userActivities.get(0), activityStore.getActivityWithId(activityId));
    assertEquals(userActivities.get(1), activityStore.getActivityWithId(activityId2));
  }

  @Test
  public void testGetUserActivities_notFound() {
    List<Activity> userActivities = activityStore.getUserActivities("user_not_found");

    Assert.assertEquals(0, userActivities.size());
  }

  @Test
  public void testAddActivity() {
    UUID inputActivityId = UUID.randomUUID();
    Activity inputActivity = new Activity(inputActivityId, 1, Instant.ofEpochMilli(1000), "activity_two", UUID.randomUUID(), "test_user2", ActivityType.CONVERSATION, UUID.randomUUID(), "test_conversation_name2", new double[4], 0);

    activityStore.addActivity(inputActivity);
    Activity resultActivity = activityStore.getActivityWithId(inputActivityId);

    assertEquals(inputActivity, resultActivity);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputActivity);
  }

  private void assertEquals(Activity expectedActivity, Activity actualActivity) {
    Assert.assertEquals(expectedActivity.getActivityId(), actualActivity.getActivityId());
    Assert.assertEquals(expectedActivity.getAllTimeCount(), actualActivity.getAllTimeCount());
    Assert.assertEquals(expectedActivity.getCreationTime(), actualActivity.getCreationTime());
    Assert.assertEquals(expectedActivity.getMessage(), actualActivity.getMessage());
    Assert.assertEquals(expectedActivity.getUserId(), actualActivity.getUserId());
    Assert.assertEquals(expectedActivity.getUsername(), actualActivity.getUsername());
    Assert.assertEquals(expectedActivity.getActivityType(), actualActivity.getActivityType());
    Assert.assertEquals(expectedActivity.getConversationId(), actualActivity.getConversationId());
    Assert.assertEquals(expectedActivity.getConversationName(), actualActivity.getConversationName());
    Assert.assertEquals(expectedActivity.getPopularityToday(), actualActivity.getPopularityToday(), 0.0001);
    Assert.assertEquals(expectedActivity.getDailyPopularity(), actualActivity.getDailyPopularity());
    Assert.assertEquals(expectedActivity.getZScore(), actualActivity.getZScore(), 0.0001);
  }
}
