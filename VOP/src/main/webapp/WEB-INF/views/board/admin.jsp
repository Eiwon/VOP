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
.request_container {
	height: 400px;
}

.page_list {
	display: flex;
	flex-direction: row;
	list-style: none;
	
}

tbody {
	height: 250px;
}
tr {
	height: 50px;
}

</style>
<title>관리자 페이지</title>
</head>
<body>
	<div>
		<h2>관리자 페이지</h2>
	</div>
	
	<div>
		<div class="request_container">
			<h3>사업자 등록 요청</h3>
			<table class="search_table">
				<tbody id="seller_req_list">
				</tbody>
				<tfoot id="seller_req_list_page"></tfoot>
			</table>
		</div>
		<div class="request_container">
			<h3>등록된 사업자 조회</h3>
			<table class="search_table">
				<tbody id="seller_approved_list">
				</tbody>
				<tfoot id="seller_approved_list_page"></tfoot>
			</table>
		</div>
		<div class="request_container">
			<h3>상품 등록 요청</h3>
			<table class="search_table">
				<tbody id="product_register_req_list">
				</tbody>
				<tfoot id="product_register_req_list_page"></tfoot>
			</table>
		</div>
		<div class="request_container">
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
			
			showSellerRequest(1);
			showSellerApproved(1);
			showProductRegisterRequest(1);
			showProductDeleteRequest(1);
			
		}); // end document.ready
		
		
		
		function showSellerRequest(page){
			let form = '';
			let pageForm = '<ul>';
			$.ajax({
				method : 'GET',
				url : '../seller/wait?pageNum=' + page,
				success : function(result){
					console.log(result);
					sellerReq.list = result.list;
					
					for(x in sellerReq.list){
						form += '<tr onclick="toDetails(this)">' + 
							'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
							'<td class="memberId">' + sellerReq.list[x].memberId + '</td>' + 
							'<td class="businessName">' + sellerReq.list[x].businessName +'</td>' +
							'<td class="requestTime">' + toDate(sellerReq.list[x].requestTime) +'</td>' +
							'<td class="requestState">' + sellerReq.list[x].requestState +'</td>' +
							'</tr>';
					}
					$('#seller_req_list_page').html(makePageForm(result.pageMaker, 'showSellerRequest'));
					
					tagSellerReqList.html(form);
				} // end success
			}); // end ajax
			
		} // end showSellerRequest
		
		function showSellerApproved(page){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../seller/approved',
				success : function(result){
					console.log(result);
					sellerApproved.list = result.list;
					
					for(x in sellerApproved.list){
						form += '<tr">' + 
							'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
							'<td class="memberId">' + sellerApproved.list[x].memberId + '</td>' + 
							'<td class="businessName">' + sellerApproved.list[x].businessName +'</td>' +
							'<td class="requestTime">' + toDate(sellerApproved.list[x].requestTime) +'</td>' +
							'</tr>';
					}
					
					$('#seller_approved_list_page').html(makePageForm(result.pageMaker, 'showSellerApproved'));
					
					tagSellerApprovedList.html(form);
				} // end success
			}); // end ajax
			
		} // end showSellerApproved
		
		function showProductRegisterRequest(page){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../seller/productReq',
				success : function(result){
					console.log(result);
					productRegisterReq.list = result.list;
					
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
					$('#product_register_req_list_page').html(makePageForm(result.pageMaker, 'showProductRegisterRequest'));
					
					tagProdutRegisterReqList.html(form);
				} // end success
			}); // end ajax
			
		} // end showProductRegisterRequest
		
		function showProductDeleteRequest(page){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../seller/productDeleteReq',
				success : function(result){
					console.log(result);
					productDeleteReq.list = result.list;
					
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
					$('#product_delete_req_list_page').html(makePageForm(result.pageMaker, 'showProductDeleteRequest'));
					
					tagProductDeleteReqList.html(form);
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
		
		function makePageForm(pageMaker, listenerName){
			const startNum = pageMaker.startNum;
			const endNum = pageMaker.endNum;
			console.log(startNum + ', ' + endNum);
			
			let pageForm = '<ul class="page_list">';
			if(pageMaker.prev){
				pageForm += '<li onclick="' + listenerName + '(' + (startNum - 1) + ')">이전</li>&nbsp&nbsp';
			}
			
			for(let x = startNum; x <= endNum; x++){
				pageForm += '<li onclick="' + listenerName + '(' + x + ')">' + x + '</li>&nbsp&nbsp';
			}
			
			if(pageMaker.next){
				pageForm += '<li onclick="' + listenerName + '(' + (endNum + 1) + ')">다음</li>';
			}
			pageForm += '</ul>';
			return pageForm;
		} // end makePageForm
		
		
	</script>
	
</body>
</html>