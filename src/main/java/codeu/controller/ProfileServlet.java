package codeu.controller;

import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.data.User;
import codeu.model.data.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.util.UUID;
import java.util.List;

/**
 * Servlet class responsible for the profile page.
 */
public class ProfileServlet extends HttpServlet {

    /**
     * Store class that gives access to Conversations.
     */
    private ConversationStore conversationStore;

    /**
     * Store class that gives access to Messages.
     */
    private MessageStore messageStore;

    /**
     * Store class that gives access to Users.
     */
    private UserStore userStore;

    /**
     * Set up state for handling profile page requests.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
        setUserStore(UserStore.getInstance());
    }

    /**
     * Sets the ConversationStore used by this servlet. This function provides a common setup method
     * for use by the test framework or the servlet's init() function.
     */
    void setConversationStore(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }

    /**
     * Sets the MessageStore used by this servlet. This function provides a common setup method for
     * use by the test framework or the servlet's init() function.
     */
    void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    /**
     * Sets the UserStore used by this servlet. This function provides a common setup method for use
     * by the test framework or the servlet's init() function.
     */
    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }

    /**
     * This function fires when a user navigates to a profile page. It checks if the user exists from the username of the URL requested.
     * It then forwards to profile.jsp for rendering.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String requestUrl = request.getRequestURI();
        String profileUsername = requestUrl.substring("/users/".length());

        // see if user exists
        User user = userStore.getUser(profileUsername);

        if (user == null) {
            // if no user exists, set profilePage attribute to be empty string
            request.setAttribute("profilePage", "");
            System.out.println("No user exists.");
        } else {
            // set profilePage attribute of request to be username
            request.setAttribute("profilePage", profileUsername);

            UUID author = user.getId();
            MessageStore m = MessageStore.getInstance();
            List<Message> sentMessages = m.getMessagesOfUser(author);
            List<String> pals = user.getPals();

            // set messages attribute of request to be sentMessages
            request.setAttribute("messages", sentMessages);

            // set pals attribute to be list of user's pals
            request.setAttribute("pals", pals);

        }
        // forward request to profile.jsp
        request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }

    /**
     * This function fires when a user edits the bio on his/her own profile page, accept or decline a pal request, or sends
     * another user a pal request. It then redirects the user back to the profile page they are viewing.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getParameter("bio") != null) {
            updateBio(request);

        } else if (request.getParameter("accept") != null) {
            acceptPal(request);

        } else if (request.getParameter("decline") != null) {
            declinePal(request);

        } else if (request.getParameter("requestPal") != null) {
            sendPalRequest(request);
        }

        // gets the username from the request URL
        String requestUrl = request.getRequestURI();
        String profileUsername = requestUrl.substring("/users/".length());

        // redirects user back to the profile page they are viewing as a GET request
        response.sendRedirect("/users/" + profileUsername);
    }

    /**
     *  This helper function cleans the user input and then updates the User's bio attribute.
     */
    void updateBio(HttpServletRequest request) {
        // gets the username from the request URL
        String requestUrl = request.getRequestURI();
        String profileUsername = requestUrl.substring("/users/".length());

        // gets user
        User user = userStore.getUser(profileUsername);

        // gets bio from request
        String bio = request.getParameter("bio");

        // cleans bio String by removing any HTML tags
        String cleanedBio = Jsoup.clean(bio, Whitelist.none());

        // sets bio as instance variable for user
        user.setBio(cleanedBio);

        // updates UserStore to store the bio for next time program opens
        userStore.updateUser(user);
    }

    /**
     *  This helper function adds the two pals to each other's list of pals after one accepts the pal request, removes
     *  the incoming and outgoing requests and updates both users.
     */
    void acceptPal(HttpServletRequest request) {
        // gets the username from the request URL
        String requestUrl = request.getRequestURI();
        String profileUsername = requestUrl.substring("/users/".length());

        // sets the requestee to be the name of the profile page
        String requestee = profileUsername;
        User requesteeUser = userStore.getUser(requestee);

        // sets the requester to be the name of the user who sent the pal request
        String requester = request.getParameter("accept");
        User requesterUser = userStore.getUser(requester);

        // adds requester to list of this user/requestee's pals
        requesteeUser.addPal(requester);

        // adds this user/requestee to the requester's list of pals
        requesterUser.addPal(requestee);

        // removes requester from this user/requestee's incomingRequests
        requesteeUser.deleteIncomingRequest(requester);

        // removes this user/requestee from requester's outgoingRequests
        requesterUser.deleteOutgoingRequest(requestee);

        // updates both users in UserStore
        userStore.updateUser(requesterUser);
        userStore.updateUser(requesteeUser);
    }

    /**
     *  This helper function simply removes the incoming and outgoing requests after one user declines a pal request and
     *  updates both users.
     */
    void declinePal(HttpServletRequest request) {
        // gets the username from the request URL
        String requestUrl = request.getRequestURI();
        String profileUsername = requestUrl.substring("/users/".length());

        // sets the requestee to be the name of the profile page
        String requestee = profileUsername;
        User requesteeUser = userStore.getUser(requestee);

        // sets the requester to be the name of the user who sent the pal request
        String requester = request.getParameter("decline");
        User requesterUser = userStore.getUser(requester);

        // removes requester from this user/requestee's incomingRequests
        requesteeUser.deleteIncomingRequest(requester);

        // removes this user/requestee from requester's outgoingRequests
        requesterUser.deleteOutgoingRequest(requestee);

        // updates both users in UserStore
        userStore.updateUser(requesterUser);
        userStore.updateUser(requesteeUser);
    }

    /**
     *  This helper function simply adds the requestee to the requester's list of outgoing pal requests and
     *  adds the requester to the requestee's list of incoming pal requests and then updates both users.
     */
    void sendPalRequest(HttpServletRequest request) {
        // gets the username from the request URL
        String requestUrl = request.getRequestURI();
        String profileUsername = requestUrl.substring("/users/".length());

        // sets the requester to be the name of the user who sent the pal request
        String requester = request.getParameter("requestPal");
        User requesterUser = userStore.getUser(requester);

        // sets the requestee to be the name of the profile page they are on
        String requestee = profileUsername;
        User requesteeUser = userStore.getUser(requestee);

        // for requester, adds name of requestee to the list of their outgoingRequests
        requesterUser.addOutgoingRequest(requestee);

        // for requestee, adds name of requester to the list of their incomingRequests
        requesteeUser.addIncomingRequest(requester);

        // updates both users in UserStore
        userStore.updateUser(requesterUser);
        userStore.updateUser(requesteeUser);
    }
}