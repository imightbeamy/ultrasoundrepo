<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<div class="span-10 colborder">
  <h2>Welcome to the Ultrasound Repository!</h2>
  <p>
  Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus mauris ipsum, elementum ut venenatis sed, aliquet venenatis dui. Integer in orci at ante vehicula convallis. Fusce quis eros risus. Quisque cursus tortor egestas magna imperdiet fermentum. Pellentesque vitae velit dui. Sed nec adipiscing nisi. In congue tincidunt turpis, nec sollicitudin eros fermentum ut. Sed aliquet malesuada scelerisque. Mauris quam justo, rhoncus non dictum et, auctor et odio. Aenean mauris orci, aliquam vel ultricies nec, placerat ac mi. Nulla semper pulvinar tellus, in facilisis mi consequat feugiat. Aenean lorem nibh, cursus vitae vulputate sed, dignissim nec purus. Praesent id nisi feugiat nunc accumsan malesuada gravida sit amet lorem. Quisque accumsan rhoncus gravida. Donec vel turpis nisi, eget consequat massa. Nulla facilisi.
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
