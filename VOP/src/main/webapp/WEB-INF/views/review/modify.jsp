<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리뷰 수정</title>
<!-- 외부 CSS 파일 링크 -->
<link href="/assets/css/star.css" rel="stylesheet"/>
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
</style>
</head>
<body>
	<%
	// 세션 객체 가져오기
	HttpSession sessionJSP = request.getSession();
	// 세션에 저장된 memberId 가져오기
	String memberId = (String) sessionJSP.getAttribute("memberId");
	%>
	
 	<div style="text-align: center;" id="myform">
 	<fieldset>
        <!-- 별점 선택 라디오 버튼 -->
        <input type="radio" value="5" id="rate1"><label for="rate1">★</label>
        <input type="radio" value="4" id="rate2"><label for="rate2">★</label>
        <input type="radio" value="3" id="rate3"><label for="rate3">★</label>
        <input type="radio" value="2" id="rate4"><label for="rate4">★</label>
        <input type="radio" value="1" id="rate5"><label for="rate5">★</label>
    </fieldset>
      <input type="text" id="reviewContent">
      <!-- 이미지는 나중에 -->
      <button id="btnUpdate">수정</button>
     </div>
     
     <script type="text/javascript">
 	 // 수정 버튼을 클릭하면 선택된 댓글 수정
     $('#btnUpdate').click(function(){
        console.log(this);
        
        // 선택된 댓글의 replyId, replyContent 값을 저장
        // prevAll() : 선택된 노드 이전에 있는 모든 형제 노드를 접근
        //let replyId = $(this).prevAll('#replyId').val();
        let reviewStar = $('#reviewStar').val();// 리뷰(별)
        let reviewContent = $('#reviewContent').val();
        let reviewId = ${productVO.reviewId};// 나중에 경로 정해 졌을때 데이터가 회원 Id, 상품 Id OR 댓글 id만 있으면 됨
        
        // ajax 요청
        $.ajax({
           type : 'PUT', 
           url : '../review/' + replyId,// 경로 나중에
           headers : {
              'Content-Type' : 'application/json'
           },
           data : replyContent, 
           success : function(result) {
              console.log(result);
              if(result == 1) {
                 alert('댓글 수정 성공!');
                 window.location.href = '/board/mypage';
              }
           }
        });
        
     }); // end replies.on()
     
     
     </script>



</body>
</html>