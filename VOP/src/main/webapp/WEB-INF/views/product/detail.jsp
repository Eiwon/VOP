<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 세션 사용할수 있게하는 코드 -->
<%@ page import="javax.servlet.http.HttpSession"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal" />
</sec:authorize>

<!DOCTYPE html>
<html>
<head>
<!-- 한국어 인코딩 -->
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">

<!-- 모바일 관련 코드라서 없어도 동작 가능 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>

<!-- 카카오 공유 api 관련 코드 -->
<!-- 위에 있는 이유는 밑에 있는 코드들이 적용 되야해서 -->
<script src="https://t1.kakaocdn.net/kakao_js_sdk/2.7.2/kakao.min.js"
	integrity="sha384-TiCUE00h649CAMonG018J2ujOgDKW/kVWlChEuu4jK2vxfAAD0eZxzCKakxg55G4"
	crossorigin="anonymous" type="text/javascript">
</script>

<!-- Kakao JavaScript SDK를 초기화하는 코드입니다. -->
<script>
Kakao.init('fc798d4c0b15af3cd0e864357925d0b3'); // 사용하려는 앱의 JavaScript 키 입력
</script>



<title>상품 상세 조회</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<style>
.product-details {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    font-family: Arial, sans-serif;
    line-height: 1.6;
}
.product-summary, .product-pricing, .product-stock, .product-actions, .product-description {
    border-bottom: 1px solid #ddd;
    margin-bottom: 20px;
    padding-bottom: 20px;
}
.product-summary {
    display: flex;
    gap: 20px;
}
.product-category, .product-info, .product-review {
    flex: 1;
}
.product-thumbnail img {
    max-width: 100%;
    height: auto;
    border: 1px solid #ddd;
    padding: 5px;
}
.product-info p, .product-review p, .product-pricing p, .product-stock p {
    margin: 10px 0;
}
.product-info span, .product-review span, .product-pricing span, .product-stock span {
    font-weight: bold;
}
.product-actions {
    display: flex;
    align-items: center;
    gap: 20px;
}
.product-actions button, .product-actions .basket-link, .product-actions form button {
    background-color: #ff6700;
    color: #fff;
    border: none;
    padding: 10px 20px;
    cursor: pointer;
    text-decoration: none;
}
.product-actions .basket-link {
    background-color: #00aaff;
}
.product-description img {
    max-width: 100%;
    height: auto;
    margin-bottom: 10px;
}
#quantity {
    width: 50px;
    padding: 5px;
}

/* 리뷰 별 폼 스타일 */
#myform fieldset {
    display: inline-block;
    border: 0;
    padding: 0;
    margin: 0;
}

/* 별표시 스타일 */
#myform label {
    font-size: 1em;
    color: transparent;
    text-shadow: 0 0 0 #f0d000; /* 색상을 별도로 지정 */
    margin-right: 10px; /* 별 사이의 간격 설정 */
}

.reviewStars {
    color: #f0d000; /* 별 색상 */
}

/* 오른쪽 정렬 코드 */
.right-align {
    display: flex;
    justify-content: flex-end;
}

/* 페이징 처리 숫자 스타일 */
.page_list {
    display: flex;
    flex-direction: row;
    list-style: none;
    padding: 0;
    margin: 0;
}

/* 테이블 스타일 */
tbody {
    height: 250px;
}
tr {
    height: 50px;
}
td {
    width: 200px;
}

/* 좋아요 or 싫어요 css 코드 */
.button-container {
    display: flex;
    gap: 20px;
}

.button {
    font-size: 24px;
    color: grey;
    cursor: pointer;
    transition: color 0.3s;
}

.button.liked {
    color: blue;
}

.button.disliked {
    color: red;
}

/* 리뷰 및 문의 섹션 스타일 */
.container {
    max-width: 1200px;
    margin: auto;
    padding: 20px;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
.section {
    margin-bottom: 40px;
    padding: 20px;
    background-color: #ffffff;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.section h3 {
    margin-bottom: 20px;
    font-size: 1.5em;
    text-align: center;
    color: #333;
}
.content-center {
    display: flex;
    justify-content: center;
}
.content-center div {
    width: 100%;
}
.review-table, .comment-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
}
.review-table th, .review-table td, .comment-table th, .comment-table td {
    border: 1px solid #e0e0e0;
    padding: 10px;
    text-align: left;
}
.pagination {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}
.pagination div {
    margin: 0 5px;
    padding: 10px 20px;
    background-color: #f0f0f0;
    border: 1px solid #ccc;
    border-radius: 4px;
    cursor: pointer;
}
.pagination div:hover {
    background-color: #e0e0e0;
}

