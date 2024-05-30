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
  	// 서버에서 전송된 날짜 문자열
  	    let dateStr = "${date}";
  	    console.log("date : " + dateStr);
		
  	// ISO 8601 형식으로 변환된 날짜 문자열을 Date 객체로 파싱
  		let expectedDate = new Date(dateStr);
  		console.log("배송예상일 : " + expectedDate);
  		
  		let currentDate = new Date();
  		console.log("현재날짜 : " + currentDate);
  		
  		if (currentDate < expectedDate) {
  			$("#deliveryStatus").text("배송 예정일 : " + formatDate(expectedDate));
  			console.log("formatDate(expectedDate) : " + formatDate(expectedDate));
  		} else {
  			$("#deliveryStatus").text("배송 완료 되었습니다! 배송 완료일 : " + formatDate(expectedDate) + ")" );
  		}
  	});
  	
  	function formatDate(date){
  	    // 날짜 포맷팅
  	    let year = date.getFullYear();
  	    let month = ("0" + (date.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 1을 더하고 2자리로 포맷팅
  	    let day = ("0" + date.getDate()).slice(-2); // 일도 2자리로 포맷팅
  	    
  	    let formattedDate = year + "-" + month + "-" + day;
  	    return formattedDate;
  	}
  </script>
	
</body>
</html>