// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.store.persistence;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Activity;
import codeu.model.data.Activity.ActivityType;
import codeu.model.data.ServerStartupTimes;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.lang.*;

/**
 * This class handles all interactions with Google App Engine's Datastore service. On startup it
 * sets the state of the applications's data objects from the current contents of its Datastore. It
 * also performs writes of new of modified objects back to the Datastore.
 */
public class PersistentDataStore {

  // Handle to Google AppEngine's Datastore service.
  private DatastoreService datastore;

  /**
   * Constructs a new PersistentDataStore and sets up its state to begin loading objects from the
   * Datastore service.
   */
  public PersistentDataStore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Loads all User objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<User> loadUsers() throws PersistentDataStoreException {

    List<User> users = new ArrayList<>();

    // Retrieve all users from the datastore.
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        String userName = (String) entity.getProperty("username");
        String passwordHash = (String) entity.getProperty("password_hash");
        String email = (String) entity.getProperty("email");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String bio = (String) entity.getProperty("bio");
        Boolean admin = Boolean.parseBoolean((String) entity.getProperty("admin"));
        ArrayList<String> pals = (ArrayList<String>) entity.getProperty("pals");
        ArrayList<String> incomingRequests = (ArrayList<String>) entity.getProperty("incoming_requests");
        ArrayList<String> outgoingRequests = (ArrayList<String>) entity.getProperty("outgoing_requests");
        User user = new User(uuid, userName, passwordHash, email, creationTime, admin);
        user.setBio(bio);
        user.setPals(pals);
        user.setIncomingRequests(incomingRequests);
        user.setOutgoingRequests(outgoingRequests);
        users.add(user);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return users;
  }

  /**
   * Loads all Conversation objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Conversation> loadConversations() throws PersistentDataStoreException {

    List<Conversation> conversations = new ArrayList<>();

    // Retrieve all conversations from the datastore.
    Query query = new Query("chat-conversations").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID ownerUuid = UUID.fromString((String) entity.getProperty("owner_uuid"));
        String title = (String) entity.getProperty("title");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        boolean privateConversation = Boolean.parseBoolean((String) entity.getProperty("private_conversation"));
        Conversation conversation = new Conversation(uuid, ownerUuid, title, creationTime, privateConversation);
        conversations.add(conversation);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return conversations;
  }

  /**
   * Loads the ServerStartupTimes object from the Datastore service and returns it
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */

