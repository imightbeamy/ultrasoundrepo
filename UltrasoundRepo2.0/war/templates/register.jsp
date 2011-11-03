
<h2>Please fill out all information to register for the system.</h2>
<form name='imgsettings' action='/confirmation' method="get">
  <div class='span-4 form-label'>
    <label for='user-name'>User Name</label>
  </div>
  <div class='span-18 last'>
    <input type=text name='tag' id='user-name' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='password1'>Password</label>
  </div>
  <div class='span-18 last'>
    <input type=password name='tag' id='password1' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>Re-enter password</label>
  </div>
  <div class='span-18'>
    <input type=password name='tag' id='password2' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>First name</label>
  </div>
  <div class='span-18'>
    <input type=text name='tag' id='password2' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>Last Name</label>
  </div>
  <div class='span-18'>
    <input type=text name='tag' id='password2' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='password2'>Role</label>
  </div>
  <div class='span-18 last'>
      <input type=radio name="imgtype" value="tag" id='tag-radio' checked onClick='change_form("tag");' > 
      <label for='tag-radio'>Resident Physician</label><br>
      <input type=radio name="imgtype" value="person" id='person-radio' onClick='change_form("person");' >
      <label for='person-radio'>Attending Physician</label><br>
  </div>
  
  <div class='prepend-2 span-4 form-label'>
    <br>
    <input type="submit" value="Register" />
  </div>
</form>