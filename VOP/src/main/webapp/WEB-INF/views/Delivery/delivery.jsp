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

<!-- 부트스트랩 CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

	<style type="text/css">
	
		 .box {
        border: 1px solid #ccc;
        padding: 10px;
        margin-bottom: 20px;
	    }
	    
	    .label {
	        font-weight: bold;
	    }
	    
	     .highlight {
        font-weight: bold;
        color: #007bff; /* 부트스트랩의 primary 색상 (파란색)을 사용 */
    	}
	</style>
</head>

<body>
<div class="container"><br>
    <h2 class="mt-4 mb-4 text-center">배송 조회</h2><br>
	 
	 <div id="deliveryStatus">
        </div><br><br>

    <div id="deliveryContainer"></div><br><br>
    
    <a href="../order/orderlist" class="d-inline-flex focus-ring py-1 px-2 text-decoration-none border rounded-3 mr-2">주문 목록으로 돌아가기</a>
</div>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

<!-- 부트스트랩 JS -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<script type="text/javascript">
$(document).ready(function() {
    // JSP 내부에서 모델로부터 받은 paymentId를 JavaScript 변수에 할당
    let paymentId = '${paymentId}';
    console.log('paymentId : ', paymentId);
    
    let formattedNow = '${formattedNow}'; //현재날짜 포멧
    console.log('formattedNow : ', formattedNow);
    
    let expectedDate = '${expectedDate}'; //예상배송일 포맷
    console.log('expectedDate : ', expectedDate);
    
    // 배송 정보 조회
    getDeliveryList(paymentId);
    
    // 배송 예상일 비교 
    getDeliveryStatus(formattedNow, expectedDate);
    
   
}); // end document.ready()


	function getDeliveryList(paymentId){
		$.ajax({
	        method : 'GET',
	        url : 'getDeliveryList/' + paymentId,
	        success : function(deliveryList) {
	            // 클라이언트 측에서 데이터를 동적으로 업데이트할 위치를 지정
	            let container = $('#deliveryContainer');
	            
	            // 기존 내용 삭제
	            container.empty();
	            
	            // deliveryList가 리스트 형식으로 응답
	            for (let i = 0; i < deliveryList.length; i++) {
	                let item = deliveryList[i];
	                let paymentId = item.paymentId;
	                let productName = item.productName;
	                let receiverName = item.receiverName;
	                let deliveryAddress = item.deliveryAddress;
	                let requirement = item.requirement;
	                let expectDeliveryDate = item.formattedExpectDeliveryDate; // 날짜 포맷 적용
	                
	                // 각 item을 출력
	                console.log(paymentId, productName, receiverName, deliveryAddress, requirement, expectDeliveryDate);
	                
	                // 새로운 HTML 요소 생성 및 데이터 삽입
	                let box = $('<div>').addClass('box')
	                    //.append('<p class="mb-1"><strong>예상 도착 날짜:</strong> <span class="highlight" style="font-size: 20px;">' + expectDeliveryDate + '</span></p>')
	                    .append('<p class="mb-1"><strong>송장 번호:</strong> ' + paymentId + '</p>')
	                    .append('<p class="mb-1"><strong>상품명:</strong> ' + productName + '</p>')
	                    .append('<p class="mb-1"><strong>받는 사람:</strong> ' + receiverName + '</p>')
	                    .append('<p class="mb-1"><strong>받는 주소:</strong> ' + deliveryAddress + '</p>')
	                    .append('<p class="mb-1"><strong>배송 요청 사항:</strong> ' + requirement + '</p>');
	                
	                // 생성된 요소를 컨테이너에 추가
	                container.append(box);
	            }
	        },
	        error: function(xhr, status, error) {
	            console.error("Error fetching delivery list: ", error);
	        }
	    }); // end ajax
	}//end getDeliveryList()
	
	
	function getDeliveryStatus(formattedNow, expectedDate){

	 	// 날짜 문자열을 Date 객체로 변환
	    let nowDate = new Date(formattedNow);
	    console.log(nowDate.getTime());
	 	let expectedDateDate = new Date(expectedDate);
		console.log(expectedDateDate.getTime());
	 	
	    // 예상 배송일과 현재 날짜 비교
	    let deliveryStatusHtml;
	    
	    if (nowDate.getTime() <= expectedDateDate.getTime()) {
	    	let formattedExpectedDate = formatDate(expectedDate);
	        // 예상 배송일이 현재 날짜와 같거나 이후인 경우
	        deliveryStatusHtml = '<p class="font-weight-bold text-primary">예상 배송일 : <span style="color: blue;">' + formattedExpectedDate + '</span></p>';
	    } else {
	    	let formattedExpectedDate = formatDate(expectedDate);
	        // 예상 배송일이 현재 날짜보다 이전인 경우
	        deliveryStatusHtml = '<p class="font-weight-bold text-primary">배송 완료 (' + formattedExpectedDate + ' 일에 배송이 완료되었습니다!)</p>';
	    }

	    // deliveryStatusHtml을 #deliveryStatus에 삽입
	    $('#deliveryStatus').html(deliveryStatusHtml);
		
	}//end getDeliveryStatus()
	
	function formatDate(dateString) {
	    let date = new Date(dateString);
	    
	    let formattedDate = new Intl.DateTimeFormat('ko-KR', {
	        year: 'numeric',
	        month: '2-digit',
	        day: '2-digit',
	        hour: '2-digit',
	        minute: '2-digit'
	    }).format(date);
	    
	    return formattedDate;
	}
	
</script>

</body>
</html>