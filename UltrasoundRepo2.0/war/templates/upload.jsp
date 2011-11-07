<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<h2>Please fill out all information to upload a file.</h2>
<form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
  <div class='span-4 form-label'>
    <label for='upload'>Choose a file</label>
  </div>
  <div class='span-18 last'>
    <input type="file" name="upload">
  </div>
  
  <div class='span-4 form-label'>
    <label for='first'>Patient First Name</label>
  </div>
  <div class='span-18'>
    <input type=text name='first' id='first' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='last'>Patient Last Name</label>
  </div>
  <div class='span-18'>
    <input type=text name='last' id='last' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='DoB'>Patient DoB</label>
  </div>
  <div class='span-18'>
    <input type=text name='DoB' id='DoB' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='gender'>Patient Gender</label>
  </div>
  <div class='span-18 last'>
      <input type=radio name="gender"> 
      <label for='male'>Male</label><br>
      <input type=radio name="gender">
      <label for='female'>Female</label><br>
      <input type=radio name="gender">
      <label for='other'>Other</label><br>
  </div>
  
  <div class='span-4 form-label'>
    <label for='complaint'>Complaint</label>
  </div>
  <div class='span-18'>
    <input type=text name='complaint' id='complaint' size=30 >
  </div>

  <div class='span-4 form-label'>
    <label for='reason'>Reason for study	</label>
  </div>
  <div class='span-18'>
    <textarea name='reason' rows='3' cols="30" ></textarea>
  </div>

  <div class='span-4 form-label'>
    <label for='resInterp'>Resident's interpretation</label>
  </div>
  <div class='span-18'>
    <textarea type=textarea name='resInterp' id='resInterp' rows="6" cols="30"></textarea>
  </div>
  
  <div class='span-4 form-label'>
    <label for='attendInterp'>Attending's interpretation</label>
  </div>
  <div class='span-18'>
    <textarea type=textarea name='attendInterp' id='attendInterp' rows="6" cols="30" ></textarea>
  </div>

  <div class='prepend-2 span-4 form-label'>
    <br>
    <input type="submit" value="Submit" />
  </div>
</form>