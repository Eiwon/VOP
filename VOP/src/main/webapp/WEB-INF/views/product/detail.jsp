<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
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
	<!-- 상품 상세 페이지 제작 중 -->
	<h2>상품 상세 페이지</h2>

	  <div>
      <p>카테고리 : ${productVO.category }</p>
     </div>
     
       <!-- 우선 상품 이미지 불러오기 제작 완료-->
      <div>
      	<p>이미지</p>
      	 <p><a href="download?productId=${productVO.productId }">
  		 ${productVO.imgRealName }.${productVO.imgExtension }</a></p>
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
      <p>댓글 총 갯수 : ${reviewCount}</p>
     </div>

 	 <div>
        <p>상품 가격 : <span id="totalPrice">${productVO.productPrice}</span></p>
    </div>
     
     <!-- 상품 증감 제작 중 -->
 
     <!-- 현재 선택된 상품 수량 -->
	 <input type="number" id="quantity" value="1" min="1" max="99" maxlength="2">

     <!-- 상품 배송 정보 제작 해야함-->
     
     <div>
      <p>판매자 : ${productVO.memberId }</p>
     </div>
     
     
     <!-- 장바구니 -->
	
	<form id="addToCartForm" action="장바구니_처리_URL" method="post">
    	<!-- 데이터 (화면에 표시되지 않음), 도착예정, 수량 작성 예정 -->
    	<input type="hidden" name="productId" value="${productVO.productId}">
    	<input type="hidden" name="quantityInput" id="quantityInput" value="1">
	</form>

	<button type="button" onclick="addToCart()">장바구니</button>
	
    
	<!-- 바로구매 버튼 -->
    <form action="설정 예정" method="post">
        <button type="submit" name="action" value="checkout">바로구매</button>
        <!-- 보낼 데이터 작성 예정 -->
    </form>
</body>
     
     <!-- 댓글 화면 코드 및 가운데 정렬 -->
     <p>댓글</p>
     <div><!-- style="text-align: center;" 가운데 정렬-->
      <div id="replies"></div>
     </div>
     
     <!-- 좋아요 표시 제작 예정 -->
     
     <script type="text/javascript">
     // 상품 수량 증감 코드
     // 제작 중 아직 미완성
     
     // 별 표시
     // 페이지 로드될 때 실행되는 함수(별표시 먼저해주는 역할)
    // 상품 수량 증감 코드

// 페이지 로드될 때 실행되는 함수(별표시 먼저해주는 역할)
window.onload = function() {
    displayStars(); // 별표시 초기화 함수 호출
    // 사용자가 수량 입력 필드를 직접 조작할 때 이벤트 핸들러 추가
    document.getElementById("quantity").addEventListener("input", function() {
        // 입력된 수량을 가져와 상품 가격을 업데이트
        updateTotalPrice(parseInt(this.value));
    });
}

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
    // 장바구니에도 수량을 업데이트
    document.getElementById("quantityInput").value = quantity;
}

// 장바구니로 데이터 보내주는 역할
function addToCart() {
    let quantity = parseInt(document.getElementById("quantity").value);
    if (quantity <= 0) {
        alert("수량은 1 이상이어야 합니다.");
        return;
    }
    document.getElementById("quantityInput").value = quantity; // 수량 업데이트
    document.getElementById("addToCartForm").submit(); // 폼 제출
}



$(document).ready(function() {

    getAllReview(); // 댓글(리뷰) 전체 검색 메소드


    // 댓글(리뷰) 전체 검색 // 이미지 및 좋아요 아직 추가 안함
    function getAllReview() {
        let productId = ${productVO.productId};
        //let productId = "${productVO.productId}";

        console.log(productId);

        // url 변경 해야함
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

                    // 댓글 이미지 및 좋아요 추가 해야함
                    list += '<div>' +
                        '<pre>' +
                        '<input type="hidden" id="review" value="' + this.review + '">' // readonly 추가해서 id을 바꾸지 못하게 해야함
                        +
                        this.memberId +
                        '&nbsp;&nbsp;' // 공백
                        +
                        '<input type="hidden" id="reviewStar" value="' + this.review + '">' // 별점 숫자만 불려와서 별 형식으로 바꾸어야함
                        +
                        this.reviewStar +
                        '&nbsp;&nbsp;' // 공백
                        +
                        reviewDateCreated // 작성 시간
                        +
                        '&nbsp;&nbsp;' +
                        '<input type="text" id="reviewContent" value="' + this.reviewContent + '">' // 내용
                        +
                        '&nbsp;&nbsp;' // 혹시 몰라 나중에 수정 현재는 아무 의미 없는 코드
                        // + '<button class="btn_update" >수정</button>'
                        // 혹시 몰라 나중에 수정 현재는 아무 의미 없는 코드
                        // + '<button class="btn_delete" >삭제</button>'
                        // 혹시 몰라 나중에 수정 현재는 아무 의미 없는 코드
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