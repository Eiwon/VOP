<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAuthenticated()">
    <sec:authentication var="memberDetails" property="principal"/>
</sec:authorize> 
<jsp:include page="../include/header.jsp"></jsp:include>
<!DOCTYPE html>
<html lang="ko">
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
    .delivery-details p {
        margin-bottom: 10px;
    }
    .delivery-details label {
        display: inline-block;
        width: 150px;
        font-weight: bold;
    }
    .default-checkbox {
        margin-top: 10px;
    }
</style>
</head>
<body>
<h2>배송지 관리</h2>

<script>
    const memberId = '${memberDetails.getUsername()}';
</script>

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
                    <a href="../Delivery/deliveryUpdate?deliveryId=${delivery.deliveryId}">수정</a>
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

<a href="../Delivery/deliveryRegister">배송지 추가</a>
</body>
</html>
