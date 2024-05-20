<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<!-- 포트원 결제 -->
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
<!-- 포트원 결제 -->
<style type="text/css">
	.box_info {
		border: 1px solid black;
		width: 1000px;
	}
	#coupon_list{
		list-style: none;
	}
</style>
<title>결제 페이지</title>
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
			<h2>받는 사람 정보 <button onclick="showDeliveryPopup()">배송지 변경</button></h2>
			<table>
				<tbody>
					<tr>
						<td>이름</td>
						<td><input type="text" id="receiverName" readonly></td>
					</tr>
					<tr>
						<td>배송주소</td>
						<td><input type="text" id="receiverAddress" readonly></td>
					</tr>
					<tr>
						<td>상세주소</td>
						<td><input type="text" id="deliveryAddressDetails" readonly></td>
					</tr>
					<tr>
						<td>연락처</td>
						<td><input type="text" id="receiverPhone" readonly></td>
					</tr>
					<tr>
						<td>배송 요청사항</td>
						<td><input type="text" id="requirement"></td>
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
						<td>쿠폰 할인</td>
						<td id="coupon_discount"></td>
						<td><button id="btn_coupon" onclick="selectCoupon()">쿠폰 선택</button></td>
					</tr>
					<tr>
						<td>
							<table>
								<tbody id="coupon_list">
									
								</tbody>
							</table>
						</td>
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
			<button id="btn_payment"> 결제하기 </button>
		
		</div>
		
	</div>
	
	<script type="text/javascript">
		let paymentWrapper = JSON.parse('${paymentWrapper}');
		let memberVO = paymentWrapper.memberVO;
		let orderList = paymentWrapper.orderList;
		let deliveryVO = paymentWrapper.deliveryVO;
		let couponList;
		let couponVO;
		let paymentVO = {
				'paymentId' : 0,
				'memberId' : '',
				'deliveryAddress' : '',
				'receiverName' : '',
				'receiverPhone' : '',
				'requirement' : '',
				'membershipDiscount' : 0,
				'couponDiscount' : 0,
				'deliveryPrice' : 0,
				'chargePrice' : 0
				};
		let infoContainer = $('#info_container');
		let tagOrderList = $('#order_list');
		
		
		$(document).ready(function(){
			
			setInfo();
			$('#btn_payment').click(function(){
				payment();
			}); // end btnPayment.click
			
		}); // end document.ready
		
		function setInfo(){
			$('#member_name').text(memberVO.memberName);
			$('#member_email').text(memberVO.memberEmail);
			$('#member_phone').text(memberVO.memberPhone);
			$('#receiverName').text(deliveryVO.receiverName);
			$('#receiverAddress').text(deliveryVO.receiverAddress);
			$('#deliveryAddressDetails').text(deliveryVO.deliveryAddressDetails);
			$('#receiverPhone').text(deliveryVO.receiverPhone);
			$('#requirement').text(deliveryVO.requirement);
			tagOrderList.html(makeOrderInfo());
			
			
			//if(memberVO.auth == 'membership') 일단 모든 유저에 멤버십 적용
			paymentVO.membershipDiscount = 20;
			
			setPaymentInfo();
			
			
		} // end setInfo
		
		function makeOrderInfo(){
			// 출력할 주문 정보 생성
			let form = '';
			for (x in orderList){
				form += '<tr><td style="width: 200px;">' + orderList[x].productName + 
				'</td><td style="width: 200px;">' + orderList[x].purchaseNum + 
				'</td><td style="width: 200px;">' + orderList[x].productPrice * orderList[x].purchaseNum + 
				'원</td><td style="width: 200px;">예상 배송일</td></tr>';
			}
			return form;
		} // end makeOrderInfo
		
		function setPaymentInfo(){
			// 결제 정보 출력
			let totalPrice = calcTotalPrice();
			let chargePrice = calcChargePrice();
			
			$('#total_price').text(totalPrice);
			$('#membership_discount').text(paymentVO.membershipDiscount + '%');
			$('#coupon_discount').text(paymentVO.couponDiscount + '%');
			$('#delivery_price').text(paymentVO.deliveryPrice);
			$('#charge_price').text(chargePrice);
			
		} // end setPaymentInfo
		
		function selectCoupon(){
			let tagCouponList = $('#coupon_list');
			couponVO = null;
			paymentVO.couponDiscount = 0;
			setPaymentInfo();
			
			$.ajax({ // 비동기로 쿠폰 정보 가져와야 함
				method : 'GET',
				url : '../coupon/myList',
				success : function(result){
					console.log(result);
					couponList = result;
					
					let form = '';
					for(x in couponList){
					form += '<tr class="couponRow"><td><input type="hidden" class="couponIdx" value="' + x + '"></td>' +
						'<td style="width: 200px;">' + couponList[x].couponName + '</td>' +
						'<td style="width: 200px;">' + couponList[x].discount + '% 할인</td>' +
						'<td style="width: 200px;">' + couponList[x].couponNum + '</td>' + 
						'<td><input type="radio" name="radioCoupon" onclick="applyCoupon(this)"></td></tr>';
					}
			
					tagCouponList.html(form);
				} // end success
			}); // end ajax
			
		} // end selectCoupon
		
		function applyCoupon(input){
			let couponIdx = $(input).parents('.couponRow').find('.couponIdx').val();
			console.log('선택된 쿠폰 이름 : ' + couponList[couponIdx].couponName);
			couponVO = couponList[x];
			paymentVO.couponDiscount = couponList[couponIdx].discount;
			setPaymentInfo();
		} // end applyCoupon
		
		function calcTotalPrice() {
			let totalPrice = 0;
			
			for(x in orderList){
				totalPrice += orderList[x].productPrice * orderList[x].purchaseNum;
			}
			console.log('합계 : ' + totalPrice);
			return Math.ceil(totalPrice);
		} // end calcTotalPrice
		
		function calcChargePrice(){
			let chargePrice = calcTotalPrice() + paymentVO.deliveryPrice;
			let discountPercent = (paymentVO.membershipDiscount + paymentVO.couponDiscount);
			chargePrice = discountPercent >= 100 ? 0 : chargePrice * (100 - discountPercent) / 100;
			
			return Math.ceil(chargePrice);
		} // end calcChargePrice
		
		
		function payment(){ // 결제 실행
			let IMP = window.IMP;
			IMP.init('imp04667313'); // 가맹점 식별코드 설정(포트원 홈페이지에서 확인)
			let paymentId;
			
			paymentVO.chargePrice = calcChargePrice(); // 최종 결제 금액 계산
			
			console.log('request paymentId');
			// 서버에서 결제 고유 번호 받아오기
			$.ajax({
				method : 'GET',
				url : 'getId',
				success : function(result){
					if(result == 0){
						console.log('고유 번호 받아오기 실패');
					}else{
						console.log('결제 고유 번호 : ' + result);
						paymentId = result;
						IMP.request_pay({
							pg: 'kakaopay.TC0ONETIME', // PG사 코드(포트원 홈페이지에서 찾아서 넣어야함)
			                pay_method: 'card', // 결제 방식
			                merchant_uid: paymentId, // 결제 고유 번호
			                name: orderList[0].productName, // 제품명
			                amount: paymentVO.chargePrice, // 결제 가격
			                buyer_name: memberVO.memberId,
			                buyer_email: memberVO.memberEmail,
			                // buyer_tel : '010-1234-5678',
			                // buyer_addr : '서울특별시 강남구 삼성동',
			                // buyer_postcode : '123-456'
			            }, async function (rsp) { // callback
			             	console.log(rsp);
			            	if (rsp.success) { //결제 성공시
			            		sendPaymentResult(rsp);
			                }
			            }); // end IMP.request_pay 
					}
				} // end success 
			}); // end ajax
			
		} // end payment
		
		
		function sendPaymentResult(paymentResult){
			console.log('결제 내역 전송');
			paymentVO.paymentId = paymentResult.merchant_uid;
			paymentVO.memberId = paymentResult.buyer_name;
			paymentVO.deliveryAddress = $('#deliveryAddress').val();
			paymentVO.receiverName = $('#receiverName').val();
			paymentVO.receiverPhone = $('#receiverPhone').val();
			paymentVO.requirement = $('#requirement').val();
			
			
			$.ajax({
				method : 'POST',
				url : 'apply',
				headers : {
					'Content-Type' : 'application/json'
				},
				data : JSON.stringify({
					'paymentVO' : paymentVO,
					'orderList' : orderList,
					'couponVO' : couponVO
				}),
				success : function(result){
					console.log('결제 내역 전송 결과 : ' + result);
					location.href = 'paymentResult';
				}
			}); // end ajax
			
		} // end sendPaymentResult
		
		function showDeliveryPopup(){
			
			const popupStat = {
					'url' : 'popupDeliverySelect',
					'name' : 'popupDeliverySelect',
					'option' : 'width=500, height=600, top=50, left=400'
			};
			
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
				deliveryVO.receiverName = $('#receiverName').val();
				deliveryVO.receiverAddress = $('#receiverAddress').val();
				deliveryVO.deliveryAddressDetails = $('#deliveryAddressDetails').val();
				deliveryVO.receiverPhone = $('#receiverPhone').val();
				deliveryVO.requirement = $('#requirement').val();
				
				console.log(deliveryVO);
			} // end popup.onbeforeunload
		} // end showDeliveryPopup
		
	</script>
	
</body>
</html>