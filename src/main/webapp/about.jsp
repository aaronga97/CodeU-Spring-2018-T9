<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@include file= "WEB-INF/view/navbar.jsp"%>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>LAAPH Chat</h1>
      <p>
        At its heart <strong>LAAPH Chat:</strong> is a social application that holds user accessibility as its core value. Our team wanted to develop a platform through which our audience would be able to connect with friends, find topics of interest, and socialize with ease. This is why most of the features found in our application were included to either deepen or uncomplicate the user experience. As you try out our application over the weekend, we hope that you will be able focus on the simple pleasure of virtual communication.
      </p>

      <p>
        <strong>Admin Page</strong>
      </p>

      <ul>
        <li>Users can be granted admin privileges by another admin,
          when this happens they can access an admin-only site whereas you can
          see some basic stats and make other people admin.</li>
        <li>To see admin features try with</li>
        <li><strong>User:</strong> admin</li>
        <li><strong>Password:</strong> admin</li>
      </ul>

      <p>
        <strong>Message Feature</strong>
      </p>

      <ul>
        <li>Messages append without refreshing - If you are on a conversation with another user there's no need to refresh the page manually to see the new messages sent, they just get instantly appended below the last message!</li>
        <li>Messages show timestamp - Self descriptive title</li>
      </ul>

      <p>
        <strong>Profile Page</strong>
      </p>

      <ul>
        <li>Every user that registers for an account will be given their own profile page. For example, if Bob with the username &#39Bob&#39 registers, he will have a profile page that everyone can visit him at /users/Bob. Profile pages display user&#39s bio, publicly sent messages, and their list of pals.</li>
        <li>Actions user can take: edit their own bio, request other users as pals when visiting their profile pages, receive notifications to accept incoming pal requests.</li>
        <li>Once users are pals, they can directly message each other, having their own private conversations at /chat/user1-user2 that other users cannot view.</li>
      </ul>

      <p>
        <strong>ActivityFeed</strong>
      </p>

      <ul>
        <li>Users can check out the activity feed to see significant events happening around the site (such as new user registrations or new conversations). The activity feed allows users to sort these events in order of popularity, recency, trend, weekly popularity, or most popular of the day according to their preference.
</li>
        <li>User Search- This feature of the Activity Feed allows users to keep up with what their friends are talking about by letting users filter site events by user association.</li>
      </ul>

      <p>
        <strong>Account Security</strong>
      </p>

      <ul>
        <li>Email Password Reset - If a user forgets their password they can create a new one through our email password reset feature, they will be emailed a temporary password and brought to a page where they can create a new permanent password once they login with the temporary one. </li>
        <li>Strong Passwords - Upon registration users are required to meet certain password criteria before creating an account.</li>
        <li>Logout Capable - Users are able to logout of an account at anytime and login to a separate one, without having to refresh or close the web browser.</li>
      </ul>

      <p>
        <strong>Website Encapsulation</strong>
      </p>

      <ul>
        <li>Most features of the site are unavailable for viewing unless an account is logged in to the application.</li>
      </ul>

    </div>
  </div>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>Meet the Team</h1>
      <p>
        <a href="../users/DatBoiLeeRoy"><strong>Lee-Roy Gaseller</strong></a>

        : A soon to be sophomore at Xavier University in New Orleans, Louisiana. Lee-Roy enjoys producing beats and creating music.

      </p>

	<p>
		<i><center>Interesting Fact: "I was born in Zimbabwe!"</center></i>
	</p>

	<br>


	<p>
	    <a href="../users/aaron"><strong>Aaron Garcia</strong></a>

		: A sophomore at Tec de Monterrey in Monterrey, Nuevo Le&oacute;n, Mexico. Aaron's hobbies include filming and editing videos.
	</p>

	<p>
		<i><center>Interesting Fact: "I have a mining rig!"</center></i>
	</p>

    <br>

	<p>
	    <a href="../users/Anita"><strong>Anita Cu</strong></a>

		: A sophomore at UC Berkeley born and raised in the Bay Area, California. Anita loves listening to music and enjoys playing the violin and piano. This summer, she is currently learning the guitar.
	</p>
	<p>
		<i><center>Interesting Fact: "I enjoy teaching!"</center></i>
	</p>

	<br>

	<p>
	    <a href="../users/pbonds"><strong>Padraic Bonds</strong></a>

		: A rising sophomore at Tennessee State University in Nashville, Tennessee. Padraic likes to read and write fantasy.
	</p>
	<p>
		<i><center>Interesting Fact: "I'm a red belt in Tae Kwon Doe!"</center></i>
	</p>

    <br>

	<p>
	    <a href="../users/tucosh"><strong>Henry Deist</strong></a>


		: Google Project Advisor
	</p>
  <p>
		<i><center>Interesting Fact: "Trying to learn to play piano.."</center></i>
    <br>
    <img src="<%=request.getContextPath()%>/images/playing-piano-in-a-band.jpg" alt="The Pulpit Rock" width="300" height="200">
	</p>

    </div>
  </div>

</body>
</html>
