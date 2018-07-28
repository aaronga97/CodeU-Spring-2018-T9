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

import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;

import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.ArrayList;

/**
 * Class representing a registered user.
 */
public class User {
    private final UUID id;
    private final String name;
    private String passwordHash;
    private String email;
    private final Instant creation;
    private String bio;
    private Boolean admin;
    private List<String> pals;
    private List<String> incomingRequests;
    private List<String> outgoingRequests;

    /**
     * Constructs a new User with an empty bio.
     * Automatically creates a new Conversation with every existing User.
     *
     * @param id           the ID of this User
     * @param name         the username of this User
     * @param passwordHash the password hash of this User
     * @param email        the email of this User
     * @param creation     the creation time of this User
     * @param admin        states whether this User is an admin
     */
    public User(UUID id, String name, String passwordHash, String email, Instant creation, Boolean admin) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
        this.creation = creation;
        setBio(name + " hasn't written a bio yet.");
        this.admin = admin;
        this.pals = new ArrayList<>();
        this.incomingRequests = new ArrayList<>();
        this.outgoingRequests = new ArrayList<>();
        createConversations();
    }

    /**
     * Returns the ID of this User.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the username of this User.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the password hash of this User.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Returns the email of this User.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets new password hash of this user.
     */
    public void setPasswordHash(String newPasswordHash) {
        this.passwordHash = newPasswordHash;

    }

    /**
     * Returns the creation time of this User.
     */
    public Instant getCreationTime() {
        return creation;
    }

    /**
     * Sets the bio of this User.
     */
    public void setBio(String aboutMe) {
        bio = aboutMe;
    }

    /**
     * Returns the bio of this User.
     */
    public String getBio() {
        return bio;
    }

    /**
     * Returns if user is admin
     */
    public Boolean isAdmin() {
        return admin;
    }

    /**
     * Set admin attribute
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /**
     * Sets the pals of this User.
     */
    public void setPals(List<String> pals) {
        if (pals != null) {
            this.pals = pals;
        }
    }

    /**
     * Returns the pals of this User.
     */
    public List<String> getPals() {
        return pals;
    }

    /** Adds a pal to the list of this User's pals as long as the pal is not this User. */
    public void addPal(String name) {
        /* Validate user input & only add the pal if he/she isn't this User and is not already a pal. */
        if (name.matches("[\\w*\\s*]*") && !name.equals(this.name) && !isPal(name)) {
            this.pals.add(name);
        }
    }

    /** Checks whether a given name is in this User's list of pals. */
    public boolean isPal(String name) {
        return this.pals.contains(name);
    }

    /**
     * Sets the incoming requests of this User.
     */
    public void setIncomingRequests(List<String> incoming) {
        if (incoming != null) {
            this.incomingRequests = incoming;
        }
    }

    /** Adds a new request to incoming requests when someone requests this User as a pal. */
    public void addIncomingRequest(String name) {
        if (!name.equals(this.name)) {
            this.incomingRequests.add(name);
        }
    }

    /** Removes a request from incoming requests when this User accepts/declines a pal. */
    public void deleteIncomingRequest(String name) {
        this.incomingRequests.remove(name);
    }

    /** Checks whether this User has sent another user/name a pal request. */
    public boolean sentPalRequest(String name) {
        return this.outgoingRequests.contains(name);
    }

    /**
     * Returns the incoming requests of this User.
     */
    public List<String> getIncomingRequests() {
        return incomingRequests;
    }

    /**
     * Sets the outgoing requests of this User.
     */
    public void setOutgoingRequests(List<String> outgoing) {
        if (outgoing != null) {
            this.outgoingRequests = outgoing;
        }
    }

    /**
     * Returns the outgoing requests of this User.
     */
    public List<String> getOutgoingRequests() {
        return outgoingRequests;
    }

    /** Adds a new request to outgoing requests when this User requests another person as a pal. */
    public void addOutgoingRequest(String name) {
        if (!name.equals(this.name)) {
            this.outgoingRequests.add(name);
        }
    }

    /** Removes a request from outgoing requests when the other person accepts/declines a request from this User. */
    public void deleteOutgoingRequest(String name) {
        this.outgoingRequests.remove(name);
    }

    /**
     * When a User is initialized, this method creates a private Conversation with every other User that already exists.
     */
    public void createConversations() {
        /* get all Users from UserStore */
        UserStore userStore = UserStore.getInstance();
        List<User> users = userStore.getUsers();

        ConversationStore conversationStore = ConversationStore.getInstance();

        String firstUser = this.name;

        /* For each existing User, create a new Conversation with this User and set to private */
        for (User u : users) {
            /* Sets the order of user's names by alphabetical order. By default, sets this User as firstUser and existing User as secondUser. */
            String secondUser = u.getName();

            String conversationName = Utils.getPrivateConversationURL(firstUser, secondUser);
            Conversation c = new Conversation(UUID.randomUUID(), this.id, conversationName, Instant.now(), true);

            /* Adds new conversation to the ConversationStore */
            conversationStore.addConversation(c);
        }
    }
}
