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
        display: flex;
        align-items: center;
        margin-bottom: 10px;
    }
    .delivery-details img {
        margin-right: 10px;
        max-width: 100px;
    }
    .delivery-buttons {
        margin-left: auto;
    }
    
    .delivery-details p {
        margin-bottom: 10px;
    }

    .delivery-details label {
        display: inline-block;
        width: 150px; /* 라벨 너비 조절 */
        font-weight: bold;
    }

    .default-checkbox {
        margin-top: 10px;
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
        <tr class="delivery-details" onclick="addDelivery()">
        	<td>배송지 추가</td>
        </tr>
    </tbody>
</table>


	<script type="text/javascript">
		const memberId = '<%= request.getSession().getAttribute("memberId")%>';	
		
		
		$(document).ready(function(){
			
			showDeliveryList();
			
		}); // end document.ready
	
		
		function showDeliveryList(){
			// memberId로 등록된 배송지 리스트 출력
			
			
		} // end showDeliveryList
		
		function addDelivery(){
			
		} // end addDelivery
		
	</script>
</body>
</html>