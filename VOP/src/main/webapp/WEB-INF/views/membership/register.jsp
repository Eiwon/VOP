<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
	
	<!-- EL을 사용하여 권한 정보를 확인 -->
    <c:set var="hasMembership" value="false"/>
    <c:forEach var="authority" items="${memberDetails.authorities}">
        <c:if test="${authority.authority eq 'ROLE_멤버십'}">
            <c:set var="hasMembership" value="true"/>
        </c:if>
    </c:forEach>
   
</sec:authorize> 



<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 웹 페이지에서 로컬 서버의 이미지에 접근 가능 -->

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
	
	 #membershipImage {
         width: 100%;
         max-width: 1200px;
         margin: 0 auto;
         display: block;
     }
     .benefits {
         margin-top: 20px;
     }
     .disabled-btn {
         cursor: not-allowed;
     }
     
      #registerBtn {
        width: 500px; /* 원하는 가로 크기 */
        height: 90px; /* 원하는 세로 크기 */
        background-color: #F25E7A; /* 배경색 */
        color: white; /* 글자색 */
        font-size: 30px; /* 글자 크기 */
        font-weight: bold; /* 볼드체 */
        border: none; /* 테두리 없애기 */
        cursor: pointer; /* 커서를 포인터로 변경하여 클릭 가능한 상태로 만듦 */
        display: block; /* 블록 레벨 요소로 설정하여 가로폭에 맞추어 표시 */
        margin: 0 auto; /* 가운데 정렬 */
        line-height: 70px; /* 버튼 높이와 같은 줄 높이로 설정하여 텍스트를 가운데 정렬 */
        text-align: center; /* 텍스트를 가운데 정렬 */
        text-decoration: none; /* 기본 텍스트 밑줄 제거 */
        border-radius: 5px; /* 모서리 둥글게 */
        transition: opacity 0.3s ease; /* 마우스 호버 시 투명도 전환 효과 추가 */
    }
	
	#registerBtn:hover {
        opacity: 0.8; /* 마우스를 올렸을 때 투명도 조정 */
    }
	
	.custom-btn {
        display: inline-block;
        width: 300px;
        height: 70px;
        line-height: 70px;
        background-color: #F2EB88;
        color: black;
        font-size: 25px;
        font-weight: bold;
        text-align: center;
        text-decoration: none;
        border-radius: 5px;
        border: none;
        cursor: pointer;
    }

    .custom-btn:hover {
        opacity: 0.8; /* 마우스를 올렸을 때 투명도 조정 */
    }
</style>
<title>멤버십 등록</title>
<meta name="${_csrf.token }" content="${_csrf.token }">
</head>

<body>
<jsp:include page="../include/sideBar.jsp"/>


