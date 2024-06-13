<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>쿠폰 발급</title>
</head>
<body>
	
	<div>
		<h3>${couponVO.couponName }</h3>
		<h1>${couponVO.discount } % 할인!!</h1>
	</div>
	
	<form action="getCoupon" method="POST">
		<input type="hidden" name="couponId" value="${couponVO.couponId}">
		<input type="submit" value="쿠폰 받기">
	</form>

</body>
</html>