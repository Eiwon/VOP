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
	<h2>아이디 찾기</h2>
	<div id="findAccount">
		<strong>등록된 휴대폰 번호로 찾기</strong><br>
		<div>
			이름 
			<input type="text" id="member_name">
			<div></div>
		</div>
		<div>
			등록된 전화번호 
			<input type="text" id="member_phone">
			<div></div>
		</div>
		<input type="button" value="본인 인증" onclick="checkIdentity()">
	</div>
	<div>
		<a href="findPassword">비밀번호 찾기</a>
	</div>

	<script type="text/javascript">
		let memberName = $('#member_name');
		let memberPhone = $('#member_phone');
	
		function checkIdentity(){
			console.log("본인 인증");
			let memberNameVal = memberName.val();
			let memberPhoneVal = memberPhone.val();
			
			if(!isWritten(memberNameVal, memberPhoneVal))
				return;
			
			let result = confirm("본인 맞나요?");
			
			if(result){
				$.ajax({ // 이름과 전화번호로 ID 검색
					method : 'POST',
					url : 'findByPhone',
					data : {
						'memberName' : memberNameVal,
						'memberPhone' : memberPhoneVal
					},
					success : function(result){
						console.log("결과 : " + result);
						if(result == ""){
							alert("입력 정보와 일치하는 회원이 존재하지 않습니다.");
						}else{
							showMemberId(memberNameVal, result); // 일치하는 ID가 있으면 출력
						}
					}
				}); // end ajax
			}
		} // end checkIdentity
	
		function showMemberId(memberName, memberId){ // 아이디 확인용 원소 출력
			let str = '<div><span>' + memberName + '회원님의 아이디는 다음과 같습니다.</span><br><br><strong>' 
						+ memberId + '</strong></div>';
			$('#findAccount').html(str);
		} // end showMemberId
		
		function isWritten(nameVal, phoneVal){
			if(nameVal.length == 0){
				memberName.next().text("이름을 입력해주세요.");
			}else{
				memberName.next().text(" ");
			}
			if(phoneVal.length == 0){
				memberPhone.next().text("전화번호를 입력해주세요.");
			}else{
				memberPhone.next().text(" ");
			}
			return (nameVal.length * phoneVal.length > 0) ? true : false;
		} // end isWritten
		
	</script>


</body>
</html>