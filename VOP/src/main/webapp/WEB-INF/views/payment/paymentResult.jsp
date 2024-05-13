<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
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
					<td id="receiver_name"></td>
				</tr>
				<tr>
					<td>배송주소</td>
					<td id="delivery_address"></td>
				</tr>
				<tr>
					<td>연락처</td>
					<td id="receiver_phone"></td>
				</tr>
				<tr>
					<td>배송 요청사항</td>
					<td id="requirement"></td>
				</tr>
			</tbody>
		</table>
	</div>
		
	<div class="box_info" id="order_info">
		<h2>주문 정보</h2>
		<table>
			<tbody id="order_list">
				
			</tbody>
		</table>
	</div>
		
	<div class="box_info" id="payment_info">
		<h2>결제 정보</h2>
		<table>
			<tbody>
				<tr>
					<td>총 상품 가격</td>
					<td id="total_price"></td>
				</tr>
				<tr>
					<td>멤버십 할인</td>
					<td id="membership_discount"></td>
				</tr>
				<tr>
					<td>할인 쿠폰</td>
					<td id="coupon_discount"></td>
				</tr>
				<tr>
					<td>배송비</td>
					<td id="delivery_price"></td>
				</tr>
				<tr>
					<td>총 결제 금액</td>
					<td id="charge_price"></td>
				</tr>
			</tbody>
		</table>
	</div>
		
	<div>
		<button> 돌아가기 </button>
		
	</div>
	
	<script type="text/javascript">
		let orderList;
		let paymentVO;
		
		$(document).ready(function(){
			
			showPaymentResult();
			
		}); // end document.ready
		
		
		function showPaymentResult() {
			
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
							'<td><img src="../product/showImg?imgId=' + orderList[x].imgId + '"></td>' +
							'<td>' + orderList[x].productName + '<br>' + orderList[x].purchaseNum + '<br>' +
							orderList[x].productPrice * orderList[x].purchaseNum + '</td></tr>';
					}
					$('#order_list').html(orderListForm);
					
				} // end success
			}); // end ajax
			
		} // end showPaymentResult
	
		
		function calcTotalPrice() {
			let totalPrice = 0;
			
			for(x in orderList){
				totalPrice += orderList[x].productPrice * orderList[x].purchaseNum;
			}
			console.log('합계 : ' + totalPrice);
			return totalPrice;
		} // end calcTotalPrice
		
		function toDetails(input){
			const index = $(input).find('.order_num').text();
			console.log(orderList[index].productId);
		} // end toDetails
		
		
	</script>
</body>
</html>