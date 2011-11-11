<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" import="java.util.*,java.io.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<!-- Favicon  -->
<link rel="shortcut icon" href="images/bluemarine_aims_favicon.ico">
<title>Agrovoc Graph Visualization</title>
</head>
<body rightmargin="0" bottommargin="0" marginwidth="0" leftmargin="0" marginheight="0" topmargin="0">
<%
 
   Properties properties = new Properties();
   InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("Config.properties");
   properties.load(stream);
   stream.close();
   String wsurl = properties.getProperty("WSURL");

   String concept, language ,ontology;
    if(request.getParameter("concept") == null)
        concept = null;
    else
        concept = request.getParameter("concept");
    
    if(request.getParameter("language") == null)
        language = null;
    else
        language = request.getParameter("language");

    if(request.getParameter("ontology") == null)
        ontology = null;
    else
        ontology = request.getParameter("ontology");    
%>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td align="center" valign="middle">
<jsp:plugin 
        type="applet"
        jreversion="1.1"
        width="100%"
        height="100%"
        codebase="./applet" 
        code="com.touchgraph.linkbrowser.LinkBrowserApplet.class"
        archive="TGLinkBrowser.jar"
>             
     <jsp:params>
          <jsp:param
               name="concept"
               value="<%=concept%>"
                                
          />
          <jsp:param
               name="language"
               value="<%=language%>"
               
          />
          <jsp:param
               name="ontology"
               value="<%=ontology%>"
               
          />
          <jsp:param
               name="wsurl"
               value="<%=wsurl%>"
               
          />
     </jsp:params>

     <jsp:fallback>
          <B>Unable to start plugin!</B>
     </jsp:fallback>

</jsp:plugin></td>
</tr>
</table>
</body>
</html>