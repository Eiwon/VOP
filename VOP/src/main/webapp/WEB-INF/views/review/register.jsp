<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리뷰 작성</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style>
/* 리뷰 별 폼 스타일 */
#myform fieldset {
    display: inline-block;
    direction: rtl; /* 텍스트 방향을 오른쪽에서 왼쪽으로 설정 */
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
}
/* 별표시에 마우스 호버 시 효과 */
#myform label:hover{
    text-shadow: 0 0 0 rgba(250, 208, 0, 0.99);
}
/* 이전 별표시에 마우스 호버 시 효과 */
#myform label:hover ~ label{
    text-shadow: 0 0 0 rgba(250, 208, 0, 0.99);
}
/* 선택된 별표시 스타일 */
#myform input[type=radio]:checked ~ label{
    text-shadow: 0 0 0 rgba(250, 208, 0, 0.99);
}
</style>

</head>
<body>
	
	<%
	// 세션 객체 가져오기
	HttpSession sessionJSP = request.getSession();
	// 세션에 저장된 memberId 가져오기
	String memberId = (String) sessionJSP.getAttribute("memberId");
	%>
	
	<h1>리뷰 작성</h1>
	
	<div>
		<p>상품 번호 "${productId}"</p>
	</div>
	
	<p>이미지 썸네일</p>
	  <div>
    	<img src="../product/showImg?imgId=${imgId}" alt="${imgRealName}.${imgExtension}">
	  </div>
	  

	
<div id="myform">
    
    <fieldset id="starFieldset">
        <!-- 별점 선택 라디오 버튼 -->
        <input type="radio" name="reviewStar" value="5" id="rate1"><label for="rate1">★</label>
        <input type="radio" name="reviewStar" value="4" id="rate2"><label for="rate2">★</label>
        <input type="radio" name="reviewStar" value="3" id="rate3"><label for="rate3">★</label>
        <input type="radio" name="reviewStar" value="2" id="rate4"><label for="rate4">★</label>
        <input type="radio" name="reviewStar" value="1" id="rate5"><label for="rate5">★</label>
    </fieldset><br>
    <input type="hidden" id="memberId" value="${memberId}"><!-- 여기 부분 없어도 될뜻 -->
    <input type="hidden" id="productId" value="${order.productId}"><!-- 여기 부분 없어도 될뜻 -->
    <input type="text" id="reviewContent"><br>
    <button id="btnAdd">등록</button>
    
    
</div>

<script type="text/javascript">
   
$(document).ready(function(){
	
	let selectedStar; // 전역 변수로 selectedStar 선언
	
    // 라디오 버튼 클릭 이벤트 핸들러
    $('#starFieldset input[type="radio"]').click(function() {
        // 선택된 별의 값 가져오기
        selectedStar = $(this).val();
        // reviewStar 변수에 선택된 별 값 할당
        $('#reviewStar').val(selectedStar);
        
        console.log(selectedStar);
    });

   // 댓글 입력 코드
   $('#btnAdd').click(function(){
       let memberId = $('#memberId').val(); // 작성자 데이터
       let productId = ${productId}; 
       let reviewStar = selectedStar;// 리뷰(별)
       let reviewContent = $('#reviewContent').val(); // 댓글 내용
       
       console.log(reviewStar);
       
       // javascript 객체 생성
       let obj = {	
             'memberId' : memberId,
             'productId' : productId,
             'reviewStar' : reviewStar,
             'reviewContent' : reviewContent
       }
       console.log(obj);
       
       // $.ajax로 송수신
       $.ajax({
          type : 'POST', // 메서드 타입
          url : '../review/register', // url
          headers : { // 헤더 정보
             'Content-Type' : 'application/json' // json content-type 설정
          }, //'Content-Type' : 'application/json' 헤더 정보가 안들어가면 4050에러가 나온다.
          data : JSON.stringify(obj), // JSON으로 변환
          success : function(result) { // 전송 성공 시 서버에서 result 값 전송
             console.log(result);
             if(result == 1) {
                alert('댓글 입력 성공');
                // 댓글 입력 완료 하면 마이페이지로 이동
                window.location.href = '../board/orderlist';
             }
          } // end success 
       }); // end ajax
    }); // end btnAdd.click()
    
  }); // end document.ready()

    </script>

</body>
</html>