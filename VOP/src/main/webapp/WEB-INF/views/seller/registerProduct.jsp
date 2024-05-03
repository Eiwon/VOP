<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
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
	
		</script>
	
</body>
</html>