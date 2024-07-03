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
	        success: function(response) {
	       	 console.log('회원 정보 조회(GET) 성공');
	       	 
	       	 let expiryDate = new Date(response);
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
		
	    
	    // 멤버십 신청
		$('#cancelMembershipBtn').on('click', function() {
			
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
	
		});// end cancelMembershipBtn.click 
		
});//end document.ready()
	
</script>

	
	
		<h2>VOP 멤버십 가입을 환영합니다!</h2>
	
 		<div id="expirydate"></div><br>
	
		<button id="cancelMembershipBtn" style="display: none;">멤버십 해지하기</button><br>
	 	
	 	<a href="../board/mypage">마이페이지로 이동</a>
	
	
	

</body>
</html>