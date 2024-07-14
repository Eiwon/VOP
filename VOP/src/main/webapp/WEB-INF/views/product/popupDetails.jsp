<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">

.body_container{
	width: 80%;
	margin: auto;
}
.inner_header {
	margin: 20px;
}
.form_foot {
	display: flex;
	justify-content: center;
}
</style>
<title>상품 상세 정보</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

</head>
<body>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	
	<c:set var="productVO" value="${productDetails.productVO }"/>
	<c:set var="memberVO" value="${productDetails.memberVO }"/>
	<div class="body_container">
		<div class="inner_header">
			<h2>상품 상세 정보</h2>
		</div>
		<div class="form_info">
			<div class="input-group mb-3">
  				<span class="input-group-text">판매자 ID</span>
  				<input type="text" class="form-control" value="${memberVO.memberId }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">판매자 이름</span>
  				<input type="text" class="form-control" value="${memberVO.memberName }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">판매자 연락처</span>
  				<input type="text" class="form-control" value="${memberVO.memberPhone }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">판매자 이메일</span>
  				<input type="text" class="form-control" value="${memberVO.memberEmail }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">사업체 명</span>
  				<input type="text" class="form-control" value="${productDetails.businessName }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">요청 시간</span>
  				<input type="text" class="form-control" value="${productVO.productDateCreated.toLocaleString() }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">등록 번호</span>
  				<input type="text" class="form-control" value="${productVO.productId }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">상품명</span>
  				<input type="text" class="form-control" value="${productVO.productName }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">분류</span>
  				<input type="text" class="form-control" value="${productVO.category }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">가격</span>
  				<input type="text" class="form-control" value="${productVO.productPrice }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">재고</span>
  				<input type="text" class="form-control" value="${productVO.productRemains }" readonly>
			</div>
			<div class="input-group mb-3">
  				<span class="input-group-text">보관 위치</span>
  				<input type="text" class="form-control" value="${productVO.productPlace }" readonly>
			</div>
			<div class="input-group mb-3">
				<span class="input-group-text">썸네일</span>
				<img src="${productDetails.thumbnailUrl}" class="img-thumbnail">
			</div>
			<div class="input-group mb-3" style="display: flex;">
				<span class="input-group-text">상세 이미지</span>
				<c:forEach items="${productDetails.detailsUrl }" var="imgUrl">
				<img src="${imgUrl}" class="img-fluid">
				</c:forEach>
			</div>
			
		</div>
		<div class="form_foot">
			<c:if test="${productVO.productState eq '삭제 대기중' }">
				<div class="btn-group" role="group">
  				<button id="btn_delete" type="button" class="btn btn-outline-danger">삭제</button>
  				<button id="btn_cancel" type="button" class="btn btn-outline-primary">취소</button>
 				</div>
			</c:if>
			<c:if test="${productVO.productState eq '승인 대기중' }">
				<div class="btn-group" role="group">
  				<button id="btn_approve" type="button" class="btn btn-outline-primary">승인</button>
  				<button id="btn_reject" type="button" class="btn btn-outline-danger">거부</button>
 				</div>
			</c:if>
		</div>
		
	</div>
	<script type="text/javascript">
	
	$(document).ready(function(){
		
		$('#btn_delete').click(function(){
			// 상품 삭제 요청
			$.ajax({
				url : 'delete',
				headers : {
					'Content-Type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				method : 'DELETE',
				data : JSON.stringify({
					'productId' : '${productVO.productId}',
					'memberId' : '${productVO.memberId}'
				}),
				success : function(result){
					window.close();	
				}
			}); // end ajax
		}); // end btn_delete.click
		
		$('#btn_cancel').click(function(){
			window.close();
		}); // end btn_cancel.click
		
		$('#btn_approve').click(function(){
			sendResult('판매중');
		});
		
		$('#btn_reject').click(function(){
			sendResult('판매 불가');
		});
		
	});
	
	
	
	
	function sendResult(productState){
		
		$.ajax({
			url : 'changeStateByAdmin',
			headers : {
				'Content-Type' : 'application/json',
				'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
			},
			method : 'PUT',
			data : JSON.stringify({
				'productId' : '${productVO.productId}',
				'productState' : productState,
				'memberId' : '${productVO.memberId}'
			}),
			success : function(result){
				window.close();	
			}
		}); // end ajax
	} // end sendResult
		
	</script>
</body>
</html>