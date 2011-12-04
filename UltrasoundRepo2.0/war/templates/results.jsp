<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.ArrayList" %>

<%@ page import="edu.umbc.ultra.logic.DataEntry" %>
<%@ page import="edu.umbc.ultra.logic.Patient.Gender" %>
<%@ page import="edu.umbc.ultra.logic.Patient" %>
<%@ page import="edu.umbc.ultra.dbase.SearchController" %>

<% 
	String keyword = request.getParameter("keyword");
	String first = request.getParameter("first");
	String last = request.getParameter("last");
	String dob = request.getParameter("dob");
	String genderString = request.getParameter("gender");
	Gender gender = null; 
	if(genderString != null) {
		if(genderString.equals("male")) {
			gender = Gender.MALE;
		}
		else if(genderString.equals("female")) {
			gender = Gender.FEMALE;
		}
		else if(genderString.equals("other")) {
			gender = Gender.OTHER;
		}
	}
	String complaint = request.getParameter("complaint");
	String reason = request.getParameter("reason");
	String interpretation = request.getParameter("interpretation");
	String user = request.getParameter("user");
	
	ArrayList<String> keywords = new ArrayList<String>();
	keywords.add(keyword);
	
	SearchController sc = SearchController.getInstance();
	ArrayList<DataEntry> results = sc.searchByKeyword(keywords);										
%>

<div>
  <h2>Search Results (<%=results.size() %>)</h2>
  <%
  	for(DataEntry de: results) {
  		Patient p = de.getPatient();
  %>
    <div class='span-20'>
      <table><tbody>
        <tr>
          <td>Patient Name</td><td> <%=p.getFirstName()%> <%=p.getLastName()%> (<%=p.getId()%>) </td>
        </tr>
        <tr>
          <td>Patient DoB</td><td> <%=p.getDob().toString()%> </td>
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
<% } %>

</div>