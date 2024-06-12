<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- jquery 라이브러리 import -->
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
			showPopupAds();
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
		
		/* function readBlockList(){
			// 쿠키 디코딩
			if(document.cookie != ''){
				let decoded = decodeURIComponent(document.cookie);
				let splitList = decoded.split(new RegExp('=|;'));
				for(let x = 0; x < splitList.length; x++){
					if(splitList[x] == 'blockPopup'){
						blockList = JSON.parse(splitList[x+1]);
					}
				}
			}
		} // end readBlockList */
		
		function showPopupAds(){
			$.ajax({
				method : 'GET',
				url : '../popupAds/myPopupAds',
				success : function(result){
					console.log(result);
					for(x in result){
						showPopup(result[x]);
					}
				}
			}); // end ajax
		} // end showPopupAds
		
		function showPopup(messageId){
			let targetUrl = '../popupAds/popupAds?messageId=' + messageId;
			
			const popupStat = {
					url : targetUrl,
					name : 'popupAds' + messageId,
					option : 'width=500, height=600, top=50, left=400'
			};
			
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			if(popup == null){
				alert('팝업을 허용해주세요.  크롬 설정->개인정보보호 및 보안->사이트설정->팝업 및 리디렉션에서 설정 가능합니다.');
				return;
			}
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
			} // end popup.onbeforeunload
		} // end showPopup
		
		
	</script>

</body>
</html>