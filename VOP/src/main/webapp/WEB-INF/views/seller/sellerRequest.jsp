<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>판매 신청</title>
</head>
<body>
	<div>
		<h2>판매자 등록 신청</h2>
	</div>
	<div id="myRequest">
		<strong>신청서 작성</strong>
		<div>
			<div>
				<b>사업자 번호</b>
			</div>
		</div>
	</div>
	<div>
		<a href="registerProduct">상품 등록</a>
		<a href='listProduct?memberId=<%= request.getSession().getAttribute("memberId")%>'>등록한 상품 조회</a>
	</div>
	
	<script type="text/javascript">
		let memberId = '<%= request.getSession().getAttribute("memberId")%>';
		let myRequest = $('#myRequest');
		
		$(document).ready(function(){
			$.ajax({
				method : 'GET',
				url : 'my/' + memberId,
				success : function(result){
					console.log(result);
					if(result == ""){
						console.log("신청내역 없음");
						printForm();
					}else{
						console.log("신청내역 있음");
						printInfo(result);
					}
					
				} // end success
			}); // end ajax
			
		}); // end document.ready
	
		function printForm(){
			console.log("신청서 출력");
			
		} // end printForm
		
		function printInfo(sellerVO){
			console.log("신청 내역 출력");
			if(sellerVO.requestState == '승인 대기중') {
				sellerVO.refuseMsg = "";
			}
			let str = '<div><strong>회원님의 판매자 등록 신청 내역입니다.</strong><table>' + 
				'<thead><tr><th>사업체 이름</th><th>신청 날짜</th><th>신청 내용</th><th>상태</th><th>비고</th></tr></thead>' + 
				'<tbody><tr>' +
				'<td>' + sellerVO.businessName + '</td>' +
				'<td>' + new Date(sellerVO.requestTime) + '</td>' +
				'<td>' + sellerVO.requestContent + '</td>' +
				'<td>' + sellerVO.requestState + '</td>' +
				'<td>' + sellerVO.refuseMsg + '</td></tr></tbody></table></div>';
			myRequest.html(str);
		} // end printInfo
	</script>
	
</body>
</html>