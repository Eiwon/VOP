<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<!-- 카카오 주소검색 API 사용하기 위한 script -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<style type="text/css">
tr {
	flex-direction: row;
	display: flex;
}
</style>
<title>배송지 등록</title>
</head>
<body>
	
	<form action="register" method="POST">
		<table>
			<tbody>
				<tr>
					<td><input type="text" name="receiverName" id="receiverName" placeholder="받는 사람" onblur="validCheck(this)"></td>
					<td id="receiverNameAlert"></td>
				</tr>
				<tr>
					<td><input type="text" name="receiverAddress" id="receiverAddress" readonly></td>
					<td><button type="button" onclick="searchAddress()">우편번호 검색</button></td>
				</tr>
				<tr>
					<td><input type="text" name="receiverPhone" id="receiverPhone" placeholder="휴대폰 번호" onblur="validCheck(this)"></td>
					<td id="receiverPhoneAlert"></td>
				</tr>
				<tr>
					<td><input type="text" name="requirement" id="requirement" placeholder="배송 요구사항"></td>
				</tr>
			</tbody>
		</table>
		<input type="button" value="저장" onclick="saveDelivery()">
	</form>
	
	
	
	
	<script type="text/javascript">
		let tagReceiverName = $('#receiverName');
		let tagReceiverAddress = $('#receiverAddress');
		let tagReceiverPhone = $('#receiverPhone');
		let tagRequirement = $('#requirement');
		let deliveryVO = {};
		let expMap = {};
		expMap.receiverName = {
				exp : new RegExp("^[a-zA-Z가-힣]{2,20}$"),
				success : "올바른 입력 형식입니다.",
				fail : "이름은 2~20자의 한글 또는 알파벳이여야 합니다.",
				isValid : false
		};
		expMap.receiverAddress = {
				exp : new RegExp("^[가-힣a-zA-Z0-9 ]{10,}$"),
				success : "올바른 입력 형식입니다.",
				fail : "올바른 주소를 입력해주세요.",
				isValid : false
		};
		expMap.receiverPhone = {
				exp : new RegExp("^[0-9]{10,15}$"),
				success : "올바른 입력 형식입니다.",
				fail : "잘못된 입력 형식입니다",
				isValid : false
		};
		
		function searchAddress(){
			new daum.Postcode({ // 카카오 주소검색 API 팝업창 띄우는 코드
		        oncomplete: function(data) {
		            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
		            // 예제를 참고하여 다양한 활용법을 확인해 보세요.
		            console.log(data);
		            tagReceiverAddress.val(data.roadAddress);
		            deliveryVO.receiverAddress = data.roadAddress;
		            expMap.receiverAddress.isValid = true;
		        }
		    }).open();
		} // end searchAddress
		
		function validCheck(input){
			console.log("유효성 체크");
			const inputVal = $(input).val();
			const targetId = $(input).attr('id');
			const tester = expMap[targetId];
			
			if(tester.exp.test(inputVal)){
				$('#' + targetId + 'Alert').text(tester.success);
				expMap[targetId].isValid = true;
				deliveryVO[targetId] = inputVal;
			}else{
				$('#' + targetId + 'Alert').text(tester.fail);
				expMap[targetId].isValid = false;
			}
			
		} // end validCheck
	
		function saveDelivery(){
			console.log(deliveryVO);
			
			for(x in expMap){
				if(expMap[x].isValid == false) {
					alert(x + "형식에 맞게 입력해주세요.");
					return;
				}
			}
			tagReceiverName.val(deliveryVO.receiverName);
			tagReceiverAddress.val(deliveryVO.receiverAddress);
			tagReceiverPhone.val(deliveryVO.receiverPhone);
			
			$('form').submit();
			
		} // end saveDelivery
		
	</script>
</body>
</html>