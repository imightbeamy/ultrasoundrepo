<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
        <a href='/home'><h1>Ultrasound Repository</h1></a>
      </div>
      
      <div class='content span-24'>
      <%@ include file="templates/search.jsp" %>
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