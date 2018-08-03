package codeu.model.store.persistence;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Activity;
import codeu.model.data.Activity.ActivityType;
import codeu.model.data.ServerStartupTimes;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.time.Instant;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PersistentDataStore. The PersistentDataStore class relies on DatastoreService,
 * which in turn relies on being deployed in an AppEngine context. Since this test doesn't run in
 * AppEngine, we use LocalServiceTestHelper to do all of the AppEngine setup so we can test. More
 * info: https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
 */
public class PersistentDataStoreTest {

  private PersistentDataStore persistentDataStore;
  private final LocalServiceTestHelper appEngineTestHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setup() {
    appEngineTestHelper.setUp();
    persistentDataStore = new PersistentDataStore();
  }

  @After
  public void tearDown() {
    appEngineTestHelper.tearDown();
  }

  @Test
  public void testSaveAndLoadUsers() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    String nameOne = "test_username_one";
    String passwordHashOne = "$2a$10$BNte6sC.qoL4AVjO3Rk8ouY6uFaMnsW8B9NjtHWaDNe8GlQRPRT1S";
    String email = "team9chatapp@gmail.com";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Boolean admin = false;
    String bio1 = "test_bio1";
    User inputUserOne = new User(idOne, nameOne, passwordHashOne, email, creationOne, admin);
    inputUserOne.setBio(bio1);
    List<String> pals1 = new ArrayList<>();
    List<String> incomingReq1 = new ArrayList<>();
    List<String> outgoingReq1 = new ArrayList<>();

    UUID idTwo = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String nameTwo = "test_username_two";
    String passwordHashTwo = "$2a$10$ttaMOMMGLKxBBuTN06VPvu.jVKif.IczxZcXfLcqEcFi1lq.sLb6i";
    String email2 = "fakeEmail@gmail.com";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    String bio2 = "test_bio2";
    User inputUserTwo = new User(idTwo, nameTwo, passwordHashTwo,email2, creationTwo, admin);
    inputUserTwo.setBio(bio2);
    List<String> pals2 = new ArrayList<>();
    List<String> incomingReq2 = new ArrayList<>();
    List<String> outgoingReq2 = new ArrayList<>();

    pals1.add(nameTwo);
    pals2.add(nameOne);
    inputUserOne.setPals(pals1);
    inputUserTwo.setPals(pals2);

    incomingReq1.add("test_username_three");
    incomingReq1.add("test_username_five");
    outgoingReq1.add("test_username_four");
    incomingReq2.add("test_username_four");
    outgoingReq2.add("test_username_three");
    outgoingReq2.add("test_username_five");
    inputUserOne.setIncomingRequests(incomingReq1);
    inputUserOne.setOutgoingRequests(outgoingReq1);
    inputUserTwo.setIncomingRequests(incomingReq2);
    inputUserTwo.setOutgoingRequests(outgoingReq2);

    // save
    persistentDataStore.writeThrough(inputUserOne);
    persistentDataStore.writeThrough(inputUserTwo);

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what we saved matches what we loaded
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(idOne, resultUserOne.getId());
    Assert.assertEquals(nameOne, resultUserOne.getName());
    Assert.assertEquals(passwordHashOne, resultUserOne.getPasswordHash());
    Assert.assertEquals(email, resultUserOne.getEmail());
    Assert.assertEquals(creationOne, resultUserOne.getCreationTime());
    Assert.assertEquals(bio1, resultUserOne.getBio());
    Assert.assertEquals(pals1, resultUserOne.getPals());
    Assert.assertEquals(incomingReq1, resultUserOne.getIncomingRequests());
    Assert.assertEquals(outgoingReq1, resultUserOne.getOutgoingRequests());

