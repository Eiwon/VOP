<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>관리자 페이지</title>
</head>
<body>
	<div>
		<h2>관리자 페이지</h2>
	</div>
	
	<div>
		<div>
			<h3>사업자 등록 요청</h3>
			<table>
				<tbody id="seller_req_list">
				</tbody>
				<tfoot id="seller_req_list_page"></tfoot>
			</table>
		</div>
		<div>
			<h3>승인된 사업자 조회</h3>
			<table>
				<tbody id="seller_approved_list">
				</tbody>
				<tfoot id="seller_approved_list_page"></tfoot>
			</table>
		</div>
		<div>
			<h3>상품 등록 요청</h3>
			<table>
				<tbody id="product_register_req_list">
				</tbody>
				<tfoot id="product_register_req_list_page"></tfoot>
			</table>
		</div>
		<div>
			<h3>상품 삭제 요청</h3>
		</div>
	</div>
	
	<div>
		
		<!-- 판매자 등록 심사 목록 -->
		
		<!-- 상품 등록 심사 목록 -->
		
		<!-- 상품 삭제 요청 -->
	</div>
	
	
	<script type="text/javascript">
		const memberId = '<%= request.getSession().getAttribute("memberId")%>';
		let tagSellerReqList = $('seller_req_list');
		let tagSellerApprovedList = $('seller_approved_list');
		let tagProdutRegisterReqList = $('product_register_req_list');
		let sellerReqList;
		let sellerApprovedList;
		let productRegisterReqList;
		
		$(document).ready(function(){
			
			//showSellerRequest();
			
		}); // end document.ready
		
		
		
		function showSellerRequest(){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../seller/reqList/',
				success : function(result){
					console.log(result);
					sellerReqList = JSON.parse(result);
					
					for(x in sellerReqList){
						form += '<tr onclick="toDetails(this)">' + 
							'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
							'<td class="memberId">' + sellerReqList[x].memberId + '</td>' + 
							'<td class="businessName">' + sellerReqList[x].businessName +'</td>' +
							'<td class="requestTime">' + sellerReqList[x].requestTime +'</td>' +
							'<td class="requestState">' + sellerReqList[x].requestState +'</td>' +
							'</tr>';
					}
					tagSellerReqList.html(form);
				} // end success
			}); // end ajax
			
		} // end showSellerRequest
		
		function showSellerApproved(){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../seller/approvedList/',
				success : function(result){
					console.log(result);
					sellerApprovedList = JSON.parse(result);
					
					for(x in sellerApprovedList){
						form += '<tr">' + 
							'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
							'<td class="memberId">' + sellerApprovedList[x].memberId + '</td>' + 
							'<td class="businessName">' + sellerApprovedList[x].businessName +'</td>' +
							'<td class="requestTime">' + sellerApprovedList[x].requestTime +'</td>' +
							'</tr>';
					}
					tagSellerApprovedList.html(form);
				} // end success
			}); // end ajax
			
		} // end showSellerApproved
		
		function showProductRegisterRequest(){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../product/registerRequest/',
				success : function(result){
					console.log(result);
					productRegisterReqList = JSON.parse(result);
					
					for(x in productRegisterReqList){
						form += '';
					}
					tagProductRegisterReqList.html(form);
				} // end success
			}); // end ajax
			
		} // end showProductRegisterRequest
		
		
		function toDetails(input) {
			const targetIndex = parseInt($(input).find('.targetIndex').text());
			const selectedMemberId = sellerReqList[targetIndex].memberId;
			
			// memberId의 request 페이지 요청
			
		} // end toDetails
		
		
		/* private String memberId; // 회원 Id
		private String businessName; // 사업체 이름
		private Date requestTime; // 요청 시간
		private String requestContent; // 요청 내용
		private String requestState; // 요청 승인 대기중, 거절 여부
		private String refuseMsg; // 요청 거부 사유 */
		
	</script>
	
</body>
</html>