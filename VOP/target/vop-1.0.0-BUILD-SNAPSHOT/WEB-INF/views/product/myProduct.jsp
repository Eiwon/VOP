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

.page_list {
	display: flex;
	flex-direction: row;
	list-style: none;
}
.product_table {
	display: flex;
	flex-direction: row;
}
.thumbnail {
	width: 200px;
}
.category {
	width : 200px;
}
.productName {
	width: 200px;
}
.productPrice {
	width: 100px;
}
.productRemains {
	width: 100px;
}
.productState {
	width: 100px;
}

</style>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>등록한 상품 조회</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<div>
		<strong>${memberDetails.getUsername() } 님이 등록한 상품</strong>
	</div>
	<div>
		<div class="product_table">
			<div class="thumbnail">썸네일</div>
			<div class="category">분류</div>
			<div class="productName">상품명</div>
			<div class="productPrice">가격</div>
			<div class="productRemains">재고</div>
			<div class="productState">상태</div>
		</div>
		<div id="product_list">
		
		</div>
		<div id="product_list_page"></div>
	</div>
	<!-- <table>
		<thead>
			<tr>
				<th>썸네일</th>
				<th>분류</th>
				<th>상품명</th>
				<th>가격</th>
				<th>재고</th>
			</tr>
		</thead>
		<tbody id="product_list"></tbody>
		<tfoot id="product_list_page"></tfoot>
	</table> -->

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
						form += '<div class="product_table">' +
								'<div class="productRow product_table" onclick="popupUpdate(this)">' + 
								'<div class="targetIndex" hidden="hidden">'+ x + '</div>' +
								'<div class="thumbnail"><img src="' + pagingListDTO.list[x].imgUrl + '"></div>' +
								'<div class="category">' + productVO.category + '</div>' + 
								'<div class="productName">' + productVO.productName + '</div>' + 
								'<div class="productPrice">' + productVO.productPrice + '원</div>' + 
								'<div class="productRemains">' + productVO.productRemains + '</div>' + 
								'<div class="productState">' + productVO.productState + '</div>' + 
			            		'</div>' + 
								'<button style="height:50px;" onclick="toInquiry(' + productVO.productId + ')">문의 목록 가기</button>' +
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

			let pageForm = $('<ul class="page_list"></ul>');
			let numForm;
			if (pageMaker.prev) {
				numForm = $('<li>이전&nbsp&nbsp</li>').click(function() {
					pagingListDTO.show(startNum - 1);
				});
				pageForm.append(numForm);
			}
			for (let x = startNum; x <= endNum; x++) {
				numForm = $('<li>' + x + '&nbsp&nbsp</li>').click(function() {
					pagingListDTO.show(x);
				});
				pageForm.append(numForm);
			}
			if (pageMaker.next) {
				numForm = $('<li>다음</li>').click(function() {
					pagingListDTO.show(endNum + 1);
				});
				pageForm.append(numForm);
			}
			return pageForm;
		} // end makePageForm
		
		function popupUpdate(input){
			// 클릭한 항목의 index 검색
			const targetIndex = $(input).find('.targetIndex').text();
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