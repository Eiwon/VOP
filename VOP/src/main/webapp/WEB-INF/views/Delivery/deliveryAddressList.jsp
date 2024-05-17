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
    
    <style>
    .delivery-details p {
        margin-bottom: 10px;
    }

    .delivery-details label {
        display: inline-block;
        width: 150px; /* 라벨 너비 조절 */
        font-weight: bold;
    }

    .default-checkbox {
        margin-top: 10px;
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

	<table border="1">
    <thead>
        <tr>
            <th>받는 사람</th>
            <th>최종배송지</th>
            <th>휴대폰 번호</th>
            <th>배송 요청사항</th>
            <th>기본 배송지 설정</th>
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${deliveryList}" var="delivery">
            <tr>
                <td>${delivery.receiverName}</td>
                <td>${delivery.receiverAddress} ${delivery.deliveryAddressDetails}</td>
                <td>${delivery.receiverPhone}</td>
                <td>${delivery.requirement}</td>
                <td>
                    <c:if test="${delivery.isDefault eq 1}">
                        기본 배송지
                    </c:if>
                </td>
                <td>
                    <c:if test="${delivery.isDefault eq 0}">
                        <a href="../Delivery/deliveryUpdate?deliveryId=${delivery.deliveryId}">수정</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
	

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