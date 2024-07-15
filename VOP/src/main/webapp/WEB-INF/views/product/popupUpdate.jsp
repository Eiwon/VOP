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
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<style type="text/css">

.body_container{
	width: 80%;
	margin: auto;
}
.inner_header {
	margin: 40px;
	text-align: center;
}
.editable {
	border-color: black;
}
</style>
<title>상품 상세 정보</title>
</head>
<body>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	<c:set var="productVO" value="${productDetailsDTO.productVO }"/>
	<c:set var="memberVO" value="${productDetailsDTO.memberVO }"/>
	<div class="body_container">
	<div class="inner_header">
		<h2>상품 상세 정보</h2>	
	</div>
	<form action="../product/update?${_csrf.parameterName }=${_csrf.token }" method="POST" id="formProduct" enctype="multipart/form-data">
	
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
			<span class="input-group-text">판매자 전화번호</span>
			<input type="text" class="form-control" value="${memberVO.memberPhone }" readonly>
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">판매자 이메일</span>
			<input type="text" class="form-control" value="${memberVO.memberEmail }" readonly>
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">사업체 명</span>
			<input type="text" class="form-control" value="${productDetailsDTO.businessName }" readonly>
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">등록 일자</span>
			<input type="text" class="form-control" value="${productVO.productDateCreated.toLocaleString() }" readonly>
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">상품 코드</span>
			<input type="text" class="form-control" value="${productVO.productId }" readonly>
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">사업체 명</span>
			<input type="text" class="form-control" value="${productDetailsDTO.businessName }" readonly>
		</div>
		<div>
			<input type="hidden" name="productId" value="${productVO.productId }">
			<input type="hidden" name="imgId" value="${productVO.imgId}">
			<input type="hidden" name="memberId" value="${productVO.memberId }">
			<input type="hidden" name="productState" value="승인 대기중">
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">상품명</span>
			<input type="text" class="form-control editable" id="productName" name="productName" value="${productVO.productName }">
		</div>
		<div class="input-group mb-3">
			<label class="input-group-text">분류</label>
        	<select id="category" class="form-select editable" name="category">
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
			</select> 
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">가격</span>
			<input type="text" class="form-control editable" id="productPrice" name="productPrice" value="${productVO.productPrice }">
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">재고</span>
			<input type="text" class="form-control editable" id="productRemains" name="productRemains" value="${productVO.productRemains }">
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">보관 위치</span>
			<input type="text" class="form-control editable" id="productPlace" name="productPlace" value="${productVO.productPlace }" readonly>
			<button type="button" class="btn btn-outline-secondary" onclick="searchAddress()">주소 검색</button>
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text">등록 상태</span>
			<input type="text" class="form-control" id="productState" value="${productVO.productState }" readonly>
		</div>
		<div class="input-group mb-3">
			<label class="input-group-text">썸네일</label>
			<input id="inputThumbnail" class="form-control" type="file" name="thumbnail" onchange="showThumbPreview()"> 
		</div>
		<div id="previewThumbnail">
			<c:choose>
					<c:when test="${productDetailsDTO.thumbnailUrl != null }">
						<img src="${productDetailsDTO.thumbnailUrl }" class="img-thumbnail">
					</c:when>
				</c:choose>
		</div>
		<div class="input-group mb-3">
			<label class="input-group-text">상세 이미지</label>
			<input id="inputDetails" class="form-control" type="file" name="details" multiple="multiple" onchange="showDetailsPreview()">
		</div>
		<div id="previewDetails">
				<c:forEach var="imgUrl" items="${productDetailsDTO.detailsUrl }">
					<img src="${imgUrl }" class="img-fluid">
				</c:forEach>
			</div>	
		<div class="btn-group" role="group">
			<button type="button" id="btn_continue" class="btn btn-outline-primary"></button>
			<button type="submit" id="btn_update" class="btn btn-outline-primary">수정 요청</button>
			<button type="button" id="btn_delete" class="btn btn-outline-primary">삭제 요청</button>
		</div>	
	</div>
	</form>
	</div>
	<script type="text/javascript">
		const productId = '${productVO.productId}';
		let productState = '${productVO.productState}';
		let tagProductState = $('#productState');
		let btnContinue = $('#btn_continue');
		let btnUpdate = $('#btn_update');
		let btnDelete = $('#btn_delete');
		
		const allowedExtensions = new RegExp("jpg|jpeg|png|bmp|tif|tiff|webp|svg");
		let validCheckMap = {};
		
		validCheckMap.productName = {
			exp : new RegExp('^[가-힣a-zA-Z0-9 !@#$%^&*()+,.<>?-]{2,30}$'),
			failMsg : '상품명은 한글, 알파벳, 숫자, 일부 특수문자만 사용 가능합니다',
		};
		validCheckMap.productPrice = {
			exp : new RegExp('^[0-9]{1,10}$'),
			failMsg : '정상적인 판매가를 입력해주세요'
		};
		validCheckMap.productRemains = {
			exp : new RegExp('^[0-9]{1,10}$'),
			failMsg : '올바른 숫자를 입력해주세요'
		};
		validCheckMap.productPlace = {
			exp : new RegExp('^[가-힣a-zA-Z0-9 !@#$%^&*()+,.<>?-]{10,65}$'),
			failMsg : '형식에 맞지 않는 주소입니다'
		};
		
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
			btnDelete.click(function(){
				deleteRequest();
			});
			
		}); // end document.ready
		
		function toggleProductState() {
			// 상품 판매 중지, 재개
			let changedState;
			if(productState == '판매중') 
				changedState = '판매 중단';
			else if(productState == '판매 중단')
				changedState = '판매중';
			else return;
			
			$.ajax({
				method : 'PUT',
				headers : {
					'Content-Type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				url : 'changeState',
				data : JSON.stringify({
					'productId' : productId,
					'productState' : changedState,
					'memberId' : '${productVO.memberId}'
				}),
				success : function(result){
					console.log('상품 상태 변경 결과 : ' + result);
					if(result == 1){
						productState = changedState;
						tagProductState.val(productState);
						console.log('변경 후 상품 상태 : ' + productState);
						if(productState == '판매 중단'){
							alert("판매가 중단되었습니다.");
							btnContinue.text('판매 재개');
						}else if(productState == '판매중'){
							alert("다시 판매를 시작합니다.");
							btnContinue.text('판매 중단');
						}else{
							btnContinue.css('visibility', "hidden");
						}
					}else{
						alert('변경에 실패했습니다. 다시 시도해주세요.');
					}
					
				} // end success
			}); // end ajax
			
		} // end toggleProductState
		
		let formProduct = $('#formProduct');
		
		function checkTextValid() {
 			let val;
 			for(x in validCheckMap){
 				val = $('#' + x).val().trim();
 				if(val.length == 0){
 					alert('모든 항목을 입력해주세요');
 					return false;
 				}
 				$('#' + x).val(val);
 				if(!validCheckMap[x].exp.test(val)){
 					alert(validCheckMap[x].failMsg);
 					return false;
 				}
 			}
 			return true;
 		 } // end checkTextValid

 		 
 		$('#formProduct').submit(function update(){
			
			if(!checkTextValid()){
				event.preventDefault();
				return;
			}
			
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
               return true;
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
            return true;
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
            $('#previewThumbnail').html($('<img src="' + fileUrl + '" class="img-thumbnail">'));
        } // end showThumbPreview()
		
        function showDetailsPreview(){
       		// 이미지 미리보기 (세부정보)
       	 	let previewDetails = $('#previewDetails');
       	 	let inputDetails = $("#inputDetails");
         	let files = inputDetails.prop('files');
         	
         	let form = '';
         	
         	for(let x = 0; x < files.length; x++){
           	 	let fileUrl = URL.createObjectURL(files[x]);
           	 	form += '<img src="' + fileUrl + '" class="img-fluid">';
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