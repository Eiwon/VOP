<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<sec:authentication var="memberDetails" property="principal"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>판매 신청</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<div>
		<h2>판매자 등록 신청</h2>
		
		<form action="sellerRequest" method="POST" id="sellerRequestForm">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			<table>
				<tbody>
					<tr>
						<td>유저 ID</td>
						<td>${memberDetails.getUsername() }</td>
					</tr>
					<tr>
						<td>사업자 명</td>
						<td><input type="text" name="businessName" onblur="validChk(this)"></td>
						<td id="businessNameAlert"></td>
					</tr>
					<tr>
						<td>세부 내용</td>
						<td><input type="text" name="requestContent" onblur="validChk(this)"></td>
						<td id="requestContentAlert"></td>
					</tr>
				</tbody>
			</table>
			<input type="hidden" name="memberId" value="${memberDetails.getUsername() }">
			<input type="submit" value="신청하기">
		</form>
	</div>
	
	<script type="text/javascript">
		let checkMap = {
			businessName : {
				exp : new RegExp('^[a-zA-Z0-9가-힣 ]{1,50}$'),
				successMsg : '',
				failMsg : '1~50자의 한글, 영어, 숫자 조합만 가능합니다.',
				isValid : false
			},
			requestContent : {
				exp : new RegExp('^[a-zA-Z0-9가-힣 ]{0,100}$'),
				successMsg : '',
				failMsg : '한글, 영어, 숫자만 최대 100자까지 입력 가능합니다.',
				isValid : false
			}
		};
		
		function validChk(input) {
			let inputVal = $(input).val().trim();
			let inputType = $(input).attr('name');
			let tagAlert = $('#' + inputType + 'Alert');
			
			if(inputVal.length == 0){ 
				checkMap[inputType].isValid = false;
				return;
			}
			
			let isValid = checkMap[inputType].exp.test(inputVal);
			console.log(inputType + ' 유효성 검사 결과 : ' + isValid);
			let msg = isValid ? checkMap[inputType].successMsg : checkMap[inputType].failMsg;
			checkMap[inputType].isValid = isValid;
			tagAlert.html(msg);
			
		} // end validChk
		
		$('#sellerRequestForm').submit(function(event){
			for(x in checkMap){
				if(!checkMap[x].isValid){
					alert("잘못된 입력입니다.");
					event.preventDefault();
					return;
				}
			}
		}); // end form submit
	
	</script>
	
</body>
</html>