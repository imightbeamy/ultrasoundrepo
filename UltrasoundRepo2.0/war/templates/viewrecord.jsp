<%@ page import="edu.umbc.ultra.dbase.SearchController" %>
<%@ page import="edu.umbc.ultra.logic.Patient.Gender" %>
<%@ page import="edu.umbc.ultra.logic.Patient" %>
<%@ page import="edu.umbc.ultra.logic.DataEntry" %>
<%@ page import="edu.umbc.ultra.logic.Comment" %>
<div>
<%
    String key = request.getParameter("entry");
    SearchController sc = SearchController.getInstance();
	DataEntry de = sc.getEntryByID(key);
	if( de == null ) {
%>
	No record
<%
	}
	else {
	Patient patient = de.getPatient();
%>
  <h2>Video Record</h2>
   <div class='span-12'>     
     <h3>Patient Information</h3>
     <table><tbody>
       <tr>
         <td>Name</td><td><%= patient.getFirstName()  %> <%= patient.getLastName()  %></td>
       </tr>
       <tr>
         <td>DoB</td><td><%= patient.getDob().toString()  %></td>
       </tr>
       <tr>
         <td>Gender</td><td><%= patient.getGender()  %></td>
       </tr>
       <tr>
         <td>ID</td><td><%= patient.getId()  %></td>
       </tr>
     </tbody></table>
     
     
     <h3>Video Information</h3> 
     <table><tbody>
       <tr>
         <td>Upload Date</td><td><%=de.getTimestamp()%></td>
       </tr>
       <tr>
         <td>Upload ID</td><td><%=key%></td>
       </tr>
     </tbody></table>
     
     <h3>Comments and Interpretations</h3> 
     <table><tbody>
      <%
     	for(Comment c: de.getComments()) { %> 
       <tr>
         <td>Complaint</td><td><%=c.getContent()%></td>
       </tr>
       <%}%>
     </tbody></table>
   </div>
   <div class='span-6'>
     <h3><a>Download File</a></h3>
  </dvi>
</div>
<%}%>
