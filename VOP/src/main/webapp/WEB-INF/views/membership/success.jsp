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
<!-- 부트스트랩 CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<meta name="${_csrf.token }" content="${_csrf.token }">

<style>

	 body {
	        font-family: Arial, sans-serif;
	        background-color: #f8f9fa;
	        color: #333;
	    }
	    
    .membership-container {
        max-width: 600px;
        margin: 0 auto;
        padding: 20px;
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    .membership-info {
        margin-bottom: 20px;
    }

	 .btn-custom {
        display: inline-block;
        padding: 10px 20px;
        font-size: 18px;
        font-weight: bold;
        text-decoration: none;
        text-align: center;
        border-radius: 5px;
        transition: background-color 0.3s ease, color 0.3s ease;
    }

    .btn-custom-primary {
        background-color: #6cb2eb;
        color: #fff;
        border: 1px solid #6cb2eb;
    }

    .btn-custom-primary:hover {
        background-color: #0056b3; /* 더 진한 연하늘색 */
        border-color: #0056b3;
        color: #fff; /* 텍스트 색상 화이트 */
    }

    .btn-custom-danger {
        background-color: #F28599;
        color: #fff;
        border: 1px solid #F28599;
    }

    .btn-custom-danger:hover {
        background-color: #c82333;
        border-color: #bd2130;
        color: #fff; /* 텍스트 색상 화이트 */
    }
    
</style>
</head>
<body>
	<jsp:include page="../include/sideBar.jsp"/>

	<div class="container membership-container text-center mt-5">
    <div class="membership-info">
        <h2 class="mb-4">VOP 멤버십 가입을 환영합니다!</h2>
        <div id="expirydate" class="mb-3"></div>
        <button id="cancelMembershipBtn" class="btn btn-custom btn-custom-danger" style="display: none;">멤버십 해지하기</button>
    </div>
    
	<a href="../board/mypage" class="btn btn-custom btn-custom-primary mb-3">마이페이지로 이동</a>
	
	
<script>
		
	let memberId = "${memberDetails.getUsername()}";
	console.log("memberId : ", memberId);
	
	$(document).ready(function() {
		
		//회원 정보 조회
		getMembershipInfo(memberId);
	  
		// 멤버십 해지 버튼 클릭 시 이벤트
	    $('#cancelMembershipBtn').on('click', async function() {
	        
	        if (confirm('멤버십을 정말 해지하시겠습니까? * 멤버십 해지 시 즉시 환불됩니다! *')) {
	            
	            try {
	                // 결제 취소를 진행
	                await cancelPayment(memberId);

	                // 결제 취소가 성공했을 경우, 멤버십 삭제와 권한 수정을 진행
	                await deleteMembership(memberId);
	                await updateMembershipAuth(memberId);

	               
	                window.location.href = "../membership/register"; // 멤버십 등록 페이지로 이동
	            } catch (error) {
	                console.error('오류 발생:', error);
	                alert('처리 중 오류가 발생했습니다.');
	            }
	        }//end if
	    }); // end cancelMembershipBtn.click
		
	});//end document.ready()		
	    
	
	
	function getMembershipInfo(memberId){
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
	           
	        },
	        error: function() {
	            $('#expirydate').text('멤버십 정보를 조회하는 데 오류가 발생했습니다.');
	        }
	    });//end ajax
	}//end getMembershipInfo()
	

			
	async function cancelPayment(memberId) {
			console.log("cancelPayment(memberId) : ", memberId);
			   $.ajax({
			      	    method: 'POST',
			      	    url: 'cancelPayment', // 실제 서버에서 결제 취소를 처리하는 URL
			      	    headers: {
			      	        'Content-Type': 'application/json',
			      	        'X-CSRF-TOKEN': $('meta[name="${_csrf.token }"]').attr('content')
			      	    },
			      	    data: JSON.stringify({
			      	    		'memberId' : memberId 	      	    	
			      	    }), 
			      	    success: function(response, textStatus, xhr) {
			      	        console.log('서버 응답 코드:', xhr.status);
			      	        if (xhr.status === 200) {
			      	            alert('결제가 성공적으로 취소되었습니다.');
			      	           
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
		      	
			
   		async function deleteMembership(memberId){
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
   		}//end deleteMembership()
			
   		
   		async function updateMembershipAuth(memberId){
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
	        			//window.location.href = "../membership/register";
	                },
	                error: function() {
	                    console.error('멤버십 권한 업데이트 실패', error);
	                }
	            }); //end ajax	
   		}//end updateMembershipAuth()
	
</script>
</div>
</body>
</html>