<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>계정 정보 찾기</title>
</head>
<body>
	<h2>비밀번호 찾기</h2>
	<div id="findPwForm">
		<div>
			이름
			<input type="text" id="memberName">
			<div></div>
		</div>
		<div>
			아이디
			<input type="text" id="memberId">
			<div></div>
		</div>
		<input type="button" value="비밀번호 찾기" onclick="findPw()">
	</div>

	<script type="text/javascript">
		const memberName = $('#memberName');
		const memberId = $('#memberId');
		const exp = new RegExp("^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{8,20}$");
		let memberIdVal;
		
		let validChk = {};
		validChk['pw'] = false;
		validChk['checkPw'] = false;
		
		function findPw(){
			if(!isWritten(memberName.val(), memberId.val()))
				return; // 모든 값이 입력되지 않았다면 return
			memberIdVal = memberId.val();
			let str = '<div>인증번호(임시 : 0000)<input type="text" id="certification"></div>' +
					  '<div>새 비밀번호<input type="password" id="memberPw" onblur="expChk()"><div></div></div>' + 
					  '<div>새 비밀번호 확인<input type="password" id="checkPw" onblur="comparePw()"><div></div></div>' + 
					  '<div><input type="button" value="비밀번호 변경" onclick="resetPw()"></div>';
			$('#findPwForm').html(str);
		} // end findPw
	
		function isWritten(nameVal, idVal){
			if(nameVal.length == 0){
				memberName.next().text("이름을 입력해주세요.");
			}else{
				memberName.next().text(" ");
			}
			if(idVal.length == 0){
				memberId.next().text("아이디를 입력해주세요.");
			}else{
				memberId.next().text(" ");
			}
			return (nameVal.length * idVal.length > 0) ? true : false;
		} // end isWritten
	
		function expChk(){
			let memberPw = $('#memberPw');
			if(exp.test(memberPw.val())){
				memberPw.next().text("");
				validChk['pw'] = true;
			}else{
				memberPw.next().text("비밀번호는 8~20자의 알파벳, 숫자, 한글이여야 합니다.");
				validChk['pw'] = false;
			}
		} // end expChk
		
		function comparePw(){ // 비밀번호 확인란의 입력값이 일치하는지 확인
			inputPw = $('#memberPw').val();
			inputChkPw = $('#checkPw').val();
			
			if(inputPw == inputChkPw){
				$('#checkPw').next().text("비밀번호가 일치합니다.");
				validChk['checkPw'] = true;
			}else{
				$('#checkPw').next().text("비밀번호 불일치");
				validChk['checkPw'] = false;
			}
		} // end checkPw
		
		function resetPw(){
			const certification = $('#certification').val();
			const memberPw = $('#memberPw').val();
			let result = true;
			
			for(x in validChk){
				result &= validChk[x]; 
			}
			
			if(certification == '0000' && result){
				$.ajax({
					method : 'POST',
					url : 'findPassword',
					data : {
						'memberId' : memberIdVal,
						'memberPw' : memberPw
					},
					success : function(result){
						console.log("비밀번호 변경 결과 : " + result);
						
						if(result == 1){
							alert("비밀번호 변경 성공");
							location.href = 'login';
						}else{
							alert("잘못된 입력입니다.");
						}
					} // end success
				}); // end ajax	
			}
		} // end resetPw
		
	</script>

</body>
</html>