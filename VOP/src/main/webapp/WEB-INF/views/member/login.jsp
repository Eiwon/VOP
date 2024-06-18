<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>로그인</title>
</head>
<body>
	<div>
		<form action="login" method="POST">
			<div>
				아이디 <input type="text" name="memberId" placeholder="아이디">
			</div>
			<div>
				비밀번호 <input type="password" name="memberPw" placeholder="비밀번호">
			</div>
			<div>
				자동 로그인 <input type="checkbox" name="rememberMe">
			</div>
			<input type="submit" value="로그인">
		</form>
		<div>
			<a href="findAccount">아이디 비밀번호 찾기</a>
		</div>
		<div>
			<a href="register">회원 가입</a>
		</div>
	</div>
	
	<script type="text/javascript">
		
		/* $('#btnLogin').click(function(){
			let memberId = $('#memberId').val();
			let memberPw = $('#memberPw').val();
			
			$.ajax({
				method : 'POST',
				url : 'login',
				headers : {
					'Content-type' : 'application/json'
				},
				data : JSON.stringify({
					memberId : memberId,
					memberPw : memberPw
				}),
				success : function(result){
					alert("로그인 성공");
					location.href = '../board/main';
				} // end success
				
			}); // end ajax
			
		}); */
		
	
	</script>
</body>
</html>