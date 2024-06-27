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
				<input type="submit" value="로그인" class="btnSubmit">
			</div>
		</form>
		<div class="linkMember">
			<a href="../board/main">VOP</a>
			<a href="register">회원 가입</a>
			<a href="findAccount">아이디 찾기</a>
			<a href="findPassword">비밀번호 찾기</a>
		</div>
		
	</div>
</body>
</html>