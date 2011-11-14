
<h2>Please fill out all information to register for the system.</h2>
<form name='imgsettings' action='/confirmation' method="post">
  <div class='span-4 form-label'>
    <label for='userName'>User Name</label>
  </div>
  <div class='span-18 last'>
    <input type=text name='userName' id='userName' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='password'>Password</label>
  </div>
  <div class='span-18 last'>
    <input type=password name='password' id='password' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='confirmPassword'>Re-enter password</label>
  </div>
  <div class='span-18'>
    <input type=password name='confirmPassword' id='confirmPassword' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='firstName'>First name</label>
  </div>
  <div class='span-18'>
    <input type=text name='firstName' id='firstName' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='lastName'>Last Name</label>
  </div>
  <div class='span-18'>
    <input type=text name='lastName' id='lastName' size=30 maxlength=75 >
  </div>
  
  <div class='span-4 form-label'>
    <label for='role'>Role</label>
  </div>
  <div class='span-18 last'>
      <input type='radio' name="role" value="resident" id='resident-radio' checked onClick='change_form("role");' > 
      <label for='resident-radio'>Resident Physician</label><br>
      <input type='radio' name="role" value="attending" id='attending-radio' onClick='change_form("role");' >
      <label for='attending-radio'>Attending Physician</label><br>
  </div>
  
  <div class='prepend-2 span-4 form-label'>
    <br>
    <input type="submit" value="Register" />
  </div>
</form>