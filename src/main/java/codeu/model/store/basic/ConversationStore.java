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

package codeu.model.store.basic;

import codeu.model.data.Conversation;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class ConversationStore {

  /** Singleton instance of ConversationStore. */
  private static ConversationStore instance;

  /**
   * Returns the singleton instance of ConversationStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static ConversationStore getInstance() {
    if (instance == null) {
      instance = new ConversationStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static ConversationStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new ConversationStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Conversations from and saving Conversations
   * to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Conversations. */
  private List<Conversation> conversations;

  /** The in-memory set of restricted conversation names reserved for private conversations. */
  private final HashSet<String> restrictedConversationNames;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private ConversationStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    conversations = new ArrayList<>();
    restrictedConversationNames = new HashSet<>();
  }

	/** Access the current set of conversations known to the application. */
  public List<Conversation> getAllConversations() {
    return conversations;
  }

  /** Access the set of restricted conversation names. */
  public HashSet<String> getRestrictedConversationNames() {
      return restrictedConversationNames;
  }

  /** Checks if a name exists in the set of restricted conversation names. */
  public boolean isRestrictedName(String name) {
      return restrictedConversationNames.contains(name);
  }

  /** Add a new conversation to the current set of conversations known to the application. */
  public void addConversation(Conversation conversation) {
    conversations.add(conversation);
    persistentStorageAgent.writeThrough(conversation);

    /* Adds the name to the list of restrictedConversationNames if is a private conversation. */
    if (conversation.getPrivate()) {
        restrictedConversationNames.add(conversation.getTitle());
    }
  }

  /** Check whether a Conversation title is already known to the application. */
  public boolean isTitleTaken(String title) {
    // This approach will be pretty slow if we have many Conversations.
    for (Conversation conversation : conversations) {
      if (conversation.getTitle().equals(title)) {
        return true;
      }
    }
    return false;
  }

  /** Find and return the Conversation with the given title. */
  public Conversation getConversationWithTitle(String title) {
    for (Conversation conversation : conversations) {
      if (conversation.getTitle().equals(title)) {
        return conversation;
      }
    }
    return null;
  }

  /** Accesses the conversations stored
   *  @return the size of conversations list
   */
  public Integer countTotalConversations() {
    return conversations.size();
  }

  /** Sets the List of Conversations stored by this ConversationStore. */
  public void setConversations(List<Conversation> conversations) {
    this.conversations = conversations;
  }

  /** Return latest conversation created */
  public Conversation getLastConversationIndex(){
    if(conversations == null || conversations.isEmpty()) {
      return null;
    }

    int lastConversation = conversations.size()-1;
    return conversations.get(lastConversation);
  }

}
