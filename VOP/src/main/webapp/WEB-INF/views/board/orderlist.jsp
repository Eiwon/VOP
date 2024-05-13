<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@page import="com.web.vop.domain.OrderVO"%>
<%@page import="java.util.List"%>
<%@page import="com.web.vop.service.OrderService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주문 목록</title>
</head>
<body>

<h1> 주문 목록 </h1>

<%
	// 세션 객체 가져오기
	HttpSession sessionJSP = request.getSession();
	// 세션에 저장된 memberId 가져오기
	String memberId = (String) sessionJSP.getAttribute("memberId");
	%>


<%
	
	if(memberId == null) {
		response.sendRedirect("../member/login");
	}else{

%>

	<!--  주문 목록을 표시할 HTML 코드를 작성 -->
	<c:forEach items="${orderList}" var="order">
		<div>
			<p>예상 배송일 : ${order.orderId}</p>
			<p>상품명 : ${order.productName}</p>
			<p>상품 가격 : ${order.productPrice} 원</p>
			<p>상품 수량 : ${order.purchaseNum} 개</p>
		</div>
	</c:forEach>
	
	<div>
    	<c:forEach items="${imageList}" var="image">
        	<img src="showImg?imgId=${image.imgId}" alt="${image.imgRealName}.${image.imgExtension}">
    	</c:forEach>
	</div>
<%	
	}	
%>
	
</body>
</html>