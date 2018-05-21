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
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AdminServletTest {

  private AdminServlet adminServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private HttpSession mockSession;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    adminServlet = new AdminServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
        .thenReturn(mockRequestDispatcher);

    UserStore mockUserStore = Mockito.mock(UserStore.class);
  }

  @Test
  public void testDoGet_NullUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/login");
  }

/*
  @Test
  public void testDoGet_ValidUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin_username");

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockSession).getAttribute("user").matches("admin_username");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }


  @Test
  public void testDoGet_NotValidUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin_username");

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify((mockSession).getAttribute("user"), never()).matches("admin_username");
    Mockito.verify(mockResponse).sendRedirect("/");
  }
  */

}
