<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<style type="text/css">
	.box_info {
		border: 1px solid grey;
	}
	.order_box {
		display: flex;
    	flex-direction: row;
	}
	.body_container{
		width: 50%;
		margin: auto
	}
	.inner_header {
		margin: 20px;
	}
	.small_header {
		margin: 10px;
		display: flex;
	}
</style>
<title>결제 결과</title>
</head>
<body>
	<div class="body_container">
	<div class="inner_header">
		<h2>결제 결과</h2>
	</div>
	
	<div class="box_info" id="receiver_info">
		<div class="small_header">
			<h3>받는 사람 정보</h3>
		</div>
		<div>
			<div class="input-group mb-3">
 				<span class="input-group-text">이름</span>
  				<input type="text" class="form-control" id="receiver_name" value="${paymentWrapper.paymentVO.receiverName}">
			</div>
			<div class="input-group mb-3">
 				<span class="input-group-text">배송주소</span>
  				<input type="text" class="form-control" id="delivery_address" value="${paymentWrapper.paymentVO.deliveryAddress}">
			</div>
			<div class="input-group mb-3">
 				<span class="input-group-text">연락처</span>
  				<input type="text" class="form-control" id="receiver_phone" value="${paymentWrapper.paymentVO.receiverPhone}">
			</div>
			<div class="input-group mb-3">
 				<span class="input-group-text">배송 요청사항</span>
  				<input type="text" class="form-control" id="requirement" value="${paymentWrapper.paymentVO.requirement}">
			</div>
		</div>
	</div>
		
	<div class="box_info" id="order_info">
		<div class="small_header">
			<h3>주문 정보</h3>
		</div>
		<c:set var="totalPrice" value="0"/>
		<c:forEach var="orderViewDTO" items="${paymentWrapper.orderList}">
			<c:set var="orderVO" value="${orderViewDTO.orderVO }"></c:set>
			<c:set var="totalPrice" value="${totalPrice + orderVO.productPrice * orderVO.purchaseNum }"/>
			<div class="order_box">
				<img src="${orderViewDTO.imgUrl}">
				<div>
					<div>${orderVO.productName }</div>
					<div>${orderVO.purchaseNum } 개</div>
					<div>${orderVO.productPrice * orderVO.purchaseNum } 원</div>
				</div>
			</div>
		</c:forEach>
	</div>
		
	<div class="box_info" id="payment_info">
		<div class="small_header">
			<h3>결제 정보</h3>
		</div>
		<div>
			<div class="input-group mb-3">
 				<span class="input-group-text">총 상품 가격</span>
  				<input type="text" class="form-control" value="${totalPrice} 원">
			</div>
			<div class="input-group mb-3">
 				<span class="input-group-text">멤버십 할인</span>
  				<input type="text" class="form-control" id="membership_discount" value="${paymentWrapper.paymentVO.membershipDiscount}">
			</div>
			<div class="input-group mb-3">
 				<span class="input-group-text">쿠폰 할인</span>
  				<input type="text" class="form-control" id="coupon_discount" value="${paymentWrapper.paymentVO.couponDiscount}">
			</div>
			<div class="input-group mb-3">
 				<span class="input-group-text">배송비</span>
  				<input type="text" class="form-control" id="delivery_price" value="${paymentWrapper.paymentVO.deliveryPrice} 원">
			</div>
			<div class="input-group mb-3">
 				<span class="input-group-text">총 결제 금액</span>
  				<input type="text" class="form-control" id="charge_price" value="${paymentWrapper.paymentVO.chargePrice} 원">
			</div>
		</div>
	</div>
		
	<div>
		<button class="btn btn-primary" onclick="toHome()"> 돌아가기 </button>
		
	</div>
	</div>
	<script type="text/javascript">
		
		function toHome(){
			location.href = "../board/main";
		} // end toHome
		
	</script>
</body>
</html>