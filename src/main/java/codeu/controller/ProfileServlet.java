package codeu.controller;

import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.data.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

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
            // set profilePage attribute to be username
            request.setAttribute("profilePage", profileUsername);
        }
        // forward request to profile.jsp
        request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }

    /**
     * This function fires when a user wants to edit his/her own profile page. It checks if the user exists from the username of the URL requested.
     * It sets the bio parameter to be the user's bio attribute and then updates the user in order to write the data to DataStore.
     * It then redirects the user back to their profile page.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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

        // redirects user back to their profile page as a GET request
        response.sendRedirect("/users/" + profileUsername);
    }
}
