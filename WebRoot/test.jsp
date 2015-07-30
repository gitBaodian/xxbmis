<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.InputStreamReader"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'test.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
<%
    try {
	String line;
	String str = "http://onecloudcmdb.pispower.com";
	URL url = new URL(str);
	BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	StringBuffer pageBuffer = new StringBuffer();
	while ((line = reader.readLine()) != null) {
		pageBuffer.append(line);
	}
	out.println(str + "\n" + pageBuffer.toString());
} catch (Exception e) {
	e.printStackTrace();
}
		
%>
    <br>
  </body>
</html>
