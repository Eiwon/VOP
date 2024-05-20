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

</style>
<meta charset="UTF-8">
<title>배송지 선택</title>
</head>
<body>
<table border="1">
    <tbody id="deliveryList" class="delivery-box">
    	<tr class="delivery-details">
    		<td class="targetIdx" hidden="hidden"></td>
    		<td> 수신자 <strong class="receiverName"></strong></td>
    		<td> 수신자 연락처 <strong class="receiverPhone"></strong></td>
    		<td> 배송 주소 <strong class="receiverAddress"></strong></td>
    		<td> 상세 주소 <strong class="deliveryAddressDetails"></strong></td>
    		<td> 배송시 요청사항 <strong class="requirement"></strong></td>
    	</tr>
    </tbody>
    <tfoot>
    	<tr class="delivery-details" onclick="addDelivery()">
        	<td>배송지 추가</td>
        </tr>
    </tfoot>
</table>


	<script type="text/javascript">
		const memberId = '<%= request.getSession().getAttribute("memberId")%>';	
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
					'option' : 'width=500, height=600, top=50, left=400'
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
			
			parent.find('#receiverName').val(deliveryList[targetIdx].receiverName);
			parent.find('#receiverAddress').val(deliveryList[targetIdx].receiverAddress);
			parent.find('#deliveryAddressDetails').val(deliveryList[targetIdx].deliveryAddressDetails);
			parent.find('#receiverPhone').val(deliveryList[targetIdx].receiverPhone);
			parent.find('#requirement').val(deliveryList[targetIdx].requirement);
			
			window.close();
			
		} // end selectDelivery
		
		function makeDeliveryForm(deliveryVO, x){
			const form = '<tr class="delivery-details" onclick="selectDelivery(this)">' +
	    		'<td class="targetIdx" hidden="hidden">' + x + '</td>' +
	    		'<td> 수신자 <strong class="receiverName">' + deliveryVO.receiverName + '</strong></td>' +
	    		'<td> 수신자 연락처 <strong class="receiverPhone">' + deliveryVO.receiverPhone + '</strong></td>' +
	    		'<td> 배송 주소 <strong class="receiverAddress">' + deliveryVO.receiverAddress + '</strong></td>' +
	    		'<td> 상세 주소 <strong class="deliveryAddressDetails">' + deliveryVO.deliveryAddressDetails + '</strong></td>' +
	    		'<td> 배송시 요청사항 <strong class="requirement">' + deliveryVO.requirement + '</strong></td></tr>';
			return form;
		} // end makeDeliveryForm
		
	</script>
</body>
</html>