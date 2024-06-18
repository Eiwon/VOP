<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
tr {
	display: flex;
	flex-direction: row;
}
.form_info {
	border: 1px solid black;
}
</style>
<title>상품 상세 정보</title>
</head>
<body>
	<c:set var="productVO" value="${productDetails.productVO }"/>
	<c:set var="memberVO" value="${productDetails.memberVO }"/>
	<h2>상품 상세 정보</h2>
	<table class="form_info">
		<tbody>
			<tr>
				<td>판매자 ID</td>
				<td>${memberVO.memberId }</td>
			</tr>
			<tr>
				<td>판매자 이름</td>
				<td>${memberVO.memberName }</td>
			</tr>
			<tr>
				<td>판매자 전화번호</td>
				<td>${memberVO.memberPhone }</td>
			</tr>
			<tr>
				<td>판매자 이메일</td>
				<td>${memberVO.memberEmail }</td>
			</tr>
			<tr>
				<td>사업체 명</td>
				<td>${productDetails.businessName }</td>
			</tr>
			<tr>
				<td>요청 시간</td>
				<td>${productVO.productDateCreated.toLocaleString() }</td>
			</tr>
			<tr>
				<td>등록 번호</td>
				<td>${productVO.productId }</td>
			</tr>
			<tr>
				<td>상품명</td>
				<td>${productVO.productName }</td>
			</tr>
			<tr>
				<td>분류</td>
				<td>${productVO.category }</td>
			</tr>
			<tr>
				<td>가격</td>
				<td>${productVO.productPrice }</td>
			</tr>
			<tr>
				<td>재고</td>
				<td>${productVO.productRemains }</td>
			</tr>
			<tr>
				<td>보관 위치</td>
				<td>${productVO.productPlace }</td>
			</tr>
			<tr>
				<td>썸네일</td>
				<td><img src="${productDetails.thumbnailUrl}"></td>
			</tr>
			<c:forEach items="${productDetails.detailsUrl }" var="imgUrl">
			<tr>
				<td><img src="${imgUrl}"></td>
			</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<c:if test="${productVO.productState eq '삭제 대기중' }">
				<tr>
					<td><button onclick="deleteProduct()">삭제</button></td>
					<td><button onclick="window.close()">취소</button></td>
				</tr>
			</c:if>
			<c:if test="${productVO.productState eq '승인 대기중' }">
			
			</c:if>
			<tr>
				<td><button onclick="sendResult('판매중')">승인</button></td>
				<td><button onclick="sendResult('판매 불가')">거부</button></td>
			</tr>
		</tfoot>
	</table>

	<script type="text/javascript">
	
	function sendResult(productState){
		
		$.ajax({
			url : 'changeState',
			headers : {
				'Content-Type' : 'application/json'
			},
			method : 'PUT',
			data : JSON.stringify({
				'productId' : '${productVO.productId}',
				'productState' : productState
			}),
			success : function(result){
				window.close();	
			}
		}); // end ajax
	} // end sendResult
	
	function deleteProduct(){
		// 상품 삭제 요청
		$.ajax({
			url : 'product',
			headers : {
				'Content-Type' : 'application/json'
			},
			method : 'DELETE',
			data : JSON.stringify({
				'productId' : '${productVO.productId}'
			}),
			success : function(result){
				window.close();	
			}
		}); // end ajax
		
	} // end deleteProduct
	
	
	function toDate(timestamp){
		let date = new Date(timestamp);
		let formatted = (date.getYear() + 1900) + '/' + (date.getMonth() + 1) + '/' + date.getDate() + ' ' + 
				date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
		return formatted;
	} // end toDate
		
	</script>
</body>
</html>