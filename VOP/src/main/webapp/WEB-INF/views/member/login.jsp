<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style type="text/css">
body {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 800px;
    }
#loginForm{
	border: solid 0.5px black;
	width: 400px;
	height: 100px;
	display: flex;
}
.loginInput{
	padding: 20px;
}
.btnSubmit{
	width: 70px;
	height: 50px;
}
</style>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>로그인</title>
</head>
<body>
	<div>
		<form action="login" method="POST" id="loginForm">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			<div class="loginInput">
				<div>
 					아이디 <input type="text" name="memberId" placeholder="아이디">
				</div>
				<div>
					비밀번호 <input type="password" name="memberPw" placeholder="비밀번호">
				</div>
				<div>
					자동 로그인 <input type="checkbox" name="rememberMe">
				</div>
			</div>
			<div class="loginInput">
				<input class="btnLogin" type="submit" value="로그인"></button>
			</div>
		</form>
		<div class="linkMember">
			<a href="../board/main">VOP</a>
			<a href="register">회원 가입</a>
			<a href="findAccount">아이디 찾기</a>
			<a href="findPassword">비밀번호 찾기</a>
		</div>
		
	</div>
	
	<script type="text/javascript">
		
		/* $('.btnLogin').click(function(){
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
					console.log(result);
					
				},
				error : function(statusCode, textStatus){
					console.log(statusCode + " : " + textStatus);
				}
				
			}); // end ajax
			
		}); */
		
	
	</script>
</body>
</html>