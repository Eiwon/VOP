<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<title>배송조회</title>
	<style type="text/css">
		.box {
		    margin-bottom: 20px;
		  }
		.label {
		    font-weight: bold;
		  }
	</style>
	
	<style>
        .box {
            border: 1px solid #ccc;
            padding: 10px;
            margin: 10px 0;
        }
    </style>
</head>

<body>

<script type="text/javascript">

	
$(document).ready(function() {
	
	// JSP 내부에서 모델로부터 받은 paymentId를 JavaScript 변수에 할당
	let paymentId = '${paymentId}';
	console.log('paymentId : ', paymentId);
	
	$.ajax({
		method : 'GET',
		url : 'getDeliveryList/' + paymentId,
		success : function(deliveryList){
			
			  			
			//  클라이언트 측에서 데이터를 동적으로 업데이트할 위치를 지정
			let container = document.getElementById('deliveryContainer');
			
			// 기존 내용 삭제 
	        container.innerHTML = '';
			
	     // deliveryList가 리스트 형식으로 응답
			for(let i = 0; i < deliveryList.length; i++){
				let item = deliveryList[i];
				let paymentId = item.paymentId;
				let productName = item.productName;
				let receiverName = item.receiverName;
				let deliveryAddress = item.deliveryAddress;
				let requirement = item.requirement;
				let expectDeliveryDate = item.formattedExpectDeliveryDate; //날짜 포맷 적용
				
				
				// 각 item을 출력
				console.log(paymentId, productName, receiverName, deliveryAddress, requirement, expectDeliveryDate);
               
				// 새로운 HTML 요소 생성 및 데이터 삽입
	            let box = document.createElement('div');
	            box.className = 'box';
	            
	            box.innerHTML =
	                '<p>송장 번호: ' + paymentId + '</p>' +
	                '<p>상품명: ' + productName + '</p>' + 
	                '<p>받는 사람: ' + receiverName + '</p>' + 
	                '<p>받는 주소: ' + deliveryAddress + '</p>' + 
	                '<p>배송 요청 사항: ' + requirement + '</p>' +
	                '<p>예상 도착 날짜: ' + expectDeliveryDate + '</p>';
	            
	            // 생성된 요소를 컨테이너에 추가
	            container.appendChild(box);
			}
		}, 
		error: function(xhr, status, error) {
	        console.error("Error fetching delivery list: ", error);
	    }
	});// end ajax	
 
});// end document.ready()


</script>


<!-- ${param.paymentId} -->

	<h2>배송 조회</h2>
	
	<div id="deliveryContainer"></div>

	
</body>
</html>