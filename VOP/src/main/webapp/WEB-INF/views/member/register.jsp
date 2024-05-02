<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<meta charset="UTF-8">
<title>회원 가입</title>
</head>
<body>
	<div>
		<div>
			<input type="text" id="member_id" name="memberId" placeholder="아이디" onblur="isValidId()">
			<div></div>
		</div>
		<div>
			<input type="password" id="member_pw" name="memberPw" placeholder="비밀번호">
			<div></div>
		</div>
		<div>
			<input type="password" id="check_pw" placeholder="비밀번호 확인">
			<div></div>
		</div>
		<div>
			<input type="text" id="member_name" name="memberName" placeholder="이름">
			<div></div>
		</div>
		<div>
			<input type="text" id="member_email" name="memberEmail" placeholder="이메일">
			<div></div>
		</div>
		<div>
			<input type="text" id="member_phone" name="memberPhone" placeholder="전화번호">
			<div></div>
		</div>
	</div>
	<div>
		<input type="button" onclick="register()">
	</div>

	<script type="text/javascript">
		
		let expMap = {};
		expMap['id'] = new RegExp("^[a-zA-Z][a-zA-Z0-9]{5,19}$");
		expMap['pw'] = new RegExp("^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{8,20}$");
		expMap['name'] = new RegExp("^[a-zA-Z가-힣]{2,20}$");
		expMap['email'] = new RegExp("^[]{}$");
		expMap['phone'] = new RegExp("^[0-9]{}$");
		
		const chkList = [
			'memberId', 'memberPw', 'checkPw', 'memberName', 'memberEmail', 'memberPhone'
			];
		let validChkList = {};
		// 각 입력값이 유효한지 검사 결과를 저장하기 위한 맵
	
		for(x in chkList){
			validChkList[chkList[x]] = false;
		} // 맵 초기화
		
		let memberId = $('#member_id');
		let memberPw = $('#member_pw');
		let checkPw = $('#check_pw');
		let memberName = $('#member_name');
		let memberEmail = $('#member_email');
		let memberPhone = $('#member_phone');
		
		
		function isValidId(){
			let inputId = memberId.val();
			let expChk = expMap['id'].test(inputId);
			
			if(!expChk){
				console.log("정규 표현식 불일치");
				memberId.next().text("아이디는 알파벳으로 시작하는 5~20자의 알파벳과 숫자 조합이여야 합니다.");
				validChkList['memberId'] = false;
			}else{
				$.ajax({
					type : 'POST',
					url : 'idDupChk',
					data : {
						'memberId' : inputId
					},
					success : function(result){
						console.log("중복 체크 결과 : " + result);
						if(result == 1){
							memberId.next().text("사용할 수 있는 아이디입니다.");
							validChkList['memberId'] = true;
						}else{
							memberId.next().text("사용 불가능한 아이디입니다.");
							validChkList['memberId'] = false;
						}
					}
				}); // end ajax
			}
		} // end isValidId
		
	
	</script>

</body>
</html>