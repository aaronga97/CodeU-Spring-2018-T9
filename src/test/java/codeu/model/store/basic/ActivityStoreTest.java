package codeu.model.store.basic;

import codeu.model.data.Activity;
import codeu.model.data.Type;
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

  private final Activity ACTIVITY_ONE = new Activity(activityId, 8, Instant.ofEpochMilli(1000), "activity_one", UUID.randomUUID(), "test_user1", Type.CONVERSATION, UUID.randomUUID(), "test_conversation_name");

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    activityStore = activityStore.getTestInstance(mockPersistentStorageAgent);

    final List<Activity> activityList = new ArrayList<>();
    activityList.add(ACTIVITY_ONE);
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
  public void testAddActivity() {
    UUID inputActivityId = UUID.randomUUID();
    Activity inputActivity = new Activity(inputActivityId, 1, Instant.ofEpochMilli(1000), "activity_two", UUID.randomUUID(), "test_user2", ActivityType.CONVERSATION, UUID.randomUUID(), "test_conversation_name2");

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
    Assert.assertEquals(expectedActivity.getType(), actualActivity.getType());
    Assert.assertEquals(expectedActivity.getConversationId(), actualActivity.getConversationId());
    Assert.assertEquals(expectedActivity.getConversationName(), actualActivity.getConversationName());
  }
}
