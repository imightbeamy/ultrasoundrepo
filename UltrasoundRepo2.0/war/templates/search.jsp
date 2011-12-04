
<h2>Search for key words in particular fields</h2>
<form name='search' action='/results' method='get'>
  
  <div class='span-5 form-label'>
    <label for='keyword'>Keyword for any field</label>
  </div>
  <div class='span-18'>
    <input type=text name='keyword' id='keyword' size=30 maxlength=75 >
  </div>
  
  <div class='span-5 form-label'>
    <label for='first'>Patient First Name</label>
  </div>
  <div class='span-18'>
    <input type=text name='first' id='first' size=30 maxlength=75 >
  </div>
  
  <div class='span-5 form-label'>
    <label for='last'>Patient Last Name</label>
  </div>
  <div class='span-18'>
    <input type=text name='last' id='last' size=30 maxlength=75 >
  </div>
   
  <div class='span-5 form-label'>
    <label for='gender'>Patient Gender</label>
  </div>
  <div class='span-18 last'>
      <input type='radio' value='male' name='gender'> 
      <label for='male'>Male</label><br>
      <input type='radio' value='female' name='gender'>
      <label for='female'>Female</label><br>
      <input type='radio' value='other' name='gender'>
      <label for='other'>Other</label><br>
      <input type='radio' value='any' name='gender' checked>
      <label for='any'>Any</label><br>
  </div>

  <div class='span-5 form-label'>
    <label for='user'>User Email Address</label>
  </div>
  <div class='span-18'>
    <input type=text name='user' id='user' size=30 maxlength=75 >
  </div>
    
  <div class='span-5 form-label'>
    <label for='complaint'>Complaint keyword</label>
  </div>
  <div class='span-18'>
    <input type='text' name='complaint' id='complaint' size=30 >
  </div>

  <div class='span-5 form-label'>
    <label for='reason'>Reason for study keyword</label>
  </div>
  <div class='span-18'>
    <input type='text' name='reason' id='reason' size=30 >
  </div>

  <div class='span-5 form-label'>
    <label for='interpretation'>Interpretation keyword</label>
  </div>
  <div class='span-18'>
    <input type='text' name='interpretation' id='interpretation' size=30 >
  </div>
 
  <div class='prepend-2 span-5 form-label'>
    <br>
    <input type='submit' value='Search' />
  </div>
</form>