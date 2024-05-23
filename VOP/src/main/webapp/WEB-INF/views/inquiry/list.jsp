<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문의 리스트</title>
</head>
<body>
	<table>
	  <tbody>
	  	<c:if test="${not empty listInquiry}"><!-- listInquiry null이 아니면 밑에 코드 실행  -->
         <c:forEach var="InquiryVO" items="${listInquiry}">
            <tr>
               <td>${InquiryVO.productId}</td>
               <td>${InquiryVO.inquiryContent}</td>
               <td>${InquiryVO.memberId}</td>
               <!-- boardDateCreated 데이터 포멧 변경 -->
               <fmt:formatDate value="${InquiryVO.inquiryDateCreated}"
                  pattern="yyyy-MM-dd HH:mm:ss" var="inquiryDateCreated" />
               <td>${inquiryDateCreated}</td>
            </tr>
            <button id="btnAdd" >답글 작성</button> 
            <button id="btnModify" >답글 수정</button> 
            <button id="btnDelete" >답글 삭제</button> 
         </c:forEach>
        </c:if>
        <!-- listInquiry null이면 코드 실행 -->
        <c:otherwise>
        	<p>문의 사항이 없습니다.</p>
    	</c:otherwise>
      </tbody>
    </table>
    
    <script type="text/javascript">
    // 코드 작성 예정
    $(document).ready(function(){
    	
    	// 답글 작성 코드
    	$('#btnAdd').click(function(){
        	let boardId = $('#boardId').val(); // 게시판 번호 데이터
            let memberId = $('#memberId').val(); // 작성자 데이터
            let replyContent = $('#replyContent').val(); // 댓글 내용
            // javascript 객체 생성
            let obj = {
                  'boardId' : boardId,
                  'memberId' : memberId,
                  'replyContent' : replyContent
            }
            console.log(obj);
            
            // $.ajax로 송수신
            $.ajax({
               type : 'POST', // 메서드 타입
               url : '../reply', // url
               headers : { // 헤더 정보
                  'Content-Type' : 'application/json' // json content-type 설정
               }, //'Content-Type' : 'application/json' 헤더 정보가 안들어가면 4050에러가 나온다.
               data : JSON.stringify(obj), // JSON으로 변환
               success : function(result) { // 전송 성공 시 서버에서 result 값 전송
                  console.log(result);
                  if(result == 1) {
                     alert('댓글 입력 성공');
                     getAllReply(); // 함수 호출
                  }
               }
            });
         }); // end btnAdd.click()
    )}; // end document
    </script>
</body>
</html>
