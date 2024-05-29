<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 세션 사용할수 있게하는 코드 -->
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
	<jsp:include page="../include/alarm.jsp"></jsp:include>
</sec:authorize> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

<!-- 카카오 공유 api 관련 코드 -->
<!-- 위에 있는 이유는 밑에 있는 코드들이 적용 되야해서 -->
<script src="https://t1.kakaocdn.net/kakao_js_sdk/2.7.2/kakao.min.js"
  	integrity="sha384-TiCUE00h649CAMonG018J2ujOgDKW/kVWlChEuu4jK2vxfAAD0eZxzCKakxg55G4" crossorigin="anonymous"
  	type="text/javascript">
</script>

<!-- Kakao JavaScript SDK를 초기화하는 코드입니다. -->
<script>
Kakao.init('fc798d4c0b15af3cd0e864357925d0b3'); // 사용하려는 앱의 JavaScript 키 입력
</script>

<jsp:include page="../include/header.jsp"></jsp:include>

<title>상품 상세 조회</title>
<style>
/* 리뷰 별 폼 스타일 */
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

</style>

</head>
<body>

	<!-- 상품 상세 페이지 제작 중 -->
	<h2>상품 상세 페이지</h2>

	<!-- 카카오 공유 아이콘 -->
	<div class="right-align">
	<a id="kakaotalk-sharing-btn" href="javascript:;">
  		<img src="https://developers.kakao.com/assets/img/about/logos/kakaotalksharing/kakaotalk_sharing_btn_medium.png"
    	 /><!-- alt="카카오톡 공유 버튼" 나중에 추가 해야함 현재 사용시 에러남 -->
	</a>
	</div>

	  <div>
      <p>카테고리 : ${productVO.category }</p>
     </div>
     
       <!-- 썸네일 상품 이미지 -->
      <div>
    	<img alt="${productVO.imgId}">
	  </div>

     <div>
      <p>상품 번호 : ${productVO.productId }</p>
     </div>
 
	 <div>
      <p>상품 이름 : ${productVO.productName }</p>
     </div>
     
      <!-- 리뷰 별점 표시 -->
     <div id="myform">	
        <fieldset>
            <!-- 리뷰 평균 별점 표시 -->
            <label for="star5">&#9733;</label>
            <label for="star4">&#9733;</label>
            <label for="star3">&#9733;</label>
            <label for="star2">&#9733;</label>
            <label for="star1">&#9733;</label>
        </fieldset>
     </div>
     
     <div>
      <p>리뷰 평균 : ${productVO.reviewAvg}</p>
 	 </div>
 	 
 	 <div>
      <p>댓글 총 갯수 : ${productVO.reviewNum}</p>
     </div>

 	 <div>
        <p>상품 가격 : <span id="totalPrice">${productVO.productPrice}</span></p>
    </div>

     <!-- 현재 선택된 상품 수량 -->
	 <input type="number" id="quantity" value="1" min="1" max="99" maxlength="2">

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
    	<button id="btnBasket" >장바구니</button>
	</c:if>
	<!-- end 장바구니 버튼 -->
	
	<!-- 장바구니 링크 -->
 	<a href="../basket/main">장바구니 바로가기</a>
 	
	<!-- 바로구매 버튼 --><!-- 로그인된 세션 아이디 전달 -->
    <form id="checkoutForm" action="../payment/checkout" method="POST">
        <input type="hidden" name="memberId" value="${memberDetails.getUsername() }">
        <input type="hidden" name="productIds" value="${productVO.productId}">
        <input type="hidden" name="productNums" value="1">
        
     <!-- 세션 아이디가 없 경우 -->
    <c:if test="${empty memberDetails.getUsername() }">
    	<button type="submit" disabled="disabled">바로구매</button>
    	<p>로그인 후 결제 가능합니다.</p>
	</c:if>
	
	<!-- 세션 아이디가 있는 경우 -->
	<c:if test="${not empty memberDetails.getUsername() }">
    	<button type="submit" >바로구매</button> 
	</c:if>
    </form>
    <!-- end 바로구매 버튼 -->

     <!-- 상품 설명 이미지 -->
     <p>상품 이미지 설명</p>
     
     <div id="kakaotalk-sharing-btn" href="javascript:;">
    	<c:forEach items="${imageList}" var="image">
        	<img alt="${image.imgId}" style="margin-right: 10px;"> <!-- 이미지 간격 조정 -->
    	</c:forEach>
	</div> 

     <!-- 댓글 화면 코드 및 가운데 정렬 -->
     
      <p>댓글</p>
      <div id="review"></div>
      
      <p>문의(대댓글)</p>
      <div id="inquiry"></div>
      
      <div>
      	<h3>배송/교환/반품 안내</h3>
      	<p></p>
      </div>

     <!-- 좋아요 표시 제작 예정? -->
     
     <script type="text/javascript">
     
 	 const memberId = '${memberDetails.getUsername() }';
 	 let productId = ${productVO.productId};
     console.log(memberId);
     
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
    	        mobileWebUrl: 'http://localhost:8080/vop/board/main', //카카오 api에 등록된 경로
    	        webUrl: 'http://localhost:8080/vop/board/main',
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
    	          mobileWebUrl: 'http://localhost:8080/vop/product/detail?productId=${productVO.productId }', // 앱 버전
    	          webUrl: 'http://localhost:8080/vop/product/detail?productId=${productVO.productId }', // 웹 버전
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
      
     
// 별표시를 업데이트하는 함수
function displayStars() {
    let value = parseInt("${productVO.reviewAvg}"); // 리뷰 별점을 정수 형으로 변환
    let stars = document.querySelectorAll('#myform label'); // 별 표시 기능 가져오기
    console.log("value: " + value);
    for (let i = 0; i < stars.length; i++) {
        if (i < value) {
            stars[i].style.color = '#f0d000'; // 선택된 별보다 작은 값의 별은 노란색으로 표시
        } else {
            stars[i].style.color = 'transparent'; // 선택된 별보다 큰 값의 별은 투명하게 표시
        }
    }
}// end displayStars()

