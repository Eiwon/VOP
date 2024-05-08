<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>상품 등록</title>
</head>
<body>
	<div>
		<form id="productForm" action="register" method="post" enctype="multipart/form-data">
			<div>
				판매자
				<input type="text" name="memberId" value='<%=request.getSession().getAttribute("memberId") %>' readonly>
			</div>
			<div>
				<input type="text" id="product_name" name="productName" placeholder="상품명" >
				<div></div>
			</div>
			<div>
				<input type="text" id="product_price" name="productPrice" placeholder="가격" >
				<div></div>
			</div>
			<div>
        		<span>카테고리</span>
        		<input type="text" name="category">
			</div>
			<div>
				<input type="number" id="product_remains" name="productRemains" placeholder="수량">
				<div></div>
			</div>
			<div>
				<input type="text" id="product_place" name="productPlace" placeholder="배송 지점">
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
		let productName = $('#product_name');
		let productPrice = $('#product_price');
		let productCategory = $('#category');
		let productRemains = $('#product_remains');
		let productPlace = $('#product_place');
		const blockedExtension = new RegExp("exe|sh|php|jsp|aspx|zip|alz");
		// 이미지 확장자 필터
		
		let expMap = {};
		expMap['id'] = {
			exp : new RegExp("^[a-zA-Z][a-zA-Z0-9]{5,19}$"),
			success : "올바른 입력 형식 입니다.",
			fail : "아이디는 알파벳으로 시작하는 5~20자의 알파벳과 숫자 조합이여야 합니다."
		};
		expMap['pw'] = {
			exp : new RegExp("^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{8,20}$"),
			success : "올바른 입력 형식 입니다.",
			fail : "비밀번호는 8~20자의 알파벳, 숫자, 한글이여야 합니다."
		};
		expMap['name'] = {
			exp : new RegExp("^[a-zA-Z가-힣]{2,20}$"),
			success : "올바른 입력 형식입니다.",
			fail : "이름은 2~20자의 한글 또는 알파벳이여야 합니다."
		};
		expMap['email'] = {
			exp : new RegExp("^[a-zA-Z][a-zA-Z0-9]{5,19}@[a-zA-Z\.]{6,20}$"),
			success : "올바른 입력 형식입니다.",
			fail : "잘못된 입력 형식입니다."
		};
		expMap['phone'] = {
			exp : new RegExp("^[0-9]{10,15}$"),
			success : "올바른 입력 형식입니다.",
			fail : "잘못된 입력 형식입니다"
		};
		
		const chkList = [
			'id', 'pw', 'checkPw', 'name', 'email', 'phone'
			];
		let validChkList = {};
		// 각 입력값이 유효한지 검사 결과를 저장하기 위한 맵

		for(x in chkList){
			validChkList[chkList[x]] = false;
		} // 맵 초기화
	
		$("#productForm").submit(function(event) {
            let inputThumbnail = $("#inputThumbnail"); // File input 요소 참조
            let file = inputThumbnail.prop('files')[0]; // file 객체 참조
            let fileName = inputThumbnail.val();   
            
            if (!file) { // file이 없는 경우
               alert("파일을 선택하세요.");
               event.preventDefault();
               return;
            }
            
            if (blockedExtensions.test(fileName)) { // 차단된 확장자인 경우
               alert("이 확장자의 파일은 첨부할 수 없습니다.");
               event.preventDefault();
               return;
            }

            let maxSize = 10 * 1024 * 1024; // 10 MB 
            if (file.size > maxSize) {
               alert("파일 크기가 너무 큽니다. 최대 크기는 10MB입니다.");
               event.preventDefault();
            }
            
            let inputDetails = $("#imgDetails");
            let files = inputDetails.prop('files');
            // 유효성 체크 추가 필요
            
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
         
	</script>
	
</body>
</html>