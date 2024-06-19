<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>계정 정보 찾기</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<h2>아이디 찾기</h2>
	<form action="findAccount" method="POST">
		<strong>등록된 이메일로 찾기</strong><br>
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
		<div>
			등록된 이메일 
			<input type="text" id="member_email" name="memberEmail">
			<div></div>
			<input type="button" id="btnAuthCode" value="인증 코드 받기">
		</div>
		<div>
			인증 코드
			<input type="text" id="auth_code" name="authCode">
			<div></div>
		</div>
		<div>
			<input type="submit" value="아이디 찾기">
		</div>
	</form>
	<div>
		<a href="findPassword">비밀번호 찾기</a>
	</div>

	<script type="text/javascript">
	// 이름, 이메일 입력 후 본인인증 버튼 클릭시, 이름과 이메일 유효성 체크
	// 입력값이 유효하면 본인인증 메일 전송
	// 인증코드 입력 후 제출
		let tagMemberEmail = $('#member_email');
		let tagAuthCode = $('#auth_code');
		let verifiedEmail; // 이메일 인증 받은 후 입력된 이메일 변경 방지. 인증한 이메일을 저장하여 form 제출시 비교.
		
		$('#btnAuthCode').click(function(){
			console.log("본인 인증");
			$('#btnAuthCode').attr('disabled', 'disabled');
			let memberEmail = tagMemberEmail.val();
			
			if(memberEmail.length == 0){
				tagMemberEmail.next().text("이메일을 입력해주세요.");
				return;
			}else{
				tagMemberEmail.next().text(" ");
			}
			
			verifiedEmail = memberEmail;
			
			$.ajax({
				method : 'GET',
				url : 'mailAuthentication?email=' + memberEmail,
				success : function(result){
					tagAuthCode.next().text('인증 번호가 발송되었습니다.');
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