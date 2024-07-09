<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style type="text/css">

.body_container{
	width: 65%;
	margin: auto;
}
.product_table{
	margin-right: 5px;
	margin-bottom: 5px;
}
</style>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>등록한 상품 조회</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<div class="body_container">
		<div>
			<strong>${memberDetails.getUsername() } 님이 등록한 상품</strong>
		</div>
		<div id="product_list" class="row row-cols-5"></div>
		<div id="product_list_page"></div>
	</div>
	
	<script type="text/javascript">
		let pagingListDTO = {}; // 상품 목록과 페이지 정보를 저장할 객체 선언
		
		$(document).ready(function(){
			pagingListDTO.show(); // 상품 목록 출력
		}); // end document.ready
		
		
		pagingListDTO.show = function(page) {
	
			let form = '';
			if(page == undefined){
				page = 1;
			}
			$.ajax({
				method : 'GET',
				url : 'myList?pageNum=' + page,
				success : function(result) {
					console.log(result);
					pagingListDTO.list = result.list; // 가져온 데이터를 저장
					pagingListDTO.pageMaker = result.pageMaker;
					
					for (x in pagingListDTO.list) {
						const productVO = pagingListDTO.list[x].productVO;
						form += '<div class="product_table card">' +
									'<div class="card-header">' + productVO.productState + '</div>' + 
									'<img class="card-img-top" src="' + pagingListDTO.list[x].imgUrl + '">' +
									'<div class="card-body">' + 
										'<h4 class="productName card-title">' + productVO.productName + '</h4>' + 
										'<h6 class="category card-subtitle mb-2 text-body-secondary">' + productVO.category + '</h6>' + 
										'<div class="productPrice card-text"> 판매가 : ' + productVO.productPrice + '원</div>' + 
										'<div class="productRemains card-text">남은 수량 : ' + productVO.productRemains + '개</div>' + 
										'<div class="targetIndex" hidden="hidden">'+ x + '</div>' +
			            			'</div>' + 
			            			'<a class="btn btn-primary" style="margin-bottom: 5px;" onclick="popupUpdate(this)">상세 정보</a>' +
									'<a class="btn btn-primary" onclick="toInquiry(' + productVO.productId + ')">문의 목록</a>' +
					    		'</div>';
					}
					// 페이지 생성 후 등록
					$('#product_list_page').html(makePageForm(pagingListDTO));
					$('#product_list').html(form);
					
				} // end success
			}); // end ajax

		} // end showProductList

		function toInquiry(productId) {
			location.href='../inquiry/list?productId=' + productId;
		} // end toInquiry
		
		function makePageForm(pagingListDTO) { // 페이지 버튼 생성 후, pagingListDTO의 리스트 출력 함수 등록
			const pageMaker = pagingListDTO.pageMaker;
			const startNum = pageMaker.startNum;
			const endNum = pageMaker.endNum;
			
			let pageForm = $('<ul class="pagination pagination-lg justify-content-center"></ul>');
			let numForm;
			
			numForm = $('<li class="page-item"><a class="page-link">&laquo;</a></li>');
			if(pageMaker.prev){
				numForm.click(function() {
					pagingListDTO.show(startNum - 1);
				});
			}else {
				numForm.addClass('disabled');
			}
			pageForm.append(numForm);
			
			for (let x = startNum; x <= endNum; x++) {
				numForm = $('<li class="page-item"><a class="page-link">' + x + '</a></li>');
				if(pageMaker.curPage == x){
					numForm.addClass('active');
				}else{
					numForm.click(function() {
						pagingListDTO.show(x);
					});
				}
				pageForm.append(numForm);
			}
			
			numForm = $('<li class="page-item"><a class="page-link">&raquo;</a></li>');
			if (pageMaker.next) {
				numForm.click(function() {
					pagingListDTO.show(endNum + 1);
				});
			}else {
				numForm.addClass('disabled');
			}
			pageForm.append(numForm);
			
			return pageForm;
		} // end makePageForm
		
		function popupUpdate(input){
			// 클릭한 항목의 index 검색
			const targetIndex = $(input).parents('.product_table').find('.targetIndex').text();
			let targetUrl = 'popupUpdate?productId=' + pagingListDTO.list[targetIndex].productVO.productId;
			
			// 팝업창 정보 설정
			const popupStat = {
					'url' : targetUrl, // 서버에 다른 페이지로 이동을 요청하는 url
					'name' : 'popupUpdate',
					'option' : 'width=700, height=1000, top=50, left=400' // 팝업창 설정
			};
			
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행 (닫기 버튼으로 닫는 경우 제외)
				console.log("팝업 닫힘");
				pagingListDTO.show(pagingListDTO.pageMaker.pagination.pageNum); // 목록 새로고침
			} // end popup.onbeforeunload
		} // end popupUpdate
		
	</script>

</body>
</html>