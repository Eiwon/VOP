<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 탈퇴</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
</head>
<body>

	<h2>회원 탈퇴</h2>
	<h3>등록된 이메일로 인증 완료 후, 탈퇴가 완료됩니다</h3>
	<form action="withdrawal" method="POST" id="withdrawal">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
		<div>
			<div>
				등록된 이메일 : ${memberEmail }
			</div>
			<input type="button" id="btnAuthCode" value="인증 코드 받기">
		</div>
		<div>
			인증 코드
			<input type="text" id="auth_code" name="authCode">
			<div></div>
		</div>
		<div>
			<input type="submit" value="회원 탈퇴">
		</div>
	</form>
	<script type="text/javascript">
	
		const memberEmail = '${memberEmail }';
		let tagAuthCode = $('#auth_code');
		let isSend = false;
		
		$(document).ready(function(){
			
			$('#btnAuthCode').click(function(){
				$.ajax({
					method : 'GET',
					url : 'mailAuthentication?email=' + memberEmail,
					success : function(result){
						tagAuthCode.next().text('인증 번호가 발송되었습니다. 3분 이내에 입력해주세요.');
						$('#btnAuthCode').attr('disabled', null);
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