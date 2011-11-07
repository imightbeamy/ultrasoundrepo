
<div class="span-8 colborder">
  <h2>Welcome to the Ultrasound Repository!</h2>
  <p>
  Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus mauris ipsum, elementum ut venenatis sed, aliquet venenatis dui. Integer in orci at ante vehicula convallis. Fusce quis eros risus. Quisque cursus tortor egestas magna imperdiet fermentum. Pellentesque vitae velit dui. Sed nec adipiscing nisi. In congue tincidunt turpis, nec sollicitudin eros fermentum ut. Sed aliquet malesuada scelerisque. Mauris quam justo, rhoncus non dictum et, auctor et odio. Aenean mauris orci, aliquam vel ultricies nec, placerat ac mi. Nulla semper pulvinar tellus, in facilisis mi consequat feugiat. Aenean lorem nibh, cursus vitae vulputate sed, dignissim nec purus. Praesent id nisi feugiat nunc accumsan malesuada gravida sit amet lorem. Quisque accumsan rhoncus gravida. Donec vel turpis nisi, eget consequat massa. Nulla facilisi.
  </p>
</div>
<div class="span-12 last">
  <h2>Please fill out all information to request access to the system.</h2>
<form name='imgsettings' action='/confirmation' method="get">
  <div class='span-3 form-label'>
    <label for='user-name'>User Name</label>
  </div>
  <div class='span-7 last'>
    <input type=text name='tag' id='user-name' size=30 maxlength=75 >
  </div>
  
  <div class='span-3 form-label'>
    <label for='password1'>Password</label>
  </div>
  <div class='span-7 last'>
    <input type=password name='tag' id='password1' size=30 maxlength=75 >
  </div>
  
  <div class='span-3 form-label'>
    <label for='password2'>Re-enter password</label>
  </div>
  <div class='span-7'>
    <input type=password name='tag' id='password2' size=30 maxlength=75 >
  </div>
  
  <div class='span-3 form-label'>
    <label for='password2'>First name</label>
  </div>
  <div class='span-7'>
    <input type=text name='tag' id='password2' size=30 maxlength=75 >
  </div>
  
  <div class='span-3 form-label'>
    <label for='password2'>Last Name</label>
  </div>
  <div class='span-7'>
    <input type=text name='tag' id='password2' size=30 maxlength=75 >
  </div>
  
  <div class='span-3 form-label'>
    <label for='password2'>Role</label>
  </div>
  <div class='span-7 last'>
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
</div>
