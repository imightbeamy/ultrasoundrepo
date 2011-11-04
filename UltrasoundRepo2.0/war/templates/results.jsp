<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.umbc.ultra.dbase.SearchController" %>
<%@ page import="java.util.ArrayList" %>

<% 
	
	String first = request.getParameter("first");
	String last = request.getParameter("last");
	String dob = request.getParameter("dob");
	String gender = request.getParameter("gender");
	String complaint = request.getParameter("complaint");
	String reason = request.getParameter("reason");
	String interpretation = request.getParameter("interpretation");
	
	SearchController sc = SearchController.getInstance();
	ArrayList<DataEntry> results = sc.searchForEntriesNOT(String firstName, 
													String lastName, 
													Gender gender,
													String chiefComplaint, 
													String keywords,
													String userEmail);												
%>
	

<div>
  <h2>Search Results</h2>
 
 	
  {% for r in search_results %}
    <div class='span-20'>
      <table><tbody>
        <tr>
          <td>Patient Name</td><td>{{ r.firstname }} {{ r.lastname }}</td>
        </tr>
        <tr>
          <td>Patient DoB</td><td>{{ r.dob }}</td>
        </tr>
        <tr>
          <td>Patient Gender</td><td>{{ r.Gender }}</td>
        </tr>
        <tr>
          <td>Upload Date</td><td>{{ r.up_date }}</td>
        </tr>
      </tbody></table>
      <a class='viewrecord' href='/viewrecord'>View full record </a>
    </div>
  {% endfor %}
</div>