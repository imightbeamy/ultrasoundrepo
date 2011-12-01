<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<div class="span-10 colborder">
  <h2>Welcome to the Ultrasound Repository!</h2>
  <p>
  	The Ultrasound Repository is a service for emergency medicine residents who  need to keep a notebook of ultrasound videos that they collect. The Ultrasound Repository can store ultrasound videos, and all metal data for those videos (information about the video, the resident's interpretation, an attending physician's interpretation, patient information).
  </p>
</div>

<%
	UserService userService = UserServiceFactory.getUserService();
%>

<div class="span-12 last">
  <h2> Login </h2>
    <p>Please sign in with your google account to use the system or register!</p>
 	<a class='button' href="<%= userService.createLoginURL("/dashboard") %>" >Sign in with Google</a>
 </div>