    User resultUserTwo = resultUsers.get(1);
    Assert.assertEquals(idTwo, resultUserTwo.getId());
    Assert.assertEquals(nameTwo, resultUserTwo.getName());
    Assert.assertEquals(passwordHashTwo, resultUserTwo.getPasswordHash());
    Assert.assertEquals(email2, resultUserTwo.getEmail());
    Assert.assertEquals(creationTwo, resultUserTwo.getCreationTime());
    Assert.assertEquals(bio2, resultUserTwo.getBio());
    Assert.assertEquals(pals2, resultUserTwo.getPals());
    Assert.assertEquals(incomingReq2, resultUserTwo.getIncomingRequests());
    Assert.assertEquals(outgoingReq2, resultUserTwo.getOutgoingRequests());
  }

  @Test
  public void testSaveAndLoadConversations() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID ownerOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String titleOne = "Test_Title";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Boolean privateConversationOne = false;
    Conversation inputConversationOne = new Conversation(idOne, ownerOne, titleOne, creationOne, privateConversationOne);

    UUID idTwo = UUID.fromString("10000002-2222-3333-4444-555555555555");
    UUID ownerTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    String titleTwo = "Test_Title_Two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Boolean privateConversationTwo = false;
    Conversation inputConversationTwo = new Conversation(idTwo, ownerTwo, titleTwo, creationTwo, privateConversationTwo);

    // save
    persistentDataStore.writeThrough(inputConversationOne);
    persistentDataStore.writeThrough(inputConversationTwo);

    // load
    List<Conversation> resultConversations = persistentDataStore.loadConversations();

    // confirm that what we saved matches what we loaded
    Conversation resultConversationOne = resultConversations.get(0);
    Assert.assertEquals(idOne, resultConversationOne.getId());
    Assert.assertEquals(ownerOne, resultConversationOne.getOwnerId());
    Assert.assertEquals(titleOne, resultConversationOne.getTitle());
    Assert.assertEquals(creationOne, resultConversationOne.getCreationTime());
    Assert.assertEquals(privateConversationOne, resultConversationOne.getPrivate());

    Conversation resultConversationTwo = resultConversations.get(1);
    Assert.assertEquals(idTwo, resultConversationTwo.getId());
    Assert.assertEquals(ownerTwo, resultConversationTwo.getOwnerId());
    Assert.assertEquals(titleTwo, resultConversationTwo.getTitle());
    Assert.assertEquals(creationTwo, resultConversationTwo.getCreationTime());
    Assert.assertEquals(privateConversationTwo, resultConversationOne.getPrivate());
  }

  @Test
  public void testSaveAndLoadServerStartupTimes() throws PersistentDataStoreException {
    UUID serverStartupTimesId = UUID.fromString("10000000-2222-3333-4444-555555555555");
    Instant referenceServerStartupTime = Instant.ofEpochMilli(1000);
    Instant currentServerStartupTime = Instant.ofEpochMilli(2000);

    ServerStartupTimes inputServerStartupTimes = new ServerStartupTimes(serverStartupTimesId, referenceServerStartupTime, currentServerStartupTime);

    // save
    persistentDataStore.writeThrough(inputServerStartupTimes);


    // load
    ServerStartupTimes resultServerStartupTimes = persistentDataStore.loadServerStartupTimes();

    // confirm that what we saved matches what we loaded
    Assert.assertEquals(serverStartupTimesId, resultServerStartupTimes.getServerStartupTimesId());
    Assert.assertEquals(referenceServerStartupTime, resultServerStartupTimes.getReferenceServerStartupTime());
    Assert.assertEquals(currentServerStartupTime, resultServerStartupTimes.getCurrentServerStartupTime());

  }

  @Test
  public void testSaveAndLoadMessages() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID conversationOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID authorOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String contentOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    boolean privateMessageOne = false;
    Message inputMessageOne =
        new Message(idOne, conversationOne, authorOne, contentOne, creationOne, privateMessageOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID conversationTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
    UUID authorTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String contentTwo = "test content one";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    boolean privateMessageTwo = true;
    Message inputMessageTwo =
        new Message(idTwo, conversationTwo, authorTwo, contentTwo, creationTwo, privateMessageTwo);

        // save
    persistentDataStore.writeThrough(inputMessageOne);
    persistentDataStore.writeThrough(inputMessageTwo);

    // load
    List<Message> resultMessages = persistentDataStore.loadMessages();


    Message resultMessageOne = resultMessages.get(0);
    Assert.assertEquals(idOne, resultMessageOne.getId());
    Assert.assertEquals(conversationOne, resultMessageOne.getConversationId());
    Assert.assertEquals(authorOne, resultMessageOne.getAuthorId());
    Assert.assertEquals(contentOne, resultMessageOne.getContent());
    Assert.assertEquals(creationOne, resultMessageOne.getCreationTime());
    Assert.assertEquals(privateMessageOne, resultMessageOne.isPrivate());

    Message resultMessageTwo = resultMessages.get(1);
    Assert.assertEquals(idTwo, resultMessageTwo.getId());
    Assert.assertEquals(conversationTwo, resultMessageTwo.getConversationId());
    Assert.assertEquals(authorTwo, resultMessageTwo.getAuthorId());
    Assert.assertEquals(contentTwo, resultMessageTwo.getContent());
    Assert.assertEquals(creationTwo, resultMessageTwo.getCreationTime());
    Assert.assertEquals(privateMessageTwo, resultMessageTwo.isPrivate());
  }

  @Test
  public void testSaveAndLoadActivities() throws PersistentDataStoreException {
    UUID activityIdOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    int allTimeCountOne = 1;
    Instant creationOne = Instant.ofEpochMilli(1000);
    String messageOne = "test message one";
    UUID userUuidOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String usernameOne = "test username one";
    ActivityType typeOne = ActivityType.CONVERSATION;
    UUID conversationIdOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String conversationNameOne = "test conversation name one";
    double[] dailyPopularityOne = {1, 2, 3, 4};
    double zScoreOne = -1;
    Activity inputActivityOne = new Activity(activityIdOne, allTimeCountOne, creationOne, messageOne, userUuidOne, usernameOne, typeOne, conversationIdOne, conversationNameOne, dailyPopularityOne, zScoreOne);

    UUID activityIdTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    int allTimeCountTwo = 5;
    Instant creationTwo = Instant.ofEpochMilli(2000);
    String messageTwo = "test message two";
    UUID userUuidTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
    String usernameTwo = "test username two";
    ActivityType typeTwo = ActivityType.REGISTRATION;
    UUID conversationIdTwo = null;
    String conversationNameTwo = null;
    double[] dailyPopularityTwo = {0, 0, 0, 0};
    double zScoreTwo = 0;

    Activity inputActivityTwo = new Activity(activityIdTwo, allTimeCountTwo, creationTwo, messageTwo, userUuidTwo, usernameTwo, typeTwo, conversationIdTwo, conversationNameTwo, dailyPopularityTwo, zScoreTwo);

    // save
    persistentDataStore.writeThrough(inputActivityOne);
    persistentDataStore.writeThrough(inputActivityTwo);

    // load
    List<Activity> resultActivities = persistentDataStore.loadActivities(false);

    // confirm that what we saved matches what we loaded
    Activity resultActivityOne = resultActivities.get(0);
    Assert.assertEquals(activityIdOne, resultActivityOne.getActivityId());
    Assert.assertEquals(allTimeCountOne, resultActivityOne.getAllTimeCount());
    Assert.assertEquals(creationOne, resultActivityOne.getCreationTime());
    Assert.assertEquals(messageOne, resultActivityOne.getMessage());
    Assert.assertEquals(userUuidOne, resultActivityOne.getUserId());
    Assert.assertEquals(usernameOne, resultActivityOne.getUsername());
    Assert.assertEquals(typeOne, resultActivityOne.getActivityType());
    Assert.assertEquals(conversationIdOne, resultActivityOne.getConversationId());
    Assert.assertEquals(conversationNameOne, resultActivityOne.getConversationName());
    Assert.assertTrue(Arrays.equals(dailyPopularityOne, resultActivityOne.getDailyPopularity()));
    Assert.assertEquals(zScoreOne, resultActivityOne.getZScore(), 0.0001);

    Activity resultActivityTwo = resultActivities.get(1);
    Assert.assertEquals(activityIdTwo, resultActivityTwo.getActivityId());
    Assert.assertEquals(allTimeCountTwo, resultActivityTwo.getAllTimeCount());
    Assert.assertEquals(creationTwo, resultActivityTwo.getCreationTime());
    Assert.assertEquals(messageTwo, resultActivityTwo.getMessage());
    Assert.assertEquals(userUuidTwo, resultActivityTwo.getUserId());
    Assert.assertEquals(usernameTwo, resultActivityTwo.getUsername());
    Assert.assertEquals(typeTwo, resultActivityTwo.getActivityType());
    Assert.assertEquals(conversationIdTwo, resultActivityTwo.getConversationId());
    Assert.assertEquals(conversationNameTwo, resultActivityTwo.getConversationName());
    Assert.assertTrue(Arrays.equals(dailyPopularityTwo, resultActivityTwo.getDailyPopularity()));
    Assert.assertEquals(zScoreTwo, resultActivityTwo.getZScore(), 0.0001);

    /** Test second method of loading activities, which should activate if a day has passed */
    resultActivities = persistentDataStore.loadActivities(true);

    resultActivityOne = resultActivities.get(0);
    double[] checkArrayOne = {2, 3, 4, 0};
    Assert.assertTrue(Arrays.equals(checkArrayOne, resultActivityOne.getDailyPopularity()));
    Assert.assertEquals(-1.52128, resultActivityOne.getZScore(), 0.0001);

    double[] checkArrayTwo = {0, 0, 0, 0};
    resultActivityTwo = resultActivities.get(1);
    Assert.assertTrue(Arrays.equals(checkArrayTwo, resultActivityTwo.getDailyPopularity()));
    Assert.assertEquals(0, resultActivityTwo.getZScore(), 0.0001);

  }
}
