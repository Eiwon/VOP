<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<style type="text/css">
.btn_submit {
	width: 100%;
}
.body_container {
	width: 40%;
	margin: auto;
}
.form_container {
	padding: 10px;
}
.inner_header {
	text-align: center;
	margin: 15px;
	margin-top: 30px;
}
.form-floating {
	height: 100px;
}
</style>
<meta charset="UTF-8">
<title>회원 가입</title>
</head>
<body>
	<div class="body_container">
		<div class="inner_header">
			<h3>회원 가입</h3>
		</div>
	
		<form class="form_container" action="register" method="POST">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			<div class="form-floating">
				<input type="text" id="memberId" class="form-control" name="memberId" placeholder="아이디" onblur="checkValid(this)">
				<div></div>
				<label for="memberId">아이디</label>
			</div>
			<div class="form-floating">
				<input type="password" id="memberPw" class="form-control" name="memberPw" placeholder="비밀번호" onblur="checkValid(this)">
				<div></div>
				<label for="memberPw">비밀번호</label>
			</div>
			<div class="form-floating">
				<input type="password" id="checkPw" class="form-control" placeholder="비밀번호 확인" onblur="comparePw()">
				<div></div>
				<label for="checkPw">비밀번호 확인</label>
			</div>
			<div class="form-floating">
				<input type="text" id="memberName" class="form-control" name="memberName" placeholder="이름" onblur="checkValid(this)">
				<div></div>
				<label for="memberName">이름</label>
			</div>
			<div class="form-floating">
				<input type="text" id="memberEmail" class="form-control" name="memberEmail" placeholder="이메일" onblur="checkValid(this)">
				<div></div>
				<label for="memberEmail">이메일</label>
			</div>
			<div class="form-floating">
				<input type="text" id="memberPhone" class="form-control" name="memberPhone" placeholder="전화번호" onblur="checkValid(this)">
				<div></div>
				<label for="memberPhone">전화번호</label>
			</div>
			<div style="margin-top: 30px;">
				<input type="submit" class="btn_submit btn btn-primary" value="회원 가입">
			</div>
		</form>
	</div>
	<script type="text/javascript">
		
		let checkMap = {
				memberId : {
					exp : new RegExp("^[a-zA-Z][a-zA-Z0-9]{5,19}$"),
					success : "올바른 입력 형식 입니다.",
					fail : "아이디는 알파벳으로 시작하는 6~20자의 알파벳과 숫자 조합이여야 합니다.",
					isValid : false
				},
				memberPw : {
					exp : new RegExp("^[a-zA-Z0-9!@#$%^&*()_+-\[={};'\"\|,.<>?]{8,20}$"),
					success : "올바른 입력 형식 입니다.",
					fail : "비밀번호는 8~20자의 알파벳, 숫자, 특수문자로 이루어져야 합니다.",
					isValid : false
				},
				memberName : {
					exp : new RegExp("^[a-zA-Z가-힣]{2,20}$"),
					success : "올바른 입력 형식입니다.",
					fail : "이름은 2~20자의 한글 또는 알파벳이여야 합니다.",
					isValid : false
				},
				checkPw : {
					isValid : false
				},
				memberEmail : {
					exp : new RegExp("^[a-zA-Z][a-zA-Z0-9]{5,19}@[a-zA-Z\.]{6,20}$"),
					success : "올바른 입력 형식입니다.",
					fail : "잘못된 입력 형식입니다.",
					isValid : false
				},
				memberPhone : {
					exp : new RegExp("^[0-9]{10,15}$"),
					success : "올바른 입력 형식입니다.",
					fail : "잘못된 입력 형식입니다",
					isValid : false
				}
		};
		
		function checkValid(input){
			let type = $(input).attr('id');
			let inputVal = $(input).val();
			
			if(inputVal.length == 0){
				checkMap[type].isValid = false;
				return;
			}
			
			let isValid = checkMap[type].exp.test(inputVal);
			console.log(type + ' 유효성 확인 = ' + isValid);
			
			let msg = isValid ? checkMap[type].success : checkMap[type].fail;
			
			$(input).next().text(msg);
			checkMap[type].isValid = isValid;
			if(!isValid) {
				$(input).addClass('is-invalid');
				checkMap[type].val = '';
				return;
			}else{
				$(input).removeClass('is-invalid');
				checkMap[type].val = inputVal;
			}
			
			if(type == 'memberId'){
				isValidId(inputVal);
			}/* else if(type == 'memberPhone'){
				isValidPhone(inputVal);
			} */else if(type == 'memberPw'){
				comparePw();
			}
		} // end checkValid
		
		// 아이디 유효성 체크
		function isValidId(memberId){
			$.ajax({ // 중복 체크
				method : 'GET',
				url : 'idDupChk?memberId=' + memberId,
				success : function(result){
					console.log("중복 체크 결과 : " + result);
					if(result == 1){
						$('#memberId').next().text("사용할 수 있는 아이디입니다.");
						$('#memberId').removeClass('is-invalid');
						checkMap['memberId'].isValid = true;
						checkMap['memberId'].val = memberId;
					}else{
						$('#memberId').next().text("사용 불가능한 아이디입니다.");
						checkMap['memberId'].isValid = false;
						$('#memberId').addClass('is-invalid');
						checkMap['memberId'].val = '';
					}
				}
			}); // end ajax
		} // end isValidId
		
		function comparePw(){ // 비밀번호 확인란의 입력값이 일치하는지 확인
			memberPwVal = $('#memberPw').val();
			checkPwVal = $('#checkPw').val();
			
			if(memberPwVal == checkPwVal){
				$('#checkPw').next().text("비밀번호가 일치합니다.");
				checkMap.checkPw.isValid = true;
				$('#checkPw').removeClass('is-invalid');
			}else{
				$('#checkPw').next().text("비밀번호 불일치");
				checkMap.checkPw.isValid = false;
				$('#checkPw').addClass('is-invalid');
			}
		} // end checkPw
		
		/* function isValidPhone(memberPhone){
			// 이미 가입된 번호인지 체크
			$.ajax({
				method : 'GET',
				url : 'phoneDupChk',
				data : {
					'memberPhone' : memberPhone
				},
				success : function(result){
					if(result == 1){
						$('#memberPhone').next().text("사용 가능한 전화번호 입니다.");
						checkMap.memberPhone.isValid = true;
					}else{
						$('#memberPhone').next().text("이미 가입된 전화번호 입니다.");
						checkMap.memberPhone.isValid = false;
					}
				}
			}); // end ajax
		} // end isValidPhone */
		
		$('form').submit(function(event){
			for(x in checkMap){
				if(!checkMap[x].isValid){
					alert('유효하지 않은 입력 : ' + x);
					event.preventDefault();
					return;
				}
			}
		});
		
		
	</script>

</body>
</html>