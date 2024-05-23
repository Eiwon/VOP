<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>리뷰 전체 검색</title>
<style>
/* 박스 스타일 */
.product-box {
    border: 1px solid #ccc;
    padding: 20px;
    margin: 20px 0;
    border-radius: 10px;
    background-color: #f9f9f9;
}

.product-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.product-info {
    flex-grow: 1;
}

.product-image {
    max-width: 150px;
    margin-right: 20px;
}

.review-content {
    margin-top: 10px;
    padding-top: 10px;
    border-top: 1px solid #ccc;
}

.review-content p {
    margin: 5px 0;
}

/* 리뷰 별 폼 스타일 */
#myform fieldset {
    display: inline-block;
    direction: ltr; /* 텍스트 방향을 오른쪽에서 왼쪽으로 설정 */
    border: 0;
}

/* 별표시 스타일 */
#myform label {
    font-size: 1em;
    color: transparent;
    text-shadow: 0 0 0 #f0f0f0;
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
<p>댓글 리스트</p>

<c:forEach var="ProductVO" items="${productList}">
    <div class="product-box">
        <div class="product-header">
            <div class="product-info">
            	<!-- 리뷰 수정 코드 -->
	                <form action="../review/modify" method="get">
	                	<input type="hidden" name="productId" value="${ProductVO.productId}">
	                	<input type="hidden" name="imgId" value="${ProductVO.imgId}">
	                	<input type="hidden" name="memberId" value="${memberId}">
	                	<button type="submit">리뷰 수정</button>
	                </form>
            	 <!-- 리뷰 삭제 코드 -->
	                <form action="../review/delete" method="POST">
	                	<input type="hidden" name="productId" value="${ProductVO.productId}">
	                	<input type="hidden" name="memberId" value="${memberId}">
	                	<button type="submit">리뷰 삭제</button>
	                </form>
                <h2>${ProductVO.productName}</h2>
                <p>리뷰 평균: ${ProductVO.reviewAvg}</p>
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
                
            </div>
            <img class="product-image" src="../product/showImg?imgId=${ProductVO.imgId}" alt="${ProductVO.productName}">
        </div>
        <div class="review-content">
            <c:forEach var="ReviewVO" items="${reviewList}">
                <c:if test="${ReviewVO.productId == ProductVO.productId}">
                    <p>리뷰 내용 : ${ReviewVO.reviewContent}</p>
                    <fmt:formatDate value="${ReviewVO.reviewDateCreated }"
                  		pattern="yyyy-MM-dd HH:mm:ss" var="reviewDateCreated" />
               		<td>${reviewDateCreated }</td>
                </c:if>
            </c:forEach>
        </div>
    </div>
</c:forEach>

<script type="text/javascript">
$(document).ready(function() {
	
	displayStars();
// 별표시를 업데이트하는 함수
function displayStars() {
    let value = parseInt("${ProductVO.reviewAvg}"); // 리뷰 별점을 정수 형으로 변환
    let stars = document.querySelectorAll('#myform label'); // 별 표시 기능 가져오기
    console.log(value);
    for (let i = 0; i < stars.length; i++) {
        if (i < value) {
            stars[i].style.color = '#f0d000'; // 선택된 별보다 작은 값의 별은 노란색으로 표시
        } else {
            stars[i].style.color = 'transparent'; // 선택된 별보다 큰 값의 별은 투명하게 표시
        }
    }
}

}); // end document
</script>

</body>
</html>
