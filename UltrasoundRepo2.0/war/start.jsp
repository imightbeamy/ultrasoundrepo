<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="edu.umbc.ultra.logic.User" %>

  <%
	Hashtable files = new Hashtable();
	files.put("home", "home.jsp");
    
	UserService userService = UserServiceFactory.getUserService();
	String redirectURL = request.getRequestURI();
	String file_loc = "templates/";
	Boolean canhazuser = (request.getUserPrincipal() != null);
	//Check if the user is logged in  
	if (canhazuser) {
		files.remove("home");
		String userEmail = request.getUserPrincipal().toString();
		User user = User.findUser(userEmail);
		//Check if user is in the system
		if(user != null) {
			if(user.getPrivilegeLevel().toString().equals("RESIDENT")) {
				files.put("dashboard", "dashboard2.jsp");
				files.put("upload", "upload.jsp");
			} else if(user.getPrivilegeLevel().toString().equals("ATTENDING")) {
				files.put("dashboard", "dashboard.jsp");
				files.put("search", "search.jsp");
				files.put("results", "results.jsp");
				files.put("upload", "upload.jsp");
				files.put("viewrecord", "viewrecord.jsp");
			}
		}
		else {
			files.put("home", "home.jsp");
			files.put("register", "register.jsp");
			files.put("confirmation", "confirmation.jsp");
		}

	  	String[] url = request.getRequestURL().toString().split("/");
	  	String content = url[url.length - 1].split("\\?")[0];
	  	
	  	//go to the page they wanted
	  	if(user != null) {
	  		if(files.containsKey(content)){
	  	   		file_loc+=files.get(content);
	  		}
	  		else {
	  			file_loc+=files.get("dashboard");
	  		}
	  	} else {
	  		if(files.containsKey(content)){
	  			file_loc+=files.get(content);
	  		}
	  		else {
	  			file_loc+=files.get("register");
	  		}
	  	}
	} 
	else {
		file_loc+=files.get("home");
	}
  %>


<html itemscope itemtype="http://schema.org/Product">
  <head>
    <title>
      Ultrasound Repository
    </title>
    <link rel="stylesheet" href="css/blueprint/screen.css" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="css/blueprint/print.css" type="text/css" media="print">
    <!--[if lt IE 8]>
      <link rel="stylesheet" href="css/blueprint/ie.css" type="text/css" media="screen, projection">
    <![endif]-->
    <link rel="stylesheet" href="css/style.css" type="text/css">
    
    <!-- +1 button data -->
    <meta itemprop="name" content="Ultrasound Repository">
    <meta itemprop="description" content="Ultrasound Repository project for CMSC 345">
  </head>
  
  <body>
    <div class="container" >
      <div class="span-24 header">
        <div class="span-16">
        	<a href='/'>
        		<h1>Ultrasound Repository</h1>
        	</a>
        </div>
        <% if(canhazuser) { %>
	        <div class="span-5 user">
	        	<p>
	        		Current user: <%=request.getUserPrincipal().getName() %><br>
	        		<a href="<%= userService.createLogoutURL(redirectURL) %>" \>Sign out</a>
	      		</p>
	      	</div>	
	    <% } %>
      </div>
      
      <div class='content span-24'> 
      <jsp:include page="<%=file_loc %>" />
      </div>

      
      <div class="span-24 footer">
          Created by 
          Nathan Broemm,
          Amy Ciavolino,
          Alex Grube, and
          James Mortensen &copy;2011 | 
          <g:plusone href='http://ultrasoundrepo.appspot.com/' size='small'></g:plusone>
          <script type="text/javascript">
          (function() {
              var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
              po.src = 'https://apis.google.com/js/plusone.js';
              var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
            })();
          </script>
      </div>
    </div>
  </body>
</html>