$(document).ready(function() { 
	loadImg(); // 이미지 불려오는 메소드
	displayStars(); // 별 표시 함수
    getAllReview(); // 댓글(리뷰) 전체 검색 메소드

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
     
    // 댓글(리뷰) 전체 검색 // 이미지 및 좋아요 아직 추가 안함
    function getAllReview() {
    	 
        let url = '../review/all/' + productId;
        console.log(url);
        $.getJSON(
            url,
            function(data) {
                // data : 서버에서 전송받은 list 데이터가 저장되어 있음.
                // getJSON()에서 json 데이터는 
                // javascript object로 자동 parsing됨.
                console.log(data);

                let list = ''; // 댓글 데이터를 HTML에 표현할 문자열 변수

                // $(컬렉션).each() : 컬렉션 데이터를 반복문으로 꺼내는 함수
                $(data).each(function() {
                    // this : 컬렉션의 각 인덱스 데이터를 의미
                    console.log(this);

                    // 전송된 replyDateCreated는 문자열 형태이므로 날짜 형태로 변환이 필요
                    let reviewDateCreated = new Date(this.reviewDateCreated);
                    
                    // 날짜와 시간을 문자열로 변환하여 가져오기
                    let dateString = reviewDateCreated.toLocaleDateString();
                    let timeString = reviewDateCreated.toLocaleTimeString();

                    
                    // 별점 숫자를 가져와서 별 모양으로 변환
                    let starsHTML = ''; // 별 모양 HTML을 저장할 변수
                    let reviewStar = parseInt(this.reviewStar); // 문자열을 정수로 변환
                    
                    for (let i = 1; i <= 5; i++) {
                        if (i <= reviewStar) {
                            starsHTML += '&#9733;'; // 별 모양 HTML 코드 추가
                        } else {
                            starsHTML += '&#9734;'; // 빈 별 모양 HTML 코드 추가
                        }
                    }

                    // 댓글 이미지 및 좋아요 추가 해야함
                    list += '<div>' +
                        '<pre>' +
                        '<input type="hidden" id="review" value="' + this.review + '">' // readonly 추가해서 id을 바꾸지 못하게 해야함
                        +
                        this.memberId +
                        '&nbsp;&nbsp;' // 공백
                        +
                        '<span class="reviewStars">' + starsHTML + '</span>'// 별점 표시 
                        +
                        '&nbsp;&nbsp;'
                        +
                        dateString + ' ' + timeString // 작성 시간 (날짜와 시간)
                        +
                        '&nbsp;&nbsp;' +
                        '<input type="text" id="reviewContent" value="' + this.reviewContent + '"readonly>' // 내용
                        +
                        '&nbsp;&nbsp;' 
                        +
                        '</pre>' +
                        '</div>';
                }); // end each()

                $('#review').html(list); // 저장된 데이터를 replies div 표현
            } // end function()
        ); // end getJSON()
    } // end getAllReply()
    
   /*  // 문의(댓글) 전체 검색
    function getAllInquiry() {
    	
    	let url = '../inquiryRest/list/' + productId;
        console.log(url);
        $.getJSON(
            url,
            function(data) {
                // data : 서버에서 전송받은 list 데이터가 저장되어 있음.
                // getJSON()에서 json 데이터는 
                // javascript object로 자동 parsing됨.
                console.log(data);

                let list = ''; // 댓글 데이터를 HTML에 표현할 문자열 변수

                // $(컬렉션).each() : 컬렉션 데이터를 반복문으로 꺼내는 함수
                $(data).each(function() {
                	 // this : 컬렉션의 각 인덱스 데이터를 의미
                    console.log(this);

                    // 전송된 replyDateCreated는 문자열 형태이므로 날짜 형태로 변환이 필요
                    let inquiryDateCreated = new Date(this.reviewDateCreated);
                    
                    // 날짜와 시간을 문자열로 변환하여 가져오기
                    let dateString = inquiryDateCreated.toLocaleDateString();
                    let timeString = inquiryDateCreated.toLocaleTimeString();

                    // 댓글 이미지 및 좋아요 추가 해야함
                    list += '<div>' +
                        '<pre>' +
                        '<input type="hidden" id="inquiry" value="' + this.inquiry + '">' // inquiry 추가해서 id을 바꾸지 못하게 해야함
                        +
                        this.memberId +
                        '&nbsp;&nbsp;' // 공백
                        +
                        dateString + ' ' + timeString // 작성 시간 (날짜와 시간)
                        +
                        '&nbsp;&nbsp;' 
                        +
                        '<input type="text" id="inquiryContent" value="' + this.inquiryContent + '"readonly>' // 내용
                        +
                        '</pre>' 
                        +
                        '</div>';
                        
                    // 해당 inquiryId 출력
                    let inquiryId = this.inquiry;
                    
                    console.log(inquiryId);
                    
                    getAllAccess(inquiryId);
                    
                }); // end each()
                
                
                $('#inquiry').html(list); // 저장된 데이터를 inquiry div 표현
            } // end function()
        ); // end getJSON()
    } // end getAllInquiry()
    
    // 문의(댓댓글) 전체 검색
    function getAllAccess(inquiryId) {
    	
    	console.log(inquiryId);
    	 
    	let url = '../access/list/' + productId;
        console.log(url);
        $.getJSON(
            url,
            function(data) {
                // data : 서버에서 전송받은 list 데이터가 저장되어 있음.
                // getJSON()에서 json 데이터는 
                // javascript object로 자동 parsing됨.
                console.log(data);

                let list = ''; // 댓글 데이터를 HTML에 표현할 문자열 변수

                // $(컬렉션).each() : 컬렉션 데이터를 반복문으로 꺼내는 함수
                $(data).each(function() {
                	 // this : 컬렉션의 각 인덱스 데이터를 의미
                    console.log(this);

                    // 전송된 replyDateCreated는 문자열 형태이므로 날짜 형태로 변환이 필요
                    let accessDateCreated = new Date(this.reviewDateCreated);
                    
                    // 날짜와 시간을 문자열로 변환하여 가져오기
                    let dateString = accessDateCreated.toLocaleDateString();
                    let timeString = accessDateCreated.toLocaleTimeString();

                    // 댓글 이미지 및 좋아요 추가 해야함 
                    if(inquiryId == this.access){
                    	list += '<div>' +
                        '<pre>' +
                        '<input type="hidden" id="access" value="' + this.access + '">' // access 추가해서 id을 바꾸지 못하게 해야함
                        +
                        this.memberId +
                        '&nbsp;&nbsp;' // 공백
                        +
                        dateString + ' ' + timeString // 작성 시간 (날짜와 시간)
                        +
                        '&nbsp;&nbsp;' 
                        +
                        '<input type="text" id="accessContent" value="' + this.accessContent + '"readonly>' // 내용
                        +
                        '</pre>' 
                        +
                        '</div>';
                    }
                    
                }); // end each()

            } // end function()
        ); // end getJSON()
    } // end getAllInquiry() */

    
    //loadImg(); 위에로 이동 하였습니다.
}); // end document
	function loadImg(){
		$(document).find('img').each(function(){
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