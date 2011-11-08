<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

  <%
  	 Hashtable files = new Hashtable();
     files.put("search", "search.jsp");
     files.put("confirmation", "confirmation.jsp");
     files.put("dashboard", "dashboard.jsp");
     files.put("home", "home.jsp");
     files.put("register", "register.jsp");
     files.put("results", "results.jsp");
     files.put("upload", "upload.jsp");
     files.put("viewrecord", "viewrecord.jsp");
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
        <a href='/'><h1>Ultrasound Repository</h1></a>
      </div>
      
      <div class='content span-24'>
      <%
        UserService userService = UserServiceFactory.getUserService();
        String redirectURL = request.getRequestURI();
        String file_loc = "templates/";
		//Check if the user is logged in  
        if (request.getUserPrincipal() != null) {

	      	String[] url = request.getRequestURL().toString().split("/");
	      	String content = url[url.length - 1].split("\\?")[0];
	      	
	      	//go to the page they wanted 
	      	if( files.containsKey(content) ){
	      	   file_loc+=files.get(content);
	      	}
	      	else {
	      		file_loc+=files.get("dashboard");
	      	}
	      	            %>
           <p>Hello, <%=request.getUserPrincipal().getName() %>!  
           	You can <a href="<%= userService.createLogoutURL(redirectURL) %>" \>sign out</a>.
           	</p>
	      	<jsp:include page="<%=file_loc %>" />
	      	<%
        } 
        else {
            %>
            <p>Please <a href="<%= userService.createLoginURL(redirectURL) %>" >sign in</a>.</p>
            <%
        }
      %>
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