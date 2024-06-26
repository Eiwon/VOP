<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 시큐리티 코드 -->
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
    <sec:authentication var="memberDetails" property="principal"/>
</sec:authorize> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<title>리뷰 작성</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<!-- 위에 창코드 -->
<jsp:include page="../include/header.jsp"></jsp:include>
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
    <c:set var="productVO" value="${productPreviewDTO.productVO }"></c:set>
    <h1>리뷰 작성</h1>
    
    <h1>${memberDetails.getUsername() }</h1>
    
    <div>
        <p>상품 번호 : ${productVO.productId}</p>
    </div>
    
    <p>이미지 썸네일</p>
      <div>
        <img src="${productPreviewDTO.imgUrl}">
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
    <input type="hidden" id="memberId" value="${memberDetails.getUsername()}"><!-- 여기 부분 없어도 될뜻 -->
    <input type="hidden" id="productId" value="${productVO.productId}"><!-- 여기 부분 없어도 될뜻 -->
    <input type="text" id="reviewContent"><br>
    <button id="btnAdd">등록</button>
</div>

<script type="text/javascript">


   
$(document).ready(function(){
        
    let selectedStar; // 전역 변수로 selectedStar 선언
    let memberId = "${memberDetails.getUsername()}";
 
    
    // 라디오 버튼 클릭 이벤트 핸들러
    $('#starFieldset input[type="radio"]').click(function() {
        // 선택된 별의 값 가져오기
        selectedStar = $(this).val();
        // reviewStar 변수에 선택된 별 값 할당
        $('#reviewStar').val(selectedStar);
        
        console.log(selectedStar);
    });

   // 댓글 입력 코드
   $('#btnAdd').click(function(event){
       let productId = "${productVO.productId}"; 
       let reviewStar = selectedStar;// 리뷰(별)
       let reviewContent = $('#reviewContent').val(); // 댓글 내용
       
       console.log("reviewStar : " + reviewStar);
       console.log("reviewContent : " + reviewContent);
       
        if(reviewStar !== null && reviewStar !== '' && reviewStar !== undefined && reviewContent !== null && reviewContent !== '') {
    	// javascript 객체 생성
           let obj = {    
                 'memberId' : memberId,
                 'productId' : productId,
                 'reviewStar' : reviewStar,
                 'reviewContent' : reviewContent
           };
           console.log(obj);
           
           // $.ajax로 송수신
           $.ajax({
              type : 'POST', // 메서드 타입
              url : '../reviewRest/register', // url
              headers : { // 헤더 정보
                 'Content-Type' : 'application/json', // json content-type 설정
                 'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
              }, //'Content-Type' : 'application/json' 헤더 정보가 안들어가면 4050에러가 나온다.
              data : JSON.stringify(obj), // JSON으로 변환
              success : function(result) { // 전송 성공 시 서버에서 result 값 전송
                 console.log(result);
                 if(result == 1) {
                    alert('댓글 입력 성공');
                    // 댓글 입력 완료 하면 마이페이지로 이동
                    window.location.href = '../review/list?memberId=' + memberId;
                 } else {
                     alert('이미 등록하신 리뷰 입니다.');
                     window.location.href = '../review/list?memberId=' + memberId;
                 }
              } // end success 
           }); // end ajax
       } else {
    	   alert('별 표시와 내용을 모두 입력 해주세요');
       } 
       
    }); // end btnAdd.click()
    
  }); // end document.ready()

</script>

</body>
</html>