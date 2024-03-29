<%@ page import="edu.umbc.ultra.logic.User" %>
<%@ page import="edu.umbc.ultra.logic.User.PrivilegeLevel" %>
<%@ page import="edu.umbc.ultra.dbase.RightsManagementController" %>
<%@ page import="edu.umbc.ultra.dbase.SearchController" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="edu.umbc.ultra.logic.Patient.Gender" %>
<%@ page import="edu.umbc.ultra.logic.Patient" %>
<%@ page import="edu.umbc.ultra.logic.DataEntry" %>

<%
	String userEmail = request.getUserPrincipal().toString();
	User user = RightsManagementController.getInstance().getUser(userEmail);
	PrivilegeLevel role = user.getPrivilegeLevel();
	
	SearchController sc = SearchController.getInstance();
	ArrayList<DataEntry> records = sc.KeysToDataEntries(sc.searchByUser(userEmail));
	if(records.size() > 5) {
	  records = new ArrayList<DataEntry>(records.subList(0, 5));
	}
%>
<div class="span-12 last">
  <% if (role == PrivilegeLevel.ATTENDING) { %>
  <div>
    <a href='/search' ><h2>Search</h2></a>
	<form name='search' action='/results' method="get">
        <label for='keyword'>Key Word</label>
        <input type=text name='ukeyword' id='keyword' size=30 maxlength=75 >
        <input type="submit" value="Search" />
    </form>
    <a href='/search'>Advanced Search</a>
  </div>
  
  <%}%>
  <div>
    <br>    <br>    <br>
    <a href='/upload' ><h2>Upload a Video</h2></a>
  </div>
  
  <div>
    <br>    <br>    <br>
    <h2>System Summary Report</h2>
    <%=sc.summaryReport()%>
  </div>
</div>

<div class="span-8 last">
	<h2>Your Recent Records (<a href='/results?thisuser=True'>All</a>)</h2>
	<%
		if(records.size() > 0) {
			for(DataEntry de: records) {
 				Patient p = de.getPatient();
	%>
				<div class='span-5'>
					<table><tbody>
						<tr>
							<td>Patient Name</td><td> <%=p.getFirstName()%> <%=p.getLastName()%> </td>
						</tr>
						<tr>
							<td>Patient DoB</td><td> <%=p.getPrintDate()%> </td>
						</tr>
						<tr>
							<td>Patient Gender</td><td> <%=p.getGender()%></td>
						</tr>
						<tr>
							<td>Upload Date</td><td><%=de.getTimestamp()%></td>
						</tr>
					</tbody></table>
					<a class='viewrecord' href='/viewrecord?entry=<%=de.getKey()%>'>View full record </a>
				</div>
	<% 	
			}
		}
		else {
	%>
		You have not uploaded any records.
	<% } %>
</div>