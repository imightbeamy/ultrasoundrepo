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