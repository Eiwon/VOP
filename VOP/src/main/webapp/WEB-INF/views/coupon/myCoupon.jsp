<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 쿠폰함</title>
<style type="text/css">
	.coupon_item{
		border: 0.5px solid blue;
	}
</style>
</head>
<body>
	<h2>내 쿠폰함</h2>
	<div>
		<c:choose>
			<c:when test="${couponList == null }">
				<strong>보유 중인 쿠폰이 없습니다</strong>
			</c:when>
			<c:otherwise>
				<c:forEach var="myCouponVO" items="${couponList }">
					<div class="coupon_item">
						<div>쿠폰명 : <strong>${myCouponVO.couponName }</strong></div>
						<div>할인률 : <strong>${myCouponVO.discount }</strong></div>
						<div>만료일 : <strong>${myCouponVO.expirationDate.toLocaleString() }</strong></div>
						<div>보유량 : <strong>${myCouponVO.couponNum }</strong></div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>
	

</body>
</html>