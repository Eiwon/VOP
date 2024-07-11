<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style type="text/css">
.form_container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 600px;
    }
.loginInput{
	padding: 20px;
	padding-top: 10px;
}
.btnSubmit{
	width: 270px;
	height: 40px;
}
</style>
<title>로그인</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	
	<div class="form_container">
	<div>
		<a href="../board/main" class="vop-link">
   	 	<img src="${pageContext.request.contextPath}/resources/vop.png">
		</a>
	</div>
	<div>
		<form action="login" method="POST" id="loginForm">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			<div class="loginInput" >
				<div class="form-floating mb-3">
 					<input type="text" name="memberId" id="memberId" class="form-control" placeholder="아이디">
 					<label>아이디</label>
				</div>
				<div class="form-floating">
					<input type="password" name="memberPw" id="memberPw" class="form-control" placeholder="비밀번호">
					<label>비밀번호</label>
				</div>
				<div class="form-check">
					<input type="checkbox" class="form-check-input" name="rememberMe">
					<label class="form-check-label">
   		 				자동 로그인 
  					</label>
				</div>
			</div>
			<div class="loginInput">
				<input type="submit" value="로그인" class="btnSubmit btn btn-primary">
			</div>
		</form>
		
		<div class="linkMember">
			<a href="register">회원 가입</a>
			<a href="findAccount">아이디 찾기</a>
			<a href="findPassword">비밀번호 찾기</a>
		</div>
		
	</div>
	</div>
</body>
</html>