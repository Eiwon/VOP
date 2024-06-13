<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<!-- jquery 라이브러리 import -->
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
<title>멤버십 등록</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>

<script type="text/javascript">
	 let paymentWrapper = JSON.parse('${paymentWrapper}');	
	 let memberVO = paymentWrapper.memberVO;
	 
	 
	 $(document).ready(function() {
		console.log(paymentWrapper);
		let memberId = "${memberDetails.getUsername()}";
		console.log("memberId:", memberId);
         
         // 멤버십 신청
         $('#registerBtn').on('click', function() {
        	 payment();
         });// end registerBtn.click
         
     });// end document.ready
	
	
     function calcTotalPrice() {
    	 return Math.ceil(1000); // 1000원을 반환, Math.ceil()은 소수점 올림
		} // end calcTotalPrice
     
	function payment(){ // 결제 실행
			let IMP = window.IMP;
			IMP.init('imp04667313'); // 가맹점 식별코드 설정(포트원 홈페이지에서 확인)
			let membershipId;
			
			let membershipFee = calcTotalPrice(); // 최종 결제 금액 계산
			
			// 서버에서 결제 고유 번호 받아오기
			$.ajax({
				method : 'GET',
				url : 'getId',
				success : function(result){
					if(result == 0){
						console.log('고유 번호 받아오기 실패');
					}else{
						console.log('결제 고유 번호 : ' + result);
						membershipId = result;
						IMP.request_pay({
							pg: 'kakaopay.TC0ONETIME', // PG사 코드(포트원 홈페이지에서 찾아서 넣어야함)
			                pay_method: 'card', // 결제 방식
			                merchant_uid: membershipId, // 결제 고유 번호
			                amount: membershipFee, // 결제 가격
			                buyer_name: memberVO.memberId,
			                buyer_email: memberVO.memberEmail,
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
		console.log('결제 완료');
		
		console.log('membershipPOST : ' + memberId);
        const formData = memberId; // memberId를 직접 전송
       
		
		$.ajax({
			method : 'POST',
			url :  'membershipRegister',
			contentType: 'text/plain',
			data: formData, // memberId를 직접 전송
			success : function(result){
				console.log('결제 내역 전송 결과 : ' + result);
				if(result > 0){
					location.href = 'paymentResult?paymentId=' + result;
				}else if(result == 0){
					alert('결제 내역 전송 실패');		
				}else {
					alert(orderList[(result +1)* -1].productName + ' 상품의 재고가 부족합니다.');
				}
			}
		}); // end ajax
		
	} // end sendPaymentResult
		
</script>



<h1>VOP 멤버십</h1>
    <p>월 1000원으로 멤버십 혜택을 누려보세요!</p>
    <button id="registerBtn">멤버십 신청하기</button>

    <h2>멤버십 혜택</h2>
    <p>무제한 할인 쿠폰 제공</p>
    
   
</body>
</html>