<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<!-- JSTL관련 코드 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>리뷰 전체 검색</title>

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
	
	<!-- 댓글 화면 코드 및 가운데 정렬 -->
    <p>댓글 리스트</p>

	<tbody>
         <c:forEach var="ReviewVO" items="${reviewList }">
         	<c:forEach var="ProductVO" items="${productList }">
         		<td><img src="../product/showImg?imgId=${productList.imgId}" 
         		alt="${productList.imgRealName}.${productList.imgExtension}"></td>
         		<td>productList.</td>
         	</c:forEach>
            <tr>
               <td>${ReviewVO.reviewId}</td>
            </tr>
         </c:forEach>
     </tbody>
	
</body>
</html>