  public ServerStartupTimes loadServerStartupTimes() throws PersistentDataStoreException {

    // Retrieve the ServerStartupTime.
    Query query = new Query("serverStartupTimes");
    PreparedQuery results = datastore.prepare(query);

    UUID uuid = null;
    Instant referenceServerStartupTime = null;
    Instant currentServerStartupTime = null;

    for (Entity entity : results.asIterable()) {
      try {
        uuid = UUID.fromString((String) entity.getProperty("uuid"));
        referenceServerStartupTime = Instant.parse((String) entity.getProperty("referenceServerStartupTime"));
        currentServerStartupTime = Instant.parse((String) entity.getProperty("currentServerStartupTime"));
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    ServerStartupTimes serverStartupTimes = new ServerStartupTimes(uuid, referenceServerStartupTime, currentServerStartupTime);
    return serverStartupTimes;
  }

  /**
   * Loads all Message objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Message> loadMessages() throws PersistentDataStoreException {

    List<Message> messages = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-messages").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID conversationUuid = UUID.fromString((String) entity.getProperty("conv_uuid"));
        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String content = (String) entity.getProperty("content");
        boolean privateMessage = Boolean.parseBoolean((String) entity.getProperty("private_message"));
        Message message = new Message(uuid, conversationUuid, authorUuid, content, creationTime, privateMessage);
        messages.add(message);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return messages;
  }

	  /**
   * Loads all Activity objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Activity> loadActivities(boolean newDay) throws PersistentDataStoreException {

    List<Activity> activities = new ArrayList<>();

    // Retrieve all activities from the datastore.
    Query query = new Query("activity-objects").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID activityUuid = UUID.fromString((String) entity.getProperty("activity_uuid"));
        int allTimeCount = Integer.parseInt((String) entity.getProperty("allTimeCount"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String message = (String) entity.getProperty("message");
        UUID userUuid = UUID.fromString((String) entity.getProperty("user_uuid"));
        String username = (String) entity.getProperty("username");
        ActivityType type = ActivityType.valueOf((String) entity.getProperty("type"));
        UUID conversationId = null;
        if (entity.getProperty("conversation_uuid") != null) {
          conversationId = UUID.fromString((String) entity.getProperty("conversation_uuid"));
        }
        String conversationName = (String) entity.getProperty("conversation_name");

        double[] dailyPopularity = new double[4];

        double zScore = Double.parseDouble((String) entity.getProperty("zScore"));

        if (newDay && type == ActivityType.CONVERSATION) {
          dailyPopularity[0] = Double.parseDouble((String) entity.getProperty("dailyPopularity[1]"));
          dailyPopularity[1] = Double.parseDouble((String) entity.getProperty("dailyPopularity[2]"));
          dailyPopularity[2] = Double.parseDouble((String) entity.getProperty("dailyPopularity[3]"));
          dailyPopularity[3] = 0;
        } else {
          dailyPopularity[0] = Double.parseDouble((String) entity.getProperty("dailyPopularity[0]"));
          dailyPopularity[1] = Double.parseDouble((String) entity.getProperty("dailyPopularity[1]"));
          dailyPopularity[2] = Double.parseDouble((String) entity.getProperty("dailyPopularity[2]"));
          dailyPopularity[3] = Double.parseDouble((String) entity.getProperty("dailyPopularity[3]"));
        }

        Activity activity = new Activity(activityUuid, allTimeCount, creationTime, message, userUuid, username, type, conversationId, conversationName, dailyPopularity, zScore);
        if(newDay && type == ActivityType.CONVERSATION) {
          activity.setZScore();
          this.writeThrough(activity);
        }
        activities.add(activity);

        } catch (Exception e) {
          // In a production environment, errors should be very rare. Errors which may
          // occur include network errors, Datastore service errors, authorization errors,
          // database entity definition mismatches, or service mismatches.
          throw new PersistentDataStoreException(e);
        }
      }

      return activities;
  }

  /** Write a User object to the Datastore service. */
  public void writeThrough(User user) {
    Entity userEntity = new Entity("chat-users", user.getId().toString());
    userEntity.setProperty("uuid", user.getId().toString());
    userEntity.setProperty("username", user.getName());
    userEntity.setProperty("password_hash", user.getPasswordHash());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("creation_time", user.getCreationTime().toString());
    userEntity.setProperty("bio", user.getBio());
    userEntity.setProperty("admin", Boolean.toString(user.isAdmin()));
    userEntity.setUnindexedProperty("pals", user.getPals());
    userEntity.setUnindexedProperty("incoming_requests", user.getIncomingRequests());
    userEntity.setUnindexedProperty("outgoing_requests", user.getOutgoingRequests());
    datastore.put(userEntity);
  }

  /** Write a Message object to the Datastore service. */
  public void writeThrough(Message message) {
    Entity messageEntity = new Entity("chat-messages", message.getId().toString());
    messageEntity.setProperty("uuid", message.getId().toString());
    messageEntity.setProperty("conv_uuid", message.getConversationId().toString());
    messageEntity.setProperty("author_uuid", message.getAuthorId().toString());
    messageEntity.setProperty("content", message.getContent());
    messageEntity.setProperty("creation_time", message.getCreationTime().toString());
    messageEntity.setProperty("private_message", Boolean.toString(message.isPrivate()));
    datastore.put(messageEntity);
  }

  /** Write a Conversation object to the Datastore service. */
  public void writeThrough(Conversation conversation) {
    Entity conversationEntity = new Entity("chat-conversations", conversation.getId().toString());
    conversationEntity.setProperty("uuid", conversation.getId().toString());
    conversationEntity.setProperty("owner_uuid", conversation.getOwnerId().toString());
    conversationEntity.setProperty("title", conversation.getTitle());
    conversationEntity.setProperty("creation_time", conversation.getCreationTime().toString());
    conversationEntity.setProperty("private_conversation", Boolean.toString(conversation.getPrivate()));
    datastore.put(conversationEntity);
  }

  /** Write an Activity object to the Datastore service. */
  public void writeThrough(Activity activity) {
    Entity activityEntity = new Entity("activity-objects", activity.getActivityId().toString());
    activityEntity.setProperty("activity_uuid", activity.getActivityId().toString());
    activityEntity.setProperty("allTimeCount", Integer.toString(activity.getAllTimeCount()));
    activityEntity.setProperty("creation_time", activity.getCreationTime().toString());
    activityEntity.setProperty("message", activity.getMessage());
    activityEntity.setProperty("user_uuid", activity.getUserId().toString());
    activityEntity.setProperty("username", activity.getUsername());
    activityEntity.setProperty("type", activity.getActivityType().toString());
    if (activity.getConversationId() == null) {
      activityEntity.setProperty("conversation_uuid", activity.getConversationId());
    } else {
      activityEntity.setProperty("conversation_uuid", activity.getConversationId().toString());
    }
    activityEntity.setProperty("conversation_name", activity.getConversationName());

    /** Write each member of dailyPopularity array seperately */
    activityEntity.setProperty("dailyPopularity[0]", Double.toString(activity.getDailyPopularity()[0]));
    activityEntity.setProperty("dailyPopularity[1]", Double.toString(activity.getDailyPopularity()[1]));
    activityEntity.setProperty("dailyPopularity[2]", Double.toString(activity.getDailyPopularity()[2]));
    activityEntity.setProperty("dailyPopularity[3]", Double.toString(activity.getDailyPopularity()[3]));

    activityEntity.setProperty("zScore", Double.toString(activity.getZScore()));

    datastore.put(activityEntity);
  }

  /** Write a ServerStartupTimes object to the Datastore service. */
  public void writeThrough(ServerStartupTimes serverStartupTimes) {
    Entity serverStartupTimesEntity = new Entity("serverStartupTimes", serverStartupTimes.getServerStartupTimesId().toString());
    serverStartupTimesEntity.setProperty("uuid", serverStartupTimes.getServerStartupTimesId().toString());
    serverStartupTimesEntity.setProperty("referenceServerStartupTime", serverStartupTimes.getReferenceServerStartupTime().toString());
    serverStartupTimesEntity.setProperty("currentServerStartupTime", serverStartupTimes.getCurrentServerStartupTime().toString());

    datastore.put(serverStartupTimesEntity);
  }

}
