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


.request_container {
	height: 500px;
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

.body_container{
	width: 65%;
	margin: auto;
}
.inner_header {
	margin: 40px;
}
.form_foot {
	display: flex;
	justify-content: center;
	margin-top: 4px;
}
</style>
<title>관리자 페이지</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>

<body>
<jsp:include page="../include/sideBar.jsp"/>
	<div class="body_container">
	<div class="inner_header">
		<h2>관리자 페이지</h2>
	</div>
	<div style="display: flex; justify-content: flex-end;">
		<div class="btn-group" role="group">
			<a href="../popupAds/main" class="btn btn-outline-primary">팝업 광고 관리</a>
			<a href="../coupon/main" class="btn btn-outline-primary">쿠폰 관리</a>
		</div>
	</div>
	<div>
		<div class="request_container table">
			<h3>사업자 등록 요청</h3>
			<table class="search_table">
				<thead>
    				<tr>
      				<th scope="col">아이디</th>
     				<th scope="col">사업자 명</th>
      				<th scope="col">신청 시간</th>
     			 	<th scope="col">상태</th>
    				</tr>
 				</thead>
				<tbody id="seller_req_list" class="requestList">
				</tbody>
			</table>
			<div class="form_foot" id="seller_req_list_page"></div>
		</div>
		<div class="request_container table">
			<h3>등록된 사업자 조회</h3>
			<table class="search_table">
				<thead>
    				<tr>
      				<th scope="col">아이디</th>
     				<th scope="col">사업자 명</th>
      				<th scope="col">신청 시간</th>
    				</tr>
 				</thead>
				<tbody id="seller_approved_list" class="requestList">
				</tbody>
			</table>
			<div class="form_foot" id="seller_approved_list_page"></div>
		</div>
		<div class="request_container table">
			<h3>상품 등록 요청</h3>
			<table class="search_table">
				<thead>
    				<tr>
      				<th scope="col">썸네일</th>
     				<th scope="col">분류</th>
      				<th scope="col">품명</th>
      				<th scope="col">가격</th>
      				<th scope="col">판매자</th>
      				<th scope="col">신청 시간</th>
    				</tr>
 				</thead>
				<tbody id="product_register_req_list" class="requestList">
				</tbody>
			</table>
			<div class="form_foot" id="product_register_req_list_page"></div>
		</div>
		<div class="request_container">
			<h3>상품 삭제 요청</h3>
			<table class="search_table table">
				<thead>
    				<tr>
      				<th scope="col">썸네일</th>
     				<th scope="col">분류</th>
      				<th scope="col">품명</th>
      				<th scope="col">가격</th>
      				<th scope="col">판매자</th>
      				<th scope="col">신청 시간</th>
    				</tr>
 				</thead>
				<tbody id="product_delete_req_list" class="requestList">
				</tbody>
			</table>
			<div class="form_foot" id="product_delete_req_list_page"></div>
		</div>
	</div>
	</div>
	
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
				listMap[key].show();
			}
		}); // end document.ready
		
		listMap.sellerReq.show = function (page){
			let form = '';
			if(page == undefined){
				page = 1;
			}
			
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
			if(page == undefined){
				page = 1;
			}
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
			if(page == undefined){
				page = 1;
			}
			$.ajax({
				method : 'GET',
				url : '../product/registerRequest?pageNum=' + page,
				success : function(result){
					console.log(result);
					listMap.productRegisterReq.list = result.list;
					listMap.productRegisterReq.pageMaker = result.pageMaker;
					const list = listMap.productRegisterReq.list;
					
					for(x in list){
						let productVO = list[x].productVO;
						form += '<tr onclick="popupDetails(this)">' + 
						'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
						'<td><img src="' + list[x].imgUrl + '"></td>' +
						'<td class="category">' + productVO.category +'</td>' +
						'<td class="productName">' + productVO.productName + '</td>' + 
						'<td class="productPrice">' + productVO.productPrice +'원</td>' +
						'<td class="memberId">' + productVO.memberId + '</td>' + 
						'<td class="productDateCreated">' + toDate(productVO.productDateCreated) + '</td>' +
						'</tr>';
					}
					$('#product_register_req_list_page').html(makePageForm(listMap.productRegisterReq));
					
					tagProdutRegisterReqList.html(form);
				} // end success
			}); // end ajax
			
		} // end showProductRegisterRequest
		
		listMap.productDeleteReq.show = function (page){
			let form = '';
			if(page == undefined){
				page = 1;
			}
			$.ajax({
				method : 'GET',
				url : '../product/deleteRequest?pageNum=' + page,
				success : function(result){
					console.log(result);
					listMap.productDeleteReq.list = result.list;
					listMap.productDeleteReq.pageMaker = result.pageMaker;
					const list = listMap.productDeleteReq.list;
					
					for(x in list){
						let productVO = list[x].productVO;
						form += '<tr onclick="popupDetails(this)">' + 
						'<td class="targetIndex" hidden="hidden">' + x + '</td>' +
						'<td><img src="' + list[x].imgUrl + '"></td>' +
						'<td class="category">' + productVO.category +'</td>' +
						'<td class="productName">' + productVO.productName + '</td>' + 
						'<td class="productPrice">' + productVO.productPrice +'원</td>' +
						'<td class="memberId">' + productVO.memberId + '</td>' + 
						'<td class="productDateCreated">' + toDate(productVO.productDateCreated) + '</td>' +
						'</tr>';
					}
					$('#product_delete_req_list_page').html(makePageForm(listMap.productDeleteReq));
					
					tagProductDeleteReqList.html(form);
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
			const curPage = pageMaker.curPage;
			
			let pageForm = $('<ul class="page_list pagination"></ul>');
			let numForm;
			
			numForm = $('<li class="page-item"><a class="page-link">&laquo;</a></li>');
			// 이전 페이지가 있으면 클릭 리스너 등록, 없으면 disabled
			if(pageMaker.prev){
				numForm.click(function() {
					board.show(startNum -1);
				});	
			}else {
				numForm.addClass('disabled');
			}
			pageForm.append(numForm);
			
			
			for(let x = startNum; x <= endNum; x++){
				numForm = $('<li class="page-item"><a class="page-link">' + x + '</a></li>').click(function(){
					board.show(x);
				});
				if(curPage == x) { // 현재 페이지 번호는 색 변경
					numForm.addClass('active');
				}
				pageForm.append(numForm);
			}
			
			numForm = $('<li class="page-item"><a class="page-link">&raquo;</a></li>');
			if(pageMaker.next){
				numForm.click(function(){
					board.show(endNum +1);
				});
			}else{
				numForm.addClass('disabled');
			}
			pageForm.append(numForm);
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
				targetUrl = '../product/popupDetails?productId=' + listMap.productRegisterReq.list[targetIndex].productVO.productId;
				key = 'productRegisterReq';
				break;
			case 'product_delete_req_list' : // 상품 삭제 요청 목록에 속해있는 경우
				targetUrl = '../product/popupDetails?productId=' + listMap.productDeleteReq.list[targetIndex].productVO.productId;
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
				if(type == 'seller_req_list'){
					listMap['sellerApproved'].show(listMap['sellerApproved'].pageMaker.pagination.pageNum);
				}
			} // end popup.onbeforeunload
		} // end popupSellerRegister
		
	</script>
	
</body>
</html>