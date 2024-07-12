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
.inner_header {
	margin-bottom: 40px;
}
.form_id {
	height: 100px;
}
</style>
<title>계정 정보 찾기</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<div class="body_container">
		<div class="inner_header">
			<h2>비밀번호 찾기</h2>
		</div>
		<form action="findPassword" method="POST">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			<div class="mb-3 form_id">
				<label  class="form-label">아이디</label>
				<input type="text" class="form-control" id="memberId" name="memberId">
				<div></div>
			</div>
			<input type="submit" class="btn btn-primary" value="비밀번호 찾기">
		</form>
	</div>
	<script type="text/javascript">
		let tagMemberId = $('#memberId');
		
		tagMemberId.blur(function(){
			let memberId = tagMemberId.val();
			
			if(memberId.length == 0){
				tagMemberId.next().text("아이디를 입력해주세요.");
				tagMemberId.addClass('is-invalid');
			}else {
				tagMemberId.next().text("");
				tagMemberId.removeClass('is-invalid');
			}
		});
		
		$('form').submit(function(event){
			let memberId = tagMemberId.val();
			
			if(memberId.length == 0){
				alert("아이디를 입력해주세요.");
				event.preventDefault();
				return;
			}
		});
	
		
		
	</script>

</body>
</html>