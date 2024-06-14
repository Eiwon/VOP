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
    getAllComments(); // 문의 
    ReviewMap.show(1); // 리뷰
    createStars(reviewAvg); // 상품 별표시
 
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
     
    
    // 문의(대댓글) 리스트
    function getAllComments() {
        let inquiryUrl = '../inquiryRest/list/' + productId;
        let answerUrl = '../answer/list/' + productId;
        
        // 변수 선언
        let allComments = '';
        let inquiryNUM = [];
        let answerNUM = [];

        // 댓글 데이터 가져오기
        $.getJSON(
            inquiryUrl,
            function(inquiryData) {
                // 댓글 데이터 반복
                $(inquiryData).each(function() {
                    // 날짜 형식 변환
                    let inquiryDateCreated = new Date(this.inquiryDateCreated);
                    let dateString = inquiryDateCreated.toLocaleDateString();
                    let timeString = inquiryDateCreated.toLocaleTimeString();

                    // 댓글 HTML 생성
                    let inquiryList = '<div>' +
                        '<pre>' +
                        '문의 내용 <input type="hidden" class="inquiryId" value="' + this.inquiryId + '">' +
                        this.memberId +
                        '&nbsp;&nbsp;' +
                        dateString + ' ' + timeString +
                        '&nbsp;&nbsp;' +
                        '<input type="text" class="inquiryContent" value="' + this.inquiryContent + '" readonly>' +
                        '</pre>' +
                        '</div>';

                    // 전체 댓글에 추가
                    inquiryNUM.push({
                        inquiryId: this.inquiryId,
                        html: inquiryList,
                        answers: [] // 해당 댓글에 대한 대댓글 리스트
                    });
                });

                // 대댓글 데이터 가져오기
                $.getJSON(
                    answerUrl,
                    function(answerData) {
                        // 대댓글 데이터 반복
                        $(answerData).each(function() {
                            // 날짜 형식 변환
                            let answerDateCreated = new Date(this.answerDateCreated);
                            let dateString = answerDateCreated.toLocaleDateString();
                            let timeString = answerDateCreated.toLocaleTimeString();

                            // 대댓글 HTML 생성
                            let answerList = '<div>' +
                                '<pre>' +
                                ' ㄴ답변 내용 <input type="hidden" class="answerId" value="' + this.answerId + '">' +
                                this.memberId +
                                '&nbsp;&nbsp;' +
                                dateString + ' ' + timeString +
                                '&nbsp;&nbsp;' +
                                '<input type="text" class="answerContent" value="' + this.answerContent + '" readonly>' +
                                '</pre>' +
                                '</div>';

                            // 해당 댓글에 대한 inquiryId 가져오기
                            let answerInquiryId = this.inquiryId;

                            // 해당 inquiryId를 가진 댓글 찾기
                            let inquiry = inquiryNUM.find(function(inquiry) {
                                return inquiry.inquiryId === answerInquiryId;
                            });

                            if (inquiry) {
                                // 해당 댓글에 대댓글 추가
                                inquiry.answers.push(answerList);
                            }
                        });

                        // HTML에 전체 댓글과 대댓글 추가
                        inquiryNUM.forEach(function(inquiry) {
                            let commentHtml = inquiry.html;
                            inquiry.answers.forEach(function(answer) {
                                commentHtml += answer;
                            });
                            allComments += commentHtml;
                        });

                        // comments 엘리먼트에 전체 댓글과 대댓글 추가
                        $('#comments').html(allComments);
                    }
                );// end  $.getJSON
            }
        );
    }
    
    //loadImg(); 위에로 이동 하였습니다.
}); // end document

//ReviewMap 객체에 show 함수를 정의합니다.
ReviewMap.show = function(page) {
    console.log('page : ' + page);
    let url = '../review/all/' + productId + '/' + page;
    
    // AJAX 요청을 보내 리뷰 데이터를 가져옵니다.
    $.ajax({
        method: 'GET',
        url: url,
        dataType: 'json',
        success: function(data) {
            console.log(data);

            let list = ''; // 댓글 데이터를 HTML로 표현할 문자열 변수

            // ReviewMap 초기화
            ReviewMap.list = data.list || [];
            ReviewMap.pageMaker = data.pageMaker || null;

            // ReviewMap의 리스트 데이터를 반복문으로 처리합니다.
            $.each(ReviewMap.list, function(index, review) {
                let reviewDateCreated = new Date(review.reviewDateCreated);
                let dateString = reviewDateCreated.toLocaleDateString();
                let timeString = reviewDateCreated.toLocaleTimeString();

                let starsHTML = '';
                let reviewStar = parseInt(review.reviewStar);
                
                for (let i = 1; i <= 5; i++) {
                    if (i <= reviewStar) {
                        starsHTML += '&#9733;';
                    } else {
                        starsHTML += '&#9734;';
                    }
                }

                list += '<div>' +
                    '<pre>' +
                    '<input type="hidden" id="review" value="' + review.reviewId + '">' +
                    review.memberId +
                    '&nbsp;&nbsp;' +
                    '<span class="reviewStars">' + starsHTML + '</span>' +
                    '&nbsp;&nbsp;' +
                    dateString + ' ' + timeString +
                    '&nbsp;&nbsp;' +
                    '<input type="text" id="reviewContent" value="' + review.reviewContent + '" readonly>' +
                    '&nbsp;&nbsp;' +
                    '<div class="button likeButton" data-value="0"><i class="fa fa-thumbs-o-up" aria-hidden="true"></i></div>' +
                    '<div class="button dislikeButton" data-value="0"><i class="fa fa-thumbs-o-down" aria-hidden="true"></i></div>' +
                    '</pre>' +
                    '</div>';
            });

            // 페이지를 생성한 후 등록합니다.
            $('#product_list_page').html(makePageForm(ReviewMap));
            
            // 저장된 데이터를 review div에 표현합니다.
            $('#review').html(list);
        }
    });
}

//이벤트 위임을 사용하여 동적 요소에 이벤트 리스너 등록
$(document).on('click', '.likeButton', function() {
    // 현재 클릭된 likeButton의 같은 행에 있는 dislikeButton을 찾습니다.
    const dislikeButton = $(this).closest('tr').find('.dislikeButton');

    // likeButton에 'liked' 클래스를 토글합니다.
    $(this).toggleClass('liked');
    // dislikeButton에서 'disliked' 클래스를 제거합니다.
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
		
		console.log('pageMaker : ' + pageMaker);
		
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
     </script>

</body>
</html>