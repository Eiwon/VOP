<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
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
<title>${sellerVO.memberId } 님의 판매자 정보</title>
</head>
<body>
	<h2>판매자 정보</h2>
	<table class="form_info">
		<tbody>
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
				<td>사업체 명</td>
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
				<td><textarea id="refuseMsg">최대 50자 입력</textarea></td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<td><button id="btn_approve"></button></td>
				<td><button id="btn_refuse"></button></td>
			</tr>
		</tfoot>
	</table>
	
	<script type="text/javascript">
		const memberAuth = '${memberVO.memberAuth }';
		let btnApprove = $('#btn_approve');
		let btnRefuse = $('#btn_refuse');
		
		// 대상 유저의 권한이 "판매자"이면 판매자 권한 취소 페이지 출력
		if(memberAuth == '판매자'){
			btnApprove.text('판매자 권한 취소');
			btnApprove.click(function(){
				sendResult('승인');
			});
			btnRefuse.text('돌아가기');
			btnRefuse.click(function(){
				window.close();
			});
		}else { // "일반"이면 판매자 권한 등록 페이지 출력
			btnApprove.text('승인');
			btnApprove.click(function(){
				sendResult('승인');
			});
			btnRefuse.text('거절');
			btnRefuse.click(function(){
				sendResult('거절');
			});
		}
		
		
		function sendResult(requestState){
			let refuseMsg = $('#refuseMsg').text();
			
			$.ajax({
				url : 'approval',
				headers : {
					'Content-Type' : 'application/json'
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