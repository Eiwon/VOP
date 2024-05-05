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
		<div>
			<input type="text" id="product_name" name="productName" placeholder="상품명" onblur="">
			<div></div>
		</div>
		<div>
			<input type="text" id="product_price" name="productPrice" placeholder="가격" onblur="expChk('pw', this)">
			<div></div>
		</div>
		<div>
        	<span>카테고리</span>
        	<div id="category">
        	</div>
		</div>
		<div>
			<input type="number" id="product_remains" name="productRemains" placeholder="수량" onblur="expChk('name', this)">
			<div></div>
		</div>
		<div>
			<input type="text" id="product_place" name="productPlace" placeholder="배송 지점" onblur="expChk('email', this)">
			<div></div>
		</div>
		<div>
			<form id="imgForm" action="thumbnail" method="post" enctype="multipart/form-data">
      			<input id="imgFile" type="file" name="file"> 
      			<input type="submit" value="업로드">
   			</form>
		</div>
	</div>
	<div>
		<input type="button" onclick="register()" value="등록">
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
	
		$("#imgForm").submit(function(event) {
            let fileInput = $("input[name='file']"); // File input 요소 참조
            let file = fileInput.prop('files')[0]; // file 객체 참조
            let fileName = fileInput.val();   
            
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
         });
      });
		
		
		</script>
	
</body>
</html>