</style>

</head>
<body>
	
<c:set var="productVO" value="${productDetails.productVO}"/>
<c:set var="memberVO" value="${productDetails.memberVO}"/>

<div class="product-details">
  <h2>상품 상세 페이지</h2>
    	<!-- 카카오 공유 아이콘 -->
		<!-- <div class="right-align"> -->
		<a id="kakaotalk-sharing-btn" href="javascript:;"> 
		<img src="https://developers.kakao.com/assets/img/about/logos/kakaotalksharing/kakaotalk_sharing_btn_medium.png" alt="카카오톡 공유 보내기 버튼"/>
		</a>
    <div class="product-summary">
    	
        <div class="product-category">
            <span>카테고리:</span> ${productVO.category}
        </div>
        <div class="product-thumbnail">
            <c:choose>
                <c:when test="${productDetails.thumbnailUrl != null}">
                    <img src="${productDetails.thumbnailUrl}" alt="상품 이미지">
                </c:when>
            </c:choose>
        </div>
        <div class="product-info">
            <p><span>상품 번호:</span> ${productVO.productId}</p>
            <p><span>상품 이름:</span> ${productVO.productName}</p>
        </div>
        <div class="product-review">
            <div id="myform">
                <fieldset id="starsFieldset">
                    <!-- 별이 여기에 동적으로 추가될 것입니다. -->
                </fieldset>
            </div>
            <p><span>리뷰 평균:</span> ${productVO.reviewAvg}</p>
            <p><span>리뷰 총 개수:</span> ${productVO.reviewNum}</p>
        </div>
    </div>
    <div class="product-pricing">
        <p><span>상품 가격:</span> <span id="totalPrice">${productVO.productPrice}</span></p>
        <input type="number" id="quantity" value="1" min="1" max="99" maxlength="2">
    </div>
    <div class="product-stock">
        <p><span>상품 현황:</span> ${productVO.productState}</p>
        <p><span>상품 재고:</span> ${productVO.productRemains}</p>
        <p><span>상품 보관 장소:</span> ${productVO.productPlace}</p>
        <p><span>판매자:</span> ${productVO.memberId}</p>
        <p><span>현재 구매 시 도착 일자:</span> <time id="current-date"></time></p>
    </div>
    <div class="product-actions">
        <button id="btnBasket">장바구니</button>
        <a href="../basket/main" class="basket-link">장바구니 바로가기</a>
        <form id="checkoutForm" action="../payment/checkout" method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="hidden" name="memberId" value="${memberDetails.getUsername()}"> 
            <input type="hidden" name="productIds" value="${productVO.productId}">
            <input type="hidden" name="productNums" value="1">
            <button type="submit">바로구매</button>
        </form>
    </div>
    <div class="product-description">
        <p>상품 상세 설명</p>
        <c:forEach var="imgUrl" items="${productDetails.detailsUrl}">
            <img src="${imgUrl}" alt="상세 설명 이미지">
        </c:forEach>
    </div>
