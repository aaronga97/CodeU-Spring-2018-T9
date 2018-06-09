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

package codeu.model.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

/**
 * Class representing a conversation, which can be thought of as a chat room. Conversations are
 * created by a User and contain Messages.
 */
public class Conversation {
  public final UUID id;
  public final UUID owner;
  public final Instant creation;
  public final String title;
  /** This Boolean states whether or not the Conversation is private or not (by default is set to false).
   * Only true when between two users become pals and want to message each other. */
  public Boolean privateConversation;

  /**
   * Constructs a new Conversation.
   *
   * @param id the ID of this Conversation
   * @param owner the ID of the User who created this Conversation
   * @param title the title of this Conversation
   * @param creation the creation time of this Conversation
   */
  public Conversation(UUID id, UUID owner, String title, Instant creation, Boolean privateConvo) {
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.privateConversation = privateConvo;
  }

  /** Returns the ID of this Conversation. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the User who created this Conversation. */
  public UUID getOwnerId() {
    return owner;
  }

  /** Returns the title of this Conversation. */
  public String getTitle() {
    return title;
  }

  /** Returns the creation time of this Conversation. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the boolean stating whether or not this Conversation is private. */
  public Boolean getPrivate() {
    return privateConversation;
  }

  /** Returns the Instant into a String time format to display to users. */
  public String getTime() {
    LocalDateTime localDate = LocalDateTime.ofInstant(creation, ZoneId.systemDefault());
    int hour = localDate.getHour();
    String timeAMPM = "";
    if (hour > 12) {
      hour = hour % 12;
      timeAMPM = "PM";
    } else {
      timeAMPM = "AM";
    }
    String date = localDate.getMonth().toString() + " " + localDate.getDayOfMonth() + ", " + localDate.getYear() + " - " + hour + ":" + localDate.getMinute() + " " + timeAMPM;
    return date;
  }
}
