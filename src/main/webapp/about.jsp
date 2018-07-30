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

      <h1>About the CodeU Chat App</h1>
      <p>
        This is an example chat application designed to be a starting point
        for your CodeU project team work. Here's some stuff to think about:
      </p>

      <ul>
        <li><strong>Algorithms and data structures:</strong> We've made the app
            and the code as simple as possible. You will have to extend the
            existing data structures to support your enhancements to the app,
            and also make changes for performance and scalability as your app
            increases in complexity.</li>
        <li><strong>Look and feel:</strong> The focus of CodeU is on the Java
          side of things, but if you're particularly interested you might use
          HTML, CSS, and JavaScript to make the chat app prettier.</li>
        <li><strong>Customization:</strong> Think about a group you care about.
          What needs do they have? How could you help? Think about technical
          requirements, privacy concerns, and accessibility and
          internationalization.</li>
      </ul>

      <p>
        This is your code now. Get familiar with it and get comfortable
        working with your team to plan and make changes. Start by updating the
        homepage and this about page to tell your users more about your team.
        This page should also be used to describe the features and improvements
        you've added.
      </p>
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
