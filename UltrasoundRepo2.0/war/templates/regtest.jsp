<%@ page import="edu.umbc.ultra.logic.User.PrivilegeLevel" %>
<%@ page import="edu.umbc.ultra.logic.User" %>
<%@ page import="edu.umbc.ultra.dbase.RightsManagementController" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Properties" %>
<%@ page import="javax.mail.Message" %>
<%@ page import="javax.mail.MessagingException" %>
<%@ page import="javax.mail.Session" %>
<%@ page import="javax.mail.Transport" %>
<%@ page import="javax.mail.internet.AddressException" %>
<%@ page import="javax.mail.internet.InternetAddress" %>
<%@ page import="javax.mail.internet.MimeMessage" %>

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
