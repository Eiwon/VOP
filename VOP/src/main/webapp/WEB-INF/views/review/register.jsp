<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리뷰 작성</title>
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
        <input type="radio" value="5" id="reviewStar"><label for="rate1">★</label>
        <input type="radio" value="4" id="reviewStar"><label for="rate2">★</label>
        <input type="radio" value="3" id="reviewStar"><label for="rate3">★</label>
        <input type="radio" value="2" id="reviewStar"><label for="rate4">★</label>
        <input type="radio" value="1" id="reviewStar"><label for="rate5">★</label>
    </fieldset>
      <input type="text" id="reviewContent">
      <!-- 이미지는 제작 중 -->
      <!-- 
        <div>
      	 <strong>상세정보 이미지</strong>
			<input id="inputDetails" type="file" name="details" multiple="multiple" onchange="showDetailsPreview()">
			<div id="previewDetails">
			</div>
		</div>
      <button id="btnAdd">등록</button>
         -->
   </div>
  
   
   <script type="text/javascript">
   
	// 이미지 확장자 필터
   //const blockedExtension = new RegExp("exe|sh|php|jsp|aspx|zip|alz");
	
   
  $(document).ready(function(){

   // 댓글 입력 코드
   $('#btnAdd').click(function(){
       let memberId = $('#memberId').val(); // 작성자 데이터
       let reviewStar = $('#reviewStar').val();// 리뷰(별)
       let reviewContent = $('#reviewContent').val(); // 댓글 내용
       // javascript 객체 생성
       let obj = {
             'memberId' : memberId,
             'reviewStar' : reviewStar,
             'replyContent' : replyContent
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
                window.location.href = '/board/mypage';
             }
          }
       });
    }); // end btnAdd.click()
    
   
   </script>
   
    


</body>
</html>