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
<meta charset="UTF-8">
<!-- 모바일 관련 코드라서 없어도 동작 가능 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

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

<jsp:include page="../include/header.jsp"></jsp:include>

<title>상품 상세 조회</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
/* 리뷰 별 폼 스타일 (여기 코드로 인해 별이 박스 형태로 안나온다.)*/ 
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
}

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
</style>

</head>
<body>

	<!-- 상품 상세 페이지 제작 중 -->
	<h2>상품 상세 페이지</h2>

	<!-- 카카오 공유 아이콘 -->
	<div class="right-align">
		<a id="kakaotalk-sharing-btn" href="javascript:;"> <img
			src="https://developers.kakao.com/assets/img/about/logos/kakaotalksharing/kakaotalk_sharing_btn_medium.png" />
		</a>
	</div>

	<div>
		<p>카테고리 : ${productVO.category }</p>
		<!-- 썸네일 상품 이미지 -->
		<img class="productImg" alt="${productVO.imgId}">
		<p>상품 번호 : ${productVO.productId }</p>
		<p>상품 이름 : ${productVO.productName }</p>
	</div>

	<!-- 리뷰 별점 표시 코드-->
	<div id="myform">
    	<fieldset id="starsFieldset">
        <!-- 별이 여기에 동적으로 추가될 것입니다. -->
    	</fieldset>
	</div>

	<div>
		<p>리뷰 평균 : ${productVO.reviewAvg}</p>
		<p>댓글 총 갯수 : ${productVO.reviewNum}</p>
	</div>
	
	<div>
		<p>
			상품 가격 : <span id="totalPrice">${productVO.productPrice}</span>
		</p>
	</div>
	<!-- 현재 선택된 상품 수량 -->
	<input type="number" id="quantity" value="1" min="1" max="99"
		maxlength="2">

	<!-- 상품 배송 정보 제작 해야함 -->
	<div>
		<p>판매자 : ${productVO.memberId}</p>
	</div>

	
	
	
	<!-- 장바구니 버튼 -->
	<!-- 세션 아이디가 없는 경우 -->
	<c:if test="${empty memberDetails.getUsername() }">
		<button id="btnBasket" disabled="disabled">장바구니</button>
		<p>로그인 후 장바구니 가능합니다.</p>
	</c:if>
	<!-- 세션 아이디가 있는 경우 -->
	<c:if test="${not empty memberDetails.getUsername() }">
		<button id="btnBasket">장바구니</button>
	</c:if>
	<!-- end 장바구니 버튼 -->

	<!-- 장바구니 링크 -->
	<a href="../basket/main">장바구니 바로가기</a>
	
	<!-- 바로구매 버튼 -->
	<!-- 로그인된 세션 아이디 전달 -->
	<form id="checkoutForm" action="../payment/checkout" method="POST">
		<input type="hidden" name="memberId"
			value="${memberDetails.getUsername() }"> <input type="hidden"
			name="productIds" value="${productVO.productId}"> <input
			type="hidden" name="productNums" value="1">

		<!-- 세션 아이디가 없 경우 -->
		<c:if test="${empty memberDetails.getUsername() }">
			<button type="submit" disabled="disabled">바로구매</button>
			<p>로그인 후 결제 가능합니다.</p>
		</c:if>

		<!-- 세션 아이디가 있는 경우 -->
		<c:if test="${not empty memberDetails.getUsername() }">
			<button type="submit">바로구매</button>
		</c:if>
	</form>
	<!-- end 바로구매 버튼 -->

	<!-- 상품 설명 이미지 -->
	<p>상품 이미지 설명</p>

	<div id="kakaotalk-sharing-btn" href="javascript:;">
		<c:forEach items="${imageList}" var="image">
			<img class="productImg" alt="${image.imgId}"
				style="margin-right: 10px;">
			<!-- 이미지 간격 조정 -->
		</c:forEach>
	</div>

	<!-- 댓글 화면 코드 및 가운데 정렬 -->

	<h3>리뷰</h3>
	<div id="review"></div>
	<!-- 리뷰 페징처리 내용 -->
	<div id="product_list_page"></div>
	
	<h3>문의</h3>
	<div id="comments"></div>

	<div>
		<h3>배송/교환/반품 안내</h3>
		<p>내용 작성 예정</p>
	</div>

	<!-- 좋아요 표시 제작 예정? -->

	<script type="text/javascript">
     
     let ReviewMap = {};ReviewMap
     
 	 const memberId = '${memberDetails.getUsername()}';
 	 let productId = ${productVO.productId};
 	 let reviewAvg = parseInt("${productVO.reviewAvg}"); // 서버 사이드 값 사용
     
  	 // 썸네일 이미지 ID 변수 정의 (이 ID가 맞는지 확인해야 합니다)
     const thumbnailImgId = '${productVO.imgId}';
     
     /* 카카오 공유 관련 api 코드 */
     function initKakaoShare(imageUrl) {
     Kakao.Share.createDefaultButton({
    	    container: '#kakaotalk-sharing-btn',// 버튼 id
    	    objectType: 'commerce',
    	    content: {
    	      title: 
    	    	  'VOP 상품 + 카테고리 : "${productVO.category }" + 상품 이름 :"${productVO.productName }"' +
    	    	  '"상품 번호 : ${productVO.productId }" + 상품 가격 : "${productVO.productPrice}" + 리뷰 평균 : "${productVO.reviewAvg}"',
    	    	  // 공유시 제목 
    	      imageUrl: imageUrl,// 썸네일 이미지 가져오는 기능(월래는 url를 통해 이미지 불려 옴)
    	      link: {
    	        // [내 애플리케이션] > [플랫폼] 에서 등록한 사이트 도메인과 일치해야 함
    	        mobileWebUrl: 'http://192.160.0.146:8080/vop/board/main', //카카오 api에 등록된 경로
    	        webUrl: 'http://192.160.0.146:8080/vop/board/main',
    	      },
    	    },
    	    commerce: { // 상품 설명
    	      productName: 'VOP 상품 : "${productVO.productName}"', // 상품 이름
    	      regularPrice: ${productVO.productPrice}, // 상품 가격
    	      discountRate: 10, // 상품 할인율
    	      discountPrice: ${productVO.productPrice} * 0.9, // 상품 할인 후 가격
    	    },
    	    buttons: [
    	      {
    	        title: '상품 보러가기', // 공유 했을때 버튼
    	        link: { // 클릭시 이동하는 링크
    	          mobileWebUrl: 'http://192.160.0.146:8080/vop/product/detail?productId=${productVO.productId }', // 앱 버전
    	          webUrl: 'http://192.160.0.146:8080/vop/product/detail?productId=${productVO.productId }', // 웹 버전
    	        },
    	      },
    	    ],
    	  });
     }/* end 카카오 공유 관련 api 코드 */
     
     /* 상품 수량 관련 코드 */
  	 // 수량 입력 필드 가져오기
     let quantityInput = document.getElementById("quantity");
     // 상품 가격을 표시하는 span 요소 가져오기
     let totalPriceSpan = document.getElementById("totalPrice");
     // 상품 가격 가져오기
     let productPrice = parseInt('${productVO.productPrice}');

     // 수량 입력 필드의 변경 이벤트 감지
     quantityInput.addEventListener("input", function() {
         // 현재 입력된 수량 가져오기
         let quantity = parseInt(this.value);
         // 총 상품 가격 계산
         let totalPrice = quantity * productPrice;
         // 총 상품 가격을 span 요소에 업데이트
         // textContent : 텍스트의 내용 변경
         totalPriceSpan.textContent = totalPrice;
         // 현재 수량을 productNums에 적용하는 코드
         document.querySelector('input[name="productNums"]').value = quantity;
     });
     /* end 상품 수량 관련 코드 */
      

