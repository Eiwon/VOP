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
		console.log("member_Id:", memberId);
         
         // 회원 정보 조회
         if (memberId) {
             $.ajax({
                 type: 'GET',
                 url: '/membership/' + memberId,
                 success: function(response) {
                     $('#remainingDays').text('남은 멤버십 무료기간: ' + response.remainingDays + '일');
                 },
                 error: function() {
                     $('#remainingDays').text('멤버십 정보를 조회하는 데 오류가 발생했습니다.');
                 }
             });
         }

         // 멤버십 신청
         $('#registerBtn').on('click', function() {
             const formData = {
                 memberId: memberId, // 이미 존재하는 memberId 사용
                 membershipDuration: 30 // 예를 들어 30일로 설정
             };

             $.ajax({
                 type: 'POST',
                 url: '/register',
                 contentType: 'application/json',
                 data: JSON.stringify(formData),
                 success: function(response) {
                     alert(response);
                     location.reload(); // 페이지 새로고침
                 },
                 error: function() {
                     alert('멤버십 신청에 오류가 발생했습니다.');
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
    
    <div id="remainingDays"></div>

</body>
</html>