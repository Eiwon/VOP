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
	
	#pageSelector {
		list-style: none;
		flex-direction: row;
		display: flex;
	}
	/* 별표시 스타일 */
	#myform label {
		font-size: 1em;
		color: transparent;
		text-shadow: 0 0 0 #f0f0f0;
		margin-right: 10px; /* 별 사이의 간격 설정 */
	}
	#myform fieldset {
		display: inline-block;
		direction: ltr; /* 텍스트 방향을 오른쪽에서 왼쪽으로 설정 */
		border: 0;
	}
	.reviewStars {
		color: #f0d000; /* 별 색상 */
	}
	.body_container{
		width: 60%;
		margin: auto;
	}
    .product_container {
		width: 200px;
		heigth : 30px;
		margin-right: 20px;
		margin-bottom: 20px;
	}
</style>
<title>상품 검색</title>
</head>
<body>
	<div class="body_container">
	<c:set var="category" value="${pageMaker.pagination.category }"></c:set>
	<c:set var="word" value="${pageMaker.pagination.word }"></c:set>
	<c:if test="${productList.size() == 0 }">
		<div>
			검색 결과가 없습니다
		</div>
	</c:if>
    
	<div id="searchResult" class="product_list row row-cols-2">
		<c:forEach var="productPreviewDTO" items="${productList}">
			<div class="product_container card col" onclick="toDetails(this)">
					<c:set var="productVO" value="${productPreviewDTO.productVO }"></c:set>
					<img class="card-img-top" src="${productPreviewDTO.imgUrl }">
					<div class="card-body">
						<input class="productId" hidden="hidden" value="${productVO.productId }">
						<h3 class="card-title">${productVO.productName }</h3>
						<p>${productVO.category }</p>
						<p class="card-text">${productVO.productPrice }원 </p>
						<span class="reviewStars"> 
						<c:forEach var="i" begin="1" end="5">
							<c:choose>
								<c:when test="${i <= productVO.reviewAvg}">
									&#9733;
								</c:when>
								<c:otherwise>
									&#9734;
								</c:otherwise>
							</c:choose>
						</c:forEach>
						</span>
						<span class="review_num"> (${productVO.reviewNum}) </span>
					</div>
			</div>
		</c:forEach>
	</div>   
	<nav aria-label="Search results pages">
	<ul id="pageSelector" class="pagination pagination-lg justify-content-center">
		<li class="page-item <c:if test="${!pageMaker.isPrev() }">disabled</c:if>" >
        	<a class="page-link" href="search?category=${category }&word=${word }&pageNum=${pageMaker.startNum - 1}">
         	&laquo;</a>
        </li>
      	<c:forEach begin="${pageMaker.startNum }" end="${pageMaker.endNum }" var="num">
         	<li class="page-item <c:if test="${pageMaker.curPage eq num }">active</c:if>">
         	<a class="page-link" href="search?category=${category }&word=${word }&pageNum=${num }">${num }</a>
         	</li>
      	</c:forEach>
      	<li class="page-item <c:if test="${!pageMaker.isNext() }">disabled</c:if>">
         	<a class="page-link" href="search?category=${category }&word=${word }&pageNum=${pageMaker.endNum + 1}">
         	&raquo;
         	</a>
        </li>
	</ul>
	</nav>
	</div>
	<script type="text/javascript">
		
		$('.search-input').val('${word}');
		$('#boxCategory').find('option[value="${category}"]').attr('selected', 'selected');
		
		function toDetails(input){
			let productId = $(input).find('.productId').val();
			window.open('detail?productId=' + productId);
		} // end toDetails
		
	</script>
	</body>
</html>