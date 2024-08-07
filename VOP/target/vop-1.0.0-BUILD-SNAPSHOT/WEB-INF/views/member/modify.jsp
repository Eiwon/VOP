<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="memberDetails" property="principal"/>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<style type="text/css">
.body_container{
	width: 50%;
	margin: auto;
}
.inner_header {
	margin: 40px;
	text-align: center;
}
.pw_alert {
	color: red;
}
</style>
<title>회원 정보 수정</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<jsp:include page="../include/sideBar.jsp"/>
	
	<div class="body_container">
		<div class="inner_header">
			<h2 id="subtitle">회원 정보 확인</h2>
		</div>
		<div class="inner_header">
			<span id="notice">비밀번호를 다시 입력해주세요</span>
		</div>	
		<div id="form" >
			<div class="input-group mb-3">
				<span class="input-group-text">아이디</span>
				<input type="text" class="form-control" value="${memberVO.memberId }" readonly>
			</div>
			<div class="input-group mb-3">
				<span class="input-group-text">비밀번호</span>
				<input type="password" class="form-control" id="member_pw">
			</div>
			<div class="pw_alert"></div>
			<div>
				<input type="button" class="btn btn-primary" value="확인" onclick="checkPw()">
				<input type="button" class="btn btn-primary" value="취소" onclick='location.href="../board/mypage"'>
			</div>
		</div>
		<div class="link_withdrawal"></div>
		
	</div>
	<script type="text/javascript">
		let memberPw = $('#member_pw');
		let memberOrigin = {
				'memberId' : '${memberVO.memberId }',
				'memberPw' : '',
				'memberName' : '${memberVO.memberName}',
				'memberPhone' : '${memberVO.memberPhone}',
				'memberEmail' : '${memberVO.memberEmail}'
		};
		let validList = {
				'memberPw' : true,
				'memberPwChk' : true,
				'memberName' : true,
				'memberPhone' : true,
				'memberEmail' : true
		};
		let expMap = {
				'memberPw' : {
					exp : new RegExp("^[a-zA-Z0-9]{8,20}$"),
					success : "올바른 입력 형식 입니다.",
					fail : "비밀번호는 8~20자의 알파벳, 숫자, 한글이여야 합니다."
				},
				'memberName' : {
					exp : new RegExp("^[a-zA-Z가-힣]{2,20}$"),
					success : "올바른 입력 형식입니다.",
					fail : "이름은 2~20자의 한글 또는 알파벳이여야 합니다."
				},
				'memberPhone' : {
					exp : new RegExp("^[0-9]{10,15}$"),
					success : "올바른 입력 형식입니다.",
					fail : "숫자만 입력해주세요."
				},
				'memberEmail' : {
					exp : new RegExp("^[a-zA-Z][a-zA-Z0-9]{5,19}@[a-zA-Z\.]{6,20}$"),
					success : "올바른 입력 형식입니다.",
					fail : "이메일 형식에 맞게 입력해주세요."
				}
		};
		console.log(memberOrigin.memberId);
		
		
		// 비밀번호 인증 성공시, 정보 수정 폼 출력
		function checkPw(){
			let memberPwVal = memberPw.val();
			
			if(memberPwVal.length == 0){
				$('.pw_alert').text("비밀번호를 입력해주세요.");
				return;
			}
			
			$.ajax({
				method : 'POST',
				headers : {
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				url : 'check',
				data : {
					'memberPw' : memberPwVal
				},
				success : function(result){
					console.log(result);
					if(result){
						printModify();	
					}else{
						$('.pw_alert').text("비밀번호가 일치하지 않습니다.");
					}
				} // end success
			}); // end ajax
			
		} // end checkPw
		
		
		function printModify(){
			$('#subtitle').text("회원 정보 변경");
			$('#notice').text('아이디는 변경할 수 없습니다');
			let form = '<form action="modify" method="POST" id="formUpdate">' +
				'<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">' +
				'<div class="input-group mb-3"><span class="input-group-text">아이디</span>' +
				'<input type="text" name="memberId" class="form-control" value="${memberVO.memberId }" readonly></div>' +
				'<div class="input-group mb-3"><span class="input-group-text">이름</span>' +
				'<input type="text" name="memberName" class="form-control" value="${memberVO.memberName }" onblur="validCheck(this)"></div><span class="alert"></span>' +
				'<div class="input-group mb-3"><span class="input-group-text">휴대폰 번호</span>' +
				'<input type="text" name="memberPhone" class="form-control" value="${memberVO.memberPhone }" onblur="validCheck(this)"></div><span class="alert"></span>' +
				'<div class="input-group mb-3"><span class="input-group-text">Email</span>' +
				'<input type="text" name="memberEmail" class="form-control" value="${memberVO.memberEmail }" onblur="validCheck(this)"></div><span class="alert"></span>' +
				'<div class="input-group mb-3"><span class="input-group-text">비밀번호</span>' +
				'<input type="password" id="memberPw" name="memberPw" class="form-control" onblur="validCheck(this)"></div><span class="alert"></span>' +
				'<div class="input-group mb-3"><span class="input-group-text">비밀번호 확인</span>' +
				'<input type="password" id="memberPwChk" class="form-control"></div><span class="alert"></span>' +
				'<div><input type="submit" class="btn btn-primary"></div>' + 
				'</form>';
			
			$('#form').html(form);
			$('.link_withdrawal').append(
					$('<a href="withdrawal">회원 탈퇴</a>')
					);
			
			$('#memberPwChk').blur(function(){
				comparePw();
			});
			$('#formUpdate').submit(function(event){
				for(x in validList){
					if(validList[x] == false){
						alert("잘못된 입력 : " + x);
						event.preventDefault();
						return;
					}
				}
				
			}); // end formUpdate submit
		} // end printModify
		
		function comparePw(){
			let pwVal = $('#memberPw').val();
			let pwChkVal = $('#memberPwChk').val();
			let msg;
			
			if(pwVal.length == 0){
				msg = '';
				validList.memberPw = true;
				validList.memberPwChk = true;
			}else if(pwVal == pwChkVal){
				msg = '비밀번호가 일치합니다.';
				validList.memberPwChk = true;
			}else {
				msg = '비밀번호가 일치하지 않습니다.';
				validList.memberPwChk = false;
			}
				
			$('#memberPwChk').parents('.input-group').next().text(msg);
			console.log(msg);
		} // end comparePw
		
		function validCheck(input){
			let type = $(input).attr('name');
			let inputVal = $(input).val();
			let boxMsg = $(input).parents('.input-group').next();
			
			console.log('validCheck = ' + type + ' : ' + inputVal);
			if(inputVal == memberOrigin[type]){ // 입력값이 기존과 같을 경우
				boxMsg.text('');
				validList[type] = true;
				return;
			}
			
			console.log('input changed');
			if(inputVal.length == 0){
				boxMsg.text('변경할 내용을 입력해주세요.');
				validList[type] = false;
				return;
			}else {
				if(expMap[type].exp.test(inputVal)){
					// 정규표현식 일치
					boxMsg.text(expMap[type].success);
					validList[type] = true;
				}else{
					// 정규표현식 불일치
					boxMsg.text(expMap[type].fail);
					validList[type] = false;
				}
			}	
			if(type == 'memberPw'){
				comparePw();
			}
		} // end validCheck
		
	</script>
</body>
</html>