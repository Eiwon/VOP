<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>회원 정보 수정</title>
</head>
<body>
	<div>
		<div>
			<h2 id="subtitle">회원 정보 확인</h2>
		</div>
		<div id="form">
			<span>비밀번호 확인</span>
			<div>
				<span>아이디</span><br>
				<span><%= request.getSession().getAttribute("memberId") %></span>
			</div>
			<div>
				<span>비밀번호</span>
				<input type="password" id="member_pw">
				<div></div>
			</div>
			<div>
				<input type="button" value="확인" onclick="checkPw()">
				<input type="button" value="취소" onclick='location.href="../board/mypage"'>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		let memberIdVal = '<%= request.getSession().getAttribute("memberId") %>';
		let memberPw = $('#member_pw');
		function checkPw(){
			let memberPwVal = memberPw.val();
			
			if(memberPwVal.length == 0){
				memberPw.next().text("비밀번호를 입력해주세요.");
				return;
			}
			
			$.ajax({
				method : 'POST',
				url : 'checkMember',
				data : {
					'memberId' : memberIdVal,
					'memberPw' : memberPwVal
				},
				success : function(result){
					console.log(result);
					if(result == 0){
						memberPw.next().text("비밀번호가 일치하지 않습니다.");
					}else{
						printModify();	
					}
				}
			}); // end ajax
			
		} // end checkPw
		
		
		function printModify(){
			$('#subtitle').text("회원 정보 변경");
		} // end printModify
		
	</script>
</body>
</html>