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
        <c:choose>
            <c:when test="${not empty listInquiry}">
                <c:forEach var="InquiryVO" items="${listInquiry}">
                    <tr>
                        <td>회원ID : ${InquiryVO.memberId}</td>
                        <td>상품ID : ${InquiryVO.productId}</td>
                        <td>문의 내용 : ${InquiryVO.inquiryContent}</td>
                        <td>
                            <!-- boardDateCreated 데이터 포멧 변경 -->
                            <fmt:formatDate value="${InquiryVO.inquiryDateCreated}" pattern="yyyy-MM-dd HH:mm:ss" var="inquiryDateCreated" />
                            ${inquiryDateCreated}
                        </td>
                        <td>
                            <button id="btnAdd" class="btnAdd">답글 작성</button>
                            <button id="btnModify" class="btnModify" style="display:none;">답글 수정</button>
                            <button id="btnDelete" class="btnDelete" style="display:none;">답글 삭제</button>
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
    	
    	// 답글 작성 클릭 했을 때 화면띄어주는 역할
    	$('#btnAdd').click(function() {
            // #inputContainer의 표시 여부를 토글합니다.
            $('#inputContainer').toggle();
            // 다른 컨테이너는 숨깁니다.
            $('#modifyContainer, #deleteContainer').hide();
        });
		
    	// 답글 수정 클릭 했을 때 화면띄어주는 역할
        $('#btnModify').click(function() {
            // #modifyContainer의 표시 여부를 토글합니다.
            $('#modifyContainer').toggle();
            // 다른 컨테이너는 숨깁니다.
            $('#inputContainer, #deleteContainer').hide();
        });

		// 답글 작성 비동기 코드
        $('#btnSubmit').click(function() {
            let input = $('#replyAnswer').val();
            alert('작성된 답글: ' + input);
            // 여기에 AJAX 호출 등 필요한 코드를 추가하세요.
        });
		
    	// 답글 수정 비동기 코드
        $('#btnModifySubmit').click(function() {
        	let input = $('#modifyAnswer').val();
            alert('수정된 답글: ' + input);
            // 여기에 AJAX 호출 등 필요한 코드를 추가하세요.
        });
		
     	// 답글 삭제 비동기 코드
        $('#btnDelete').click(function() {
            alert('답글이 삭제되었습니다.');
            // 여기에 AJAX 호출 등 필요한 코드를 추가하세요.
        });
        
    }); // end document
    </script>
</body>
</html>