</div>
	
	<!-- 댓글 화면 코드 및 가운데 정렬 -->
	 <div class="container">
        <div class="section">
            <h3>상품 리뷰</h3>
            <div class="content-center">
                <div id="review"></div>
            </div>
            <!-- 리뷰 페이징 처리 내용 -->
            <div class="pagination" id="product_list_page"></div>
        </div>
        
        <div class="section">
            <h3>상품 문의</h3>
            <div class="content-center">
                <div id="comments"></div>
            </div>
            <!-- 문의 페이징 처리 내용 -->
            <div class="pagination" id="comments_list_page"></div>
        </div>
    </div>

	<!-- <div>
		<h3>배송/교환/반품 안내</h3>
		<p>내용 작성 예정</p>
	</div> -->
	
	<!-- 좋아요 표시 제작 예정? -->
	<script type="text/javascript">
	
	 //const vopUrl = "http://3.36.204.47:8080/vop";
	 const vopUrl = "http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}";
	 // 현재 시험 용으로는 사용하면 안되는데 나중에 완성 되면는 사용해도 될것 같다. 우선는 주소는 하드 코딩으로 작성 하기
	 // ${pageContext.request.serverName} ip 주소 현재 pc ip 주소(로컬 호스트)
	 // ${pageContext.request.serverPort} 포트 번호 현재 pc 포트 번호
	 // ${pageContext.request.contextPath} 프로젝트 명 현재 pc의 서버 명
     
     let reviewMap = {};
     let inquiryMap = {};
     
 	 const memberId = '${memberDetails.getUsername()}';
 	 let productId = ${productVO.productId};
 	 let reviewAvg = parseInt("${productVO.reviewAvg}"); // 서버 사이드 값 사용
  	 // 썸네일 이미지 ID 변수 정의 (이 ID가 맞는지 확인해야 합니다)
     const thumbnailImgId = '${productVO.imgId}';
     
     // 현재 날짜 보여주는 코드 (현재 사용자의 pc 시간을 기반으로 하기 때문에 문제가 있음)
     function displayCurrentDate() {
    // 현재 시간을 가져오기 위해 Date 객체를 생성합니다.
    let now = new Date();
	
    // 현재 날짜에 2일을 더합니다.
    now.setDate(now.getDate() + 2);
    
    // 연도를 가져옵니다.
    let year = now.getFullYear();

    // 월을 가져오고 1을 더하여 실제 월 값을 가져옵니다.
    let month = (now.getMonth() + 1).toString().padStart(2, '0');

    // 일을 가져옵니다.
    let day = now.getDate().toString().padStart(2, '0');

    // 연도, 월, 일을 문자열로 조합하여 YYYY-MM-DD 형식의 날짜를 생성합니다.
    let currentDate = year + '-' + month + '-' + day ;

    // HTML 문서에서 id가 'current-date'인 요소를 찾아서 날짜를 표시합니다.
    document.getElementById('current-date').textContent = currentDate;
}// end displayCurrentDate
     
     /* 상품 수량 관련 코드 */
  	 // 수량 입력 필드 가져오기
    let quantityInput = document.getElementById("quantity");
    let totalPriceSpan = document.getElementById("totalPrice");
    let productPrice = parseInt('${productVO.productPrice}');

    quantityInput.addEventListener("input", function() {
        let quantity = parseInt(this.value);

        // 수량이 99를 초과할 경우
        if (quantity > 99) {
            this.value = 99; // 수량을 99로 제한
            quantity = 99;   // 수량 변수도 업데이트
        }

        // 총 상품 가격 계산 및 표시
        let totalPrice = quantity * productPrice;
        totalPriceSpan.textContent = totalPrice;

        // 수량을 form 요소에 업데이트
        document.querySelector('input[name="productNums"]').value = quantity;
    });
     /* end 상품 수량 관련 코드 */
      

//별을 생성하여 추가하는 함수
function createStars(reviewAvg) {
    const starContainer = document.getElementById('starsFieldset'); // 별을 추가할 컨테이너
    const starCount = 5; // 생성할 별의 개수
    console.log('reviewAvg : ' + reviewAvg);
    // 별을 생성하여 컨테이너에 추가하는 반복문
    for (let i = 0; i < starCount; i++) {
        const starLabel = document.createElement('label'); // 별을 나타낼 라벨 요소 생성
        starLabel.innerHTML = '&#9733;'; // HTML 엔티티를 사용하여 별 모양 설정
        if (i < reviewAvg) {
            starLabel.style.color = '#f0d000'; // 선택된 별보다 작은 값의 별은 노란색으로 표시
            console.log('로그1 : ');
        } else {
            starLabel.style.color = 'lightgray'; // 선택된 별보다 큰 값의 별은 회색으로 표시
            console.log('로그2 : ');
        }
        starContainer.appendChild(starLabel); // 컨테이너에 별 추가
    }
}


$(document).ready(function() { 
	displayCurrentDate();
    reviewMap.show(1); // 리뷰
    inquiryMap.show(1);// 문의
    createStars(reviewAvg); // 상품 평균 리뷰값 별 표시
    
    // 장바구니
    $('#btnBasket').click(function(){
        let productNum = $('#quantity').val(); // 수량 
        // javascript 객체 생성
        let obj = {
              'productId' : productId,
              'memberId' : memberId,
              'productNum' : productNum
        }
        console.log(obj);
        
        // $.ajax로 송수신
        $.ajax({
           type : 'POST', // 메서드 타입
           url : '../basket/myBasketDate', // url
           headers : { // 헤더 정보
              'Content-Type' : 'application/json', // json content-type 설정
              'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')

           }, //'Content-Type' : 'application/json' 헤더 정보가 안들어가면 4050에러가 나온다.
           data : JSON.stringify(obj), // JSON으로 변환
           success : function(result) { // 전송 성공 시 서버에서 result 값 전송
              console.log(result);
              if(result == 1) {
            	 alert('장바구니 저장 성공!');
              }
           }
        });
     }); // end btnAdd.click()

}); // end document

