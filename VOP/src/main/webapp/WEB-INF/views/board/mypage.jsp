<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="../include/header.jsp"></jsp:include>
<style type="text/css">
.container {
	flex-direction: row;
	display: flex;
}
	
</style>
<title>마이페이지</title>
</head>

<body>
	<div class="info_container">
		<div class="side_bar">
			<div> 
				<strong>내 정보</strong>
				<div id="my_info">
					<a href="myInfo">내 정보 확인 / 수정</a><br>
					<a href="../order/orderlist">주문 목록</a><br>
					<a href="delivery">배송지 관리</a><br>
					<a href="seller">판매자 페이지</a><br>
				</div>
			</div>
		</div>
		<sec:authorize access="hasRole('ROLE_관리자')">
			<a href="admin">관리자 페이지</a>
		</sec:authorize>
	</div>
</body>
</html>