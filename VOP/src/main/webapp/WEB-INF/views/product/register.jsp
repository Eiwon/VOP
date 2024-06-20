<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<!-- 카카오 주소검색 API 사용하기 위한 script -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<title>상품 등록</title>
</head>
<jsp:include page="../include/header.jsp"></jsp:include>
<body>
	<div>
		<form id="productForm" action="register" method="post" enctype="multipart/form-data">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
			<div>
				판매자
				<input type="text" value='${memberDetails.getUsername() }' readonly>
			</div>
			<div>
				<input type="text" id="productName" name="productName" placeholder="상품명" >
				<div></div>
			</div>
			<div>
				<input type="text" id="productPrice" name="productPrice" placeholder="가격" >
				<div></div>
			</div>
			<div>
        		<span>카테고리</span> 
        		<select id="boxCategory" name="category">
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
			<div>
				<input type="number" id="productRemains" name="productRemains" placeholder="수량">
				<div></div>
			</div>
			<div>
				<div>
					<input type="text" id="productPlace" name="productPlace" placeholder="배송 지점" readonly="readonly">
					<input type="button" onclick="searchAddress()" value="주소 검색">
				</div>
				<div></div>
			</div>
			<div>
				<strong>썸네일 선택</strong>
				<input id="inputThumbnail" type="file" name="thumbnail" onchange="showThumbPreview()"> 
				<div id="previewThumbnail">
				</div>
			</div>
			<div>
				<strong>상세정보 이미지</strong>
				<input id="inputDetails" type="file" name="details" multiple="multiple" onchange="showDetailsPreview()">
				<div id="previewDetails">
				</div>
			</div>
			
			<input type="submit" value="등록">
		</form>
	</div>
	
	
	<script type="text/javascript">
		let validCheckMap = {};
		const allowedExtension = new RegExp("jpg|jpeg|png|bmp|tif|tiff|webp|svg");
		
		
		validCheckMap.productName = {
			exp : new RegExp('^[가-힣a-zA-Z0-9 !@#$%^&*()-+,.<>?]{2,30}$'),
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
			exp : new RegExp('^[가-힣a-zA-Z0-9 !@#$%^&*()-+,.<>?]{10,65}$'),
			failMsg : '형식에 맞지 않는 주소입니다'
		};
		
		
		$("#productForm").submit(function(event) {
			
			if(!checkTextValid()){
				event.preventDefault();
            	return;
			}
	        
            let inputThumbnail = $("#inputThumbnail"); // File input 요소 참조
            let file = inputThumbnail.prop('files')[0]; // file 객체 참조
            
            if(!checkFileValid(file)){
            	event.preventDefault();
            	return;
            }
            
            let inputDetails = $("#inputDetails");
            let files = inputDetails.prop('files');
            
            for(let x = 0; x < files.length; x++){
            	console.log(files[x].name);
            	if(!checkFileValid(files[x])){
            		event.preventDefault();
                	return;
            	}
            }
         }); //end imgForm.submit
		
         function showThumbPreview(){
        	 // 이미지 미리보기 (썸네일)
        	 console.log("changed");
        	 let inputThumbnail = $("#inputThumbnail"); // File input 요소 참조
             let file = inputThumbnail.prop('files')[0]; // file 객체 참조
             let previewThumbnail = $('#previewThumbnail');
             const fileUrl = URL.createObjectURL(file);
             let str = "";
             
             str = '<img src="' + fileUrl + '">';
             
        	 previewThumbnail.html(str);
         } // end showThumbPreview()
         
         function showDetailsPreview(){
        	 // 이미지 미리보기 (세부정보)
        	 console.log("changed");
        	 let previewDetails = $('#previewDetails');
        	 
        	 let inputDetails = $("#inputDetails");
             let files = inputDetails.prop('files');
             let str = "";
             console.log(files.length);
             for(let x = 0; x < files.length; x++){
            	 let fileUrl = URL.createObjectURL(files[x]);
            	 str += '<img src="' + fileUrl + '">';
             }
        	 previewDetails.html(str);
         } // end showDetailsPreview()
         
         function checkTextValid() {
 			let val;
 			for(x in validCheckMap){
 				val = $('#' + x).val();
 				if(val.length == 0){
 					alert('모든 항목을 입력해주세요');
 					return false;
 				}
 				if(!validCheckMap[x].exp.test(val)){
 					alert(validCheckMap[x].failMsg);
 					return false;
 				}
 			}
 			return true;
 		 } // end checkTextValid
         
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
         }
         
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