/* 카카오 공유 관련 api 코드 */
Kakao.Share.createDefaultButton({
	    container: '#kakaotalk-sharing-btn',// 버튼 id
	    objectType: 'commerce',
	    content: {
	      title: 
	    	  'VOP 사이트는 PC로 이용해 주세요.',
	    	  // 공유시 제목 
	      imageUrl: '${productDetails.thumbnailUrl}',// 썸네일 이미지 가져오는 기능(월래는 url를 통해 이미지 불려 옴)
	      link: {
	        // [내 애플리케이션] > [플랫폼] 에서 등록한 사이트 도메인과 일치해야 함
	        mobileWebUrl: vopUrl + '/board/main', //카카오 api에 등록된 경로
	        webUrl: vopUrl + '/board/main',
	      },
	    },
	    commerce: { // 상품 설명
	      productName: 'VOP 상품 : ${productVO.productName}', // 상품 이름
	      regularPrice: ${productVO.productPrice}, // 상품 가격
	      //discountRate: 10, // 상품 할인율
	      //discountPrice: ${productVO.productPrice} * 0.9, // 상품 할인 후 가격
	    },
	    buttons: [
	      {
	        title: '상품 보러가기', // 공유 했을때 버튼
	        link: { // 클릭시 이동하는 링크
	          mobileWebUrl: vopUrl + '/product/detail?productId=${productVO.productId }', // 앱 버전
	          webUrl: vopUrl + '/product/detail?productId=${productVO.productId }', // 웹 버전
	        },
	      },
	      {
		    title: '메인 페이지', // 공유 했을때 버튼
		    link: { // 클릭시 이동하는 링크
		      mobileWebUrl: vopUrl + '/board/main', // 앱 버전
		      webUrl: vopUrl + '/board/main', // 웹 버전
		     },
		   },
	    ],
	  });// end Kakao.Share.createDefaultButton()
	  /* end 카카오 공유 관련 api 코드 */


