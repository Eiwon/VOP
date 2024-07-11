<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">

.body_container{
	width: 80%;
	margin: auto;
}
.inner_header {
	margin: 20px;
}
.form_foot {
	display: flex;
	justify-content: center;
}
</style>
<title>${sellerDetailsDTO.memberId } 님의 판매자 정보</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

	<div class="body_container">
		<div class="inner_header">
			<h2>판매자 정보</h2>
		</div>
		<div class="form_info">
			<c:set var="sellerVO" value="${sellerRequestDTO.sellerVO }"/>
			<c:set var="memberVO" value="${sellerRequestDTO.memberVO }"/>
			<div class="input-group mb-3">
  				<span class="input-group-text">ID</span>
  				<input type="text" class="form-control" value="${sellerVO.memberId }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">이름</span>
  				<input type="text" class="form-control" value="${memberVO.memberName }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">전화번호</span>
  				<input type="text" class="form-control" value="${memberVO.memberPhone }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">이메일</span>
  				<input type="text" class="form-control" value="${memberVO.memberEmail }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">사업자 명</span>
  				<input type="text" class="form-control" value="${sellerVO.businessName }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">요청 시간</span>
  				<input type="text" class="form-control" value="${sellerVO.requestTime.toLocaleString() }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">요청 메시지</span>
  				<input type="text" class="form-control" value="${sellerVO.requestContent }" readonly>
			</div>
			<div class="input-group mb-3">
				<span class="input-group-text">결과 메시지</span>
  				<textarea id="refuseMsg" class="form-control" placeholder="최대 50자 입력" style="border-color: blue;">${sellerVO.refuseMsg }</textarea>
			</div>
		</div>
		<div class="form_foot">
			<c:choose>
				<c:when test="${memberVO.memberAuth eq '판매자' }">
					<div class="btn-group" role="group">
  					<button type="button" id="btn_approve" class="btn btn-outline-danger" onclick="revokeSellerAuth()">판매자 권한 취소</button>
  					<button type="button" id="btn_refuse" class="btn btn-outline-primary" onclick="window.close()">돌아가기</button>
					</div>
				</c:when>
				<c:otherwise>
					<div class="btn-group" role="group">
  					<button type="button" id="btn_approve" class="btn btn-outline-primary" onclick="approveReq()">승인</button>
  					<button type="button" id="btn_refuse" class="btn btn-outline-danger" onclick="rejectReq()">거절</button>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		
	</div>
	<script type="text/javascript">
		
		function revokeSellerAuth(){
			let refuseMsg = $('#refuseMsg').val();
			
			$.ajax({
				url : 'revoke',
				method : 'PUT',
				headers : {
					'Content-type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				data : JSON.stringify({
					'memberId' : '${sellerVO.memberId}',
					'refuseMsg' : refuseMsg
				}),
				success : function(result){
					console.log('권한 취소 결과 : ' + result);
					if(result == 1){
						window.close();
					}else{
						alert('권한 취소 실패');
					}
				}
			}); // end ajax
		} // end revokeSellerAuth
	
		function approveReq(){
			sendResult('approve');
		}
		
		function rejectReq(){
			sendResult('reject');
		}
		
		function sendResult(requestState){
			let refuseMsg = $('#refuseMsg').val();
			
			$.ajax({
				url : 'approval',
				headers : {
					'Content-Type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				method : 'PUT',
				data : JSON.stringify({
					'memberId' : '${sellerVO.memberId}',
					'refuseMsg' : refuseMsg,
					'requestState' : requestState
				}),
				success : function(result){
					window.close();	
				}
			}); // end ajax
		} // end sendResult
		
	
	</script>
	
</body>
</html>