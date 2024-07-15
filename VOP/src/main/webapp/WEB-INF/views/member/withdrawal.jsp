<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 탈퇴</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
.body_container {
	width: 65%;
	margin: auto;
}
.inner_header {
	margin: 20px;
}

</style>
<jsp:include page="../include/header.jsp"></jsp:include>
</head>
<body>

	<div class="spinner-border text-primary start-50 top-50" style="position: fixed; z-index: 1030;" role="status" hidden="hidden">
  		<span class="visually-hidden">Loading...</span>
	</div>
	<div class="body_container">
		<div class="inner_header">
			<h2>회원 탈퇴</h2>
		</div>
		<div class="inner_header">
			<h4>등록된 이메일로 인증 완료 후, 탈퇴가 완료됩니다</h4>
		</div>
		<form action="withdrawal" method="POST" id="withdrawal">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			<div class="input-group mb-3">
				<span class="input-group-text">등록된 이메일</span>
				<span class="form-control">${memberEmail }</span>
				<input type="button" id="btnAuthCode" class="btn btn-outline-secondary" value="인증 코드 받기">
			</div>
			<div class="input-group mb-3">
				<span class="input-group-text">인증 코드</span>
				<input type="text" class="form-control" id="auth_code" name="authCode">
				<div></div>
			</div>
			<div>
			<input type="submit" class="btn btn-danger" value="회원 탈퇴">
		</div>
	</form>
	</div>
	
	<script type="text/javascript">
	
		const memberEmail = '${memberEmail }';
		let tagAuthCode = $('#auth_code');
		let isSend = false;
		let btnAuthCode = $('#btnAuthCode');
		let spinner = $('.spinner-border');
		
		$(document).ready(function(){
			
			btnAuthCode.click(function(){
				btnAuthCode.attr('disabled', 'disabled');
				spinner.attr('hidden', null);
				
				$.ajax({
					method : 'GET',
					url : 'mailAuthentication?email=' + memberEmail,
					success : function(result){
						tagAuthCode.next().text('인증 번호가 발송되었습니다. 3분 이내에 입력해주세요.');
						btnAuthCode.attr('disabled', null);
						spinner.attr('hidden', 'hidden');
						isSend = true;
					} // end success
				});
			}); // end btnAuthCode.click
		});	// end document.ready
	
		$('#withdrawal').submit(function(event){
			let authCode = tagAuthCode.val();
			
			if(!isSend){
				alert('인증번호를 먼저 발송해주세요.');
				event.preventDefault();
				return;
			}
			
			if(authCode.length == 0){
				alert('인증번호를 입력해주세요.');
				event.preventDefault();
				return;
			}
			
			let isReal = confirm('정말 탈퇴하시겠습니까?');
			
			if(!isReal){
				event.preventDefault();
				return;
			}
			
		}); // end form.submit
	</script>
</body>
</html>