<%@ page import="edu.umbc.ultra.logic.User.PrivilegeLevel" %>
<%@ page import="edu.umbc.ultra.logic.User" %>
<%@ page import="edu.umbc.ultra.dbase.RightsManagementController" %>

<%
	String email = request.getUserPrincipal().toString();	
  	User newuser = new User(email, PrivilegeLevel.ATTENDING, new Date(), "test", "Test");
  	RightsManagementController rm = RightsManagementController.getInstance();
  	rm.addUser(newuser);
%>

<%=email %>
<div class='span-10'>
  <h2>You have been added as a resident</h2>

</div>
