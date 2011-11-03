<div>
  <h2>Video Record</h2>
   <div class='span-12'>
     
     
     <h3>Patient Information</h3>
     <table><tbody>
       <tr>
         <td>Name</td><td>{{ record.firstname }} {{ record.lastname }}</td>
       </tr>
       <tr>
         <td>DoB</td><td>{{ record.dob }}</td>
       </tr>
       <tr>
         <td>Gender</td><td>{{ record.Gender }}</td>
       </tr>
       <tr>
         <td>ID</td><td>{{ record.p_id }}</td>
       </tr>
     </tbody></table>
     
     
     <h3>Video Information</h3> 
     <table><tbody>
       <tr>
         <td>Upload Date</td><td>{{ record.up_date }}</td>
       </tr>
       <tr>
         <td>Upload ID</td><td>{{ record.up_id }}</td>
       </tr>
     </tbody></table>
     
     
     <h3>Visit Information</h3> 
     <table><tbody>
       <tr>
         <td>Complaint</td><td>{{ record.complaint }}</td>
       </tr>
       <tr>
         <td>Reason for study</td><td>{{ record.reason }}</td>
       </tr>
       <tr>
         <td>Resident’s interpretation</td><td>{{ record.r_interpretation }}</td>
       </tr>
       <tr>
         <td>Attending’s interpretation</td><td>{{ record.r_interpretation }}</td>
       </tr>
     </tbody></table>
   </div>
   <div class='span-6'>
     <h3><a>Download File</a></h3>
  </dvi>
</div>