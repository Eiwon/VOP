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

.body_container {
	width: 60%;
	margin: auto;
}

.product_row {
	padding-bottom: 30px;
}
.product_list {
        	display: flex;
    		flex-direction: row;
        }
/* end 리뷰 별 폼 스타일 */
    </style>
</head>
<body>
<div class="body_container">
	<div class="product_row">
		<h2>최근 등록된 상품</h2>
		<div class="product_list" id="recent_list">
	
		</div>
	</div>
	<div>
		<h2>추천 상품</h2>
		<div id="recommend_container"></div>
	</div>
</div>
	<script type="text/javascript">
	
		let listRecent = $('#recent_list');
		let containerRecommend = $('#recommend_container');
		
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
					let form = '';
					let productVO;
					for(x in result){ 
						productVO = result[x].productVO;
						// 별표시 코드
						let starsHTML = ''; // 별 모양 HTML을 저장할 변수
		                let reviewStar = parseInt(productVO.reviewAvg); // 문자열을 정수로 변환
						for (let i = 1; i <= 5; i++) {
		                    if (i <= reviewStar) {
		                        starsHTML += '&#9733;'; // 별 모양 HTML 코드 추가
		                    } else {
		                        starsHTML += '&#9734;'; // 빈 별 모양 HTML 코드 추가
		                    }
		                }
						form += '<div class="card" style="width: 20rem;" onclick="toDetails(this)">' + 
							'<img class="card-img-top" src="' + result[x].imgUrl + '">' + 
							'<div class="card-body">' + 
							'<h3 class="card-title">' + productVO.productName + '</h3>' +
							'<p class="card-text">' + productVO.productPrice + '원</p>' + 
							'<span class="reviewStars card-text">' + starsHTML + '</span>' +
							'<span class="review_num card-text">(' + productVO.reviewNum + ')</span>' + 
							'<input hidden="hidden" class="product_id" value="' + productVO.productId + '"/>' + 
							'</div></div>';
					}
					listRecent.html(form);
				} // end success
			}); // end ajax
		
		} // end printRecentList
		
		function printRecommendByCategory(){ // 카테고리 별 최고 리뷰 상품 5개씩 출력
			$.ajax({
				method : 'GET',
				url : '../product/bestReview',
				success : function(result){ // result : key=카테고리명, value=해당 카테고리의 최고리뷰 상품 List<ProductPreviewDTO>
					console.log(result);
					for (x in result){
						let form = '<div class="product_row"><h2>' + x + '</h2>' + 
								   '<div class="product_list">';
						for(i in result[x]){
							let productVO = result[x][i].productVO;
							// 별표시 코드
							let starsHTML = ''; // 별 모양 HTML을 저장할 변수
			                let reviewStar = parseInt(productVO.reviewAvg); // 문자열을 정수로 변환
							for (let i = 1; i <= 5; i++) {
			                    if (i <= reviewStar) {
			                        starsHTML += '&#9733;'; // 별 모양 HTML 코드 추가
			                    } else {
			                        starsHTML += '&#9734;'; // 빈 별 모양 HTML 코드 추가
			                    }
			                }
							form += '<div class="card" style="width: 20rem;" onclick="toDetails(this)">' + 
									'<img class="card-img-top" src="' + result[x][i].imgUrl + '">' + 
									'<div class="card-body">' +
									'<h3 class="card-title">' + productVO.productName + '</h3>' + 
									'<p class="card-text">' + productVO.productPrice + '원</p>' +
									'<span class="reviewStars card-text">' + starsHTML + '</span>' +
									'<span class="review_num card-text">(' + productVO.reviewNum + ')</span>' +
									'<input hidden="hidden" class="product_id" value="' + productVO.productId + '"/>' +
									'</div></div>';
						}
						form += '</div></div>';
						containerRecommend.append(form);
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
		
		// 서버로부터 팝업 광고 불러오기
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
		
		// 팝업 광고 출력
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
		} // end showPopup
		
		
	</script>

</body>
</html>