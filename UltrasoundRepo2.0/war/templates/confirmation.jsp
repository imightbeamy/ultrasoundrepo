<%@ page import="edu.umbc.ultra.logic.User.PrivilegeLevel" %>
<%@ page import="edu.umbc.ultra.logic.User" %>
<%@ page import="java.util.Date" %>

<div class='span-10'>
	String email = request.getUserPrincipal().getEmail();
	String first = request.getParameter("first");
	String last = request.getParameter("last");
	String requested_role = request.getParameter("role");
  	User newuser = new User(email, PrivilegeLevel.PENDING, new Date())

  <h2>Thank you for registering!</h2>
  <p>
    A confirmation email has been sent to you and an email 
    has been sent to existing users to approve or deny your registration.
    You will receive an email once you have been approved or denied. 
  </p>
</div>