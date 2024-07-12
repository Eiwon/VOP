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
		border: 1px solid black;
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
		margin-bottom: 10px;
		margin-top: 15px;
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
		<table>
			<tbody>
				<tr>
					<td>이름</td>
					<td id="receiver_name">${paymentWrapper.paymentVO.receiverName}</td>
				</tr>
				<tr>
					<td>배송주소</td>
					<td id="delivery_address">${paymentWrapper.paymentVO.deliveryAddress}</td>
				</tr>
				<tr>
					<td>연락처</td>
					<td id="receiver_phone">${paymentWrapper.paymentVO.receiverPhone}</td>
				</tr>
				<tr>
					<td>배송 요청사항</td>
					<td id="requirement">${paymentWrapper.paymentVO.requirement}</td>
				</tr>
			</tbody>
		</table>
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
		<table>
			<tbody>
				<tr>
					<td>총 상품 가격</td>
					<td>${totalPrice} 원</td>
				</tr>
				<tr>
					<td>멤버십 할인</td>
					<td id="membership_discount">${paymentWrapper.paymentVO.membershipDiscount} %</td>
				</tr>
				<tr>
					<td>쿠폰 할인</td>
					<td id="coupon_discount">${paymentWrapper.paymentVO.couponDiscount} %</td>
				</tr>
				<tr>
					<td>배송비</td>
					<td id="delivery_price">${paymentWrapper.paymentVO.deliveryPrice} 원</td>
				</tr>
				<tr>
					<td>총 결제 금액</td>
					<td id="charge_price">${paymentWrapper.paymentVO.chargePrice} 원</td>
				</tr>
			</tbody>
		</table>
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