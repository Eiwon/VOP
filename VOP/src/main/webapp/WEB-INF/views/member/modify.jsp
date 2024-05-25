<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="memberDetails" property="principal"/>
<html>
<head>
<meta charset="EUC-KR">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<style type="text/css">
tr{
	height: 50px;
}
</style>
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
				<span>${memberDetails.getUsername() }</span>
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
		let memberPw = $('#member_pw');
		let memberOrigin = {
				'memberId' : '${memberDetails.getUsername() }',
				'memberPw' : '',
				'memberName' : '${memberDetails.memberVO.memberName}',
				'memberPhone' : '${memberDetails.memberVO.memberPhone}',
				'memberEmail' : '${memberDetails.memberVO.memberEmail}'
		};
		let expMap = {
				'memberPw' : {
					exp : new RegExp("^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{8,20}$"),
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
				memberPw.next().text("비밀번호를 입력해주세요.");
				return;
			}
			
			$.ajax({
				method : 'POST',
				url : 'check',
				data : {
					'memberPw' : memberPwVal
				},
				success : function(result){
					console.log(result);
					if(result){
						printModify();	
					}else{
						memberPw.next().text("비밀번호가 일치하지 않습니다.");
					}
				} // end success
			}); // end ajax
			
		} // end checkPw
		
		
		function printModify(){
			$('#subtitle').text("회원 정보 변경");
			let form = '<form action="modify" method="POST"><table><tbody>' +
				'<tr><td>아이디</td>' +
				'<td><input type="text" value="${memberDetails.getUsername() }" readonly></td><td></td></tr>' +
				'<tr><td>이름</td>' +
				'<td><input type="text" name="memberName" value="${memberDetails.memberVO.memberName }" onblur="validCheck(this)"></td><td></td></tr>' +
				'<tr><td>휴대폰 번호</td>' +
				'<td><input type="text" name="memberPhone" value="${memberDetails.memberVO.memberPhone }" onblur="validCheck(this)"></td><td></td></tr>' +
				'<tr><td>Email</td>' +
				'<td><input type="text" name="memberEmail" value="${memberDetails.memberVO.memberEmail }" onblur="validCheck(this)"></td><td></td></tr>' +
				'<tr><td>비밀번호</td>' +
				'<td><input type="password" id="memberPw" name="memberPw" onblur="validCheck(this)"></td><td></td></tr>' +
				'<tr><td>비밀번호 확인</td>' +
				'<td><input type="password" id="memberPwChk"><br><strong class="alert"></strong></td></tr>' +
				'</tbody></table>' + 
				'<td><input type="submit"></td>' + 
				'</form>';
			
			$('#form').html(form);
			
			$('#memberPwChk').blur(function(){
				comparePw();
			});
		} // end printModify
		
		function test(){
			new daum.Postcode({
		        oncomplete: function(data) {
		            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
		            // 예제를 참고하여 다양한 활용법을 확인해 보세요.
		            console.log(data);
		        }
		    }).open();
		}
		
		function comparePw(){
			let pwVal = $('#memberPw').val();
			let pwChkVal = $('#memberPwChk').val();
			
			if(pwVal.length == 0 || pwChkVal.length == 0){
				return;
			}
			let msg = (pwVal == pwChkVal) ? '비밀번호가 일치합니다.' : '비밀번호가 일치하지 않습니다.';
				
			$('#memberPwChk').siblings('.alert').text(msg);
			console.log(msg);
		} // end comparePw
		
		function validCheck(input){
			let type = $(input).attr('name');
			let inputVal = $(input).val();
			
			console.log('validCheck = ' + type + ' : ' + inputVal);
			if(inputVal == memberOrigin[type]){ // 입력값이 기존과 같을 경우
				console.log('변경사항 없음');
				return;
			}
			console.log('input changed');
			if(inputVal.length == 0){
				console.log('변경할 내용을 입력해주세요.');
				return;
			}else {
				if(expMap[type].exp.test(inputVal)){
					console.log(expMap[type].success);
				}else{
					console.log(expMap[type].fail);
				}
			}	
			if(type == 'memberPw'){
				comparePw();
			}
		} // end validCheck
		
	</script>
</body>
</html>