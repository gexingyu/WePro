<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>逆天改命</title>
  <meta name="Generator" content="Cocoa HTML Writer">
  <meta name="CocoaVersion" content="1504.81">
  <style type="text/css">
       h1 {font-size: 12px;color: #FFF; display : inline}
  </style>
  <script type="text/javascript" src="../js/jquery-1.9.1.js"></script>
  <script type="text/javascript" src="../js/bootstrap.js"></script>
  <link rel="stylesheet" href="../css/bootstrap.css" type="text/css" />
  <script type="text/javascript">

$(document).ready(function(){
  $("#getNum").click(function(){
	  $.ajax({
			 type:'post',
			 async : false, 
		     url: '<%=basePath%>dlt?operation=ssq' ,
		     success:function(data) {  
		    	// alert(data.ssqList);
		    	 if(data!= null){
			    	 $("#numbers_red").text(data.ssqList);
			    	 $("#numbers_blue").text(","+data.ssqListRed);
		    	 }
	   	     }
		});
  })
$("#getDlt").click(function(){
	 $.ajax({
		 type:'post', 
	     url: '<%=basePath%>dlt?operation=dlt' ,
	     success:function(data) {  
	    	 if(data!= null){
		    	 $("#dlt_red").text(data.dltList);
		    	 $("#dlt_blue").text(","+data.dltListRed);
	    	 }
   	     }
	});
  })
$("#bac").click(function(){
  $("#shuangseqiu").show();
  $("#daletou").show();
  $("#bac").hide();
  })
})
function load(){
  $("#shuangseqiu").hide();
  $("#daletou").hide();
}
  </script>
</head>
<body onload="load()">
  <div align="center"><img id='bac' src="../pic/ak.jpg" class="img-responsive" alt="Responsive image" style="width: 800px;height: 600px;"></div>
  <div id='shuangseqiu' align="center">
    <h1 id='numbers_red' style="color: red"></h1><h1 id='numbers_blue' style="color: blue"></h1> <br>   
    <button id='getNum' class="btn btn-success">逆天改命(双色球)</button>
  </div>
  <div id='daletou' align="center">
    <h1 id='dlt_red'  style="color: red"></h1><h1 id='dlt_blue' style="color: blue"></h1> <br>   
    <button id='getDlt' class="btn btn-success">逆天改命(大乐透)</button>
  </div>
</body>
</html>
