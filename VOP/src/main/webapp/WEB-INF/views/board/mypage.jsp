<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
.container {
	flex-direction: row;
	display: flex;
}
	
</style>
<title>마이페이지</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<div class="info_container">
		<div class="side_bar">
			<div> 
				<strong>내 정보</strong>
				<div id="my_info">
					<a href="myInfo">내 정보 확인 / 수정</a>
					<a href="..order/orderlist">주문 목록</a>
					<a href="delivery">배송지 관리</a>
					<a href="seller">판매자 페이지</a>
				</div>
			</div>
		</div>
		<div id="print_form">
			
			
		</div>

	</div>
	<script type="text/javascript">
		const memberId = '${memberDetails.getUsername() }';
		let memberAuth = '${memberDetails.getAuth() }';
		let paymentList;
		let tagPrintForm = $('#print_form');
		
		$(document).ready(function(){
			console.log('현재 id : ' + memberId);
			console.log('현재 권한 : ' + memberAuth);
			if(memberAuth == '관리자'){
				loadAdminService();
			}
			
			
		}); // end document.ready
		
		
		function loadAdminService(){
			let form = '<a href="admin">관리자 페이지</a>';
			$('#my_info').append(form);
			
		} // end loadAdminService
		
		function loadPaymentForm(){
			let form = '<h2>주문 목록</h2><div>';
			
			$.ajax({ // 주문 목록 조회
				method : 'POST',
				url : '../payment/list',
				data : {
					'memberId' : memberId
				},
				success : function(result){
					console.log('주문 목록 조회 결과 : ' + result);
					paymentList = result;
					
					
				} // end success
			}); // end ajax
			
			form += '</div>';
			tagPrintForm.html(form);
		} // end loadPaymentForm
		
		
	</script>
</body>
</html>