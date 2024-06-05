<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>계정 정보 찾기</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<h2>아이디 찾기</h2>
	<div id="findAccount">
		<c:choose>
			<c:when test="${memberIdList.size() == 0 }">
				<h2>가입된 아이디가 없습니다.</h2>
				<div>
					<a href="findAccount">아이디 찾기</a>
				</div>
			</c:when>
			<c:otherwise>
				<h2>해당 이메일로 가입된 아이디입니다</h2>
				<c:forEach items="${memberIdList }" var="memberId">
					<strong>${memberId }</strong><br>
				</c:forEach>
				<div>
					<a href="findPassword">비밀번호 찾기</a>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

</body>
</html>