<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 쿠폰함</title>
<jsp:include page="../include/header.jsp"></jsp:include>
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
			<c:when test="${couponList.size() == 0 }">
				<strong>보유 중인 쿠폰이 없습니다</strong>
			</c:when>
			<c:otherwise>
				<c:forEach var="myCouponVO" items="${couponList }">
					<div class="coupon_item">
						<div>쿠폰명 : <strong>${myCouponVO.couponName }</strong></div>
						<div>할인률 : <strong>${myCouponVO.discount }</strong></div>
						<div>만료일 : <strong>${myCouponVO.expirationDate.toLocaleString() }</strong></div>
						<c:choose>
							<c:when test="${myCouponVO.isUsed == 0}">
								<div>사용 여부 : <strong>미사용</strong></div>
							</c:when>
							<c:otherwise>
								<div>사용 여부 : <strong>사용</strong></div>
							</c:otherwise>
						</c:choose>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>
	

</body>
</html>