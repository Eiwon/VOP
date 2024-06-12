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
		width: 1000px;
	}
	.order_box {
		display: flex;
    	flex-direction: row;
	}
</style>
<title>결제 결과</title>
</head>
<body>

	<div>
		<h2>결제 결과</h2>
	</div>
	
		
	<div class="box_info" id="receiver_info">
		<h2>받는 사람 정보</h2>
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
		<h2>주문 정보</h2>
		<c:set var="totalPrice" value="0"/>
		<c:forEach var="order" items="${paymentWrapper.orderList}">
			<c:set var="totalPrice" value="${totalPrice + order.productPrice * order.purchaseNum }"/>
			<div class="order_box">
				<img alt="${order.imgId}">
				<div>
					<div>${order.productName }</div>
					<div>${order.purchaseNum } 개</div>
					<div>${order.productPrice * order.purchaseNum } 원</div>
				</div>
			</div>
		</c:forEach>
	</div>
		
	<div class="box_info" id="payment_info">
		<h2>결제 정보</h2>
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
		<button onclick="toHome()"> 돌아가기 </button>
		
	</div>
	
	<script type="text/javascript">
		//let orderList;
		//let paymentVO;
		
		$(document).ready(function(){
			//showPaymentResult();
			loadImg($('#order_info'));
		}); // end document.ready
		
		
		/* function showPaymentResult() {
			
			$.ajax({
				method : 'GET',
				url : 'payment',
				success : function(result){
					console.log('결제 결과 : ' + result);
					paymentVO = result.paymentVO;
					orderList = result.orderList;
					
					$('#receiver_name').text(paymentVO.receiverName);
					$('#receiver_phone').text(paymentVO.receiverPhone);
					$('#delivery_address').text(paymentVO.deliveryAddress);
					$('#requirement').text(paymentVO.requirement);
					$('#total_price').text(calcTotalPrice() + '원');
					$('#membership_discount').text(paymentVO.membershipDiscount + '%');
					$('#coupon_discount').text(paymentVO.couponDiscount + '%');
					$('#delivery_price').text(paymentVO.deliveryPrice + '원');
					$('#charge_price').text(paymentVO.chargePrice + '원');
					
					let orderListForm = '';
					for(x in orderList){
						orderListForm += '<tr class="order_box" onclick="toDetails(this)">' +
							'<td hidden="hidden" class="order_num">' + x + '</td>' +
							'<td><img alt="' + orderList[x].imgId + '"></td>' +
							'<td>' + orderList[x].productName + '<br>' + orderList[x].purchaseNum + '<br>' +
							orderList[x].productPrice * orderList[x].purchaseNum + '</td></tr>';
					}
					$('#order_list').html(orderListForm);
					loadImg($('#order_list'));
				} // end success
			}); // end ajax
			
		} // end showPaymentResult */
	
		
		/* function calcTotalPrice() {
			let totalPrice = 0;
			
			for(x in orderList){
				totalPrice += orderList[x].productPrice * orderList[x].purchaseNum;
			}
			console.log('합계 : ' + totalPrice);
			return totalPrice;
		} // end calcTotalPrice */
		
		
		function loadImg(input){
			$(input).find('img').each(function(){
				let target = $(this);
				let imgId = target.attr("alt");
				$.ajax({
					method : 'GET',
					url : '../image/' + imgId,
					success : function(result){
						target.attr('src', result);
					}
				}); // end ajax
			});
		} // end loadImg
		
		function toHome(){
			location.href = "../board/main";
		} // end toHome
		
		
	</script>
</body>
</html>