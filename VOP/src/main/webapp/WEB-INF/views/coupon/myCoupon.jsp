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
		margin: 2px;
	}
	.coupon_disable{
		border: 0.5px solid red;
	}
	.coupon_enable{
		border: 0.5px solid blue;
	}
	.body_container{
		width: 60%;
		margin: auto;
	}
	.inner_header {
		margin: 40px;
	}
</style>
</head>
<body>
	<jsp:useBean id="current" class="java.util.Date"/>
	
	<jsp:include page="../include/sideBar.jsp"/>
	
	<div class="body_container">
		<div class="inner_header">
			<h3>내 쿠폰함</h3>
		</div>
		<div class="row row-cols-2">
			<c:choose>
			<c:when test="${couponList.size() == 0 }">
				<strong>보유 중인 쿠폰이 없습니다</strong>
			</c:when>
			<c:otherwise>
				<c:forEach var="myCouponVO" items="${couponList }">
					<div class="coupon_item card <c:choose>
							<c:when test="${myCouponVO.isUsed == 0 && current.before(myCouponVO.expirationDate) }">
								coupon_enable
							</c:when>
							<c:otherwise>
								coupon_disable
							</c:otherwise>
						</c:choose>" style="width: 18rem">
						<div class="card-body">
    					<h4 class="card-title">${myCouponVO.couponName }</h4>
   						<p class="card-text">${myCouponVO.discount } % 할인</p>
   	 					<p class="card-text">${myCouponVO.expirationDate.toLocaleString() } 까지</p>
   	 					<c:choose>
							<c:when test="${myCouponVO.isUsed == 0}">
								<div>사용 여부 : <strong>미사용</strong></div>
							</c:when>
							<c:otherwise>
								<div>사용 여부 : <strong>사용</strong></div>
							</c:otherwise>
						</c:choose>
  						</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>
	</div>
</body>
</html>