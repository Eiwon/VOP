<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 세션 사용할수 있게하는 코드 -->
<%@ page import="javax.servlet.http.HttpSession" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>상품 상세 조회</title>
<style>
/* 리뷰 별 폼 스타일 */
#myform fieldset {
    display: inline-block;
    direction: ltr; /* 텍스트 방향을 오른쪽에서 왼쪽으로 설정 */
    border: 0;
}

/* 라디오 버튼 숨김 */
#myform input[type=radio] {
    display: none;
}

/* 별표시 스타일 */
#myform label {
    font-size: 1em;
    color: transparent;
    text-shadow: 0 0 0 #f0f0f0;
    pointer-events: none; /* 별점 조작 비활성화 */
    cursor: default; /* 커서를 기본값으로 설정하여 클릭 이벤트 제거 */
    
}


/* end 리뷰 별 폼 스타일 */

</style>

</head>
<body>

	<%
	// 세션 객체 가져오기
	HttpSession sessionJSP = request.getSession();
	// 세션에 저장된 memberId 가져오기
	String memberId = (String) sessionJSP.getAttribute("memberId");
	%>
	
	<a href="../board/main">VOP</a>

	<!-- 상품 상세 페이지 제작 중 -->
	<h2>상품 상세 페이지</h2>

	  <div>
      <p>카테고리 : ${productVO.category }</p>
     </div>
     
       <!-- 썸네일 상품 이미지 -->
      <div>
    	<p>썸네일 이미지</p>
    	<img src="showImg?imgId=${productVO.imgId}" alt="${ImageVO.imgRealName}.${ImageVO.imgExtension}">
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
      <p>리뷰 평균 : ${reviewStar}</p>
 	 </div>
 	 
 	 <div>
      <p>댓글 총 갯수 : ${productVO.reviewNum}</p>
     </div>

 	 <div>
        <p>상품 가격 : <span id="totalPrice">${productVO.productPrice}</span></p>
    </div>
     
     <!-- 상품 증감 -->
 
     <!-- 현재 선택된 상품 수량 -->
	 <input type="number" id="quantity" value="1" min="1" max="99" maxlength="2">

     <!-- 상품 배송 정보 제작 해야함 -->
     
     <div>
      <p>판매자 : ${productVO.memberId }</p>
     </div>
 
	<!-- 장바구니 버튼 -->
    <!-- 세션 아이디가 있는 경우 -->
     <c:if test="${empty memberId}">
    	<button id="btnBasket" disabled="disabled">장바구니</button>
    	<p>로그인 후 장바구니 가능합니다.</p>
	 </c:if>
	<!-- 세션 아이디가 없 경우 -->
	<c:if test="${not empty memberId}">
    	<button id="btnBasket">장바구니</button>
	</c:if>
	
	<a href="../basket/main">장바구니 바로가기</a>

	<!-- 바로구매 버튼 -->
    <form action="설정 예정" method="post">
        <input type="hidden" name="memberId" value="${memberId}">
        <input type="hidden" name="productId" value="${productVO.productId}">
        <input type="hidden" name="productNum" value="${quantity}">
    </form>
    
    
    <!-- 세션 아이디가 있는 경우 -->
     <c:if test="${empty memberId}">
    	<button type="submit" name="action" value="checkout" disabled="disabled">바로구매</button>
    	<p>로그인 후 결제 가능합니다.</p>
	 </c:if>
	<!-- 세션 아이디가 없 경우 -->
	<c:if test="${not empty memberId}">
    	<button type="submit" name="action" value="checkout">바로구매</button>
	</c:if>

     <!-- 상품 설명 이미지 -->
     <p>상품 이미지 설명</p>
     
     <div>
    	<c:forEach items="${imageList}" var="image">
        	<img src="showImg?imgId=${image.imgId}" alt="${image.imgRealName}.${image.imgExtension}">
    	</c:forEach>
	</div>
     
     <!-- 댓글 화면 코드 및 가운데 정렬 -->
     <p>댓글</p>
     
      <div id="replies"></div>
     
     
     <!-- 좋아요 표시 제작 예정? -->
     
     <script type="text/javascript">
    
// 별표시를 업데이트하는 함수
function displayStars() {
    let value = parseInt("${reviewStar}"); // 리뷰 별점을 정수 형으로 변환
    let stars = document.querySelectorAll('#myform label'); // 별 표시 기능 가져오기
    for (let i = 0; i < stars.length; i++) {
        if (i < value) {
            stars[i].style.color = '#f0d000'; // 선택된 별보다 작은 값의 별은 노란색으로 표시
        } else {
            stars[i].style.color = 'transparent'; // 선택된 별보다 큰 값의 별은 투명하게 표시
        }
    }
}

// 상품 가격 업데이트 함수
function updateTotalPrice(quantity) {
    // 상품 가격과 수량을 가져와 곱한 값을 계산
    let productPrice = parseInt("${productVO.productPrice}");
    let totalPrice = productPrice * quantity;
    // 계산된 결과를 화면에 출력
    document.getElementById("totalPrice").innerText = totalPrice;
    
    // 수량 입력 필드 요소에도 값을 설정
    document.getElementById("quantity").value = quantity;
}


$(document).ready(function() {
	displayStars(); // 별 표시 함수
    getAllReview(); // 댓글(리뷰) 전체 검색 메소드

    // 장바구니
    $('#btnBasket').click(function(){
    	let productId = ${productVO.productId}; // 게시판 번호 데이터
        let memberId = "${memberId}"; // 작성자 데이터 // Strgin 형태여서 ""가 들어감
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
        let productId = ${productVO.productId};

        console.log(productId);
        
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
                        '<span>' + starsHTML + '</span>' + // 별점 표시
                        +
                        this.reviewStar +
                        '&nbsp;&nbsp;' // 공백
                        +
                        dateString + ' ' + timeString + // 작성 시간 (날짜와 시간)
                        +
                        '&nbsp;&nbsp;' +
                        '<input type="text" id="reviewContent" value="' + this.reviewContent + '">' // 내용
                        +
                        '&nbsp;&nbsp;' 
                        +
                        '</pre>' +
                        '</div>';
                }); // end each()

                $('#replies').html(list); // 저장된 데이터를 replies div 표현
            } // end function()
        ); // end getJSON()
    } // end getAllReply()

}); // end document

     </script>

</body>
</html>