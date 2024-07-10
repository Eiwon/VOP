<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
.body_container{
	width: 40%;
	margin: auto;
}
</style>
<title>판매 신청</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>

	<jsp:include page="../include/sideBar.jsp"/>
	
	<div class="body_container">
		<div id="seller_link" class="btn-group">
			<a href="registerProduct" class="btn btn-outline-primary">상품 등록</a>
			<a href="myProduct" class="btn btn-outline-primary">등록한 상품 조회</a>
		</div>
	</div>
</body>
</html>