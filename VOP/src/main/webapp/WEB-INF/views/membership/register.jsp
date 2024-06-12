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
             const formData = memberId; // memberId를 직접 전송

             $.ajax({
                 type: 'POST',
                 url: 'membershipRegister',
                 contentType: 'text/plain',
                 data: formData, // memberId를 직접 전송
                 success: function(response) {
                	 console.log('멤버십 등록 성공: ' + response);
                     alert('멤버십 등록이 완료되었습니다.');
                     
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
                     window.location.href = "../membership/success";
                 },
                 error: function() {
                     console.log('멤버십 권한 업데이트 실패');
                 }
             });

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
    
   
</body>
</html>