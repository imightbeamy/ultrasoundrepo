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
	String last = request.getParameter("last");
	
	String role_string = request.getParameter("role");
	
  	User newuser = new User(email, PrivilegeLevel.PENDING, new Date(), first, last);
  	RightsManagementController rm = RightsManagementController.getInstance();
  	rm.addUser(newuser);
  	
  	Properties props = new Properties();
    Session mailsesh = Session.getDefaultInstance(props, null);
    String aprovalurl = "http://ultrasoundrepo.appspot.com/approve?user=" + email + "&level=" + role_string;
    String msgBody = "<a href='" + aprovalurl + "'>Click here to approve " + first + " " + last + "</a>";

    try {
        Message msg = new MimeMessage(mailsesh);
        msg.setFrom(new InternetAddress("ultrasoundrepo.reg@gmail.com", "Registration"));
        msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress("AmyCiav@gmail.com", "Amy Ciavolino"));
        msg.setSubject("Please approve a registration for ulrasoundrepo");
        msg.setText(msgBody);
        Transport.send(msg);

    } catch (AddressException e) { %>
        <%=e.getMessage()%>
<%   } catch (MessagingException e) { %>
        <%=e.getMessage()%>
   <% }
  	
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