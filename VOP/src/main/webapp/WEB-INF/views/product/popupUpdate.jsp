<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<!-- 카카오 주소검색 API 사용하기 위한 script -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
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
	<c:set var="productVO" value="${productDetailsDTO.productVO }"/>
	<c:set var="memberVO" value="${productDetailsDTO.memberVO }"/>
	<h2>상품 상세 정보</h2>
	<form action="../product/update" method="POST" id="formProduct" enctype="multipart/form-data">
	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
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
				<td>${productDetailsDTO.businessName }</td>
			</tr>
			<tr>
				<td>등록 일자</td>
				<td>${productVO.productDateCreated.toLocaleString() }</td>
			</tr>
			<tr>
				<td>등록 번호</td>
				<td>${productVO.productId }</td>
			</tr>
			<tr>
				<td><input type="hidden" name="productId" value="${productVO.productId }"></td>
				<td><input type="hidden" name="imgId" value="${productVO.imgId}"></td>
				<td><input type="hidden" name="memberId" value="${productVO.memberId }"></td>
				<td><input type="hidden" name="productState" value="승인 대기중"></td>
			</tr>
			<tr>
				<td>상품명</td>
				<td><input type="text" id="productName" name="productName" value="${productVO.productName }"></td>
			</tr>
			<tr>
				<td>분류</td>
				<td>
				<select id="category" name="category">
					<option value="${productVO.category }" selected hidden="hidden">${productVO.category }</option>
					<option value="여성패션">여성패션</option>
					<option value="남성패션">남성패션</option>
					<option value="남녀 공용 의류">남녀 공용 의류</option>
					<option value="유아동 패션">유아동 패션</option>
					<option value="뷰티">뷰티</option>
					<option value="출산/유아동">출산/유아동</option>
					<option value="식품">식품</option>
					<option value="주방용품">주방용품</option>
					<option value="생활용품">생활용품</option>
					<option value="홈인테리어">홈인테리어</option>
					<option value="가전디지털">가전디지털</option>
					<option value="스포츠/레저">스포츠/레저</option>
					<option value="자동차 용품">자동차 용품</option>
					<option value="도서/음반/DVD">도서/음반/DVD</option>
					<option value="완구/취미">완구/취미</option>
					<option value="문구/오피스">문구/오피스</option>
					<option value="반려동물용품">반려동물용품</option>
					<option value="헬스/건강식품">헬스/건강식품</option>
				</select> </td>
			</tr>
			<tr>
				<td>가격</td>
				<td><input type="text" id="productPrice" name="productPrice" value="${productVO.productPrice }"></td>
			</tr>
			<tr>
				<td>재고</td>
				<td><input type="number" id="productRemains" name="productRemains" value="${productVO.productRemains }"></td>
			</tr>
			<tr>
				<td>보관 위치</td>
				<td><input id="productPlace" name="productPlace" value="${productVO.productPlace }" readonly>
				<input type="button" value="주소 검색" onclick="searchAddress()"></td>
			</tr>
			<tr>
				<td>등록 상태</td>
				<td id="productState">${productVO.productState }</td>
			</tr>
			<tr>
				<td>썸네일 <input id="inputThumbnail" type="file" name="thumbnail" onchange="showThumbPreview()"></td>
				<td id="productThumbnail">
				<c:choose>
					<c:when test="${productDetailsDTO.thumbnailUrl != null }">
						<img src="${productDetailsDTO.thumbnailUrl }">
					</c:when>
				</c:choose>
				</td>
			</tr>
			
			<tr>
				<td>상세 이미지 <input id="inputDetails" type="file" name="details" multiple="multiple" onchange="showDetailsPreview()"></td>
				<td id="productDetails">
				<c:forEach var="imgUrl" items="${productDetailsDTO.detailsUrl }">
					<img src="${imgUrl }">
				</c:forEach>
				</td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<td><button type="button" id="btn_continue"></button></td>
				<td><button type="submit" id="btn_update">수정 요청</button></td>
				<td><button type="button" id="btn_delete">삭제 요청</button></td>
			</tr>
		</tfoot>
	</table>
	</form>
	<script type="text/javascript">
		const productId = '${productVO.productId}';
		let productState = '${productVO.productState}';
		let btnContinue = $('#btn_continue');
		let btnUpdate = $('#btn_update');
		let btnDelete = $('#btn_delete');
		const allowedExtension = new RegExp("jpg|jpeg|png|bmp|tif|tiff|webp|svg");
		
		$(document).ready(function(){
			
			if(productState == '판매중'){
				btnContinue.text('판매 중단');
			}else if(productState == '판매 중단'){
				btnContinue.text('판매 재개');
			}else{
				btnContinue.css('visibility', "hidden");
			}
			
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
		
		function toggleProductState() {
			// 상품 판매 중지, 재개
			if(productState != '판매중' && productState != '판매 중단'){
				return;
			}
			
			$.ajax({
				method : 'PUT',
				headers : {
					'Content-Type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				url : 'changeState',
				data : JSON.stringify({
					'productId' : productId,
					'productState' : productState,
					'memberId' : '${productVO.memberId}'
				}),
				success : function(result){
					console.log('상품 상태 변경 결과 : ' + result);
					if(result == 1){
						if(productState == '판매중'){
							productState = '판매 중단';
							alert("판매가 중단되었습니다.");
							btnContinue.text('판매 재개');
						}else if(productState == '판매 중단'){
							productState = '판매중';
							alert("다시 판매를 시작합니다.");
							btnContinue.text('판매 중단');
						}
					}
				} // end success
			}); // end ajax
			
		} // end toggleProductState
		
		let formProduct = $('#formProduct');
		
		formProduct.submit(function update(){
			
            let file = $('#inputThumbnail').prop('files')[0]; // file 객체 참조
            
            if(!checkFileValid(file)){
            	event.preventDefault();
            	return;
            }
            
            let files = $('#inputDetails').prop('files');
            
            for(let x = 0; x < files.length; x++){
            	console.log(files[x].name);
            	if(!checkFileValid(files[x])){
            		event.preventDefault();
                	return;
            	}
            }
			
			if(!confirm("변경시 관리자의 승인 전까지 판매가 중지됩니다. 변경하시겠습니까?")){
				 event.preventDefault();
				 return;
			}
			
			alert("변경 사항은 관리자의 승인 후 반영됩니다.");
		});
		
		
		function checkFileValid(file){
			 
            if (!file) { // file이 없는 경우
               alert("파일을 선택하세요.");
               return false;
            }
            
            if (!allowedExtensions.test(file.name)) { // 차단된 확장자인 경우
               alert("이미지 파일만 첨부해주세요\n(jpg, jpeg, png, bmp, tif, tiff, webp, svg)");
               return false;
            }

            let maxSize = 10 * 1024 * 1024; // 10 MB 
            if (file.size > maxSize) {
               alert("파일 크기가 너무 큽니다. 최대 크기는 10MB입니다.");
               return false;
            }
        } // end checkFileValid
		
        
        
		function deleteRequest() {
			
			$.ajax({
				method : 'DELETE',
				headers : {
					'Content-Type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				url : 'request',
				data : JSON.stringify({
					productId : '${productVO.productId}',
					memberId : '${productVO.memberId}'
				}),
				success : function(result){
					console.log(result);
					if(result == 201){
						alert('삭제 요청이 등록되었습니다. 관리자의 승인 후 삭제됩니다.');
					}else if(result == 200){
						alert('삭제 요청 실패');
					}else if(result == 101){
						alert('삭제 성공');
						window.close();
					}else if(result == 100){
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
            let fileUrl = URL.createObjectURL(file);
            $('#productThumbnail').html($('<img src="' + fileUrl + '">'));
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
		
		 function searchAddress(){
	 			new daum.Postcode({ // 카카오 주소검색 API 팝업창 띄우는 코드
	 		        oncomplete: function(data) {
	 		            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
	 		            console.log(data);
	 		            $('#productPlace').val(data.roadAddress);
	 		        }
	 		    }).open();
	 	} // end searchAddress
	 		
	</script>
	
		
</body>
</html>