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
	<h2>비밀번호 찾기</h2>
	<form action="findPassword" method="POST">
		<div>
			아이디
			<input type="text" id="memberId" name="memberId">
			<div></div>
		</div>
		<input type="submit" value="비밀번호 찾기">
	</form>

	<script type="text/javascript">
		let tagMemberId = $('#memberId');
		
		$('form').submit(function(event){
			let memberId = tagMemberId.val();
			
			if(memberId.length == 0){
				tagMemberId.next().text("아이디를 입력해주세요.");
				event.preventDefault();
				return;
			}else{
				tagMemberId.next().text("");
			}
		});
	
		
		
	</script>

</body>
</html>