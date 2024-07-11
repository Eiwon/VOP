<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
.body_container{
	width: 65%;
	margin: auto;
}
.email_container {
	height: 120px;
}
.email_alert{
	font: red;
}
.code_alert{
	color: red;
}
</style>
<title>계정 정보 찾기</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<div class="body_container">
	<h2>아이디 찾기</h2>
	<form action="findAccount" method="POST">
		<strong>등록된 이메일로 찾기</strong><br>
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
		<div class="email_container">
			<div class="form-floating input-group mb-3">
				<input type="text" id="member_email" class="form-control" name="memberEmail" placeholder="이메일">
				<label for="member_email">이메일</label>
				<button type="button" id="btnAuthCode" class="btn btn-outline-secondary">인증 코드 받기</button>
			</div>
			<div class="email_alert"></div>
		</div>
		<div class="code_alert"></div>
		<div class="input-group mb-3">
			<span class="input-group-text">인증 코드</span>
			<input type="text" class="form-control" id="auth_code" name="authCode">
		</div>
		<div>
			<input type="submit" value="아이디 찾기" class="btn btn-primary">
		</div>
	</form>
	<div>
		<a href="findPassword">비밀번호 찾기</a>
	</div>
	</div>
	<script type="text/javascript">
	// 이름, 이메일 입력 후 본인인증 버튼 클릭시, 이름과 이메일 유효성 체크
	// 입력값이 유효하면 본인인증 메일 전송
	// 인증코드 입력 후 제출
		let tagMemberEmail = $('#member_email');
		let tagEmailAlert = $('.email_alert');
		let tagAuthCode = $('#auth_code');
		let tagCodeAlert = $('.code_alert');
		let verifiedEmail; // 이메일 인증 받은 후 입력된 이메일 변경 방지. 인증한 이메일을 저장하여 form 제출시 비교.
		let emailExp = new RegExp("^[a-zA-Z][a-zA-Z0-9]{5,19}@[a-zA-Z\.]{6,20}$");
		
		tagMemberEmail.focus(function(){
			tagMemberEmail.removeClass('is-invalid');
			tagEmailAlert.text("");
		});
		
		$('#btnAuthCode').click(function(){
			console.log("본인 인증");
			let memberEmail = tagMemberEmail.val();
			
			if(memberEmail.length == 0){
				tagMemberEmail.addClass('is-invalid');
				tagEmailAlert.text("이메일을 입력해주세요.");
				return;
			}else if(!emailExp.test(memberEmail)) {
				tagMemberEmail.addClass('is-invalid');
				tagEmailAlert.text("잘못된 입력 형식입니다.");
				return;
			}
			
			verifiedEmail = memberEmail;
			$('#btnAuthCode').attr('disabled', 'disabled');
			
			$.ajax({
				method : 'GET',
				url : 'mailAuthentication?email=' + memberEmail,
				success : function(result){
					tagCodeAlert.text('인증 번호가 발송되었습니다. 3분 이내에 입력해주세요.');
					$('#btnAuthCode').attr('disabled', null);
					tagAuthCode.val('');
				}
			});
		}); 
	
		$('form').submit(function(event){
			let memberEmail = tagMemberEmail.val();
			let authCode = tagAuthCode.val();
			
			if(memberEmail != verifiedEmail){
				alert('이메일 인증부터 해주세요!');
				event.preventDefault();
				return;
			}
		});
		
	</script>


</body>
</html>