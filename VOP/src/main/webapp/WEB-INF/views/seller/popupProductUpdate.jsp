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
	<h2>상품 상세 정보</h2>
	<table class="form_info">
		<tbody>
			<tr>
				<td>판매자 ID</td>
				<td>${productDetails.memberId }</td>
			</tr>
			<tr>
				<td>판매자 이름</td>
				<td>${productDetails.memberName }</td>
			</tr>
			<tr>
				<td>판매자 전화번호</td>
				<td>${productDetails.memberPhone }</td>
			</tr>
			<tr>
				<td>판매자 이메일</td>
				<td>${productDetails.memberEmail }</td>
			</tr>
			<tr>
				<td>사업체 명</td>
				<td>${productDetails.businessName }</td>
			</tr>
			<tr>
				<td>요청 시간</td>
				<td>${productDetails.productDateCreated }</td>
			</tr>
			<tr>
				<td>등록 번호</td>
				<td>${productDetails.productId }</td>
			</tr>
			<tr>
				<td>상품명</td>
				<td>${productDetails.productName }</td>
			</tr>
			<tr>
				<td>분류</td>
				<td>${productDetails.category }</td>
			</tr>
			<tr>
				<td>가격</td>
				<td>${productDetails.productPrice }</td>
			</tr>
			<tr>
				<td>재고</td>
				<td>${productDetails.productRemains }</td>
			</tr>
			<tr>
				<td>보관 위치</td>
				<td>${productDetails.productPlace }</td>
			</tr>
			<tr>
				<td>등록 번호</td>
				<td>${productDetails.productId }</td>
			</tr>
			<tr>
				<td>썸네일</td>
				<td><img src="../product/showImg?imgId=${productDetails.imgId}"></td>
			</tr>
			<c:forEach items="${productDetails.imgIdDetails }" var="imgId">
			<tr>
				<td><img src="../product/showImg?imgId=${imgId}"></td>
			</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td><button id="btn_continue"></button></td>
				<td><button id="btn_update">수정 요청</button></td>
				<td><button id="btn_delete">삭제 요청</button></td>
			</tr>
		</tfoot>
	</table>
	
	<script type="text/javascript">
		// 변경해도 되는 값 : productName, productPrice, productRemains,
		const productId = '${productDetails.productId}';
		let productState = '${productDetails.productState}';
		let btnContinue = $('#btn_continue');
		let btnUpdate = $('#btn_update');
		let btnDelete = $('#btn_delete');
		
		if(productState == '판매중'){
			btnContinue.text('판매 중단');
		}else if(productState == '판매 중단'){
			btnContinue.text('판매 재개');
		}
		
	</script>
	
		
</body>
</html>