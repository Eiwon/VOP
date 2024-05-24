<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 시큐리티 회원id 관련 코드 -->
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>문의 리스트</title>
</head>
<body>
	<table>
    <tbody>
        <c:choose>
            <c:when test="${not empty listInquiry}">
                <c:forEach var="InquiryVO" items="${listInquiry}">
                    <tr>
                    	<td>문의ID : ${InquiryVO.inquiryId}</td>
                        <td>회원ID : ${InquiryVO.memberId}</td>
                        <td>상품ID : ${InquiryVO.productId}</td>
                        <td>문의 내용 : ${InquiryVO.inquiryContent}</td>
                        <td>
                            <!-- boardDateCreated 데이터 포멧 변경 -->
                            <fmt:formatDate value="${InquiryVO.inquiryDateCreated}" pattern="yyyy-MM-dd HH:mm:ss" var="inquiryDateCreated" />
                            ${inquiryDateCreated}
                        </td>
                        <td>
                            <button class="btnAdd" data-inquiryid="${InquiryVO.inquiryId}"  data-productid="${InquiryVO.productId}">답글 작성</button>
                            <button class="btnModify" data-inquiryid="${InquiryVO.inquiryId}" style="display:none;">답글 수정</button>
                            <button class="btnDelete" data-inquiryid="${InquiryVO.inquiryId}" style="display:none;">답글 삭제</button>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5">
                            <!-- 답글 작성 -->
                            <div id="inputContainer" class="inputContainer" style="text-align: center; margin-top: 10px; display: none;">
                                <!-- 입력 필드와 제출 버튼이 추가될 위치 -->
                                <input type="text" id="replyAnswer" placeholder="답글 작성">
                                <button id="btnSubmit" class="btnSubmit">작성</button>
                            </div>

                            <!-- 답글 수정 -->
                            <div id="modifyContainer" class="modifyContainer" style="text-align: center; margin-top: 10px; display: none;">
                                <!-- 수정 필드와 제출 버튼이 추가될 위치 -->
                                <input type="text" id="modifyAnswer" placeholder="답글 수정">
                                <button id="btnModifySubmit" class="btnModifySubmit">수정</button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="5"><p>문의 사항이 없습니다.</p></td>
                </tr>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>

    
    <script type="text/javascript">
    // 코드 작성 예정
    $(document).ready(function(){

    	const memberId = '${memberDetails.getUsername()}';
    	
    	// 답글 작성 클릭 했을 때 화면띄어주는 역할
    	$('#btnAdd').click(function() {
    		// 현재 버튼의 데이터를 가져옵니다.
            let inquiryId = $(this).data('inquiryid');
            let productId = $(this).data('productid');
            
            // #inputContainer의 표시 여부를 토글합니다.
            $('#inputContainer').toggle();
            // 다른 컨테이너는 숨깁니다.
            $('#modifyContainer, #deleteContainer').hide();
            
            // 전송할 데이터 설정
            $('#btnSubmit').data('inquiryid', inquiryId).data('productid', productId);
        }); // end btnAdd
		
    	// 답글 수정 클릭 했을 때 화면띄어주는 역할
        $('#btnModify').click(function() {
        	// 현재 버튼의 데이터를 가져옵니다.
            let inquiryId = $(this).data('inquiryid');
            
            // #modifyContainer의 표시 여부를 토글합니다.
            $('#modifyContainer').toggle();
            // 다른 컨테이너는 숨깁니다.
            $('#inputContainer, #deleteContainer').hide();
            
            // 전송할 데이터 설정
            $('#btnModifySubmit').data('inquiryid', inquiryId);
        }); // end btnModify

		// 답글 작성 비동기 코드
        $('#btnSubmit').click(function() {
            let answerContent = $('#replyAnswer').val();
            let inquiryId = $(this).data('inquiryid');
            let productId = $(this).data('productid');
            
            let obj = {
            		'inquiryId' : inquiryId,
                    'memberId' : memberId,
                    'answerContent' : answerContent
              }
              console.log(obj);
              // $.ajax로 송수신
              $.ajax({
                 type : 'POST', // 메서드 타입
                 url : '설정예정', // url
                 headers : { // 헤더 정보
                    'Content-Type' : 'application/json' // json content-type 설정
                 }, //'Content-Type' : 'application/json' 헤더 정보가 안들어가면 4050에러가 나온다.
                 data : JSON.stringify(obj), // JSON으로 변환
                 success : function(result) { // 전송 성공 시 서버에서 result 값 전송
                    console.log(result);
                    if(result == 1) {
                       alert('답변 입력 성공');
                    }
                 }
              });
        }); // end btnSubmit
		
    	// 답글 수정 비동기 코드
        $('#btnModifySubmit').click(function() {
        	let answerContent = $('#modifyAnswer').val();
        	let answerContent = $('#replyAnswer').val();
        	let inquiryId = $(this).data('inquiryid');
             
             let obj = {
             		 'inquiryId' : inquiryId,
                     'memberId' : memberId,
                     'answerContent' : answerContent
               }
              console.log(obj);
             
        	// ajax 요청
            $.ajax({
               type : 'PUT', 
               url : '설정예정',
               headers : {
                  'Content-Type' : 'application/json'
               },
               data : replyContent, 
               success : function(result) {
                  console.log(result);
                  if(result == 1) {
                     alert('답변 수정 성공!');
                  }
               }
            });
        }); // end btnModifySubmit
		
     	// 답글 삭제 비동기 코드
        $('#btnDelete').click(function() {
        	let inquiryId
        	 let obj = {
            		'inquiryId' : inquiryId,
                    'memberId' : memberId
              }
             console.log(obj);
        	// ajax 요청
            $.ajax({
               type : 'DELETE', 
               url : '설정예정',
               headers : {
                  'Content-Type' : 'application/json'
               },
               
               data : obj,
               success : function(result) {
                  console.log(result);
                  if(result == 1) {
                     alert('답변 삭제 성공!');
                  }
               }
            });
         }); // end btnDelete()

    }); // end document
    </script>
</body>
</html>
