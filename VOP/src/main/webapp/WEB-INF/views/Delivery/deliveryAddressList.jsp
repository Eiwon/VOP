<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>배송지 관리</title>


<style>

    .delivery-box {
        border: 1px solid #ccc;
        padding: 10px;
        margin-bottom: 20px;
    }
    .delivery-details {
        display: flex;
        align-items: center;
        margin-bottom: 10px;
    }
    .delivery-details img {
        margin-right: 10px;
        max-width: 100px;
    }
    .delivery-buttons {
        margin-left: auto;
    }
</style>
</head>
<body>
<h2>배송지 관리</h2>
<%
	// 세션 객체 가져오기
	HttpSession sessionJSP = request.getSession();
	// 세션에 저장된 memberId 가져오기
	String memberId = (String) sessionJSP.getAttribute("memberId");
%>

<%
	if(memberId == null) {
		response.sendRedirect("../member/login");
	} else {
%>

	<c:forEach items="${deliveryList}" var="delivery">
	    <div class="delivery-box">
    		<div class="delivery-details">
       		<!-- 이미지 목록 표시 -->
				<c:forEach items="${imageList}" var="image">
					   <img src="showImg?imgId=${image.imgId}" alt="${image.imgRealName}.${image.imgExtension}">
				</c:forEach>
	            <p>받는 사람 : ${delivery.receiverName}</p>
	            <p>배송지 : ${delivery.receiverAddress + delivery.deliveryAddressDetails}</p>
	            <p>핸드폰 번호 : ${delivery.phoneNumber}</p>
	            <p>요청사항 : ${delivery.deliveryRequest}</p>
	        </div>
	    </div>
	</c:forEach>

	<!-- 주문 목록이 비어있을 때 -->
    <c:if test="${empty deliveryList}">
        <div>
            <p>배송지 목록이 비어 있습니다.</p>
        </div>
    </c:if>

<% } %>
    
    <a href="../Delivery/deliveryRegister">배송지 추가</a>
</body>
</html>