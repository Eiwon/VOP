<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
td {
	width: 200px;
}
tr {
	display: flex;
	flex-direction: row;
}
.form_info {
	border: 1px solid black;
}
img {
	object-fit: none;
}
</style>
<title>상품 상세 정보</title>
</head>
<body>
	<h2>상품 상세 정보</h2>
	<form action="updateProduct" method="POST" id="formProduct">
	<table class="form_info">
		<tbody>
			<tr>
				<td>판매자 ID</td>
				<td id="memberId"></td>
			</tr>
			<tr>
				<td>판매자 이름</td>
				<td id="memberName"></td>
			</tr>
			<tr>
				<td>판매자 전화번호</td>
				<td id="memberPhone"></td>
			</tr>
			<tr>
				<td>판매자 이메일</td>
				<td id="memberEmail"></td>
			</tr>
			<tr>
				<td>사업체 명</td>
				<td id="businessName"></td>
			</tr>
			<tr>
				<td>등록 일자</td>
				<td id="productDateCreated"></td>
			</tr>
			<tr>
				<td>등록 번호</td>
				<td id="productId"></td>
			</tr>
			<tr>
				<td>상품명</td>
				<td><input id="productName" name="productName"></td>
			</tr>
			<tr>
				<td>분류</td>
				<td><input id="category" name="category"></td>
			</tr>
			<tr>
				<td>가격</td>
				<td><input id="productPrice" name="productPrice"></td>
			</tr>
			<tr>
				<td>재고</td>
				<td><input id="productRemains" name="productRemains"></td>
			</tr>
			<tr>
				<td>보관 위치</td>
				<td><input id="productPlace" name="productPlace"></td>
			</tr>
			<tr>
				<td>썸네일 <input id="inputThumbnail" type="file" name="thumbnail" onchange="showThumbPreview()"></td>
				<td id="productThumbnail"></td>
			</tr>
			
			<tr>
				<td>상세 이미지 <input id="inputDetails" type="file" name="details" multiple="multiple" onchange="showDetailsPreview()"></td>
				<td id="productDetails"></td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<td><button type="button" id="btn_continue"></button></td>
				<td><button type="button" id="btn_update">수정 요청</button></td>
				<td><button type="button" id="btn_delete">삭제 요청</button></td>
			</tr>
		</tfoot>
	</table>
	</form>
	<script type="text/javascript">
		// 변경해도 되는 값 : productName, productPrice, productRemains,
		let productDetails = JSON.parse('${productDetails}');
		const productId = productDetails.productId;
		let btnContinue = $('#btn_continue');
		let btnUpdate = $('#btn_update');
		let btnDelete = $('#btn_delete');
		
		$(document).ready(function(){
			
			setInfo();
			btnContinue.click(function(){
				toggleProductState();
			});
			btnUpdate.click(function(){
				updateRequest();
			});
			btnDelete.click(function(){
				deleteRequest();
			});
			
		}); // end document.ready
		
		
		function setInfo(){
			$('#memberId').text(productDetails.memberId);
			$('#memberName').text(productDetails.memberName);
			$('#memberPhone').text(productDetails.memberPhone);
			$('#memberEmail').text(productDetails.memberEmail);
			$('#businessName').text(productDetails.businessName);
			$('#productDateCreated').text(productDetails.productDateCreated);
			$('#productId').text(productDetails.productId);
			
			$('#productName').val(productDetails.productName);
			$('#category').val(productDetails.category);
			$('#productPrice').val(productDetails.productPrice);
			$('#productRemains').val(productDetails.productRemains);
			$('#productPlace').val(productDetails.productPlace);
			$('#productThumbnail').html('<img src="../product/showImg?imgId=' + productDetails.imgId + '">');
			
			let imgDetailsForm = '';
			for (x in productDetails.imgIdDetails){
				imgDetailsForm += '<img src="../product/showImg?imgId=' + productDetails.imgIdDetails[x] + '"><br>';
			}
			$('#productDetails').html(imgDetailsForm);
			
			if(productDetails.productState == '판매중'){
				btnContinue.text('판매 중단');
			}else if(productDetails.productState == '판매 중단'){
				btnContinue.text('판매 재개');
			}
			
		} // end setInfo
		
		function toggleProductState() {
			// 상품 판매 중지, 재개
			let productState;
			
			if(productDetails.productState == '판매중'){
				productState = '판매 중단';
			}else if(productDetails.productState == '판매 중단'){
				productState = '판매중';
			}
			
			$.ajax({
				method : 'PUT',
				headers : {
					'Content-Type' : 'application/json'
				},
				url : 'productState',
				data : JSON.stringify({
					'productId' : productId,
					'productState' : productState
				}),
				success : function(result){
					console.log('상품 상태 변경 결과 : ' + result);
					if(result == 1){
						productDetails.productState = productState;
						if(productDetails.productState == '판매 중단'){
							alert("판매가 중단되었습니다.");
							btnContinue.text('판매 재개');
						}else if(productDetails.productState == '판매중'){
							alert("다시 판매를 시작합니다.");
							btnContinue.text('판매 중단');
						}
					}
				} // end success
			}); // end ajax
			
		} // end toggleProductState
		
		function updateRequest() { // 업데이트 요청
			
			let formProduct = $('#formProduct');
		
			formProduct.append('<input type="hidden" name="productId" value="' + productDetails.productId + '">');
			formProduct.append('<input type="hidden" name="imgId" value="' + productDetails.imgId + '">');
			for(x in productDetails.imgIdDetails){
				formProduct.append('<input type="hidden" name="imgIdDetails" value="' + productDetails.imgIdDetails[x] + '">');			
			}
			
			formProduct.submit();
			//window.close();
			
		} // end updateRequest
		
		function deleteRequest() {
			
			$.ajax({
				method : 'DELETE',
				headers : {
					'Content-Type' : 'application/json'
				},
				url : 'productReq',
				data : JSON.stringify(productId),
				success : function(result){
					console.log(result);
					if(result == 1){
						alert('삭제 요청이 등록되었습니다. 관리자의 승인 후 삭제됩니다.');
					}else if(result == 2){
						alert('삭제 성공');
						window.close();
					}else {
						alert('삭제 실패');
					}
				} // end success
			}); // end ajax
			
			
		} // end deleteRequest
		
		function showThumbPreview(){
       	 	// 이미지 미리보기 (썸네일)
       	 	console.log("changed");
       		let inputThumbnail = $("#inputThumbnail");
            let file = inputThumbnail.prop('files')[0]; // file 객체 참조
            let previewThumbnail = $('#productThumbnail');
            previewThumbnail.html('');
            resizeImg(file, 120, 120, previewThumbnail);
            
        } // end showThumbPreview()
		
        function showDetailsPreview(){
       		// 이미지 미리보기 (세부정보)
       	 	let previewDetails = $('#productDetails');
       	 	let inputDetails = $("#inputDetails");
         	let files = inputDetails.prop('files');
         	
         	let form = '';
         	
         	for(let x = 0; x < files.length; x++){
           	 	let fileUrl = URL.createObjectURL(files[x]);
           	 	form += '<img src="' + fileUrl + '">';
            }
         	previewDetails.html(form);
        } // end showDetailsPreview()
		
		function toDate(timestamp){
			let date = new Date(timestamp);
			let formatted = (date.getYear() + 1900) + '/' + (date.getMonth() + 1) + '/' + date.getDate() + ' ' + 
					date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
			return formatted;
		} // end toDate
		
		function resizeImg(file, width, height, previewThumbnail){ // 변경할 파일, 너비, 높이, 변경 후 출력할 form
			let fileUrl = URL.createObjectURL(file);
            
            let image = new Image();
            image.src = fileUrl; // 이미지 객체 생성 후 src 등록 (<img src="fileUrl"> 과 동일)
            image.onload = function(){ // 이미지 등록이 완료되면 함수 실행
            	let canvas = $('<canvas>');
                let context = canvas[0].getContext('2d');
                context.drawImage(image, 0, 0, width, height); // 캔버스 객체 생성 후, 이미지를 지정한 크기로 다시 그림
                image.src = canvas[0].toDataURL(); // 그려진 이미지를 다시 image 객체에 등록
                image.onload = function(){ // 이미지 등록이 완료되면 출력
	                previewThumbnail.append(image);
                }
            }
		} // end resizeImg
		
		
	</script>
	
		
</body>
</html>