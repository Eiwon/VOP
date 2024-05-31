<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style>

    .delivery-box {
        border: 1px solid #ccc;
        padding: 10px;
        margin-bottom: 20px;
    }
    .delivery-details {
    	border: 1px solid #ccc;
        align-items: center;
        margin-bottom: 10px;
    }
    #deliveryAdd {
    	border: 1px solid #ccc;
    	text-align:center;
        height: 50px;
    }

</style>
<meta charset="UTF-8">
<title>배송지 선택</title>
</head>
<body>

<div id="deliveryList"></div>
<div id="deliveryAdd" onclick="addDelivery()">
	<strong>배송지 추가</strong>
</div>

	<script type="text/javascript">
		let deliveryList;
		let tagDeliveryList = $('#deliveryList');
		
		$(document).ready(function(){
			
			showDeliveryList();
			
		}); // end document.ready
	
		
		function showDeliveryList(){
			// memberId로 등록된 배송지 리스트 출력
			
			$.ajax({
				method : 'GET',
				url : 'popupList',
				success : function(result){
					console.log(result);
					deliveryList = result;
					let form = '';
					for(x in deliveryList){
						form += makeDeliveryForm(deliveryList[x], x);
					}
					tagDeliveryList.html(form);
				} // end success
			}); // end ajax
			
		} // end showDeliveryList
		
		function addDelivery(){
			
			const popupStat = {
					'url' : 'popupRegister',
					'name' : 'popupDeliveryRegister',
					'option' : 'width=1000, height=600, top=50, left=400'
			};
			
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
				showDeliveryList();
			} // end popup.onbeforeunload
			
		} // end addDelivery
		
		function selectDelivery(input){
			let targetIdx = $(input).find('.targetIdx').text();
			console.log(targetIdx + '번 배송지 선택됨');
			let parent = $(opener.document);
			
			window.opener.saveDelivery(deliveryList[targetIdx]);
			
			window.close();
			
		} // end selectDelivery
		
		function makeDeliveryForm(deliveryVO, x){
			let form = 
				'<div class="delivery-box">' +
					'<div class="delivery-details" onclick="selectDelivery(this)">' + 
						'<div class="targetIdx" hidden="hidden">' + x + '</div>' +
						'<div> 수신자 <strong class="receiverName">' + deliveryVO.receiverName + '</strong></div>' +
						'<div> 수신자 연락처 <strong class="receiverPhone">' + deliveryVO.receiverPhone + '</strong></div>' +
						'<div> 배송 주소 <strong class="receiverAddress">' + deliveryVO.receiverAddress + '</strong></div>' +
						'<div> 상세 주소 <strong class="deliveryAddressDetails">' + deliveryVO.deliveryAddressDetails + '</strong></div>' +
						'<div> 배송시 요청사항 <strong class="requirement">' + deliveryVO.requirement + '</strong></div>';
			if(deliveryVO.isDefault == '1'){
				form += '<div style="color:blue;">기본 배송지</div>';
			}
			form +='</div>' +
					'<div>' + 
						'<input type="button" value="수정" onclick="showPopupUpdate(this)">' + 
						'<input type="button" value="삭제" onclick="deleteDelivery(this)">' +
					'</div>' +
				'</div>';
			return form;
		} // end makeDeliveryForm
		
		function showPopupUpdate(input){
			let targetIdx = $(input).parents('.delivery-box').find('.targetIdx').text();
			let targetId = deliveryList[targetIdx].deliveryId;
			
			const popupStat = {
					'url' : 'popupUpdate?deliveryId=' + targetId,
					'name' : 'popupDeliveryUpdate',
					'option' : 'width=1000, height=600, top=50, left=400'
			};
			
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
				showDeliveryList();
			} // end popup.onbeforeunload
		} // end showPopupUpdate
		
		
		function deleteDelivery(input){
			let targetIdx = $(input).parents('.delivery-box').find('.targetIdx').text();
			let targetId = deliveryList[targetIdx].deliveryId;
			console.log(targetId);
			
			$.ajax({
				method : 'POST',
				url : 'delete',
				data : targetId,
				success : function(result){
					console.log(result);
					showDeliveryList();
				}
			}); // end ajax
			
		} // end deleteDelivery
		
	</script>
</body>
</html>