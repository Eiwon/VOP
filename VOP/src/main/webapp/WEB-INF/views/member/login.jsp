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
			<input type="text" name="memberId" placeholder="아이디"><br>
			<input type="password" name="memberPw" placeholder="비밀번호"><br>
		</form>
		<div>
			<a href="">아이디 비밀번호 찾기</a>
		</div>
		<div>
			<input type="submit" value="로그인"><br>
			<input type="button" value="회원가입" onclick="location.href='register'">
		</div>
	</div>
</body>
</html>