<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

<!-- ${param.paymentId} -->

	<h2>배송 조회</h2>
	<div>
    <p>배송예정일 : ${date}</p>
    <p>송장 번호 : ${getPayment}</p>
  </div>

  <div class="box">
     <c:forEach items="${paymentList}" var="payment">
        <div class="label">받는 사람:</div>
        <div>${payment.receiverName}</div><br>
        <div class="label">받는 주소:</div>
        <div>${payment.deliveryAddress}</div><br>
        <div class="label">배송요청사항:</div>
        <div>${payment.requirement}</div>
    </c:forEach>
  </div>
  
  <script>
  	$(document).ready(function(){
  		let expectedDate = new Date("${date}");
  		let currentDate = new Date();
  		
  		if (currentDate < expectedDate) {
  			$("#deliveryStatus").text("배송 예정일 : " + formatDate(expectedDate));
  		} else {
  			$("#deliveryStatus").text("배송 완료 되었습니다! 배송 완료일 : " + formatDate(expectedDate) + ")" );
  		}
  	});
  	
  	function formatDate(date){
  		let formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
  		return formattedDate;
  	}
  </script>
	
</body>
</html>