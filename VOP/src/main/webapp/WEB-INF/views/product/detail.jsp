<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>상품 상세 조회</title>
</head>
<body>
	<!-- 제작 중 -->
	<h2>상품 상세 페이지</h2>
	 <div>
      <p>상품 이름 : ${productVO.productName }</p>
     </div>
     
     <div>
      <p>리뷰 평균 : </p>
 	 </div>
 	 <div>
      <p>상품 가격 : ${productVO.productPrice }</p>
     </div>
     
     <!-- 클릭 했을때 상품 감소 -->
     <button class="decrease">-</button>
     
     <!-- 현재 선택된 상품 수량 -->
	 <span id="quantity">${productVO.product_remains}</span>
	 
	 <!-- 클릭 했을때 상품 증가 -->
	 <button class="increase">+</button>
     <!-- 상품 배송 정보 -->
     
     <div>
      <p>판매자 : ${productVO.memberId }</p>
     </div>
     <div>
      <p>상품 번호 : ${productVO.productId }</p>
     </div>
     
     <script type="text/javascript">
     // 상품 수량 증감 코드
     // 제작 중 아직 미완성
     $(document).ready(function() {
    	 
    	    $('.decrease').on('click', function() {
    	        let productId = ${productVO.productId};
    	        let productRemains = parseInt($('#productRemains').text());
    	        if (productRemains > 1) {
    	        	productRemains--;
    	            updateRemains(productId, productRemains);
    	        }
    	    }); // end decrease
    	    
    	    $('.increase').on('click', function() {
    	    	let productId = ${productVO.productId};
    	    	let productRemains = parseInt($('#productRemains').text());
    	        productRemains++;
    	        updateRemains(productId, productRemains);
    	    });// end increase
    	    
    	    function updateRemains(productId, productRemains) {
    	        $.ajax({
    	            type: "POST",
    	            url: "/updateRemains",
    	            contentType: "application/json",
    	            data: JSON.stringify({ productId: productId, productRemains: productRemains }),
    	            success: function(response) {
    	                // 성공 시 수행할 작업
    	                $('#productRemains').text(productRemains);
    	            },
    	            error: function(xhr, status, error) {
    	                // 오류 발생 시 수행할 작업
    	                console.error("수량 업데이트 실패: " + error);
    	            }
    	        });// end ajax
    	    }// end updateRemains()
    	    
    	}); // end document
     // end 상품 수량 증감 코드 
     
     </script>
     
		
</body>
</html>