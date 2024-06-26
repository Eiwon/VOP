<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<!-- 포트원 결제 -->
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<!-- 포트원 결제 -->
<style type="text/css">
	.box_info {
		border: 1px solid black;
		width: 1000px;
	}
	#coupon_list{
		list-style: none;
	}
	.couponRow {
		border: 0.5px solid blue;
	}
</style>
<title>결제 페이지</title>
</head>
<body>
	<c:set var="memberVO" value="${paymentWrapper.memberVO}"/>
	<c:set var="deliveryVO" value="${paymentWrapper.deliveryVO}"/>
	<c:set var="orderList" value="${paymentWrapper.orderList}"/>
	<c:set var="membershipVO" value="${paymentWrapper.membershipVO }"/>
	<c:set var="totalPrice" value="0"/>
	<div>
		<h2>주문 / 결제</h2>
		<div class="box_info" id="buyer_info">
			<h2>구매자 정보</h2>
			<table>
				<tbody>
					<tr>
						<td>이름</td>
						<td id="member_name">${memberVO.memberName }</td>
					</tr>
					<tr>
						<td>이메일</td>
						<td id="member_email">${memberVO.memberEmail }</td>
					</tr>
					<tr>
						<td>휴대폰 번호</td>
						<td id="member_phone">${memberVO.memberPhone }</td>
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
						<td><input type="text" id="receiverName" value="${deliveryVO.receiverName }" readonly></td>
					</tr>
					<tr>
						<td>배송주소</td>
						<td><input type="text" id="receiverAddress" value="${deliveryVO.receiverAddress }" readonly></td>
					</tr>
					<tr>
						<td>상세주소</td>
						<td><input type="text" id="deliveryAddressDetails" value="${deliveryVO.deliveryAddressDetails }" readonly></td>
					</tr>
					<tr>
						<td>연락처</td>
						<td><input type="text" id="receiverPhone" value="${deliveryVO.receiverPhone }" readonly></td>
					</tr>
					<tr>
						<td>배송 요청사항</td>
						<td><input type="text" id="requirement" value="${deliveryVO.requirement }"></td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="box_info" id="order_info">
			<h2>주문 정보</h2>
			<div id="order_list">
				<c:forEach var="orderDTO" items="${orderList }">
					<c:set var="orderVO" value="${orderDTO.orderVO }"/>
					<c:set var="totalPrice" value="${totalPrice + orderVO.productPrice *  orderVO.purchaseNum }"></c:set>
					<div>
						<div>
							<img src="${orderDTO.imgUrl }">
						</div>
						<div>
							<div style="width: 200px;">${orderVO.productName }</div>
							<div style="width: 200px;">${orderVO.purchaseNum }</div>
							<div style="width: 200px;">${orderVO.productPrice *  orderVO.purchaseNum }원</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
		
		<div class="box_info" id="payment_info">
			<h2>결제 정보</h2>
			<table>
				<tbody>
					<tr>
						<td>총 상품 가격</td>
						<td id="total_price">${totalPrice }</td>
					</tr>
					<tr>
						<td>멤버십 할인</td>
						<td id="membership_discount">
						<c:set var="membershipDiscount" value="0"/>
						<c:choose>
							<c:when test="${membershipVO != null }">
							<c:set var="membershipDiscount" value="20"/>
							${membershipDiscount }%
							</c:when>
							<c:otherwise>
							0%
							</c:otherwise>
						</c:choose>
						</td>
					</tr>
					<tr>
						<td>쿠폰 할인</td>
						<td id="coupon_discount">${couponVO.discount }%</td>
						<td><button id="btn_coupon" onclick="selectCoupon()">쿠폰 선택</button></td>
					</tr>
					<tr>
						<td>
							<div id="coupon_list">
							</div>
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
		let deliveryVO;
		let myCouponList;
		let myCouponVO;
		let paymentVO = {
				'couponDiscount' : 0,
				'deliveryPrice' : 0,
				'chargePrice' : 0
				};
		let infoContainer = $('#info_container');
		let tagOrderList = $('#order_list');
		let totalPrice = parseInt('${totalPrice}');
		let addressChk = false;
		
		$(document).ready(function(){
			//console.log(paymentWrapper);
			setInfo();
			$('#btn_payment').click(function(){
				
				paymentVO.requirement = $('#requirement').val();
				if(!addressChk){
					alert('주소를 입력해주세요');
					return;
				}
				
				payment();
			}); // end btnPayment.click
			
		}); // end document.ready
		
		function setInfo(){
			
			paymentVO.membershipDiscount = parseInt('${membershipDiscount}');
			paymentVO.deliveryAddress = '${deliveryVO.receiverAddress}' + '${deliveryVO.deliveryAddressDetails}';
			paymentVO.receiverName = '${deliveryVO.receiverName}';
			paymentVO.receiverPhone = '${deliveryVO.receiverPhone}';
			paymentVO.requirement = '${deliveryVO.requirement}';
			if(paymentVO.deliveryAddress.length != 0){
				addressChk = true;			
			}
			
			setPaymentInfo();
		} // end setInfo
		
		
		function setPaymentInfo(){
			// 결제 정보 출력
			$('#coupon_discount').text(paymentVO.couponDiscount + '%');
			$('#delivery_price').text(paymentVO.deliveryPrice);
			$('#charge_price').text(calcChargePrice());
			
		} // end setPaymentInfo
		
		function selectCoupon(){
			let tagCouponList = $('#coupon_list');
			myCouponVO = null;
			paymentVO.couponDiscount = 0;
			setPaymentInfo();
			
			$.ajax({ // 비동기로 쿠폰 정보 가져와야 함
				method : 'GET',
				url : '../coupon/myList',
				success : function(result){
					console.log(result);
					myCouponList = result;
					
					let form = '';
					for(x in myCouponList){
					form += '<div class="couponRow"><div><input type="hidden" class="couponIdx" value="' + x + '"></div>' +
						'<div style="width: 200px;">쿠폰명 ' + myCouponList[x].couponName + '<input type="radio" name="radioCoupon" onclick="applyCoupon(this)"></div>' +
						'<div style="width: 200px;">할인률 ' + myCouponList[x].discount + '% 할인</div>' + 
						'</div>';
					}
			
					tagCouponList.html(form);
				} // end success
			}); // end ajax
			
		} // end selectCoupon
		
		function applyCoupon(input){
			let couponIdx = $(input).parents('.couponRow').find('.couponIdx').val();
			console.log('선택된 쿠폰 이름 : ' + myCouponList[couponIdx].couponName);
			myCouponVO = myCouponList[x];
			paymentVO.couponDiscount = myCouponList[couponIdx].discount;
			setPaymentInfo();
		} // end applyCoupon
		
		function calcChargePrice(){
			let chargePrice = totalPrice + paymentVO.deliveryPrice;
			let discountPercent = (paymentVO.membershipDiscount + paymentVO.couponDiscount);
			chargePrice = (discountPercent >= 100) ? 0 : Math.floor(chargePrice * (100 - discountPercent) / 100);
			
			paymentVO.chargePrice = chargePrice;
			return chargePrice;
		} // end calcChargePrice
		
		
		function payment(){ // 결제 실행
			let IMP = window.IMP;
			IMP.init('imp04667313'); // 가맹점 식별코드 설정(포트원 홈페이지에서 확인)
			let paymentId;
			
			paymentVO.chargePrice = calcChargePrice(); // 최종 결제 금액 계산
			
			// 서버에서 결제 고유 번호 받아오기
			$.ajax({
				method : 'GET',
				url : 'getId',
				success : function(result){
					if(result == 0){
						console.log('고유 번호 받아오기 실패');
					}else{
						console.log('결제 고유 번호 : ' + result);
						paymentVO.paymentId = result;
						IMP.request_pay({
							pg: 'kakaopay.TC0ONETIME', // PG사 코드(포트원 홈페이지에서 찾아서 넣어야함)
			                pay_method: 'card', // 결제 방식
			                merchant_uid: paymentVO.paymentId, // 결제 고유 번호
			                name: '${orderList.get(0).orderVO.productName} 외 ${orderList.size() -1} 개', // 제품명
			                amount: paymentVO.chargePrice, // 결제 가격
			                buyer_name: '${memberVO.memberId}',
			                buyer_email: '${memberVO.memberEmail}',
			            }, async function (rsp) { // callback
			             	/*rsp 예시
			             	{
			            	apply_num:""
			            	bank_name:null
			            	buyer_addr:""
			            	buyer_email:"test1234@naver.com"
			            	buyer_name:"test1234"
			            	buyer_postcode:""
			            	buyer_tel:""
			            	card_name:null
			            	card_number:""
			            	card_quota:0
			            	currency:"KRW"
			            	custom_data:null
			            	imp_uid:"imp_899047014817"
			            	merchant_uid:"1046"
			            	name:"사과"
			            	paid_amount:30
			            	paid_at: 1717991067
			            	pay_method:"point"
			            	pg_provider:"kakaopay"
			            	pg_tid:"T6667687257c75a57ce9"
			            	pg_type:"payment"
			            	receipt_url:"https://mockup-pg-web.kakao.com/v1/confirmation/p/T6667687257c75a57ce9/f57fa5a352ed130933506fabcef19aa974d46a98c05b9da464d5a602a2e34da7"
			            	status: "paid"
			            	success:true
			             	}
			             	*/
			            
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
			console.log(deliveryVO);
			paymentVO.paymentId = paymentResult.merchant_uid;
			paymentVO.memberId = paymentResult.buyer_name;
			paymentVO.chargeId = paymentResult.imp_uid;
			
			let orderList = [];
			<c:forEach var="orderDTO" items="${orderList }">
			<c:set var="orderVO" value="${orderDTO.orderVO }"/>
				orderList.push({
					orderVO : {
						productId : '${orderVO.productId}',
						productName : '${orderVO.productName}',
						productPrice : '${orderVO.productPrice}',
						purchaseNum : '${orderVO.purchaseNum}',
						imgId : '${orderVO.imgId}'
					}
				});
			</c:forEach>
			
			console.log(paymentVO);
			
			$.ajax({
				method : 'POST',
				url : 'apply',
				headers : {
					'Content-Type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				data : JSON.stringify({
					'paymentVO' : paymentVO,
					'orderList' : orderList,
					'myCouponVO' : myCouponVO
				}),
				success : function(result){
					console.log('결제 내역 전송 결과 : ' + result);
					if(result > 0){
						location.href = 'paymentResult?paymentId=' + paymentVO.paymentId;
					}else if(result == 0){
						alert('결제 내역 전송 실패');		
					}else if(result == -1){
						alert('매진된 상품입니다.');
					}
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

		} // end showDeliveryPopup
		function saveDelivery(delivery){
			console.log("팝업 닫힘");
			deliveryVO = delivery;
			$('#receiverName').val(deliveryVO.receiverName);
			$('#receiverAddress').val(deliveryVO.receiverAddress);
			$('#deliveryAddressDetails').val(deliveryVO.deliveryAddressDetails);
			$('#receiverPhone').val(deliveryVO.receiverPhone);
			$('#requirement').val(deliveryVO.requirement);
			paymentVO.deliveryAddress = deliveryVO.receiverAddress + deliveryVO.deliveryAddressDetails;
			paymentVO.receiverName = deliveryVO.receiverName;
			paymentVO.receiverPhone = deliveryVO.receiverPhone;
			paymentVO.requirement = deliveryVO.requirement;
			addressChk = true;
			console.log(deliveryVO);
		}
		
	</script>
	
</body>
</html>