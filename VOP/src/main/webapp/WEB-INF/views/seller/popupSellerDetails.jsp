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
tr {
	display: flex;
	flex-direction: row;
}
.form_info {
	border: 1px solid black;
}
</style>
<title>${sellerDetailsDTO.memberId } 님의 판매자 정보</title>
</head>
<body>
	<h2>판매자 정보</h2>
	<table class="form_info">
		<tbody>
			<c:set var="sellerVO" value="${sellerRequestDTO.sellerVO }"/>
			<c:set var="memberVO" value="${sellerRequestDTO.memberVO }"></c:set>
			<tr>
				<td>ID</td>
				<td>${sellerVO.memberId }</td>
			</tr>
			<tr>
				<td>이름</td>
				<td>${memberVO.memberName }</td>
			</tr>
			<tr>
				<td>전화번호</td>
				<td>${memberVO.memberPhone }</td>
			</tr>
			<tr>
				<td>이메일</td>
				<td>${memberVO.memberEmail }</td>
			</tr>
			<tr>
				<td>사업자 명</td>
				<td>${sellerVO.businessName }</td>
			</tr>
			
			<tr>
				<td>요청 시간</td>
				<td>${sellerVO.requestTime }</td>
			</tr>
			<tr>
				<td>요청 메시지</td>
				<td>${sellerVO.requestContent }</td>
			</tr>
			<tr>
				<td>결과 메시지</td>
				<c:if test="${sellerVO.refuseMsg != null }">
					<td><textarea id="refuseMsg">최대 50자 입력</textarea></td>
				</c:if>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<c:choose>
					<c:when test="${memberVO.memberAuth eq '판매자' }">
						<td><button id="btn_approve" onclick="revokeSellerAuth()">판매자 권한 취소</button></td>
						<td><button id="btn_refuse" onclick="window.close()">돌아가기</button></td>
					</c:when>
					<c:otherwise>
						<td><button id="btn_approve" onclick="approveReq()">승인</button></td>
						<td><button id="btn_refuse" onclick="rejectReq()">거절</button></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</tfoot>
	</table>
	
	<script type="text/javascript">
		
		function revokeSellerAuth(){
			let refuseMsg = $('#refuseMsg').text();
			
			$.ajax({
				url : 'revoke',
				method : 'PUT',
				headers : {
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
			let refuseMsg = $('#refuseMsg').text();
			
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