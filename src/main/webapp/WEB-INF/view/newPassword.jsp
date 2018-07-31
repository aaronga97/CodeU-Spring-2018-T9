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
  <title>NewPassword</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>



  <div id="container">
    <h1>New Password</h1>

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <form action="/newPassword" method="POST">
      <label for="password">New password: </label>
      <br/>
      <input type="password" name="newPassword" id="newPassword">
      <br/><br/>
      <label for="password">Re-type new password: </label>
      <br/>
      <input type="password" name="reTypedNewPassword" id="reTypedNewPassword">
      <br/><br/>
      <button type="submit">Submit</button>
    </form>
  </div>
</body>

<script>
    document.getElementById("username").focus();
</script>
</html>
