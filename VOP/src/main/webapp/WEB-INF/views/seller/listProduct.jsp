<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>등록한 상품 조회</title>
</head>
<body>
	<div>
		<strong><%=request.getSession().getAttribute("memberId") %> 님이 등록한 상품</strong>
	</div>
	<table>
		<thead>
			<tr>
				<th>번호</th>
				<th>이름</th>
				<th>가격</th>
				<th>리뷰 수</th>
				<th>재고</th>
				<th>위치</th>
				<th>상태</th>
				<th>카테고리</th>
				<th>수정</th>
				<th>삭제</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="productVO" items="${productList }">
				<tr class="productItem" onclick="toDetails(this)">
					<td class="productId">${productVO.productId }</td>
					<td>${productVO.productName }</td>
					<td>${productVO.productPrice }</td>
					<td>${productVO.reviewNum }</td>
					<td>${productVO.productRemains }</td>
					<td>${productVO.productPlace }</td>
					<td class="productState">${productVO.productState }</td>
					<td>${productVO.category }</td>
					<td onclick="requestUpdate(this)">ㅇ</td>
					<td onclick="requestDelete(this)">X</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<ul>
      <!-- 이전 버튼 생성을 위한 조건문 -->
      <c:if test="${pageMaker.isPrev() }">
         <li><a href="listProduct?memberId=<%=request.getSession().getAttribute("memberId") %>&pageNum=${pageMaker.startNum - 1}">이전</a></li>
      </c:if>
      <!-- 반복문으로 시작 번호부터 끝 번호까지 생성 -->
      <c:forEach begin="${pageMaker.startNum }"
         end="${pageMaker.endNum }" var="num">
         <li><a href="listProduct?memberId=<%=request.getSession().getAttribute("memberId") %>&pageNum=${num }">${num }</a></li>
      </c:forEach>
      <!-- 다음 버튼 생성을 위한 조건문 -->
      <c:if test="${pageMaker.isNext() }">
         <li><a href="listProduct?memberId=<%=request.getSession().getAttribute("memberId") %>&pageNum=${pageMaker.endNum + 1}">다음</a></li>
      </c:if>
   </ul>

	<script type="text/javascript">
		function requestUpdate(input){
			const selectedId = $(input).siblings('.productId').text();
			console.log(selectedId);
			
			location.href="updateProduct?productId=" + selectedId;
			
		} // end requestUpdate
		
		function requestDelete(input) {
			const selectedId = $(input).siblings('.productId').text();
			console.log(selectedId);
			
			$.ajax({
				method : 'POST',
				url : 'delReqProduct',
				data : {
					'productId' : selectedId
				},
				success : function(result){
					console.log(result);
					if(result == 1){
						$(input).siblings('.productState').text("삭제 대기중");
					}else if(result == 2){
						$(input).siblings('.productState').text("삭제됨");
					}
				}
			}); // end ajax
		} // end requestDelete
		
		function toDetails(input){
			let productId = $(input).find('.productId').text();
			location.href = '../product/detail?productId=' + productId;
		} // end toDetails
	</script>

</body>
</html>