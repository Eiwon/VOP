<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<div id="findAccount">
			<c:choose>
				<c:when test="${memberId == null }">
					<h2>등록되지 않은 아이디입니다</h2>
				</c:when>
				<c:otherwise>
					<div>
						<h2>가입 아이디 ${memberId }</h2>
						<h2>등록된 이메일 ${memberEmail }</h2>
					</div>
					<form action="changePw" method="POST">
						<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
						<input type="hidden" name="memberId" value="${memberId }">
						<span>등록된 이메일로 인증번호가 발송되었습니다. 인증번호와 새 비밀번호를 입력해주세요.</span>
						<div>인증 번호 <input type="text" id="authCode" name="authCode"></div>
						<div>새 비밀번호 <input type="password" id="memberPw" name="memberPw"><div></div></div>
						<div>새 비밀번호 확인 <input type="password" id="checkPw"><div></div></div>
						<input type="submit" value="변경하기">
					</form>
				</c:otherwise>
			</c:choose>
	</div>
	<script type="text/javascript">
		let tagMemberPw = $('#memberPw');
		let tagCheckPw = $('#checkPw');
		
		let memberPwExp = {
			exp : new RegExp("^[a-zA-Z0-9]{8,20}$"),
			success : "올바른 입력 형식 입니다.",
			fail : "비밀번호는 8~20자의 알파벳, 숫자이여야 합니다."
			};
		
		let validCheckList = {
			memberPw : false,
			checkPw : false
		};
	
		$(document).ready(function(){
			tagMemberPw.blur(isValidPw);
			tagCheckPw.blur(comparePw);
		}); // end document.ready
		
		// 비밀번호 유효성 검사
		function isValidPw(){
			let memberPw = tagMemberPw.val();
			
			if(memberPw.length == 0){
				return;
			}
			
			validCheckList.memberPw = memberPwExp.exp.test(memberPw);
			console.log('비밀번호 유효성 검사 결과 : ' + validCheckList.memberPw);
			let msg = validCheckList.memberPw ? memberPwExp.success : memberPwExp.fail;
			tagMemberPw.next().text(msg);
			
			comparePw();
		} // end isValidPw
		
		// 비밀번호 확인란의 입력값이 일치하는지 확인
		function comparePw() { 
			memberPw = tagMemberPw.val();
			checkPw = tagCheckPw.val();

			if (memberPw == checkPw) {
				tagCheckPw.next().text("비밀번호가 일치합니다.");
				validCheckList.checkPw = true;
			} else {
				tagCheckPw.next().text("비밀번호 불일치");
				validCheckList.checkPw = false;
			}
		} // end comparePw
		
		
		$('form').submit(function(event){
			if($('#authCode').val().length == 0){
				alert('인증번호를 입력해주세요');
				event.preventDefault();
				return;
			}
			for(x in validCheckList){
				if(!validCheckList[x]){
					alert('비밀번호를 확인해주세요');
					event.preventDefault();
					return;
				}
			}
		}); // end form.submit
		
	</script>

</body>
</html>