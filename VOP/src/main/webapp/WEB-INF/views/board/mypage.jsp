<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<body>
	<div class="info_container">
		<div class="side_bar">
			<div> 
				<strong>내 정보</strong>
				<div id="my_info">
					<a href="myInfo">내 정보 확인 / 수정</a>
					<a href="seller">판매자 페이지</a>
				</div>
			</div>
		</div>
		<div id="print_form">
			
			
		</div>

	</div>
	<script type="text/javascript">
		const memberId = '<%= request.getSession().getAttribute("memberId")%>';
		const memberAuth = '${memberAuth }';
		let paymentList;
		memberAuth = 'admin'; // 테스트용 임시 값
		let tagMyInfo = $('#my_info');
		let tagPrintForm = $('#print_form');
		
		$(document).ready(function(){
		
			if(memberAuth == 'admin'){
				loadAdminService();
			}
			
			
		}); // end document.ready
		
		
		function loadAdminService(){
			let form = '<a href="admin">관리자 페이지</a>';
			tagMyInfo.append(form);
			
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