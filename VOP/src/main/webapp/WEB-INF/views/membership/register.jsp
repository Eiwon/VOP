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
<title>멤버십 등록</title>
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

<script>


	 $(document).ready(function() {
		let memberId = "${memberDetails.getUsername()}";
		console.log("memberId:", memberId);
         

         // 멤버십 신청
         $('#registerBtn').on('click', function() {
        	 console.log('membershipPOST : ' + memberId);
             const formData = {
                 memberId: memberId, // 이미 존재하는 memberId 사용
             };

             $.ajax({
                 type: 'POST',
                 url: 'membershipRegister',
                 contentType: 'application/json',
                 data: JSON.stringify(formData),
                 success: function(response) {
                	 console.log('멤버십 등록 성공: ' + response);
                     alert('멤버십 등록이 완료되었습니다.');
                     // 등록 완료 메시지 표시
                     $('#expirydate').text('멤버십 가입을 환영합니다!');
                     $('#cancelMembershipBtn').show(); // 해지하기 버튼을 보이도록 함
                 },
                 error: function(xhr, status, error) {
                     console.error('멤버십 등록 실패: ' + error);
                     alert('멤버십 등록에 오류가 발생했습니다.');
                 }
             });
             
             
          // 회원 권한 업데이트
             $.ajax({
                 type: 'PUT',
                 url: 'updateAuth/' + memberId,
                 success: function() {
                     console.log('멤버십 권한 업데이트 성공');
                 },
                 error: function() {
                     console.log('멤버십 권한 업데이트 실패');
                 }
             });
          
          // 회원 정보 조회
             if (memberId) {
                 $.ajax({
                     type: 'GET',
                     url: 'getMembership/' + memberId,
                     success: function(response) {
                    	 console.log('회원 정보 조회(GET) 성공');
                    	 
                    	 let expiryDate = new Date(response);
                    	 
                    	// 받아온 만료 날짜를 표시
                        $('#expirydate').text('다음 결제 일은 ' + expiryDate.toLocaleDateString() + '입니다.');
                     },
                     error: function() {
                         $('#expirydate').text('멤버십 정보를 조회하는 데 오류가 발생했습니다.');
                     }
                 });
             }

         });
         
         
         
     });
	
	
</script>
</head>
<body>

<h1>VOP 멤버십</h1>
    <p>월 1000원으로 멤버십 혜택을 누려보세요!</p>
    <button id="registerBtn">멤버십 신청하기</button>

    <h2>멤버십 혜택</h2>
    <p>무제한 할인 쿠폰 제공</p>
    
    <div id="expirydate"></div>

	<button id="cancelMembershipBtn" style="display: none;">멤버십 해지하기</button>
</body>
</html>