<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<!-- 카카오 주소검색 API 사용하기 위한 script -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<style type="text/css">
	.body_container{
		width: 80%;
		margin:auto;
		margin-top: 10%;
	}
</style>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

<title>배송지 등록</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	
	<div class="body_container">
		<div class="form-floating mb-3">
			<input type="text" class="form-control" id="receiverName" onblur="validCheck(this)">
			<label for="receiverName">받는 사람</label>
			<div id="receiverNameAlert"></div>
		</div>
		<div class="form-floating mb-3">
			<input type="text" class="form-control" id="receiverAddress" readonly>
			<label for="receiverAddress">배송 주소</label>
			<button class="btn btn-outline-secondary" type="button" onclick="searchAddress()">우편번호 검색</button>
		</div>
		<div class="form-floating mb-3">
			<input type="text" class="form-control" id="deliveryAddressDetails">
			<label for="deliveryAddressDetails">상세 주소</label>
		</div>
		<div class="form-floating mb-3">
			<input type="text" class="form-control" id="receiverPhone" onblur="validCheck(this)">
			<label for="receiverPhone">휴대폰 번호</label>
			<div id="receiverPhoneAlert"></div>
		</div>
		<div class="form-floating mb-3">
			<input type="text" class="form-control" id="requirement">
			<label for="requirement">배송 요구사항</label>
		</div>
		<div>
			<input type="checkbox" id="isDefault">기본 배송지로 설정
		</div>
	</div>
	<div style="text-align: center;">
		<input type="button" class="btn btn-outline-primary" value="저장" onclick="saveDelivery()">
	</div>
	
	<script type="text/javascript">
		let tagReceiverName = $('#receiverName');
		let tagReceiverAddress = $('#receiverAddress');
		let tagDeliveryAddressDetails = $('#deliveryAddressDetails');
		let tagReceiverPhone = $('#receiverPhone');
		let tagRequirement = $('#requirement');
		let tagIsDefault = $('#isDefault');
		
		let deliveryVO = {};
		let submitUrl = 'popupRegister';
		
		let expMap = {
				receiverName : {
							exp : new RegExp("^[a-zA-Z가-힣]{2,20}$"),
							success : "올바른 입력 형식입니다.",
							fail : "이름은 2~20자의 한글 또는 알파벳이여야 합니다.",
							isValid : false
				},
				receiverAddress : {
						exp : new RegExp("^[가-힣a-zA-Z0-9 ]{10,}$"),
						success : "올바른 입력 형식입니다.",
						fail : "올바른 주소를 입력해주세요.",
						isValid : false
				},
				receiverPhone : {
						exp : new RegExp("^[0-9]{10,15}$"),
						success : "올바른 입력 형식입니다.",
						fail : "잘못된 입력 형식입니다",
						isValid : false
				}
			};
		
		if('${deliveryVO}' != ''){
			deliveryVO.deliveryId = '${deliveryVO.deliveryId}';
			deliveryVO.receiverName = '${deliveryVO.receiverName}';
			deliveryVO.receiverAddress = '${deliveryVO.receiverAddress}';
			deliveryVO.deliveryAddressDetails = '${deliveryVO.deliveryAddressDetails}';
			deliveryVO.receiverPhone = '${deliveryVO.receiverPhone}';
			deliveryVO.requirement = '${deliveryVO.requirement}';
			deliveryVO.isDefault = '${deliveryVO.isDefault}';
			submitUrl = 'popupUpdate';
			console.log(deliveryVO);
			
			tagReceiverName.val(deliveryVO.receiverName);
			tagReceiverAddress.val(deliveryVO.receiverAddress);
			tagDeliveryAddressDetails.val(deliveryVO.deliveryAddressDetails);
			tagReceiverPhone.val(deliveryVO.receiverPhone);
			tagRequirement.val(deliveryVO.requirement);
			if(deliveryVO.isDefault == '1'){
				tagIsDefault.prop('checked', true);
			}
			for(x in expMap){
				expMap[x].isValid = true;
			}
		}
		
		
		
		function searchAddress(){
			new daum.Postcode({ // 카카오 주소검색 API 팝업창 띄우는 코드
		        oncomplete: function(data) {
		            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
		            console.log(data);
		            tagReceiverAddress.val(data.roadAddress);
		            deliveryVO.receiverAddress = data.roadAddress;
		            expMap.receiverAddress.isValid = true;
		        }
		    }).open();
		} // end searchAddress
		
		function validCheck(input){
			console.log("유효성 체크 : " + $(input).val());
			const inputVal = $(input).val().trim();
			$(input).val(inputVal);
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
			deliveryVO.deliveryAddressDetails = tagDeliveryAddressDetails.val();
			deliveryVO.requirement = tagRequirement.val();
			console.log('isDefault : ' + tagIsDefault.prop('checked'));
			deliveryVO.isDefault = (tagIsDefault.prop('checked')) ? 1 : 0;
			
			$.ajax({
				method : 'POST',
				url : submitUrl,
				headers : {
					'Content-Type' : 'application/json',
					'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
				},
				data : JSON.stringify(deliveryVO),
				success : function(result){
					console.log(result);
					if(result == 1){
						
						window.close();
					}
				} // end success
			}); // end ajax
			
		} // end saveDelivery
		
	</script>
</body>
</html>