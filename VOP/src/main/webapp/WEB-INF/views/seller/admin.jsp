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
	width: 1000px;
}


.request_container {
	height: 400px;
}

.page_list {
	display: flex;
	flex-direction: row;
	list-style: none;
	
}

.requestList {
	height: 250px;
}
.requestList tr {
	height: 50px;
}
.requestList td {
	width: 200px;
}
</style>
<title>관리자 페이지</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>

<body>
	<div>
		<h2>관리자 페이지</h2>
	</div>
	<div>
		<!-- <input type="button" value="공지사항 등록" onclick="popupRegisterNotice()"> -->
		<a href="../popupAds/main">팝업 광고 관리</a>
		<input type="button" value="전체 유저에게 알림 띄우기" onclick="sendInstanceMsg()">
		<input type="button" value="전체 유저에게 ALERT 띄우기" onclick="sendAlert()">
		<a href="../coupon/main">쿠폰 관리</a>
	</div>
	<div>
		<div class="request_container">
			<h3>사업자 등록 요청</h3>
			<table class="search_table">
				<tbody id="seller_req_list" class="requestList">
				</tbody>
				<tfoot id="seller_req_list_page"></tfoot>
			</table>
		</div>
		<div class="request_container">
			<h3>등록된 사업자 조회</h3>
			<table class="search_table">
				<tbody id="seller_approved_list" class="requestList">
				</tbody>
				<tfoot id="seller_approved_list_page"></tfoot>
			</table>
		</div>
		<div class="request_container">
			<h3>상품 등록 요청</h3>
			<table class="search_table">
				<tbody id="product_register_req_list" class="requestList">
				</tbody>
				<tfoot id="product_register_req_list_page"></tfoot>
			</table>
		</div>
		<div class="request_container">
			<h3>상품 삭제 요청</h3>
			<table class="search_table">
				<tbody id="product_delete_req_list" class="requestList">
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
		let tagSellerReqList = $('#seller_req_list');
		let tagSellerApprovedList = $('#seller_approved_list');
		let tagProdutRegisterReqList = $('#product_register_req_list');
		let tagProductDeleteReqList = $('#product_delete_req_list');
		let listMap = {};
		listMap.sellerReq = {};
		listMap.sellerApproved = {};
		listMap.productRegisterReq = {};
		listMap.productDeleteReq = {};
		
		$(document).ready(function(){
			for(key in listMap){
				listMap[key].show(1);
			}
		}); // end document.ready
		
		listMap.sellerReq.show = function (page){
			let form = '';
			$.ajax({
				method : 'GET',
				url : 'wait?pageNum=' + page,
				success : function(result){
					console.log(result);
					listMap.sellerReq.list = result.list;
					listMap.sellerReq.pageMaker = result.pageMaker;
					const list = listMap.sellerReq.list;
					
					for(x in list){
						form += '<tr onclick="popupDetails(this)">' + 
							'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
							'<td class="memberId">' + list[x].memberId + '</td>' + 
							'<td class="businessName">' + list[x].businessName +'</td>' +
							'<td class="requestTime">' + toDate(list[x].requestTime) +'</td>' +
							'<td class="requestState">' + list[x].requestState +'</td>' +
							'</tr>';
					}
					$('#seller_req_list_page').html(makePageForm(listMap.sellerReq));
					
					tagSellerReqList.html(form);
				} // end success
			}); // end ajax
			
		} // end showSellerRequest
		
		listMap.sellerApproved.show = function (page){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : 'approved?pageNum=' + page,
				success : function(result){
					console.log(result);
					listMap.sellerApproved.list = result.list;
					listMap.sellerApproved.pageMaker = result.pageMaker;
					const list = listMap.sellerApproved.list;
					
					for(x in list){
						form += '<tr onclick="popupDetails(this)">' + 
							'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
							'<td class="memberId">' + list[x].memberId + '</td>' + 
							'<td class="businessName">' + list[x].businessName +'</td>' +
							'<td class="requestTime">' + toDate(list[x].requestTime) +'</td>' +
							'</tr>';
					}
					
					$('#seller_approved_list_page').html(makePageForm(listMap.sellerApproved));
					
					tagSellerApprovedList.html(form);
				} // end success
			}); // end ajax
			
		} // end showSellerApproved
		
		listMap.productRegisterReq.show = function (page){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../product/registerRequest?pageNum=' + page,
				success : function(result){
					console.log(result);
					listMap.productRegisterReq.list = result.list;
					listMap.productRegisterReq.pageMaker = result.pageMaker;
					const list = listMap.productRegisterReq.list;
					
					for(x in list){
						form += '<tr onclick="popupDetails(this)">' + 
						'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
						'<td><img alt="' + list[x].imgId + '"></td>' +
						'<td class="category">' + list[x].category +'</td>' +
						'<td class="productName">' + list[x].productName + '</td>' + 
						'<td class="productPrice">' + list[x].productPrice +'</td>' +
						'<td class="memberId">' + list[x].memberId + '</td>' + 
						'<td class="productDateCreated">' + toDate(list[x].productDateCreated) + '</td>' +
						'</tr>';
					}
					$('#product_register_req_list_page').html(makePageForm(listMap.productRegisterReq));
					
					tagProdutRegisterReqList.html(form);
					loadImg(tagProdutRegisterReqList);
				} // end success
			}); // end ajax
			
		} // end showProductRegisterRequest
		
		listMap.productDeleteReq.show = function (page){
			let form = '';
			
			$.ajax({
				method : 'GET',
				url : '../product/deleteRequest?pageNum=' + page,
				success : function(result){
					console.log(result);
					listMap.productDeleteReq.list = result.list;
					listMap.productDeleteReq.pageMaker = result.pageMaker;
					const list = listMap.productDeleteReq.list;
					
					for(x in list){
						form += '<tr onclick="popupDetails(this)">' + 
						'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
						'<td><img alt="' + list[x].imgId + '"></td>' +
						'<td class="category">' + list[x].category +'</td>' +
						'<td class="productName">' + list[x].productName + '</td>' + 
						'<td class="productPrice">' + list[x].productPrice +'</td>' +
						'<td class="memberId">' + list[x].memberId + '</td>' + 
						'<td class="productDateCreated">' + toDate(list[x].productDateCreated) + '</td>' +
						'</tr>';
					}
					$('#product_delete_req_list_page').html(makePageForm(listMap.productDeleteReq));
					
					tagProductDeleteReqList.html(form);
					loadImg(tagProductDeleteReqList);
				} // end success
			}); // end ajax
			
		} // end showProductRegisterRequest
		
		function toDate(timestamp){
			let date = new Date(timestamp);
			let formatted = (date.getYear() + 1900) + '/' + (date.getMonth() + 1) + '/' + date.getDate() + ' ' + 
					date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
			return formatted;
		} // end toDate
		
		function makePageForm(board){
			const pageMaker = board.pageMaker;
			const startNum = pageMaker.startNum;
			const endNum = pageMaker.endNum;
			
			let pageForm = $('<ul class="page_list"></ul>');
			let numForm;
			if(pageMaker.prev){
				numForm = $('<li>이전&nbsp&nbsp</li>').click(function() {
					board.show(startNum -1);
				});
				pageForm.append(numForm);
			}
			for(let x = startNum; x <= endNum; x++){
				numForm = $('<li>' + x + '&nbsp&nbsp</li>').click(function(){
					board.show(x);
				});
				pageForm.append(numForm);
			}
			if(pageMaker.next){
				numForm = $('<li>다음</li>').click(function(){
					board.show(endNum +1);
				});
				pageForm.append(numForm);
			}
			return pageForm;
		} // end makePageForm
		
		function popupDetails(input){
			// 클릭한 요소의 부모 요소 id를 찾아서, 클릭한 요소가 어떤 목록에 속해있는지 확인
			const targetIndex = $(input).find('.targetIndex').text();
			const type = $(input).parents('tbody').prop('id');
			let targetUrl;
			let key;
			console.log(type);
			
			switch (type){
			case 'seller_req_list' : // 판매자 등록 요청 목록에 속해있는 경우
				targetUrl = 'popupSellerDetails?memberId=' + listMap.sellerReq.list[targetIndex].memberId;
				key = 'sellerReq';
				break;
			case 'seller_approved_list' : // 승인된 판매자 목록에 속해있는 경우
				targetUrl = 'popupSellerDetails?memberId=' + listMap.sellerApproved.list[targetIndex].memberId;
				key = 'sellerApproved';
				break;
			case 'product_register_req_list' : // 상품 등록 요청 목록에 속해있는 경우
				targetUrl = '../product/popupDetails?productId=' + listMap.productRegisterReq.list[targetIndex].productId;
				key = 'productRegisterReq';
				break;
			case 'product_delete_req_list' : // 상품 삭제 요청 목록에 속해있는 경우
				targetUrl = '../product/popupDetails?productId=' + listMap.productDeleteReq.list[targetIndex].productId;
				key = 'productDeleteReq';
				break;
			default : return;
			}
			
			const popupStat = {
					'url' : targetUrl,
					'name' : 'popupDetails',
					'option' : 'width=500, height=600, top=50, left=400'
			};
			
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
				listMap[key].show(listMap[key].pageMaker.pagination.pageNum); // 팝업에 해당하는 목록만 새로고침
			} // end popup.onbeforeunload
		} // end popupSellerRegister
		
		/* function popupRegisterNotice(){
			const popupStat = {
					'url' : 'popupRegisterNotice',
					'name' : 'popupRegisterNotice',
					'option' : 'width=500, height=600, top=50, left=400'
			};
			
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
			} // end popup.onbeforeunload
		}
		 */
		
		function loadImg(input){
			$(input).find('img').each(function(){
				let target = $(this);
				let imgId = target.attr("alt");
				$.ajax({
					method : 'GET',
					url : '../image/' + imgId,
					success : function(result){
						target.attr('src', result);
					}
				}); // end ajax
			});
		} // end loadImg
		
	</script>
	
</body>
</html>