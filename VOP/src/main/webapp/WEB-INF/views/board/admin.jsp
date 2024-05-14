<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
.search_table {
	border: 1px solid black;
}

td {
	width: 200px;
}


</style>
<title>관리자 페이지</title>
</head>
<body>
	<div>
		<h2>관리자 페이지</h2>
	</div>
	
	<div>
		<div>
			<h3>사업자 등록 요청</h3>
			<table class="search_table">
				<tbody id="seller_req_list">
				</tbody>
				<tfoot id="seller_req_list_page"></tfoot>
			</table>
		</div>
		<div>
			<h3>등록된 사업자 조회</h3>
			<table class="search_table">
				<tbody id="seller_approved_list">
				</tbody>
				<tfoot id="seller_approved_list_page"></tfoot>
			</table>
		</div>
		<div>
			<h3>상품 등록 요청</h3>
			<table class="search_table">
				<tbody id="product_register_req_list">
				</tbody>
				<tfoot id="product_register_req_list_page"></tfoot>
			</table>
		</div>
		<div>
			<h3>상품 삭제 요청</h3>
			<table class="search_table">
				<tbody id="product_delete_req_list">
				</tbody>
				<tfoot id="product_delete_req_list_page"></tfoot>
			</table>
		</div>
	</div>
	<!-- 
	1. 권한 요청 승인/거부
	2. 등록된 사업자 검색, 권한 삭제
	3. 상품 등록 승인/거부
	4. 상품 삭제
	5. 페이징
	 -->
	
	
	<script type="text/javascript">
		const memberId = '<%= request.getSession().getAttribute("memberId")%>';
		let tagSellerReqList = $('#seller_req_list');
		let tagSellerApprovedList = $('#seller_approved_list');
		let tagProdutRegisterReqList = $('#product_register_req_list');
		let tagProductDeleteReqList = $('#product_delete_req_list');
		let sellerReq = {};
		let sellerApproved = {};
		let productRegisterReq = {};
		let productDeleteReq = {};
		
		$(document).ready(function(){
			
			showSellerRequest();
			showSellerApproved();
			showProductRegisterRequest();
			showProductDeleteRequest();
			
		}); // end document.ready
		
		
		
		function showSellerRequest(){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../seller/wait',
				success : function(result){
					console.log(result);
					sellerReq.list = result.list;
					sellerReq.pageMaker = result.pageMaker;
					
					for(x in sellerReq.list){
						form += '<tr onclick="toDetails(this)">' + 
							'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
							'<td class="memberId">' + sellerReq.list[x].memberId + '</td>' + 
							'<td class="businessName">' + sellerReq.list[x].businessName +'</td>' +
							'<td class="requestTime">' + toDate(sellerReq.list[x].requestTime) +'</td>' +
							'<td class="requestState">' + sellerReq.list[x].requestState +'</td>' +
							'</tr>';
					}
					console.log(form);
					tagSellerReqList.html(form);
				} // end success
			}); // end ajax
			
		} // end showSellerRequest
		
		function showSellerApproved(){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../seller/approved',
				success : function(result){
					console.log(result);
					sellerApproved.list = result.list;
					sellerApproved.pageMaker = result.pageMaker;
					
					for(x in sellerApproved.list){
						form += '<tr">' + 
							'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
							'<td class="memberId">' + sellerApproved.list[x].memberId + '</td>' + 
							'<td class="businessName">' + sellerApproved.list[x].businessName +'</td>' +
							'<td class="requestTime">' + toDate(sellerApproved.list[x].requestTime) +'</td>' +
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
				url : '../seller/productReq',
				success : function(result){
					console.log(result);
					productRegisterReq.list = result.list;
					productRegisterReq.pageMaker = result.pageMaker;
					
					for(x in productRegisterReq.list){
						form += '<tr>' + 
						'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
						'<td><img src="../product/showImg?imgId=' + productRegisterReq.list[x].imgId + '"></td>' +
						'<td class="category">' + productRegisterReq.list[x].category +'</td>' +
						'<td class="productName">' + productRegisterReq.list[x].productName + '</td>' + 
						'<td class="productPrice">' + productRegisterReq.list[x].productPrice +'</td>' +
						'<td class="memberId">' + productRegisterReq.list[x].memberId + '</td>' + 
						'<td class="productDateCreated">' + toDate(productRegisterReq.list[x].productDateCreated) + '</td>' +
						'</tr>';
					}
					tagProdutRegisterReqList.html(form);
				} // end success
			}); // end ajax
			
		} // end showProductRegisterRequest
		
		function showProductDeleteRequest(){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../seller/productDeleteReq',
				success : function(result){
					console.log(result);
					productDeleteReq.list = result.list;
					productDeleteReq.pageMaker = result.pageMaker;
					
					for(x in productDeleteReq.list){
						form += '<tr>' + 
						'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
						'<td><img src="../product/showImg?imgId=' + productDeleteReq.list[x].imgId + '"></td>' +
						'<td class="category">' + productDeleteReq.list[x].category +'</td>' +
						'<td class="productName">' + productDeleteReq.list[x].productName + '</td>' + 
						'<td class="productPrice">' + productDeleteReq.list[x].productPrice +'</td>' +
						'<td class="memberId">' + productDeleteReq.list[x].memberId + '</td>' + 
						'<td class="productDateCreated">' + toDate(productDeleteReq.list[x].productDateCreated) + '</td>' +
						'</tr>';
					}
					tagProdutDeleteReqList.html(form);
				} // end success
			}); // end ajax
			
		} // end showProductRegisterRequest
		
		
		function toDetails(input) {
			const targetIndex = parseInt($(input).find('.targetIndex').text());
			const selectedMemberId = sellerReqList[targetIndex].memberId;
			
			// memberId의 request 페이지 요청
			
		} // end toDetails
		
		function toDate(timestamp){
			let date = new Date(timestamp);
			let formatted = (date.getYear() + 1900) + '/' + (date.getMonth() + 1) + '/' + date.getDate() + ' ' + 
					date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
			return formatted;
		} // end toDate
		
	</script>
	
</body>
</html>