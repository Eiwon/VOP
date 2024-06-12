<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div>
		<h2>${messageVO.title }</h2>
	</div>
	<div>
		<h3>${messageVO.content }</h3>
	</div>
	<div>
		<c:when test="${messageVO.type }">
			<form action="../getCoupon" method="POST">
				<input type="submit" value="쿠폰 받기">
			</form>
		</c:when>
	</div>

</body>
</html>