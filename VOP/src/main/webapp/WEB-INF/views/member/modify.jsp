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
		<!-- <form action="modify" method="POST"> -->
			<table>
				<tbody>
					<tr>
						<td>아이디</td>
						<td><input type="text" value="" readonly>${memberDetails.getUsername() }</td>
					</tr>
					<tr>
						<td>이름</td>
						<td><input type="text" name="memberName" value="">${memberDetails.memberVO.memberName }</td>
					</tr>
					<tr>
						<td>휴대폰 번호</td>
						<td><input type="text" name="memberPhone" value="">${memberDetails.memberVO.memberPhone }</td>
					</tr>
					
					<tr>
						<td><button onclick="test()">주소 api 테스트</button></td>
						<td></td>
					</tr>
					
					<tr></tr>
					<tr></tr>
				</tbody>
			</table>
		
		<!-- </form> -->
		
	</div>
	
	<script type="text/javascript">
		let memberIdVal = '${memberDetails.memberVO.memberId }';
		let memberPw = $('#member_pw');
		
		console.log(memberIdVal);
		
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
		
		function test(){
			new daum.Postcode({
		        oncomplete: function(data) {
		            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
		            // 예제를 참고하여 다양한 활용법을 확인해 보세요.
		            console.log(data);
		        }
		    }).open();
		}
		
	</script>
</body>
</html>