<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

<title>배송조회</title>
	<style type="text/css">
		.box {
		    margin-bottom: 20px;
		  }
		.label {
		    font-weight: bold;
		  }
	</style>

</head>
<body>
	<h2>배송 조회</h2>
	<form action="delivery" method="POST">
		<div>
			<p>배송예정일 : </p>
			<input type="text" name="expectDeliveryDate" value="${OrderVO.expectDeliveryDate }" readonly>
		</div>
	
		<div>
			<p>송장 번호 : </p>
			<input type="text" name="paymentId" value="${OrderVO.paymentId }" readonly>
		</div>
		
		
	</form>
	
	
	  <div class="box">
	    <div class="label">받는 사람:</div>
	    <div id="receiverName"></div>
	    <div class="label">받는 주소:</div>
	    <div id="receiverAddress"></div>
	    <div class="label">배송요청사항:</div>
	    <div id="requirement"></div>
	  </div>
	
	<script>
	// 서버로부터 데이터를 받아오는 함수
	function fetchData() {
	  $.ajax({
		  url : '/Delivery/getDeliveryInfo',
		  method : 'GET',
		  success : function(data) {
			  document.getElementById("receiverName").innerText = data.receiverName;
			  document.getElementById("receiverAddress").innerText = data.receiverAddress;
			  document.getElementById("requirement").innerText = data.requirement;
		  },
		  error: function(xhr, status, error) {
			  console.error('AJAX 오류 :', status, error);
		  }
	  });
	
	}
	
	// 페이지 로드시 데이터를 받아오도록 설정
	$(document).ready(function() {
		fetchData();
	});
	</script>

</body>
</html>