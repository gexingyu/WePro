<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
   <%
	String basePath = "//" + request.getHeader("host") + request.getContextPath();
		request.setAttribute("basePath",basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<h1>index</h1>
	<img alt="二维码" src="http:<%=basePath %>/erweima"><br>
	<h1>nickName:${snsUserInfo.nickname} <br></h1>
	<h1>city:${snsUserInfo.city} <br></h1>
	<h1>openId:${snsUserInfo.openId} <br></h1>
</body>
</html>