<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>판매 신청</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>

	<div id="seller_link">
		<a href="registerProduct">상품 등록</a>
		<a href="myProduct">등록한 상품 조회</a>
	</div>
	
</body>
</html>