<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<style type="text/css">
	.product_container {
		border: 1px solid black;
		width: 200px;
	}
	#pageSelector {
		list-style: none;
		flex-direction: row;
		display: flex;
	}
</style>
<title>상품 검색</title>
</head>
<body>
	
	<c:if test="${productList.size() == 0 }">
		<div>
			검색 결과가 없습니다
		</div>
	</c:if>
    
	<div id="searchResult">
		<c:forEach var="productPreviewDTO" items="${productList}">
			<div class="product_container" onclick="toDetails(this)">
					<c:set var="productVO" value="${productPreviewDTO.productVO }"></c:set>
					<img src="${productPreviewDTO.imgUrl }">
					<div class="productId" hidden="hidden">${productVO.productId }</div>
					<div>${productVO.productName }</div>
					<div>${productVO.productPrice }</div>
					<div>${productVO.reviewNum }</div>
					<div>${productVO.productRemains }</div>
					<div>${productVO.productPlace }</div>
					<div>${productVO.category }</div>
			</div>
		</c:forEach>
	</div>   
	<div id="pageSelector">
      	<c:if test="${pageMaker.isPrev() }">
         	<li><a href="search?category=${category }&word=${word }&pageNum=${pageMaker.startNum - 1}">이전</a></li>
      	</c:if>
      	<c:forEach begin="${pageMaker.startNum }" end="${pageMaker.endNum }" var="num">
         	<li><a href="search?category=${category }&word=${word }&pageNum=${num }">${num }</a></li>
      	</c:forEach>
      	<c:if test="${pageMaker.isNext() }">
         	<li><a href="search?category=${category }&word=${word }&pageNum=${pageMaker.endNum + 1}">다음</a></li>
      	</c:if>
	</div>
	
	<script type="text/javascript">
		
		function toDetails(input){
			let productId = $(input).find('.productId').text();
			location.href = 'detail?productId=' + productId;
		} // end toDetails
		
	</script>
</body>
</html>