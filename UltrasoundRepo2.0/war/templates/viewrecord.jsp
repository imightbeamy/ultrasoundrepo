<%@ page import="edu.umbc.ultra.dbase.SearchController" %>
<%@ page import="edu.umbc.ultra.logic.Comment" %>
<%@ page import="edu.umbc.ultra.logic.DataEntry" %>
<%@ page import="edu.umbc.ultra.logic.Patient.Gender" %>
<%@ page import="edu.umbc.ultra.logic.Patient" %>

<div>
<%
    String key = request.getParameter("entry");
    SearchController sc = SearchController.getInstance();
	DataEntry dataEntry = sc.getEntryByID(key);
	if( dataEntry == null ) {
%>
	No record
<%
	}
	else {
	Patient patient = dataEntry.getPatient();
%>
  <h2>Video Record</h2>
   <div class='span-13'>     
     <h3>Patient Information</h3>
     <table><tbody>
       <tr>
         <td>Name</td><td><%=patient.getFullName()%></td>
       </tr>
       <tr>
         <td>DoB</td><td><%=patient.getPrintDate()%></td>
       </tr>
       <tr>
         <td>Gender</td><td><%=patient.getGender()%></td>
       </tr>
       <tr>
         <td>ID</td><td><%=patient.getId()%></td>
       </tr>
     </tbody></table>
     <a href='/results?first<%=patient.getFirstName()%>&last=<%=patient.getLastName()%>'>view all records for <%=patient.getFullName()%></a>
     
     <h3>Video Information</h3> 
     <table><tbody>
       <tr>
         <td>Upload Date</td><td><%=dataEntry.getTimestamp()%></td>
       </tr>
       <tr>
         <td>Upload ID</td><td><div class='break-word'><%=key%></div></td>
       </tr>
     </tbody></table>
     
     <h3>Comments and Interpretations</h3> 
     <table><tbody>
      <% for(Comment c: dataEntry.getComments()) { %> 
       <tr>
         <td><%=c.getTitle()%></td>
         <td><%=c.getContent()%></td>
         <td><%=c.getAuthor().getGoogleUser()%></td>
       </tr>
       <% } %>
     </tbody></table>
     
    <br />
    <form action='/videoupload' method='get' enctype='multipart/form-data'>
	    <div class='span-4 form-label'>
		  <label for='resInterp'>Add new interpretation</label>
		</div>
		<div class='span-18'>
		  <textarea type=textarea name='newcomment' id='newcomment' rows=6 cols=30></textarea>
		</div>
	   <div class='prepend-2 span-4 form-label'>
	    <br>
	    <input type='submit' value='Submit' />
	  	</div>
	  	<div class='hide'>
	  		<input type=text name='entry' id='entry' VALUE='<%=request.getParameter("entry")%>'>
	  	</div>
    </form>
   </div>
   <div class='span-6'>
   
  <video width="380" height="250" controls>
   	<source src='/serve?blob-key=<%=dataEntry.getBlobKey().getKeyString()%>' type='video/mp4; codecs="avc1.42E01E, mp4a.40.2"'>
  </video>
     <h3><a href='/serve?blob-key=<%=dataEntry.getBlobKey().getKeyString()%>'>Download File</a></h3>
   </div>
</div>
<% } %>