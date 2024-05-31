<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
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
</body>
</html>