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
				<td><img alt="${productDetails.imgId}"></td>
			</tr>
			<c:forEach items="${productDetails.imgIdDetails }" var="imgId">
			<tr>
				<td><img alt="${imgId}"></td>
			</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td><button id="btn_approve"></button></td>
				<td><button id="btn_refuse"></button></td>
			</tr>
		</tfoot>
	</table>

	<script type="text/javascript">
	
		let btnApprove = $('#btn_approve');
		let btnRefuse = $('#btn_refuse');
		const productState = '${productDetails.productState }';
		
		$(document).ready(function(){
			loadImg();
		}); // end document.ready
		
		
		
		// 상품 상태가 '삭제 대기중'이면 상품 삭제 / 취소 버튼 출력
		if(productState == '삭제 대기중'){
			btnApprove.text('삭제');
			btnApprove.click(function(){
				deleteProduct();
			});
			btnRefuse.text('취소');
			btnRefuse.click(function(){
				window.close();
			});
		}else { // '승인 대기중'이면 등록 승인 / 거부 버튼 출력
			btnApprove.text('승인');
			btnApprove.click(function(){
				sendResult('판매중');
			});
			btnRefuse.text('거부');
			btnRefuse.click(function(){
				sendResult('판매 불가');
			});
		}
	
	
	function sendResult(productState){
		
		$.ajax({
			url : 'changeState',
			headers : {
				'Content-Type' : 'application/json'
			},
			method : 'PUT',
			data : JSON.stringify({
				'productId' : '${productDetails.productId}',
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
				'productId' : '${productDetails.productId}'
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
	
	function loadImg(){
		$(document).find('img').each(function(){
			let target = $(this);
			let imgId = target.attr("alt");
			$.ajax({
				method : 'GET',
				url : '../image/' + imgId,
				success : function(result){
					target.attr('src', result);
				}
			}); // end ajax
		});
	} // end loadImg
		
	</script>
</body>
</html>