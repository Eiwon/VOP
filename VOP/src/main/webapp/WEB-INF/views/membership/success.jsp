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
<title>멤버십</title>
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<meta name="${_csrf.token }" content="${_csrf.token }">
</head>
<body>
	

<script>
		
$(document).ready(function() {
		let memberId = "${memberDetails.getUsername()}";
		console.log("memberId : ", memberId);
	
		//회원 정보 조회
	    $.ajax({
	        type: 'GET',
	        url: 'getMembership/' + memberId,
	        success: function(membershipVO) {
	       	 console.log('회원 정보 조회(GET) 성공');
	       	 
	       	 let expiryDate = new Date(membershipVO.expiryDate);
	       	 console.log('expiryDate : ', expiryDate);
	       	 
	       	 $('#expirydate').text('멤버십 가입을 환영합니다!');
             $('#cancelMembershipBtn').show(); // 해지하기 버튼을 보이도록 함
             
	       	// 받아온 만료 날짜를 표시
	         $('#expirydate').text('다음 결제 일은 ' + expiryDate.toLocaleDateString() + '입니다.');
             
	        // 저장된 결제 정보 사용
	           $('#cancelMembershipBtn').data('paymentId', membershipVO.chargeId); // 버튼 요소에 데이터 속성 추가
	           $('#cancelMembershipBtn').data('membershipFee', membershipVO.membershipFee);
	           
	        },
	        error: function() {
	            $('#expirydate').text('멤버십 정보를 조회하는 데 오류가 발생했습니다.');
	        }
	    });//end ajax
		
	    
	   
		$('#cancelMembershipBtn').on('click', function() {
			
			if(confirm('멤버십을 정말 해지하시겠습니까? * 멤버십 해지 시 즉시 환불됩니다! *')){
				//아까 버튼으로 보낸 데이터 꺼낼거임
				let paymentId = $(this).data('paymentId'); // 저장된 결제 고유 번호 사용
			    let membershipFee = $(this).data('membershipFee'); // 저장된 결제 금액 사용

			    //결제 취소를 진행하는 함수
			    cancelPayment(paymentId, membershipFee);
		
			}
		});// end cancelMembershipBtn.click 
			
		 function cancelPayment(paymentId, membershipFee) {
		      	let cancelData = {
		        	cid: 'kakaopay.TC0ONETIME', // 가맹점 코드
		            tid: paymentId, // 결제 고유 번호
		            cancel_amount: membershipFee, // 취소 금액
		            cancel_tax_free_amount: 0 // 취소 비과세 금액
		           };
		      	
		      	$.ajax({
		      	    method: 'POST',
		      	    url: 'cancelPayment', // 실제 서버에서 결제 취소를 처리하는 URL
		      	    headers: {
		      	        'Content-Type': 'application/json',
		      	        'X-CSRF-TOKEN': $('meta[name="${_csrf.parameterName}"]').attr('content')
		      	    },
		      	    data: JSON.stringify(cancelData), // 취소 데이터를 JSON 형식으로 변환하여 전송
		      	    success: function(response) {
		      	        if (response.success) {
		      	            alert('결제가 성공적으로 취소되었습니다.');
		      	            window.location.href = "../membership/cancelSuccess"; // 취소 성공 페이지로 이동
		      	        } else {
		      	            alert('결제 취소에 실패했습니다.');
		      	        }
		      	    },
		      	    error: function(xhr, status, error) {
		      	        console.error('서버 요청 중 오류 발생:', error);
		      	        alert('결제 취소 요청 중 오류가 발생했습니다.');
		      	    }
		      	    
		      	 });//end ajax POST
		   }//end function cancelPayment()
		      	
			
				// 멤버십 삭제(해지)
				$.ajax({
					type : 'DELETE',
					url : 'deleteMembership/' + memberId,
					headers : {
						'X-CSRF-TOKEN' : '${_csrf.token }' 
					},
					success: function(response) {
						console.log('멤버십 정보가 삭제되었습니다.');
					},
					error: function(){
						console.error('멤버십 삭제 도중 오류 발생 . ',error);
					}
			}); //end ajax
			
			
			// 멤버십 권한(일반) 수정
			$.ajax({
                type: 'PUT',
                url: 'updateAuthOnUser/' + memberId,
                headers : {
					'X-CSRF-TOKEN' : '${_csrf.token }' 
				},
                success: function() {
                    console.log('멤버십 권한 업데이트 성공');
                 // 일반 유저로 권한까지 수정 하면 다시 등록 페이지로 이동한다. 
        			window.location.href = "../membership/register";
                },
                error: function() {
                    console.error('멤버십 권한 업데이트 실패', error);
                }
            }); //end ajax
            
			}//end if
	
		
		
});//end document.ready()
	
</script>

	
		<h2>VOP 멤버십 가입을 환영합니다!</h2>
	
 		<div id="expirydate"></div><br>
	
		<button id="cancelMembershipBtn" style="display: none;">멤버십 해지하기</button><br>
	 	
	 	<a href="../board/mypage">마이페이지로 이동</a>
	

</body>
</html>