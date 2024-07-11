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
	width: 65%;
	margin: auto;
}
.inner_header {
	margin: 40px;
}
</style>
<title>판매 신청</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<jsp:include page="../include/sideBar.jsp"/>
	
	<div class="body_container">
		<div class="inner_header">
			<h3>${memberDetails.getUsername() }회원님의 판매자 등록 신청 내역입니다.</h3>
		</div>
		<form action="retry" method="POST" class="form_container">
			<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			<div class="input-group mb-3">
  				<span class="input-group-text">사업체 명</span>
  				<input type="text" class="form-control" id="businessName" name="businessName" value="${sellerRequest.businessName }" onblur="validChk(this)">
				<div></div>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">신청 날짜</span>
  				<input type="text" class="form-control" value="${sellerRequest.requestTime.toLocaleString() }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">신청 내용</span>
  				<input type="text" class="form-control" id="requestContent" name="requestContent" value="${sellerRequest.requestContent }" onblur="validChk(this)">
				<div></div>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">상태</span>
  				<input type="text" class="form-control" value="${sellerRequest.requestState }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">비고</span>
  				<input type="text" class="form-control" value="${sellerRequest.refuseMsg }" readonly>
				<div></div>
			</div>
			<%-- <div class="input_box">
				<div>사업체 명</div>
				<input type="text" id="businessName" class="input_tag" name="businessName" value="${sellerRequest.businessName }" onblur="validChk(this)">
				<div></div>
			</div> --%>
			<%-- <div class="input_box">
				<div>신청 날짜</div>
				<input type="text" class="input_tag" value="${sellerRequest.requestTime.toLocaleString() }" readonly>
				<div></div>
			</div> --%>
			<%-- <div class="input_box">
				<div>신청 내용</div>
				<input type="text" id="requestContent" class="input_tag" name="requestContent" value="${sellerRequest.requestContent }" onblur="validChk(this)">
				<div></div>
			</div> --%>
			<%-- <div class="input_box">
				<div>상태</div>
				<input type="text" class="input_tag" value="${sellerRequest.requestState }" readonly>
				<div></div>
			</div>
			<div class="input_box">
				<div>비고</div>
				<input type="text" class="input_tag" value="${sellerRequest.refuseMsg }" readonly>
				<div></div>
			</div> --%>
			<c:if test="${sellerRequest.requestState eq '거절' }">
				<div class="input_box">
					<input type="submit" class="btn btn-outline-primary" value="재요청">
				</div>
			</c:if>
		</form>
	</div>
	<script type="text/javascript">
		let checkMap = {
			businessName : {
				exp : new RegExp('^[a-zA-Z0-9가-힣 ]{1,50}$'),
				successMsg : '',
				failMsg : '1~50자의 한글, 영어, 숫자 조합만 가능합니다.',
				isValid : true
			},
			requestContent : {
				exp : new RegExp('^[a-zA-Z0-9가-힣 ]{0,100}$'),
				successMsg : '',
				failMsg : '한글, 영어, 숫자만 최대 100자까지 입력 가능합니다.',
				isValid : true
			}
		};
		
		function validChk(input) {
			let inputVal = $(input).val().trim();
			let inputType = $(input).attr('name');
			let tagAlert = $(input).next();
			
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