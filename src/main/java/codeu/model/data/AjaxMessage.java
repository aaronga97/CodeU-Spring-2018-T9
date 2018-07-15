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

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

import java.time.Instant;
import java.util.UUID;

import java.time.LocalDateTime;
import java.time.ZoneId;

/** Class that is exactly as a message but instead of author UUID is the name of the author. */
public class AjaxMessage {

  private UUID id;
  private UUID conversation;
  private String author;
  private String content;
  private String creation;

  //Create an ajaxMessage out of a message received
  public static AjaxMessage toAjaxMessage(Message msg){
    if(msg == null) return null;

    AjaxMessage ajaxMessage = new AjaxMessage();
    String creationTime = Utils.getTime(msg.getCreationTime());

    ajaxMessage.id = msg.getId();
    ajaxMessage.conversation = msg.getConversationId();
    //Replace author id with name
    ajaxMessage.author = UserStore.getInstance().getUser(msg.getAuthorId()).getName();
    ajaxMessage.content = msg.getContent();
    //Get the hour when message was created
    ajaxMessage.creation = creationTime.substring(creationTime.length()-8);

    System.out.println("AjaxMessage created, author: " + ajaxMessage.author);

    return ajaxMessage;
  }


}
