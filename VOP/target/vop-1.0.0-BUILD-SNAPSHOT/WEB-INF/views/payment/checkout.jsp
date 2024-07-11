<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
	#coupon_list{
		list-style: none;
	}
	.couponRow {
		border: 0.5px solid blue;
		height: 40px;
		display: flex;
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
<title>결제 페이지</title>
</head>
<body>
	<c:set var="memberVO" value="${paymentWrapper.memberVO}"/>
	<c:set var="deliveryVO" value="${paymentWrapper.deliveryVO}"/>
	<c:set var="orderList" value="${paymentWrapper.orderList}"/>
	<c:set var="membershipVO" value="${paymentWrapper.membershipVO }"/>
	<c:set var="totalPrice" value="0"/>
	<div class="body_container">
		<div class="inner_header">
			<h2>주문 / 결제</h2>
		</div>
		<div class="box_info" id="buyer_info">
			<div class="small_header">
				<h3>구매자 정보</h3>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">이름</span>
  				<input type="text" class="form-control" value="${memberVO.memberName }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">이메일</span>
  				<input type="text" class="form-control" value="${memberVO.memberEmail }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">휴대폰 번호</span>
  				<input type="text" class="form-control" value="${memberVO.memberPhone }" readonly>
			</div>
		</div>
		<div class="box_info" id="receiver_info">
			<div class="small_header">
				<h3>받는 사람 정보</h3>
				<button onclick="showDeliveryPopup()" style="margin-left: 55%" class="btn btn-outline-primary">배송지 변경</button>
			</div>
			<div>
				<div class="input-group mb-3">
  					<span class="input-group-text">이름</span>
  					<input type="text" id="receiverName" class="form-control" value="${deliveryVO.receiverName }" readonly>
				</div>
				<div class="input-group mb-3">
  					<span class="input-group-text">배송주소</span>
  					<input type="text" id="receiverAddress" class="form-control" value="${deliveryVO.receiverAddress }" readonly>
				</div>
				<div class="input-group mb-3">
  					<span class="input-group-text">상세주소</span>
  					<input type="text" id="deliveryAddressDetails" class="form-control" value="${deliveryVO.deliveryAddressDetails }" readonly>
				</div>
				<div class="input-group mb-3">
  					<span class="input-group-text">연락처</span>
  					<input type="text" id="receiverPhone" class="form-control" value="${deliveryVO.receiverPhone }" readonly>
				</div>
				<div class="input-group mb-3">
  					<span class="input-group-text">배송 요청사항</span>
  					<input type="text" id="requirement" class="form-control" value="${deliveryVO.requirement }" readonly>
				</div>
			</div>
		</div>
		
		<div class="box_info" id="order_info">
			<div class="small_header">
				<h3>주문 정보</h3>
			</div>
			<div id="order_list">
				<c:forEach var="orderDTO" items="${orderList }">
					<c:set var="orderVO" value="${orderDTO.orderVO }"/>
					<c:set var="totalPrice" value="${totalPrice + orderVO.productPrice *  orderVO.purchaseNum }"/>
					<div style="display: flex;">
						<div>
							<img src="${orderDTO.imgUrl }" class="img-thumbnail">
						</div>
						<div>
							<h4 style="width: 200px;">${orderVO.productName }</h4><br>
							<span style="width: 200px;">${orderVO.purchaseNum }개</span>
							<span>${orderVO.productPrice }원</span>
						</div>
						<div>
							<div style="width: 200px;">${orderVO.productPrice *  orderVO.purchaseNum }원</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
		
		<div class="box_info" id="payment_info">
			<div class="small_header">
				<h3>결제 정보</h3>
			</div>
			<div>
				<div class="input-group mb-3">
					<span class="input-group-text">총 상품 가격</span>
  					<input type="text" class="form-control" id="total_price" value="${totalPrice }원" readonly>
				</div>
				<div class="input-group mb-3">
					<span class="input-group-text">멤버십 할인</span>
					<c:set var="membershipDiscount" value="0"/>
					<sec:authorize access="hasAnyRole('판매자', '관리자') || ( hasRole('멤버십') && ${not empty membershipVO })">
						<c:set var="membershipDiscount" value="20"/>
					</sec:authorize>
  					<input type="text" id="membership_discount" class="form-control" value="${membershipDiscount }%" readonly>
				</div>
				<div class="input-group mb-3">
					<span class="input-group-text">쿠폰 할인</span>
  					<input type="text" class="form-control" id="coupon_discount" value="${couponVO.discount }%" readonly>
  					<button id="btn_coupon" class="btn btn-outline-secondary" type="button" onclick="selectCoupon()">쿠폰 선택</button>
				</div>
				<div id="coupon_list">
				
				</div>
				<div class="input-group mb-3">
					<span class="input-group-text">배송비</span>
  					<input type="text" class="form-control" id="delivery_price" readonly>
				</div>
				<div class="input-group mb-3">
					<span class="input-group-text">총 결제 금액</span>
  					<input type="text" class="form-control" id="charge_price" readonly>
				</div>
			</div>
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
			$('#coupon_discount').val(paymentVO.couponDiscount + '%');
			$('#delivery_price').val(paymentVO.deliveryPrice);
			$('#charge_price').val(calcChargePrice());
			
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
					console.log('불러온 쿠폰들 : ' + result);
					if(result.length == 0){
						alert('사용할 수 있는 쿠폰이 없습니다.');
						return;
					}
					
					myCouponList = result;
					
					let form = '';
					for(x in myCouponList){
					form += '<div class="couponRow">' + 
						'<div><input type="hidden" class="couponIdx" value="' + x + '"></div>' +
						'<div style="width:200px;">' + myCouponList[x].couponName + '</div>' +
						'<div style="width:200px;">' + myCouponList[x].discount + '% 할인</div>' + 
						'<div style="width:200px;"><input type="radio" name="radioCoupon" onclick="applyCoupon(this)"></div>' +
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
			
			let membershipVO = {
					'membershipId' : '${membershipVO.membershipId}',
					'expiryDate' : '${membershipVO.expiryDate}'
			};
			
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
					'myCouponVO' : myCouponVO,
					'membershipVO' : membershipVO
				}),
				success : function(result){
					console.log('결제 내역 전송 결과 : ' + result);
					if(result == 100){
						location.href = 'paymentResult?paymentId=' + paymentVO.paymentId;
					}else if(result == 201 || result == 202){
						alert('결제 정보가 일치하지 않습니다.');
					}else if(result == 203){
						alert('만료된 멤버십입니다.');
					}else if(result == 205){
						alert('상품의 남은 수량이 부족합니다.');
					}else {
						alert('알 수 없는 문제 발생');
					}
				}
			}); // end ajax
			
		} // end sendPaymentResult
		
		function showDeliveryPopup(){
			
			const popupStat = {
					'url' : 'popupDeliverySelect',
					'name' : 'popupDeliverySelect',
					'option' : 'width=800, height=600, top=50, left=400'
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