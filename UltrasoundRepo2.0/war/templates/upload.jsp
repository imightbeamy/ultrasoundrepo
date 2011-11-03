
<h2>Please fill out all information to upload a file.</h2>
<form name='imgsettings' action='/upload' method="get">
  <div class='span-4 form-label'>
    <label for='upload'>Choose a file</label>
  </div>
  <div class='span-18 last'>
    <input type="submit" name='upload' value="Browse" />
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>Patient First Name</label>
  </div>
  <div class='span-18'>
    <input type=text name='tag' id='password2' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>Patient Last Name</label>
  </div>
  <div class='span-18'>
    <input type=text name='tag' id='password2' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>Patient DoB</label>
  </div>
  <div class='span-18'>
    <input type=text name='tag' id='password2' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>Patient Gender</label>
  </div>
  <div class='span-18 last'>
      <input type=radio name="gender"> 
      <label for='tag-radio'>Male</label><br>
      <input type=radio name="gender">
      <label for='person-radio'>Female</label><br>
      <input type=radio name="gender">
      <label for='person-radio'>Other</label><br>
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>Complaint</label>
  </div>
  <div class='span-18'>
    <input type=text name='tag' id='password2' size=30 >
  </div>

  <div class='span-4 form-label'>
    <label for='password2'>Reason for study	</label>
  </div>
  <div class='span-18'>
    <textarea name='tag' rows='3' cols="30" ></textarea>
  </div>

  <div class='span-4 form-label'>
    <label for='password2'>Resident’s interpretation</label>
  </div>
  <div class='span-18'>
    <textarea type=textarea name='tag' id='password2' rows="6" cols="30"></textarea>
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>Attending’s interpretation</label>
  </div>
  <div class='span-18'>
    <textarea type=textarea name='tag' id='password2' rows="6" cols="30" ></textarea>
  </div>

  <div class='prepend-2 span-4 form-label'>
    <br>
    <input type="submit" value="Submit" />
  </div>
</form>