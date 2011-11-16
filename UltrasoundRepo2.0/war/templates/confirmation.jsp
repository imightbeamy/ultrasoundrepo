<%@ page import="edu.umbc.ultra.logic.User.PrivilegeLevel" %>
<%@ page import="edu.umbc.ultra.logic.User" %>
<%@ page import="edu.umbc.ultra.dbase.RightsManagementController" %>
<%@ page import="java.util.Date" %>

<%
	String email = request.getUserPrincipal().toString();
	String first = request.getParameter("first");
	String last = request.getParameter("last");
	String requested_role = request.getParameter("role");
  	User newuser = new User(email, PrivilegeLevel.PENDING, new Date(), first, last);
  	RightsManagementController rm = RightsManagementController.getInstance();
  	rm.addUser(newuser);
%>

<%=email %>
<div class='span-10'>
  <h2>Thank you for registering!</h2>
  <p>
    A confirmation email has been sent to you and an email 
    has been sent to existing users to approve or deny your registration.
    You will receive an email once you have been approved or denied. 
  </p>
</div>