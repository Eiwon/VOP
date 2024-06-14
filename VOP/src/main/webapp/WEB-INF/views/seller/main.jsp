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
	<c:if test="${sellerRequest != null }">
		<div>
			<h3>${memberDetails.getUsername() }회원님의 판매자 등록 신청 내역입니다.</h3>
			<table>
				<thead>
					<tr>
						<th>사업체 이름</th>
						<th>신청 날짜</th>
						<th>신청 내용</th>
						<th>상태</th>
						<th>비고</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${sellerRequest.businessName }</td>
						<td>${sellerRequest.requestTime.toLocaleString() }</td>
						<td>${sellerRequest.requestContent }</td>
						<td>${sellerRequest.requestState }</td>
						<td>${sellerRequest.refuseMsg }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</c:if>
	<sec:authorize access="!hasAnyRole('ROLE_판매자', 'ROLE_관리자')">
		<c:if test="${sellerRequest == null }">
			<a onload="location.href='sellerRequest'"></a>
		</c:if>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('ROLE_판매자', 'ROLE_관리자')">
		<div id="seller_link">
			<a href="registerProduct">상품 등록</a>
			<a href="myProduct">등록한 상품 조회</a>
		</div>
	</sec:authorize>
	
</body>
</html>