<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 시큐리티 관련코드 -->
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal" />
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<title>회원 문의 리스트</title>
</head>
<body>
	<h3>${memberDetails.getUsername()}님 문의 리스트</h3> 
	
	<script type="text/javascript">
	const memberId = '${memberDetails.getUsername() }';
	
	$(document).ready(function() { 
		function getAllInquiry() {
			let url = '../inquiry/myList/' + memberId;
			 $.getJSON(
			     url,
			     function(data) {
			    	 console.log(data);
			    	 
			     }
			     
		  )// end getJSON
		}// end getAllInquiry()
	});//end document
	
	</script>
	
	
</body>
</html>