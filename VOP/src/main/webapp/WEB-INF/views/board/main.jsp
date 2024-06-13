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
	#myform fieldset {
	display: inline-block;
	direction: ltr; /* 텍스트 방향을 오른쪽에서 왼쪽으로 설정 */
	border: 0;
}

/* 별표시 스타일 */
#myform label {
	font-size: 1em;
	color: transparent;
	text-shadow: 0 0 0 #f0f0f0;
	margin-right: 10px; /* 별 사이의 간격 설정 */
}

.reviewStars {
	color: #f0d000; /* 별 색상 */
}

/* end 리뷰 별 폼 스타일 */
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
			printNotice();
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
								// 별점 숫자를 가져와서 별 모양으로 변환
				                let starsHTML = ''; // 별 모양 HTML을 저장할 변수
				                let reviewStar = parseInt(selected.reviewAvg); // 문자열을 정수로 변환
				                
				                for (let i = 1; i <= 5; i++) {
				                    if (i <= reviewStar) {
				                        starsHTML += '&#9733;'; // 별 모양 HTML 코드 추가
				                    } else {
				                        starsHTML += '&#9734;'; // 빈 별 모양 HTML 코드 추가
				                    }
				                }
								let tagImg = $('<img>');
								tagImg.attr('src', result);
								boxProduct.append(tagImg);
								boxProduct.append($('<br><strong class="product_name">' + selected.productName + '</strong><br>'));
								boxProduct.append($('<strong class="product_price">' + selected.productPrice + '</strong><br>'));
								boxProduct.append($('<span class="reviewStars">' + starsHTML + '</span>'));
								boxProduct.append($('<span class="review_num">(' + selected.reviewNum + ')</span>'));
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
									// 별점 숫자를 가져와서 별 모양으로 변환
					                let starsHTML = ''; // 별 모양 HTML을 저장할 변수
					                let reviewStar = parseInt(selected.reviewAvg); // 문자열을 정수로 변환
					                
					                for (let i = 1; i <= 5; i++) {
					                    if (i <= reviewStar) {
					                        starsHTML += '&#9733;'; // 별 모양 HTML 코드 추가
					                    } else {
					                        starsHTML += '&#9734;'; // 빈 별 모양 HTML 코드 추가
					                    }
					                }
									let tagImg = $('<img>');
									tagImg.attr('src', result);
									boxProduct.append(tagImg);
									boxProduct.append($('<br><strong class="product_name">' + selected.productName + '</strong><br>'));
									boxProduct.append($('<strong class="product_price">' + selected.productPrice + '</strong><br>'));
									boxProduct.append($('<span class="reviewStars">' + starsHTML + '</span>'));
									boxProduct.append($('<span class="review_num">(' + selected.reviewNum + ')</span>'));
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
		
		function printNotice(){
			let blockList = [];
			// 쿠키 디코딩
			if(document.cookie != ''){
				let decoded = decodeURIComponent(document.cookie);
				let splitList = decoded.split(new RegExp('=|;'));
				console.log(splitList);
				for(let x = 0; x < splitList.length; x++){
					if(splitList[x] == 'blockPopup'){
						blockList = JSON.parse(splitList[x+1]);
						console.log('차단된 팝업 id : ' + blockList);
					}
				}
			}
			
			$.ajax({
				method : 'GET',
				url : 'notice',
				success : function(result){
					let noticeList = result;
					console.log('팝업 광고 : ' + result);
					for(x in noticeList){
						if(!blockList.includes(noticeList[x].messageId)){ // 차단 목록에 등록되지 않은 메시지만 출력
							showPopup(noticeList[x].messageId);
						}
					}
				} // end success
			}); // end ajax
		} // end printNotice
		
		function showPopup(messageId){
			let targetUrl = 'popupNotice?messageId=' + messageId;
			
			const popupStat = {
					url : targetUrl,
					name : 'popupNotice' + messageId,
					option : 'width=500, height=600, top=50, left=400'
			};
			console.log(popupStat.url);
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
		} // end showSocketPopup
	</script>

</body>
</html>