//ReviewMap 객체에 show 함수를 정의합니다.
reviewMap.show = function(page) {
    
    let reviewUrl = '../reviewRest/all/' + productId + '/' + page;
    
    let likesUrl = '../likes/list/' + productId + '/' + memberId;
    // 실행중인 코드 
    let reviewNUM = [];
    let likesNUM = [];

 	// inquiry 데이터 가져오기
    $.ajax({
        method: 'GET',
        url: reviewUrl,
        success: function(data) {
        	// ReviewMap 초기화
            reviewMap.list = data.list;
            reviewMap.pageMaker = data.pageMaker;
            
            reviewNUM =  reviewMap.list;  // 성공적으로 데이터를 가져오면 inquiryNUM에 저장
           
            console.log("reviewNUM : " + reviewNUM);
            
            
            reviewProcessComments();
            
         	// 페이지를 생성한 후 등록합니다.
            $('#product_list_page').html(makePageForm(reviewMap));
        },
    });// end ajax
    
    
    $.ajax({
        method: 'GET',
        url: likesUrl,
        headers: {
            'Content-Type': 'application/json' // json content-type 설정
        }, // 'Content - Type' : application/json; 헤더 정보가 안들어가면 405 에러가 나온다.
        success: function(data) {
        likesNUM = data;
        console.log("likesNUM : " + likesNUM);
        reviewProcessComments();
        },
    });
    
 	// 문의와 답변 데이터를 처리하는 함수
    function reviewProcessComments() {// 여기 비동기 동작 어떻게 처리 하는지 잘모르겠음
        
        if (reviewNUM.length > 0) {
            // 데이터를 비교하여 일치하는 요소들을 찾음
            
            let totalList = reviewPrintMatchingItems(reviewNUM, likesNUM);// 그럼 여기서 함수가 실행 된 다음 변수에 저장?
            reviewPrint(totalList);  // 일치하는 요소들을 렌더링 함수로 전달하여 출력
         
            /* let matchingItems = printMatchingItems(inquiryNUM, answerNUM);// 그럼 여기서 함수가 실행 된 다음 변수에 저장?
            renderComments(matchingItems);  // 일치하는 요소들을 렌더링 함수로 전달하여 출력 */
        }
    }
 	
    function reviewPrintMatchingItems(reviewNUM, likesNUM) {
        let result = [];

        // 모든 문의와 답변 데이터를 비교하여 일치하는 경우를 찾음
        for (let i = 0; i < reviewNUM.length; i++) {
            let matchingLikes = []; // 현재 문의에 대한 일치하는 답변들을 저장할 배열

            for (let j = 0; j < likesNUM.length; j++) {//
                if (reviewNUM[i].reviewId === likesNUM[j].reviewId) {
                    // 일치하는 경우 matchingAnswers 배열에 객체로 저장
                    matchingLikes.push({
                    	likesType: likesNUM[j].likesType
                    });
                }
            }

            // 문의와 해당하는 모든 답변들을 result 배열에 객체로 저장
            result.push({
            	reviewId: reviewNUM[i].reviewId,
                memberId: reviewNUM[i].memberId,
                reviewStar: reviewNUM[i].reviewStar,
                reviewContent: reviewNUM[i].reviewContent,
                reviewDateCreated: reviewNUM[i].reviewDateCreated,
                reviewLike: reviewNUM[i].reviewLike,
                likes: matchingLikes  // 일치하는 답변들 배열을 answers 필드로 저장
            });
        }

        return result;  // 일치하는 요소들을 담은 배열 반환
    }// end reviewPrintMatchingItems
   
    
    function reviewPrint(comments) {
    	  let form = '';  
    	    for (let i = 0; i < comments.length; i++) {
    	        let reviewStar = comments[i].reviewStar;
    	        let starsHTML = ''; 
    	        let star = parseInt(reviewStar); 
    	        for (let k = 1; k <= 5; k++) {
    	            if (k <= star) {
    	                starsHTML += '&#9733;'; 
    	            } else {
    	                starsHTML += '&#9734;'; 
    	            }
    	        }

    	        // 좋아요 및 싫어요 버튼 상태 결정
    	        let likeClass = "";
    	        let dislikeClass = "";
    	        let likeIcon = "fa-thumbs-o-up";
    	        let dislikeIcon = "fa-thumbs-o-down";

    	        if (comments[i].likes && comments[i].likes.length > 0) {
    	            for (let j = 0; j < comments[i].likes.length; j++) {
    	                let likesType = comments[i].likes[j].likesType;
    	                if (likesType === 1) {
    	                    likeClass = "liked";
    	                    likeIcon = "fa-thumbs-up";
    	                } else if (likesType === 0) {
    	                    dislikeClass = "disliked";
    	                    dislikeIcon = "fa-thumbs-down";
    	                }
    	            }
    	        }

    	        // 리뷰 정보와 좋아요/싫어요 버튼을 같은 행에 추가
    	        form += '<tr>' +
    	                '<td class="reviewId">' + comments[i].reviewId + '</td>' +
    	                '<td class="memberId">' + comments[i].memberId + '</td>' +
    	                '<td class="reviewStars">' + starsHTML + '</td>' + 
    	                '<td class="reviewContent">' + comments[i].reviewContent + '</td>' + 
    	                '<td class="reviewDateCreated">' + toDate(comments[i].reviewDateCreated) + '</td>' +
    	                '<td class="button-container">' +
    	                '(' + comments[i].reviewLike + ')<div class="button likeButton ' + likeClass + '" data-value="1" data-page="' + page + '"><i class="fa ' + likeIcon + '" aria-hidden="true"></i></div>' +
    	                '<div class="button dislikeButton ' + dislikeClass + '" data-value="0" data-page="' + page + '"><i class="fa ' + dislikeIcon + '" aria-hidden="true"></i></div>' +
    	                '</td>' +
    	                '</tr>';
    	    }
    	    $('#review').html(form);
    	}
    
}// end reviewMap.show

