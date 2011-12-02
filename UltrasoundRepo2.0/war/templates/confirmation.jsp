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
	String first = request.getParameter("first");
	first = (first == null)? "No First Name" : first;
	String last = request.getParameter("last");
	last = (last == null)? "No Last Name" : last;
	String roleString = request.getParameter("role");
	String adminEmail = "AmyCiav@gmail.com";
	
  	User newUser = new User(email, PrivilegeLevel.ATTENDING, new Date(), first, last);
  	RightsManagementController rm = RightsManagementController.getInstance();
  	rm.addUser(newUser);
  	
  	Properties props = new Properties();
    Session mailSession = Session.getDefaultInstance(props, null);
    String approvalURL = "http://ultrasoundrepo.appspot.com/approve?user=" + email + "&level=" + roleString;
    String msgBody = "<a href='" + approvalURL + "&approve=True'>Click here to approve " + first + " " + last + "</a>" +
    				  "<a href='" + approvalURL + "'>Click here to deny request</a>";

    try {
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress("ultrasoundrepo.reg@gmail.com", "Registration"));
        msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress(adminEmail, "Admin"));
        msg.setSubject("Please approve a registration for Ultrasound Repository.");
        msg.setContent(msgBody, "text/html;charset=iso-8859-1");
        Transport.send(msg);

    } 
    catch (AddressException e) { 
%>
        <%=e.getMessage()%>
<%  
	} 
	catch (MessagingException e) { 
%>
        <%=e.getMessage()%>
<%  } %>

<div class='span-10'>
  <h2>Thank you for registering!</h2>
  <p>
    An email has been sent to an admin to approve or deny your 
    registration. You will receive an email once you have been 
    approved or denied. 
  </p>
</div>
