<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
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
    .delivery_col {
    	width: 150px;
    }
	.body_container{
		width: 90%;
		margin: auto;
	}
</style>
<meta charset="UTF-8">

<title>배송지 선택</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	<div class="body_container">
		<div id="deliveryList"></div>
		<div style="text-align: center;">
			<button type="button" id="deliveryAdd" class="btn btn-outline-primary" onclick="addDelivery()">
				<strong>배송지 추가</strong>
			</button>
		</div>
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
					'option' : 'width=800, height=800, top=50, left=400'
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
				'<div class="delivery-box card">' +
					'<div class="delivery-details card-body" onclick="selectDelivery(this)">' + 
						'<div class="targetIdx" hidden="hidden">' + x + '</div>' +
						'<div style="display:flex;">' + 
							'<div class="delivery_col">수신자</div>' + 
							'<div class="receiverName">' + deliveryVO.receiverName + '</div>' + 
						'</div>' +
						'<div style="display:flex;">' + 
							'<div class="delivery_col">수신자 연락처</div>' + 
							'<div class="receiverPhone">' + deliveryVO.receiverPhone + '</div>' + 
						'</div>' +
						'<div style="display:flex;">' + 
							'<div class="delivery_col">배송 주소</div>' + 
							'<div class="receiverAddress">' + deliveryVO.receiverAddress + '</div>' + 
						'</div>' +
						'<div style="display:flex;">' + 
							'<div class="delivery_col">상세 주소</div>' + 
							'<div class="deliveryAddressDetails">' + deliveryVO.deliveryAddressDetails + '</div>' + 
						'</div>' +
						'<div style="display:flex;">' + 
							'<div class="delivery_col">배송시 요청사항</div>' + 
							'<div class="requirement">' + deliveryVO.requirement + '</div>' + 
						'</div>';
			if(deliveryVO.isDefault == '1'){
				form += '<div style="color:blue;">기본 배송지</div>';
			}
			form +='</div>' +
					'<div class="btn-group" role="group">' + 
						'<input type="button" class="btn btn-outline-success" value="수정" onclick="showPopupUpdate(this)">' + 
						'<input type="button" class="btn btn-outline-danger" value="삭제" onclick="deleteDelivery(this)">' +
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
				method : 'DELETE',
				url : 'delete/' + targetId,
				 headers: {
                     'X-CSRF-TOKEN': $('meta[name="${_csrf.parameterName }"]').attr('content')
                 },
				success : function(result){
					console.log(result);
					showDeliveryList();
				}
			}); // end ajax
			
		} // end deleteDelivery
		
	</script>
</body>
</html>