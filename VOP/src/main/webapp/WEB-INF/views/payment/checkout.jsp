<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
</style>
<title>Insert title here</title>
</head>
<body>
	<div>
		<h2>주문 / 결제</h2>
		<div class="box_info" id="buyer_info">
			<h2>구매자 정보</h2>
			<table>
				<tbody>
					<tr>
						<td>이름</td>
						<td id="member_name"></td>
					</tr>
					<tr>
						<td>이메일</td>
						<td id="member_email"></td>
					</tr>
					<tr>
						<td>휴대폰 번호</td>
						<td id="member_phone"></td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="box_info" id="receiver_info">
			<h2>받는 사람 정보</h2>
			<table>
				<tbody>
					<tr>
						<td>이름</td>
						<td>배송지정보</td>
					</tr>
					<tr>
						<td>배송주소</td>
						<td>구현 후</td>
					</tr>
					<tr>
						<td>연락처</td>
						<td>추가 예정</td>
					</tr>
					<tr>
						<td>배송 요청사항</td>
						<td>...</td>
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
						<td></td>
					</tr>
					<tr>
						<td>할인 쿠폰</td>
						<td id="coupon_price"></td>
						<td><button onclick="selectCoupon()">쿠폰 선택</button></td>
					</tr>
					<tr id="coupon_list"></tr>
					<tr>
						<td>배송비</td>
						<td></td>
					</tr>
					<tr>
						<td>총 결제 금액</td>
						<td id="total_payment"></td>
					</tr>
				</tbody>
			</table>
		</div>
		
	</div>
	
	<script type="text/javascript">
		let memberVO = JSON.parse('${memberVO}');
		let orderList = JSON.parse('${orderList}');
		let infoContainer = $('#info_container');
		let tagMemberName = $('#member_name');
		let tagMemberEmail = $('#member_email');
		let tagMemberPhone = $('#member_phone');
		let tagOrderList = $('#order_list');
		let tagTotalPrice = $('#total_price');
		let tagTotalPayment = $('#total_payment');
		
		$(document).ready(function(){
		}); // end document.ready
		
		function setInfo(){
			tagMemberName.text(memberVO.memberName);
			tagMemberEmail.text(memberVO.memberEmail);
			tagMemberPhone.text(memberVO.memberPhone);
			tagOrderList.html(makeOrderInfo());
			tagTotalPrice.text(calcTotalPrice());
		} // end setInfo
		
		function makeOrderInfo(){
			let form = '';
			for (x in orderList){
				form += '<tr><td style="width: 200px;">' + orderList[x].productName + 
				'</td><td style="width: 200px;">' + orderList[x].purchaseNum + 
				'</td><td style="width: 200px;">' + orderList[x].productPrice * orderList[x].purchaseNum + 
				'원</td><td style="width: 200px;">예상 배송일</td></tr>';
			}
			return form;
		} // end makeOrderInfo
		
		function selectCoupon(){
			let tagCouponList = $('#coupon_list');
			let couponList = [
				{
					'couponId' : '1234',
					'couponName' : '쿠폰 테스트',
					'couponPrice' : '1000'
				},{
					'couponId' : '1234',
					'couponName' : '테스트 쿠폰',
					'couponPrice' : '3000'
				}
			];
			
			// 비동기로 쿠폰 정보 가져와야 함
			
			let form = '';
			for(x in couponList){
				form += '<div style="display: flex;">' + 
				'<input type="hidden" value="' + couponList[x].couponId + '">' +
				'<span>' + couponList[x].couponName + '</span>' +
				'<span>' + couponList[x].couponPrice + '</span>' +
				'<input type="radio" name="coupon"><div>';
			}
			
			tagCouponList.html(form);
			
		} // end selectCoupon
		
		function calcTotalPrice() {
			let totalPrice = 0;
			
			for(x in orderList){
				totalPrice += orderList[x].productPrice * orderList[x].purchaseNum;
			}
			console.log('합계 : ' + totalPrice);
			return totalPrice;
		} // end calcTotalPrice
		
	</script>
	
</body>
</html>