<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script src="https://cdn.portone.io/v2/browser-sdk.js"></script>
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
<meta charset="UTF-8">
<title>회원 가입</title>
</head>
<body>
	<div>
		<div>
			<input type="text" id="member_id" name="memberId" placeholder="아이디" onblur="isValidId(this)">
			<div></div>
		</div>
		<div>
			<input type="password" id="member_pw" name="memberPw" placeholder="비밀번호" onblur="expChk('pw', this)">
			<div></div>
		</div>
		<div>
			<input type="password" id="check_pw" placeholder="비밀번호 확인" onblur="comparePw()">
			<div></div>
		</div>
		<div>
			<input type="text" id="member_name" name="memberName" placeholder="이름" onblur="expChk('name', this)">
			<div></div>
		</div>
		<div>
			<input type="text" id="member_email" name="memberEmail" placeholder="이메일" onblur="expChk('email', this)">
			<div></div>
		</div>
		<div>
			<input type="text" id="member_phone" name="memberPhone" placeholder="전화번호" onblur="isValidPhone(this)">
			<div></div>
			<button onclick="whoYouAre()">본인인증</button>
		</div>
	</div>
	<div>
		<input type="button" onclick="register()" value="회원 가입">
	</div>

	<script type="text/javascript">
		
		let memberId = $('#member_id');
		let memberPw = $('#member_pw');
		let checkPw = $('#check_pw');
		let memberName = $('#member_name');
		let memberEmail = $('#member_email');
		let memberPhone = $('#member_phone');
	
		let expMap = {};
		expMap['id'] = {
				exp : new RegExp("^[a-zA-Z][a-zA-Z0-9]{5,19}$"),
				success : "올바른 입력 형식 입니다.",
				fail : "아이디는 알파벳으로 시작하는 6~20자의 알파벳과 숫자 조합이여야 합니다."
		};
		expMap['pw'] = {
				exp : new RegExp("^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{8,20}$"),
				success : "올바른 입력 형식 입니다.",
				fail : "비밀번호는 8~20자의 알파벳, 숫자, 한글이여야 합니다."
		};
		
		expMap['name'] = {
				exp : new RegExp("^[a-zA-Z가-힣]{2,20}$"),
				success : "올바른 입력 형식입니다.",
				fail : "이름은 2~20자의 한글 또는 알파벳이여야 합니다."
		};
		expMap['email'] = {
				exp : new RegExp("^[a-zA-Z][a-zA-Z0-9]{5,19}@[a-zA-Z\.]{6,20}$"),
				success : "올바른 입력 형식입니다.",
				fail : "잘못된 입력 형식입니다."
		};
		expMap['phone'] = {
				exp : new RegExp("^[0-9]{10,15}$"),
				success : "올바른 입력 형식입니다.",
				fail : "잘못된 입력 형식입니다"
		};
		
		const chkList = [
			'id', 'pw', 'checkPw', 'name', 'email', 'phone'
			];
		let validChkList = {};
		// 각 입력값이 유효한지 검사 결과를 저장하기 위한 맵
	
		for(x in chkList){
			validChkList[chkList[x]] = false;
		} // 맵 초기화
		
		// 아이디 유효성 체크
		function isValidId(input){
			let inputId = memberId.val();
			
			if(!expChk('id', input)){ // 정규표현식 만족 여부 체크
				console.log("정규 표현식 불일치");
			}else{
				$.ajax({ // 중복 체크
					type : 'POST',
					url : 'idDupChk',
					data : {
						'memberId' : inputId
					},
					success : function(result){
						console.log("중복 체크 결과 : " + result);
						if(result == 1){
							memberId.next().text("사용할 수 있는 아이디입니다.");
							validChkList['id'] = true;
						}else{
							memberId.next().text("사용 불가능한 아이디입니다.");
							validChkList['id'] = false;
						}
					}
				}); // end ajax
			}
		} // end isValidId
		
		
		function comparePw(){ // 비밀번호 확인란의 입력값이 일치하는지 확인
			inputPw = memberPw.val();
			inputChkPw = checkPw.val();
			
			if(inputPw == inputChkPw){
				checkPw.next().text("비밀번호가 일치합니다.");
				validChkList['checkPw'] = true;
			}else{
				checkPw.next().text("비밀번호 불일치");
				validChkList['checkPw'] = false;
			}
			
		} // end checkPw
		
		function isValidPhone(input){
			let inputVal = $(input).val();
			
			if(!expChk('phone', input)) // 정규표현식 불일치시 return
				return;
			
			// 이미 가입된 번호인지 체크
			$.ajax({
				method : 'POST',
				url : 'phoneDupChk',
				data : {
					'memberPhone' : inputVal
				},
				success : function(result){
					if(result == 1){
						memberPhone.next().text("사용 가능한 전화번호 입니다.");
						validChkList['phone'] = true;
					}else{
						memberPhone.next().text("이미 가입된 전화번호 입니다.");
						validChkList['phone'] = false;
					}
				}
			}); // end ajax
			
		} // end isValidPhone
		
		
		function register(){
			let result = true;
			for(x in chkList){
				result &= validChkList[chkList[x]];
			} // 모든 입력값의 유효성 검사 결과값을 AND 연산 (하나라도 false면 결과는 false)
			console.log(result);
			
			let memberInfo = {
					'memberId' : memberId.val(),
					'memberPw' : memberPw.val(),
					'memberName' : memberName.val(),
					'memberEmail' : memberEmail.val(),
					'memberPhone' : memberPhone.val()
			};
			
			if(result == 0){
				alert("모든 정보를 정확히 입력해주세요.");
			}else {
				$.ajax({
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					},
					url : 'register',
					data : JSON.stringify(memberInfo),
					success : function(result){
						console.log(result);
						if(result == 1){
							alert("회원 가입에 성공했습니다.");
							location.href = "login";
						}else{
							alert("회원 가입에 실패했습니다.");	
						}
						
					} // end success		
				}); // end ajax
			}
			
		} // end register
		
		function expChk(type, input){ // 정규 표현식 만족 여부 체크
			let regExp = expMap[type].exp; // 입력 타입에 맞는 정규표현식을 맵에서 꺼내온다
			console.log(regExp);
			let isValid = regExp.test($(input).val()); 
			// 정규표현식.test(문자열) : 문자열이 정규표현식을 만족하는지 확인하는 자바스크립트 내장 함수
			console.log(isValid);
			
			if(isValid){
				$(input).next().text(expMap[type].success);
				validChkList[type] = true;
				return true;
			}else{
				$(input).next().text(expMap[type].fail);
				validChkList[type] = false;
				return false;
			}
			
		} // end expChk
		
		function whoYouAre(){
			IMP.init('imp04667313');
			IMP.certification(
					  {
					    // param
					    // 주문 번호
					    pg: "A010002002", //본인인증 설정이 2개이상 되어 있는 경우 필
					    merchant_uid: "ORD20180131-0000011",
					    // 모바일환경에서 popup:false(기본값) 인 경우 필수
					    // PC환경에서는 popup 파라미터가 무시되고 항상 true 로 적용됨
					    popup: true
					  },
					  function (rsp) {
					    // callback
					    if (rsp.success) {
					    	console.log(rsp);
					   		$.ajax({
					   			
					   		});
					      // 인증 성공 시 로직
					    } else {
					    	console.log('fail : ' + rsp);
					      // 인증 실패 시 로직
					    }
					  },
					);
		} // end whoYouAre
		
	</script>

</body>
</html>