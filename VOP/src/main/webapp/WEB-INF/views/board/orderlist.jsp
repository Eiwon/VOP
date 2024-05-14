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
<title>주문 목록</title>
<style>
    .order-box {
        border: 1px solid #ccc;
        padding: 10px;
        margin-bottom: 20px;
    }
    .order-details {
        display: flex;
        align-items: center;
        margin-bottom: 10px;
    }
    .order-details img {
        margin-right: 10px;
        max-width: 100px;
    }
    .order-buttons {
        margin-left: auto;
    }
</style>

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
	} else {
%>

	
    <c:forEach items="${orderList}" var="order">
        <div class="order-box">
            <div class="order-details">
                <p>예상 배송일 : ${order.expectDeliveryDate}</p>
                	<!-- 이미지 목록 표시 -->
					<c:forEach items="${imageList}" var="image">
					    <img src="showImg?imgId=${image.imgId}" alt="${image.imgRealName}.${image.imgExtension}">
					</c:forEach>
                <div>
                    <p>상품명 : ${order.productName}</p>
                    <p>상품 가격 : ${order.productPrice} 원</p>
                    <p>상품 수량 : ${order.purchaseNum} 개</p>
                </div>
            </div>
            <div class="order-buttons">
                <a href=""><button>배송 조회</button></a>
                <a href=""><button>리뷰 쓰기</button></a>
				<a href=""><button>교환/반품 신청</button></a>
            </div>
        </div>
    </c:forEach>
    
    <!-- 주문 목록이 비어있을 때 -->
    <c:if test="${empty orderList}">
        <div>
            <p>주문 목록이 비어 있습니다.</p>
        </div>
    </c:if>
    
<%  
    }   
%>

	
</body>
</html>