<script type="text/javascript">
	 let paymentWrapper = JSON.parse('${paymentWrapper}');	
	 let memberVO = paymentWrapper.memberVO;
	 let membershipVO = paymentWrapper.membershipVO;
	
	 console.log(paymentWrapper);
	 console.log(memberVO);
	 console.log("멤버 권한 = ", memberVO.memberAuth);
	 
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
    	 let total = Math.ceil(1000);
    	 console.log('total : ',total);
    	 return total; // 1000원을 반환, Math.ceil()은 소수점 올림
		} // end calcTotalPrice
     
	function payment(){ // 결제 실행
			let IMP = window.IMP;
			IMP.init('imp04667313'); // 가맹점 식별코드 설정(포트원 홈페이지에서 확인)
			let paymentId;
			
			let membershipVO = {
				    membershipId: null,
				    memberId: null,
				    chargeId: null,
				    membershipFee : null
				    // 필요한 다른 필드들도 추가할 수 있음
				};
			
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
						paymentId = result;
						console.log('paymentId : ', paymentId);
						IMP.request_pay({
							pg: 'kakaopay.TC0ONETIME', // PG사 코드(포트원 홈페이지에서 찾아서 넣어야함)
			                pay_method: 'card', // 결제 방식
			                merchant_uid: paymentId, // 결제 고유 번호
			                name: 'VOP_ Membership Fee', // 제품명 (멤버십 결제를 위한 기본 값)
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
			            		// membershipVO 객체의 필드들 설정
			            		membershipVO.membershipId = rsp.merchant_uid;
			            		membershipVO.memberId = rsp.buyer_name;
			                    membershipVO.chargeId = rsp.imp_uid;
			            		membershipVO.membershipFee = rsp.paid_amount;
			                    
			            		// membershipVO 객체의 내용 확인
			                	console.log('membershipVO : ', membershipVO);
			            		
			            		sendPaymentResult(rsp);
			                } else {
		                        console.log('결제 실패:', rsp);
		                    }
			            }); // end IMP.request_pay 
					}
				}, // end success 
				error: function(xhr, status, error) {
		            console.error('서버 요청 중 오류 발생:', error);
		        }
			}); // end ajax	
			
	} // end payment
	
	
	
	
	function sendPaymentResult(membershipResult){
		let memberId = "${memberDetails.getUsername()}";
		console.log("memberId:", memberId);
		
		let orderList = []; // 예시: 빈 배열로 초기화
		let myCouponVO = null; // 예시: null로 초기화
		
		let membershipVO = {
		        membershipId: null,
		        memberId: null,
		        chargeId: null,
		        membershipFee : null
		        // 필요한 다른 필드들도 추가할 수 있음
		    };
		
		console.log('결제 완료');
		
		membershipVO.membershipId = membershipResult.merchant_uid;
		membershipVO.memberId = membershipResult.buyer_name;
		membershipVO.chargeId = membershipResult.imp_uid;
		membershipVO.membershipFee = membershipResult.paid_amount;
		
		console.log('sendPaymentResult - MembershipVO : ', membershipVO);
		
		
		$.ajax({
			method : 'POST',
			url :  'membershipRegister',
			headers : {
				'Content-type' : 'application/json',
				'X-CSRF-TOKEN' : '${_csrf.token }' 
			},
			data : JSON.stringify({
				'membershipVO': membershipVO,
				'orderList': orderList, // 필요 없다면 제외하거나 빈 배열로 전달
				'myCouponVO': myCouponVO // 필요 없다면 제외하거나 null로 전달
			}),
			success : function(result){
				console.log('결제 내역 전송 결과 : ' + result);
				if(result == 1){
					window.location.href = "../membership/success";
					console.log("결제 내역 전송 성공");
				}else {
					alert('결제 내역 전송 실패');		
				}
			}
		}); // end ajax
		
	    
        // 회원 권한 업데이트
           $.ajax({
               type: 'PUT',
               url: 'updateAuth/' + memberId,
               headers : {
   				'X-CSRF-TOKEN' : '${_csrf.token }' 
   				},
               success: function() {
                   console.log('멤버십 권한 업데이트 성공');
               },
               error: function() {
                   console.log('멤버십 권한 업데이트 실패');
               }
           });
	} // end sendPaymentResult
	
		
</script>


<div class="container text-center mt-5">


	<!-- 혜택 이미지 -->
    <img id="membershipImage" src="${pageContext.request.contextPath }/resources/path_to_your_image.png" alt="멤버십 혜택 이미지"><br><br>
    
    
    <button id="registerBtn">멤버십 신청하기</button><br><br>
    
    <!-- 멤버십 권한이 있는 유저는 버튼 비활성화 -->
    <sec:authorize access="hasAnyRole('멤버십', '판매자', '관리자')">
		<script type="text/javascript">
			$('#registerBtn').attr('disabled', 'disabled');
		</script>
	</sec:authorize>

    
   
    <div class="mt-3"> <!-- 위의 버튼과 간격을 주기 위해 mt-3 클래스 추가 -->
        <div class="d-flex justify-content-center">
			 <a id="membershipLink" href="success" class="custom-btn" style="display:none;">멤버십 페이지 이동</a>   
		</div>
	</div>		

</div>

  <!-- 멤버십 권한이 있는 유저 멤버십 이동하기 링크 보여주기 -->
    <sec:authorize access="hasAnyRole('멤버십')">
		<script type="text/javascript">
		document.getElementById("membershipLink").style.display = "block";
		</script>
	</sec:authorize>



   <!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> 

</body>
</html>