<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js">
</script>
<jsp:include page="../include/header.jsp"></jsp:include>
<title>VOP</title>
	<style>
    </style>
</head>
<body>

<div id="recommend_container">
	<div>
		<h2>최근 등록된 상품</h2>
		<ul class="flex_list" id="recent_list">
	
		</ul>
	</div>
</div>
	<script type="text/javascript">
	
		let listRecent = $('#recent_list');
		let containerRecommend = $('#recommend_container');
		
		const constCategory = ["여성패션", "남성패션", "남녀 공용 의류", "유아동 패션", "뷰티", "출산/유아동", 
			"식품", "주방용품", "생활용품", "홈인테리어", "가전디지털", "스포츠/레저", "자동차 용품", "도서/음반/DVD", 
			"완구/취미", "문구/오피스", "반려동물용품", "헬스/건강식품"];
        
		$(document).ready(function(){
			printRecentList();
			printRecommendByCategory();
		}); // end document.ready
		
		
		function printRecentList(){
			$.ajax({
				method : 'GET',
				url : '../product/recent',
				success : function(result){ // result = 최근 등록된 5개 ProductVO
					console.log(result);
					let productList = result;
					for(x in productList){ 
						let boxProduct = $('<li class="product_box" onclick="toDetails(this)"></li>');
						let selected = productList[x];
						$.ajax({
							method : 'GET',
							url : '../image/' + selected.imgId,
							success : function(result){
								let tagImg = $('<img>');
								tagImg.attr('src', result);
								boxProduct.append(tagImg);
								boxProduct.append($('<br><strong class="product_name">' + selected.productName + '</strong><br>'));
								boxProduct.append($('<strong class="product_price">' + selected.productPrice + '</strong><br>'));
								boxProduct.append($('<span class="review_num">' + selected.reviewNum + '</span>'));
								boxProduct.append($('<input hidden="hidden" class="product_id" value="' + selected.productId + '"/>'));
							}
						}); // end ajax
						listRecent.append(boxProduct);
					}
				} // end success
			}); // end ajax
		
		} // end printRecentList
		
		function printRecommendByCategory(){ // 카테고리 별 최고 리뷰 상품 5개씩 출력
			$.ajax({
				method : 'GET',
				url : '../product/bestReview',
				success : function(result){ // result : key=카테고리명, value=해당 카테고리의 최고리뷰 상품 List<ProductVO>
					let productList = result;
					console.log(result);
					for(x in constCategory){ 
						const selectedCategory = constCategory[x];
						let listByCategory = $('<div><h2>' + selectedCategory + '</h2></div>'); 
						let list = $('<ul class="flex_list"></ul>');
						for(i in productList[selectedCategory]){
							const selected = productList[selectedCategory][i];
							let boxProduct = $('<li class="product_box" onclick="toDetails(this)"></li>');
							$.ajax({
								method : 'GET',
								url : '../image/' + selected.imgId,
								success : function(result){
									let tagImg = $('<img>');
									tagImg.attr('src', result);
									boxProduct.append(tagImg);
									boxProduct.append($('<br><strong class="product_name">' + selected.productName + '</strong><br>'));
									boxProduct.append($('<strong class="product_price">' + selected.productPrice + '</strong><br>'));
									boxProduct.append($('<span class="review_num">' + selected.reviewNum + '</span>'));
									boxProduct.append($('<input hidden="hidden" class="product_id" value="' + selected.productId + '"/>'));
								}
							}); // end ajax
							
							list.append(boxProduct);
						} 
						listByCategory.append(list);
						containerRecommend.append(listByCategory);
					}
					
				} // end success
			}); // end ajax
		} // end printRecentList
		
		
		// 상품 클릭 했을때 이동하는 코드
		function toDetails(input){
			const selectedId = $(input).find('.product_id').val();
			console.log(selectedId);
			location.href = '../product/detail?productId=' + selectedId;
		} // end addDetailsEvent
		
	</script>
	

</body>
</html>