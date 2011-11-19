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
	String email = request.getParameter("user");
    String role_string = request.getParameter("level");
	PrivilegeLevel requested_role = null;
	if(role_string != null) {
		if(role_string.equals("resident")) {
			requested_role = PrivilegeLevel.RESIDENT;
		}
		else if(role_string.equals("attending")) {
			requested_role = PrivilegeLevel.ATTENDING;
		}
	}
  	String action="You have denied";
  	Properties props = new Properties();
    Session mailsesh = Session.getDefaultInstance(props, null);
    String msgBody = "";
    if( request.getParameter("approve") != null && request.getParameter("approve").equals("True")) {
	  	RightsManagementController rm = RightsManagementController.getInstance();
	  	rm.changePrivilegeLevel(email, requested_role);
    	msgBody = "You have been approved as a " + requested_role + 
    	"<a href='http://ultrasoundrepo.appspot.com/'>Click Here to use the system</a>";
    	action = "Thank you for approving";
    }
    else {
    	msgBody = "You have been denied as a " + requested_role;
    }

    try {
        Message msg = new MimeMessage(mailsesh);
        msg.setFrom(new InternetAddress("ultrasoundrepo.reg@gmail.com", "Registration"));
        msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress(email, requested_role.toString()));
        msg.setSubject("Registration for ulrasoundrepo");
        msg.setText(msgBody);
        Transport.send(msg);

    } catch (AddressException e) { %>
        <%=e.getMessage()%>
<%   } catch (MessagingException e) { %>
        <%=e.getMessage()%>
   <% }
  	
%>

<div class='span-10'>
  <h2><%=action %> <%=email %> as a <%=role_string%>!</h2>
  <p>
    An email has been sent to the approved use letting them know thay can use the system 
  </p>
</div>