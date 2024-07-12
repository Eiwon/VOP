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
<style type="text/css">
.body_container{
	width: 50%;
	margin: auto;
}
.inner_header {
	margin-bottom: 40px;
}
</style>
<title>판매 신청</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<jsp:include page="../include/sideBar.jsp"/>
	
	<div class="body_container">
		<div class="inner_header">
			<h2>판매자 등록 신청</h2>
		</div>
		
		<form action="sellerRequest" method="POST" id="sellerRequestForm">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			<div class="input-group mb-3">
  				<span class="input-group-text">유저 ID</span>
 				<input type="text" class="form-control" value="${memberDetails.getUsername() }" readonly>
			</div>
			<div class="form-floating mb-3">
  				<input type="text" class="form-control" id="businessName" name="businessName" placeholder="사업자 명" onblur="validChk(this)">
  				<div id="businessNameAlert"></div>
  				<label for="businessName">사업자 명</label>
			</div>
			<div class="form-floating mb-3">
  				<input type="text" class="form-control" id="requestContent" name="requestContent" placeholder="세부 사항" onblur="validChk(this)">
  				<div id="requestContentAlert"></div>
  				<label for="requestContent">세부 사항</label>
			</div>
			<input type="hidden" name="memberId" value="${memberDetails.getUsername() }">
			<input type="submit" class="btn btn-outline-primary" value="신청하기">
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