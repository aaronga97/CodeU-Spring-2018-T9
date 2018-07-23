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

package codeu.controller;

import codeu.model.data.User;
import java.util.UUID;
import java.time.Instant;

import codeu.model.store.basic.UserStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProfileServletTest {

    private ProfileServlet profileServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;

    @Before
    public void setup() {
        profileServlet = new ProfileServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp")).thenReturn(mockRequestDispatcher);
    }

    @Test
    public void testDoGet_ValidUser() throws IOException, ServletException {
        User userCandace = new User(
                        UUID.randomUUID(), "Candace", "candacepassword", "team9chatapp@gmail.com", Instant.now(), false);
        UserStore mockUserStore = Mockito.mock(UserStore.class);

        Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/candace");
        Mockito.when(mockUserStore.getUser("candace")).thenReturn(userCandace);

        profileServlet.setUserStore(mockUserStore);
        profileServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoGet_UserDoesNotExist() throws IOException, ServletException {
        UserStore mockUserStore = Mockito.mock(UserStore.class);

        Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/candace");
        Mockito.when(mockUserStore.getUser("candace")).thenReturn(null);

        profileServlet.setUserStore(mockUserStore);
        profileServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostEditBio() throws IOException, ServletException {
        User userCandace = new User(
                UUID.randomUUID(), "Candace", "candacepassword", "team9chatapp@gmail.com", Instant.now(), false);
        UserStore mockUserStore = Mockito.mock(UserStore.class);

        Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/candace");
        Mockito.when(mockUserStore.getUser("candace")).thenReturn(userCandace);

        Mockito.when(mockRequest.getParameter("bio")).thenReturn("This is Candace's bio.");

        profileServlet.setUserStore(mockUserStore);
        profileServlet.doPost(mockRequest, mockResponse);

        Assert.assertEquals("This is Candace's bio.", userCandace.getBio());
        Mockito.verify(mockResponse).sendRedirect("/users/candace");
    }

    @Test
    public void testEditBio() throws IOException, ServletException {
        User userCandace = new User(
                UUID.randomUUID(), "Candace", "candacepassword","team9ChatApp@gmail.com", Instant.now(), false);
        UserStore mockUserStore = Mockito.mock(UserStore.class);

        Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/candace");
        Mockito.when(mockUserStore.getUser("candace")).thenReturn(userCandace);

        Mockito.when(mockRequest.getParameter("bio")).thenReturn("This is Candace's bio.");

        profileServlet.setUserStore(mockUserStore);
        profileServlet.updateBio(mockRequest);

        Assert.assertEquals("This is Candace's bio.", userCandace.getBio());
    }

    @Test
    public void testDoPostUncleanedBio() throws IOException, ServletException {
        User userCandace = new User(
                UUID.randomUUID(), "Candace", "candacepassword", "team9ChatApp@gmail.com", Instant.now(), false);
        UserStore mockUserStore = Mockito.mock(UserStore.class);

        Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/candace");
        Mockito.when(mockUserStore.getUser("candace")).thenReturn(userCandace);

        Mockito.when(mockRequest.getParameter("bio")).thenReturn("<h1> This is Candace's bio. <h1>");

        profileServlet.setUserStore(mockUserStore);
        profileServlet.updateBio(mockRequest);
        Assert.assertEquals("This is Candace's bio.", userCandace.getBio());
    }

    @Test
    public void testSendPalRequest() throws IOException, ServletException {
        User userBob = new User(
                UUID.randomUUID(), "Bob", "bobpassword","team9ChatApp@gmail.com", Instant.now(), false);
        User userHelen = new User(
                UUID.randomUUID(), "Helen", "helenpassword","team9ChatApp@gmail.com", Instant.now(), false);

        UserStore mockUserStore = Mockito.mock(UserStore.class);
        Mockito.when(mockUserStore.getUser("Bob")).thenReturn(userBob);
        Mockito.when(mockUserStore.getUser("Helen")).thenReturn(userHelen);

        Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/Helen");
        Mockito.when(mockRequest.getParameter("requestPal")).thenReturn("Bob");

        Assert.assertEquals(0, userBob.getOutgoingRequests().size());
        Assert.assertEquals(0, userHelen.getIncomingRequests().size());

        profileServlet.setUserStore(mockUserStore);
        profileServlet.sendPalRequest(mockRequest);

        Assert.assertEquals(1, userBob.getOutgoingRequests().size());
        Assert.assertEquals(1, userHelen.getIncomingRequests().size());
    }

    @Test
    public void testAcceptPal() throws IOException, ServletException {
        User userBob = new User(
                UUID.randomUUID(), "Bob", "bobpassword", "team9Chatapp@gmail.com", Instant.now(), false);
        User userHelen = new User(
                UUID.randomUUID(), "Helen", "helenpassword", "team9Chatapp@gmail.com", Instant.now(), false);

        UserStore mockUserStore = Mockito.mock(UserStore.class);
        Mockito.when(mockUserStore.getUser("Bob")).thenReturn(userBob);
        Mockito.when(mockUserStore.getUser("Helen")).thenReturn(userHelen);

        Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/Helen");
        Mockito.when(mockRequest.getParameter("requestPal")).thenReturn("Bob");

        Assert.assertEquals(0, userBob.getOutgoingRequests().size());
        Assert.assertEquals(0, userHelen.getIncomingRequests().size());

        profileServlet.setUserStore(mockUserStore);
        profileServlet.sendPalRequest(mockRequest);

        Assert.assertEquals(1, userBob.getOutgoingRequests().size());
        Assert.assertEquals(1, userHelen.getIncomingRequests().size());

        // Bob has sent Helen a pal request

        Assert.assertEquals(0, userBob.getPals().size());
        Assert.assertEquals(0, userHelen.getPals().size());

        Mockito.when(mockRequest.getParameter("accept")).thenReturn("Bob");

        profileServlet.acceptPal(mockRequest);

        Assert.assertEquals(1, userBob.getPals().size());
        Assert.assertEquals(1, userHelen.getPals().size());

        Assert.assertEquals(0, userBob.getOutgoingRequests().size());
        Assert.assertEquals(0, userHelen.getIncomingRequests().size());
    }

    @Test
    public void testDeclinePal() throws IOException, ServletException {
        User userBob = new User(
                UUID.randomUUID(), "Bob", "bobpassword", "team9ChatApp@gmail.com", Instant.now(), false);
        User userHelen = new User(
                UUID.randomUUID(), "Helen", "helenpassword", "team9ChatApp@gmail.com", Instant.now(), false);

        UserStore mockUserStore = Mockito.mock(UserStore.class);
        Mockito.when(mockUserStore.getUser("Bob")).thenReturn(userBob);
        Mockito.when(mockUserStore.getUser("Helen")).thenReturn(userHelen);

        Mockito.when(mockRequest.getRequestURI()).thenReturn("/users/Helen");
        Mockito.when(mockRequest.getParameter("requestPal")).thenReturn("Bob");

        Assert.assertEquals(0, userBob.getOutgoingRequests().size());
        Assert.assertEquals(0, userHelen.getIncomingRequests().size());

        profileServlet.setUserStore(mockUserStore);
        profileServlet.sendPalRequest(mockRequest);

        Assert.assertEquals(1, userBob.getOutgoingRequests().size());
        Assert.assertEquals(1, userHelen.getIncomingRequests().size());

        // Bob has sent Helen a pal request

        Assert.assertEquals(0, userBob.getPals().size());
        Assert.assertEquals(0, userHelen.getPals().size());

        Mockito.when(mockRequest.getParameter("decline")).thenReturn("Bob");

        profileServlet.declinePal(mockRequest);

        Assert.assertEquals(0, userBob.getPals().size());
        Assert.assertEquals(0, userHelen.getPals().size());

        Assert.assertEquals(0, userBob.getOutgoingRequests().size());
        Assert.assertEquals(0, userHelen.getIncomingRequests().size());
    }
}
