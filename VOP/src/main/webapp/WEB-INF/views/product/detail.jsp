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
#myform fieldset{
    display: inline-block;
    direction: rtl;
    border:0;
}

/* 라디오 버튼 숨김 */
#myform input[type=radio]{
    display: none;
}
/* 별표시 스타일 */
#myform label{
    font-size: 3em;
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
/* end 리뷰 별 폼 스타일 */

</style>

</head>
<body>
	<!-- 상품 상세 페이지 제작 중 -->
	<h2>상품 상세 페이지</h2>
	
	 <!-- 상품 이미지 불러오기 제작 예정 -->
	 
	 <div>
      <p>상품 이름 : ${productVO.productName }</p>
     </div>
     
     <!-- 리뷰 평균 제작 중 -->
     <div>
      <p>리뷰 평균 : </p>
 	 </div>
 	 <div>
      <p>상품 가격 : ${productVO.productPrice }</p>
     </div>
     
     <!-- 상품 증감 제작 중 -->
     <!-- 클릭 했을때 상품 감소 -->
     <button class="decrease">-</button>
     
     <!-- 현재 선택된 상품 수량 -->
	 <span id="quantity">1</span>
	 
	 <!-- 클릭 했을때 상품 증가 -->
	 <button class="increase">+</button>
     <!-- 상품 배송 정보 제작 해야함-->
     
     <div>
      <p>판매자 : ${productVO.memberId }</p>
     </div>
     <div>
      <p>상품 번호 : ${productVO.productId }</p>
     </div>
     
     
     
     <!-- 댓글 화면 코드 및 가운데 정렬 -->
     <div style="text-align: center;">
      <div id="replies"></div>
     </div>
     
     <!-- 좋아요 표시 제작 예정 -->
     
     <script type="text/javascript">
     // 상품 수량 증감 코드
     // 제작 중 아직 미완성
     $(document).ready(function() {
    	 getAllReview(); // 댓글(리뷰) 전체 검색 메소드
    	    $('.decrease').on('click', function() {
    	        let productId = ${productVO.productId};
    	        let productRemains = parseInt($('#productRemains').text());
    	        if (productRemains > 1) {
    	        	productRemains--;
    	            updateRemains(productId, productRemains);
    	        }
    	    }); // end decrease
    	    
    	    $('.increase').on('click', function() {
    	    	let productId = ${productVO.productId};
    	    	let productRemains = parseInt($('#productRemains').text());
    	        productRemains++;
    	        updateRemains(productId, productRemains);
    	    });// end increase
    	    
    	    function updateRemains(productId, productRemains) {
    	        $.ajax({
    	            type: "POST",
    	            url: "/updateRemains",
    	            contentType: "application/json",
    	            data: JSON.stringify({ productId: productId, productRemains: productRemains }),
    	            success: function(response) {
    	                // 성공 시 수행할 작업
    	                $('#productRemains').text(productRemains);
    	            },
    	            error: function(xhr, status, error) {
    	                // 오류 발생 시 수행할 작업
    	                console.error("수량 업데이트 실패: " + error);
    	            }
    	        });// end ajax
    	    }// end updateRemains()
    	    
    	}); // end document
     // end 상품 수량 증감 코드 
     
      // 댓글(리뷰) 전체 검색
      function getAllReview() {
          let productId = $('#productVO.productId').val();
          
          // url 변경 해야함
          let url = '../review/all/' + productId;
          $.getJSON(
             url,       
             function(data) {
                // data : 서버에서 전송받은 list 데이터가 저장되어 있음.
                // getJSON()에서 json 데이터는 
                // javascript object로 자동 parsing됨.
                console.log(data);
                
                let list = ''; // 댓글 데이터를 HTML에 표현할 문자열 변수
                
                // $(컬렉션).each() : 컬렉션 데이터를 반복문으로 꺼내는 함수
                $(data).each(function(){
                   // this : 컬렉션의 각 인덱스 데이터를 의미
                   console.log(this);
                  
                   // 전송된 replyDateCreated는 문자열 형태이므로 날짜 형태로 변환이 필요
                   let reviewDateCreated = new Date(this.reviewDateCreated);

                   list += '<div>'
                      + '<pre>'
                      + '<input type="hidden" id="review" value="'+ this.review +'">'// readonly 추가해서 id을 바꾸지 못하게 해야함
                      + this.memberId
                      + '&nbsp;&nbsp;' // 공백
                      + '<input type="text" id="reviewContent" value="'+ this.reviewContent +'">'
                      + '&nbsp;&nbsp;'
                      + reviewDateCreated
                      + '&nbsp;&nbsp;'
                     // + '<button class="btn_update" >수정</button>'// 혹시 몰라 나중에 수정 현재는 아무 의미 없는 코드
                     // + '<button class="btn_delete" >삭제</button>'// 혹시 몰라 나중에 수정 현재는 아무 의미 없는 코드
                      + '</pre>'
                      + '</div>';
                }); // end each()
                   
                $('#replies').html(list); // 저장된 데이터를 replies div 표현
             } // end function()
          ); // end getJSON()
       } // end getAllReply()
    	
    
     </script>
     
    
     
     
     
		
</body>
</html>