//별을 생성하여 추가하는 함수
function createStars(reviewAvg) {
    const starContainer = document.getElementById('starsFieldset'); // 별을 추가할 컨테이너
    const starCount = 5; // 생성할 별의 개수

    // 별을 생성하여 컨테이너에 추가하는 반복문
    for (let i = 0; i < starCount; i++) {
        const starLabel = document.createElement('label'); // 별을 나타낼 라벨 요소 생성
        starLabel.setAttribute('for', 'star'); // for 속성 설정
        starLabel.innerHTML = '&#9733;'; // HTML 엔티티를 사용하여 별 모양 설정
        if (i < reviewAvg) {
            starLabel.style.color = '#f0d000'; // 선택된 별보다 작은 값의 별은 노란색으로 표시
        } else {
            starLabel.style.color = 'transparent'; // 선택된 별보다 큰 값의 별은 투명하게 표시
        }
        starContainer.appendChild(starLabel); // 컨테이너에 별 추가
    }
}

$(document).ready(function() { 
	loadImg(); // 이미지 불려오는 메소드
    ReviewMap.show(1); // 리뷰
    createStars(reviewAvg); // 상품 평균 리뷰값 별 표시
    getAllComments(); // 문의 
 
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
              'Content-Type' : 'application/json' // json content-type 설정
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
     
    

 // 문의(대댓글) 
    function getAllComments() {
        let inquiryUrl = '../inquiryRest/list/' + productId; // 문의 데이터 API 엔드포인트
        let answerUrl = '../answer/list/' + productId;      // 답변 데이터 API 엔드포인트
        
        let inquiryNUM = [];   // 문의 데이터 배열
        let answerNUM = [];    // 답변 데이터 배열
        
        // inquiry 데이터 가져오기
        $.ajax({
            method: 'GET',
            url: inquiryUrl,
            success: function(data) {
                inquiryNUM = data;  // 성공적으로 데이터를 가져오면 inquiryNUM에 저장
                processComments();  // 데이터를 가져왔으므로 처리 함수 호출
            },
        });

        // answer 데이터 가져오기
        $.ajax({
            method: 'GET',
            url: answerUrl,
            success: function(data) {
                answerNUM = data;   // 성공적으로 데이터를 가져오면 answerNUM에 저장
                processComments();  // 데이터를 가져왔으므로 처리 함수 호출
            },
        });

        // 문의와 답변 데이터를 처리하는 함수
        function processComments() {// 여기 비동기 동작 어떻게 처리 하는지 잘모르겠음
            // inquiryNUM과 answerNUM이 모두 데이터를 가져온 경우에만 처리
            if (inquiryNUM.length > 0 && answerNUM.length > 0) {
                // 데이터를 비교하여 일치하는 요소들을 찾음
                let matchingItems = printMatchingItems(inquiryNUM, answerNUM);// 그럼 여기서 함수가 실행 된 다음 변수에 저장?
                renderComments(matchingItems);  // 일치하는 요소들을 렌더링 함수로 전달하여 출력
            }
        }

        // 문의와 답변 데이터를 비교하여 일치하는 요소들을 반환하는 함수
        function printMatchingItems(inquiryNUM, answerNUM) {
            let result = [];

            // 모든 문의와 답변 데이터를 비교하여 일치하는 경우를 찾음
            for (let i = 0; i < inquiryNUM.length; i++) {
                let matchingAnswers = []; // 현재 문의에 대한 일치하는 답변들을 저장할 배열

                for (let j = 0; j < answerNUM.length; j++) {
                    if (inquiryNUM[i].inquiryId === answerNUM[j].inquiryId) {
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
                result.push({
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
        	console.log("comments : " + comments);// 문의 내용 리스트만 저장 되어 있는 것 같다.
            let form = '';  // 출력할 HTML 문자열을 저장할 변수

            // 모든 일치하는 요소들을 테이블의 각 행으로 변환하여 form에 추가
            for (let i = 0; i < comments.length; i++) {
                // 문의 내용 행 추가
                form += '<tr>' +
                		'<td colspan="4">질문 내용</td>' +  
                		'</tr>' +
                		'<tr>' +
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
                            '<td class="answerId">' + comments[i].answers[j].answerId + '</td>' +
                            '<td class="answerMemberId">' + comments[i].answers[j].memberId + '</td>' +
                            '<td class="answerContent">' + comments[i].answers[j].answerContent + '</td>' + 
                            '<td class="answerDateCreated">' + toDate(comments[i].answers[j].answerDateCreated) + '</td>' +
                            '</tr>';
                }
            }

            // 결과를 id가 'comments'인 요소에 HTML로 출력
            $('#comments').html(form);
        }
    }



}); // end document

//ReviewMap 객체에 show 함수를 정의합니다.
ReviewMap.show = function(page) {
    
    let url = '../review/all/' + productId + '/' + page;
    
    // AJAX 요청을 보내 리뷰 데이터를 가져옵니다.
    $.ajax({
        method: 'GET',
        url: url,
        dataType: 'json',
        success: function(data) {
            console.log(data);
            let form = '';
            
            // ReviewMap 초기화
            ReviewMap.list = data.list;
            ReviewMap.pageMaker = data.pageMaker;
            
            const list = ReviewMap.list;
           
            for (let x in list) {
            	let reviewStar = list[x].reviewStar;
            	
            	let starsHTML = ''; // 별 모양 HTML을 저장할 변수
                star = parseInt(reviewStar); // 문자열을 정수로 변환
				for (let i = 1; i <= 5; i++) {
                    if (i <= star) {
                        starsHTML += '&#9733;'; // 별 모양 HTML 코드 추가
                    } else {
                        starsHTML += '&#9734;'; // 빈 별 모양 HTML 코드 추가
                    }
                } 
                form += '<tr>' +
                        '<td class="reviewId">' + list[x].reviewId + '</td>' +
                        '<td class="memberId">' + list[x].memberId + '</td>' +
                        '<td class="reviewStars">' + starsHTML + '</td>' + 
                        '<td class="reviewContent">' + list[x].reviewContent + '</td>' + 
                        '<td class="reviewDateCreated">' + toDate(list[x].reviewDateCreated) + '</td>' +
                        '<td class="button-container">' +
                        '<div class="button likeButton" data-value="0"><i class="fa fa-thumbs-o-up" aria-hidden="true"></i></div>' +
                        '<div class="button dislikeButton" data-value="0"><i class="fa fa-thumbs-o-down" aria-hidden="true"></i></div>' +
                        '</td>' +
                        '</tr>';
            }

      
            // 페이지를 생성한 후 등록합니다.
            $('#product_list_page').html(makePageForm(ReviewMap));
            
            // 저장된 데이터를 review div에 표현합니다.
            $('#review').html(form);
        }
    });
}

//좋아요 코드
$(document).on('click', '.likeButton', function() {
    // 현재 클릭된 likeButton의 같은 행에 있는 dislikeButton을 찾습니다.
    // 찾는 이유는 싫어요가 클릭 되어있는 상태인지 확인 하기 위해서
    const dislikeButton = $(this).closest('tr').find('.dislikeButton');
    

    // likeButton에 'liked' 클래스를 토글합니다.
    $(this).toggleClass('liked');
    
    // dislikeButton에서 'disliked' 클래스를 제거합니다.
    // 여기서 dislikeButton 클릭이 되어있든 아니든 제거 하는것 같다.
    dislikeButton.removeClass('disliked');
    
    // likeButton 내의 <i> 요소를 찾습니다.
    const likeIcon = $(this).find('i');
    // dislikeButton 내의 <i> 요소를 찾습니다.
    const dislikeIcon = dislikeButton.find('i');

    // 'liked' 클래스가 있는지 확인합니다.
    if ($(this).hasClass('liked')) {
        // 'liked' 클래스가 있으면 likeIcon의 클래스를 변경합니다.
        likeIcon.removeClass('fa-thumbs-o-up').addClass('fa-thumbs-up');
        // likeButton의 data-value를 1로 설정합니다.
        $(this).attr('data-value', '1');
        // dislikeIcon의 클래스를 원래대로 돌립니다.
        dislikeIcon.removeClass('fa-thumbs-down').addClass('fa-thumbs-o-down');
        // dislikeButton의 data-value를 0으로 설정합니다.
        dislikeButton.attr('data-value', '0');
    } else {
        // 'liked' 클래스가 없으면 likeIcon의 클래스를 원래대로 돌립니다.
        likeIcon.removeClass('fa-thumbs-up').addClass('fa-thumbs-o-up');
        // likeButton의 data-value를 0으로 설정합니다.
        $(this).attr('data-value', '1');
    }

    // 현재 likeButton의 data-value 값을 콘솔에 출력합니다.
    const likeValue = $(this).attr('data-value');
    console.log('Like value:', likeValue);
});

//싫어요 코드
$(document).on('click', '.dislikeButton', function() {
    // 현재 클릭된 dislikeButton의 같은 행에 있는 likeButton을 찾습니다.
    const likeButton = $(this).closest('tr').find('.likeButton');

    // dislikeButton에 'disliked' 클래스를 토글합니다.
    $(this).toggleClass('disliked');
    // likeButton에서 'liked' 클래스를 제거합니다.
    likeButton.removeClass('liked');
    
    // dislikeButton 내의 <i> 요소를 찾습니다.
    const dislikeIcon = $(this).find('i');
    // likeButton 내의 <i> 요소를 찾습니다.
    const likeIcon = likeButton.find('i');

    // 'disliked' 클래스가 있는지 확인합니다.
    if ($(this).hasClass('disliked')) {
        // 'disliked' 클래스가 있으면 dislikeIcon의 클래스를 변경합니다.
        dislikeIcon.removeClass('fa-thumbs-o-down').addClass('fa-thumbs-down');
        // dislikeButton의 data-value를 0으로 설정합니다.
        $(this).attr('data-value', '0');
        // likeIcon의 클래스를 원래대로 돌립니다.
        likeIcon.removeClass('fa-thumbs-up').addClass('fa-thumbs-o-up');
        // likeButton의 data-value를 0으로 설정합니다.
        likeButton.attr('data-value', '0');
    } else {
        // 'disliked' 클래스가 없으면 dislikeIcon의 클래스를 원래대로 돌립니다.
        dislikeIcon.removeClass('fa-thumbs-down').addClass('fa-thumbs-o-down');
        // dislikeButton의 data-value를 0으로 설정합니다.
        $(this).attr('data-value', '0');
    }

    // 현재 dislikeButton의 data-value 값을 콘솔에 출력합니다.
    const dislikeValue = $(this).attr('data-value');
    console.log('Dislike value:', dislikeValue);
});

    
 	// 페이지 버튼 생성 후, ReviewMap의 리스트 출력 함수 등록
    function makePageForm(ReviewMap) { 
		const pageMaker = ReviewMap.pageMaker;
		const startNum = pageMaker.startNum;
		const endNum = pageMaker.endNum;
		
		// 스타일 적용 코드 및 리스트 코드
		let pageForm = $('<ul class="page_list"></ul>');
		let numForm;
		// 클릭 했을 때 이전 페이지로
		if (pageMaker.prev) {
			numForm = $('<li>이전&nbsp&nbsp</li>').click(function() {
				ReviewMap.show(startNum - 1);
			});
			pageForm.append(numForm);
		}
		// 현재 리스트 출력
		for (let x = startNum; x <= endNum; x++) {
			numForm = $('<li>' + x + '&nbsp&nbsp</li>').click(function() {
				ReviewMap.show(x);
			});
			pageForm.append(numForm);
		}
		// 클릭 했을 때 다음 페이지로
		if (pageMaker.next) {
			numForm = $('<li>다음</li>').click(function() {
				ReviewMap.show(endNum + 1);
			});
			pageForm.append(numForm);
		}
		return pageForm;
	} // end makePageForm
	
	function loadImg(){
		$(document).find('.productImg').each(function(){
			let target = $(this);
			let imgId = target.attr("alt");
			$.ajax({
				method : 'GET',
				url : '../image/' + imgId,
				success : function(result){
					target.attr('src', result);
					// 카카오 공유시 썸네일 이미지 사용하기 위해 사용
					if (imgId === thumbnailImgId) {
				          // 썸네일 이미지 URL을 카카오 공유 설정에 사용
				          initKakaoShare(result);
				        }
				}// end success
			}); // end ajax
		});//end document
	} // end loadImg
	
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