$(document).on('click', '.likeButton, .dislikeButton', function() {
	
	if (memberId && memberId.trim() !== '') {
	
    const isLikeButton = $(this).hasClass('likeButton');
    const otherButton = isLikeButton ? $(this).closest('tr').find('.dislikeButton') : $(this).closest('tr').find('.likeButton');
    const icon = $(this).find('i');
    const otherIcon = otherButton.find('i');

    // 현재 클릭된 버튼의 상태
    const isActive = $(this).hasClass(isLikeButton ? 'liked' : 'disliked');
    // 상대 버튼의 상태
    const isOtherActive = otherButton.hasClass(isLikeButton ? 'disliked' : 'liked');
    
    // 현재 클릭된 행에서 reviewId를 가져옵니다.
    const reviewId = $(this).closest('tr').find('.reviewId').text();
   
    
    // 현재 버튼의 data-value 값을 콘솔에 출력
    const likesType = $(this).attr('data-value');
    
    const page = $(this).attr('data-page'); // 페이지 정보 가져오기

    const $this = $(this); // `this`를 변수에 저장
    
    console.log("reviewId : " + reviewId);

    if (!isActive && isOtherActive) {
        let obj = {
            'reviewId': reviewId,
            'productId': productId,
            'memberId': memberId,
            'likesType': likesType
        };
        // ajax 요청
        $.ajax({
            type: 'PUT', // 메서드 타입
            url: '../likes/modify', // 경로 
            headers: {
                'Content-Type': 'application/json', // json content-type 설정
                'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
            }, // 'Content - Type' : application/json; 헤더 정보가 안들어가면 405 에러가 나온다.
            data: JSON.stringify(obj), // JSON으로 변환
            success: function(result) { // 전송 성공 시 서버에서 result 값 전송
                if (result == 1) {
                    // 클릭된 버튼을 활성화하고 상대 버튼을 비활성화
                    $this.addClass(isLikeButton ? 'liked' : 'disliked');
                    icon.removeClass(isLikeButton ? 'fa-thumbs-o-up' : 'fa-thumbs-o-down').addClass(isLikeButton ? 'fa-thumbs-up' : 'fa-thumbs-down');

                    otherButton.removeClass(isLikeButton ? 'disliked' : 'liked');
                    otherIcon.removeClass(isLikeButton ? 'fa-thumbs-down' : 'fa-thumbs-up').addClass(isLikeButton ? 'fa-thumbs-o-down' : 'fa-thumbs-o-up');
                    console.log(isLikeButton ? '좋아요 수정 성공' : '싫어요 수정 성공');
                    console.log("page : " + page)
                    reviewMap.show(page);
                } else {
                	console.log(isLikeButton ? '좋아요 수정 실패' : '싫어요 수정 실패');
                }
            }
        }); // end ajax()

    } else if (!isActive) {
        let obj = {
            'reviewId': reviewId,
            'productId': productId,
            'memberId': memberId,
            'likesType': likesType
        };
        // ajax 요청
        $.ajax({
            type: 'POST', // 메서드 타입
            url: '../likes/register', // 경로 
            headers: {
                'Content-Type': 'application/json', // json content-type 설정
                'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')

            }, // 'Content - Type' : application/json; 헤더 정보가 안들어가면 405 에러가 나온다.
            data: JSON.stringify(obj), // JSON으로 변환
            success: function(result) { // 전송 성공 시 서버에서 result 값 전송
                if (result == 1) {
                    // 클릭된 버튼을 활성화
                    $this.addClass(isLikeButton ? 'liked' : 'disliked');
                    icon.removeClass(isLikeButton ? 'fa-thumbs-o-up' : 'fa-thumbs-o-down').addClass(isLikeButton ? 'fa-thumbs-up' : 'fa-thumbs-down');
                    console.log(isLikeButton ? '좋아요 등록 성공' : '싫어요 등록 성공');
                    console.log("page : " + page)
                    reviewMap.show(page);
                } else {
                	console.log(isLikeButton ? '좋아요 등록 실패' : '싫어요 등록 실패');
                }
            }
        }); // end ajax()

    } else {
        let obj = {
            'reviewId': reviewId,
            'productId': productId,
            'memberId': memberId,
            'likesType': likesType
        };
        // ajax 요청
        $.ajax({
            type: 'DELETE', // 메서드 타입
            url: '../likes/delete', // 경로 
            headers: {
                'Content-Type': 'application/json', // json content-type 설정
                'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')

            }, // 'Content - Type' : application/json; 헤더 정보가 안들어가면 405 에러가 나온다.
            data: JSON.stringify(obj), // JSON으로 변환
            success: function(result) { // 전송 성공 시 서버에서 result 값 전송
                if (result == 1) {
                    $this.removeClass(isLikeButton ? 'liked' : 'disliked');
                    icon.removeClass(isLikeButton ? 'fa-thumbs-up' : 'fa-thumbs-down').addClass(isLikeButton ? 'fa-thumbs-o-up' : 'fa-thumbs-o-down');
                    console.log(isLikeButton ? '좋아요 삭제 성공' : '싫어요 삭제 성공');
                    console.log("likesType : " + likesType)
                    console.log("page : " + page)
                    reviewMap.show(page);
                } else {
                	console.log(isLikeButton ? '좋아요 삭제 실패' : '싫어요 삭제 실패');
                }
            }
        }); // end ajax()
    }// end else 
	
	} else {
		alert('로그인 후 사용 가능 합니다.');
		window.location.href = '../member/login';
	}
}); // end (document).on


 	// 페이지 버튼 생성 후, reviewMap의 리스트 출력 함수 등록
    function makePageForm(reviewMap) { 
		const pageMaker = reviewMap.pageMaker;
		const startNum = pageMaker.startNum;
		const endNum = pageMaker.endNum;
		
		// 스타일 적용 코드 및 리스트 코드
		let pageForm = $('<ul class="page_list"></ul>');
		let numForm;
		// 클릭 했을 때 이전 페이지로
		if (pageMaker.prev) {
			numForm = $('<li>이전&nbsp&nbsp</li>').click(function() {
				reviewMap.show(startNum - 1);
			});
			pageForm.append(numForm);
		}
		// 현재 리스트 출력
		for (let x = startNum; x <= endNum; x++) {
			numForm = $('<li>' + x + '&nbsp&nbsp</li>').click(function() {
				reviewMap.show(x);
			});
			pageForm.append(numForm);
		}
		// 클릭 했을 때 다음 페이지로
		if (pageMaker.next) {
			numForm = $('<li>다음</li>').click(function() {
				reviewMap.show(endNum + 1);
			});
			pageForm.append(numForm);
		}
		return pageForm;
	} // end makePageForm
	
	// 문의(대댓글) 
    inquiryMap.show = function(page) {
        let inquiryUrl = '../inquiryRest/list/' + productId + '/' + page;// 문의 데이터 API 엔드포인트
        let answerUrl = '../answer/list/' + productId; // 답변 데이터 API 엔드포인트
        
        let inquiryNUM = []; // 문의 데이터 배열
        let answerNUM = []; // 답변 데이터 배열
        
        // inquiry 데이터 가져오기
        $.ajax({
            method: 'GET',
            url: inquiryUrl,
            success: function(data) {
             	// reviewMap 초기화
                inquiryMap.inquiryList = data.inquiryList || [];
                inquiryMap.pageMaker = data.pageMaker || null;

                inquiryNUM = inquiryMap.inquiryList;  // 성공적으로 데이터를 가져오면 inquiryNUM에 저장
                console.log("inquiryNUM : " + inquiryNUM);
                processComments();  // 데이터를 가져왔으므로 처리 함수 호출
                $('#comments_list_page').html(inquiryMakePageForm(inquiryMap));
            },
        });
       
        // answer 데이터 가져오기
        $.ajax({
            method: 'GET',
            url: answerUrl,
            success: function(data) {
                answerNUM = data;   // 성공적으로 데이터를 가져오면 answerNUM에 저장
                console.log("answerNUM : " + answerNUM);
                processComments();  // 데이터를 가져왔으므로 처리 함수 호출
            },
        });

        // 문의와 답변 데이터를 처리하는 함수
        function processComments() {// 여기 비동기 동작 어떻게 처리 하는지 잘모르겠음
            // inquiryNUM과 answerNUM이 모두 데이터를 가져온 경우에만 처리
            // 비동기로 리스트를 가져왔을때 두 데이터가 있는지 확인 하는 코드이다.
            /* if (inquiryNUM.length > 0 && answerNUM.length > 0) { */
            if (inquiryNUM.length > 0) {
                // 데이터를 비교하여 일치하는 요소들을 찾음
                let matchingItems = printMatchingItems(inquiryNUM, answerNUM);// 그럼 여기서 함수가 실행 된 다음 변수에 저장?
                renderComments(matchingItems);  // 일치하는 요소들을 렌더링 함수로 전달하여 출력
            }
        }

        // 문의와 답변 데이터를 비교하여 일치하는 요소들을 반환하는 함수
        function printMatchingItems(inquiryNUM, answerNUM) {
            let result = [];
            // 모든 문의와 답변 데이터를 비교하여 일치하는 경우를 찾음
            for (let i = 0; i < inquiryNUM.length; i++) {// 모든 데이터를 꺼내는 작업
                let matchingAnswers = []; // 현재 문의에 대한 일치하는 답변들을 저장할 배열

                for (let j = 0; j < answerNUM.length; j++) {// 모든 데이터를 꺼내는 작업
                    if (inquiryNUM[i].inquiryId === answerNUM[j].inquiryId) {// 여기서 페이징 한 정보에 맞는 정보만 불려온다. 
                        // 일치하는 경우 matchingAnswers 배열에 객체로 저장
                        matchingAnswers.push({
                            answerId: answerNUM[j].answerId,
                            memberId: answerNUM[j].memberId,
                            answerContent: answerNUM[j].answerContent,
                            answerDateCreated: answerNUM[j].answerDateCreated
                        });
                    }
                }

                // 문의와 해당하는 모든 답변들을 result 배열에 객체로 저장
                result.push({//그냥 페이지 정보 저장
                    inquiryId: inquiryNUM[i].inquiryId,
                    memberId: inquiryNUM[i].memberId,
                    inquiryContent: inquiryNUM[i].inquiryContent,
                    inquiryDateCreated: inquiryNUM[i].inquiryDateCreated,
                    answers: matchingAnswers  // 일치하는 답변들 배열을 answers 필드로 저장
                });
            }
            return result;  // 일치하는 요소들을 담은 배열 반환
        }

        // 일치하는 요소들을 HTML 테이블 형식으로 렌더링하여 출력하는 함수
        function renderComments(comments) {// comments변수 값은 따로 선언 하는것이 아니라 그 어떤값이 들어 가도 상관이없다.
        	// comments의 변수 값은 printMatchingItems함수를 통해 조건문에 맞게 정렬된 배열 형태의 값이다.
            let form = '';  // 출력할 HTML 문자열을 저장할 변수

            // 모든 일치하는 요소들을 테이블의 각 행으로 변환하여 form에 추가
            for (let i = 0; i < comments.length; i++) {
                // 문의 내용 행 추가
                form += '<tr>' +
                		'<td colspan="4">문의 내용</td>' +  
                		'</tr>' +
                		'<tr>' +
                		'<td>문의 내용</td>' +
                        '<td class="inquiryId">' + comments[i].inquiryId + '</td>' +
                        '<td class="memberId">' + comments[i].memberId + '</td>' +
                        '<td class="inquiryContent">' + comments[i].inquiryContent + '</td>' + 
                        '<td class="inquiryDateCreated">' + toDate(comments[i].inquiryDateCreated) + '</td>' +
                        '</tr>';

                // 모든 일치하는 답변들에 대해 행 추가
                for (let j = 0; j < comments[i].answers.length; j++) {
                    form += '<tr>' +
                            '<td colspan="3">ㄴ답변 내용</td>' +  // 답변 내용 표시
                            '</tr>' +
                            '<tr>' +
                            '<td>ㄴ답변 내용</td>' +
                            '<td class="answerId">' + comments[i].answers[j].answerId + '</td>' +
                            '<td class="answerMemberId">' + comments[i].answers[j].memberId + '</td>' +
                            '<td class="answerContent">' + comments[i].answers[j].answerContent + '</td>' + 
                            '<td class="answerDateCreated">' + toDate(comments[i].answers[j].answerDateCreated) + '</td>' +
                            '</tr>';
                }
            }
            // 결과를 id가 'comments'인 요소에 HTML로 출력
            $('#comments').html(form);
        }// end renderComments()
    }// end getAllComments()
	
	
	// 페이지 버튼 생성 후, reviewMap의 리스트 출력 함수 등록
    function inquiryMakePageForm(inquiryMap) { 
		const inquiryPageMaker = inquiryMap.pageMaker;  
		const inquiryStartNum = inquiryPageMaker.startNum; 
		const inquiryEndNum = inquiryPageMaker.endNum; 
		
		// 스타일 적용 코드 및 리스트 코드
		let pageForm = $('<ul class="page_list"></ul>');
		let numForm;
		// 클릭 했을 때 이전 페이지로
		if (inquiryPageMaker.prev) {
			numForm = $('<li>이전&nbsp&nbsp</li>').click(function() {
				inquiryMap.show(inquiryStartNum - 1);
			});
			pageForm.append(numForm);
		}
		// 현재 리스트 출력
		for (let x = inquiryStartNum; x <= inquiryEndNum; x++) {
			numForm = $('<li>' + x + '&nbsp&nbsp</li>').click(function() {
				inquiryMap.show(x);
			});
			pageForm.append(numForm);
		}
		// 클릭 했을 때 다음 페이지로
		if (inquiryPageMaker.next) {
			numForm = $('<li>다음</li>').click(function() {
				inquiryMap.show(inquiryEndNum + 1);
			});
			pageForm.append(numForm);
		}
		return pageForm;
	} // end inquiryMakePageForm

	// 시간 변환 함수
	function toDate(timestamp) {
        let date = new Date(timestamp);
        let formatted = (date.getFullYear()) + '/' + (date.getMonth() + 1) + '/' + date.getDate() + ' ' +
                        date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
        return formatted;
    } // end toDate
     </script>

</body>
</html>
