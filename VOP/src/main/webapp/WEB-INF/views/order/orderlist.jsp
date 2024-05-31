<%@page import="com.web.vop.domain.OrderVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<!-- 시큐리티 회원id 관련 코드 -->
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize> 
<%
    OrderVO orderVO = new OrderVO(); // OrderVO 객체 생성
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<!-- 모달 스타일 창크기가 변하면는 같이변하게 하는기능 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>주문 목록</title>
<style>

    .order-box {
        border: 1px solid #ccc;
        padding: 10px;
        margin-bottom: 20px;
    }
    .order-details {
        display: flex;
        align-items: center;
        margin-bottom: 10px;
    }
    .order-details img {
        margin-right: 10px;
        max-width: 100px;
    }
    .order-buttons {
        margin-left: auto;
    }
		 /* 모달 스타일 */
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
        }

        /* 모달 닫기 버튼 스타일 */
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
		/* 모달 스타일 끝 */
		
		
        
</style>

</head>
<body>

<h1> 주문 목록 </h1>

	<!-- 회원id -->
	<h1>${memberDetails.getUsername() }</h1>

	<div id="order-container">
    <c:forEach items="${orderList}" var="order">
        <div class="order-box">
            <div class="order-details">
                <p>예상 배송일 : ${order.expectDeliveryDate.toLocaleString()}</p>
                	<!-- 이미지 목록 표시 -->
					<div>
                        <img alt="${order.imgId}" src="" />
					</div>	
                <div>
                    <p>상품명 : ${order.productName}</p>
                    <p>상품 가격 : ${order.productPrice} 원</p>
                    <p>상품 수량 : ${order.purchaseNum} 개</p>
                </div>
            </div>
            <div class="order-buttons">
                <a href="../Delivery/delivery?paymentId=${order.paymentId}"><button>배송 조회</button></a>
                
                	<!-- 리뷰 쓰기 코드 -->
	                <form action="../review/register" method="get">
	                	<input type="hidden" name="productId" value="${order.productId}">
	                	<input type="hidden" name="imgId" value="${order.imgId}">
	                	<button type="submit">리뷰 쓰기</button>
	                </form>

	                 <!-- 리뷰 관리 코드 -->
	                 <form action="../review/list" method="get">
	                	<input type="hidden" name="memberId" value="${memberDetails.getUsername()}">
	                	<button type="submit">리뷰 관리</button>
	                </form>
	                
	                <!-- 판매자 문의 코드 -->
	                <button class="sellerInquiry">판매자 문의</button>

					<!-- 판매자 문의 등록 모달 -->
					<div class="modal sellerModal createModal">
    					<div class="modal-content">
        				<span class="close">&times;</span>
        				<h2>판매자 문의</h2>
        					<form class="createInquiry">
            					<input type="hidden" id="memberId" name="memberId" value="${memberDetails.getUsername()}">
            					<input type="hidden" id="productId" name="productId" value="${order.productId}">
            					<label for="message">내용:</label><br>
            					<textarea class="content" name="content"></textarea><br>
            					<button type="submit">판매자에게 1:1문의하기</button>
        					</form>    
    					</div>
					</div>
					
					<!-- 판매자 수정 코드 -->
	                <button class="inquiryUpdate">판매자 문의 수정</button>
	                
					<!-- 판매자 문의 수정 모달 -->
					<div class="modal sellerModal updateModal">
    					<div class="modal-content">
        				<span class="close">&times;</span>
        				<h2>판매자 수정</h2>
        					<form class="updateInquiry">
            					<input type="hidden" id="memberId" name="memberId" value="${memberDetails.getUsername()}">
            					<input type="hidden" id="productId" name="productId" value="${order.productId}">
            					<label for="message">내용:</label><br>
            					<textarea class="content" name="content"></textarea><br>
            					<button type="submit">수정하기</button>
        					</form>    
    					</div>
					</div>
					
					<!-- 판매자 삭제 코드 -->
	                <button class="inquiryDelete">판매자 문의 삭제</button>
					
	                <!-- 판매자 문의 삭제 모달 -->
					<div class="modal sellerModal deleteModal">
    					<div class="modal-content">
        				<span class="close">&times;</span>
	                	<form class="deleteInquiry">
	                		<input type="hidden" id="memberId" name="memberId" value="${memberDetails.getUsername()}">
            				<input type="hidden" id="productId" name="productId" value="${order.productId}">
            				<button type="submit">삭제하시 겠습니까?</button>
	                	</form>
	                	</div>
					</div>
	                
				<!-- <a href=""><button>교환/반품 신청</button></a> -->
            </div>
        </div>
    </c:forEach>
    </div>
    
    <!-- 주문 목록이 비어있을 때 -->
    <c:if test="${empty orderList}">
        <div>
            <p>주문 목록이 비어 있습니다.</p>
        </div>
    </c:if>

	<!-- 배송지 관리 페이지 -->
    <a href="../Delivery/deliveryAddressList">배송지 관리</a>
    	
    <script type="text/javascript">
    
    const memberId = "${memberDetails.getUsername()}";
    
    $(document).ready(function(){
    	// 해당 버튼의 부모 요소로부터 productId 가져오기
        let productId;
        // 세션에서 memberId 가져오기
        /* let memberId; */ // JSP 코드를 사용하여 세션 데이터 가져오기
        let buttons;
    	// 공통 함수로 이벤트 리스너 추가
    	function addModalEventListener(buttonClass, modalClass) {
    	    buttons = document.querySelectorAll(buttonClass);
    	    buttons.forEach(function(button) {
    	        button.addEventListener('click', function() {
    	            let modal = document.querySelector(modalClass);
    	            modal.style.display = 'block';
    	            // 해당 버튼의 부모 요소로부터 productId 가져오기
    	            productId = button.closest('.order-box').querySelector('[name="productId"]').value;
    	            // 세션에서 memberId 가져오기
    	            /* memberId = '${memberId}'; */ // JSP 코드를 사용하여 세션 데이터 가져오기
    	            console.log(productId);
    	            console.log(memberId);
    	        });
    	    });
    	}

    	// 각 판매자 문의 버튼에 대한 이벤트 리스너 추가
    	addModalEventListener('.sellerInquiry', '.createModal');

    	// 각 판매자 문의 수정 버튼에 대한 이벤트 리스너 추가
    	addModalEventListener('.inquiryUpdate', '.updateModal');
    	
    	// 각 판매자 문의 수정 버튼에 대한 이벤트 리스너 추가
    	addModalEventListener('.inquiryDelete', '.deleteModal');

    	// 모달 닫기 이벤트 리스너 추가
    	let closeButtons = document.querySelectorAll('.modal .close');
    	closeButtons.forEach(function(button) {
    	    button.addEventListener('click', function() {
    	        let modal = button.closest('.modal');
    	        modal.style.display = 'none';
    	    });
    	});

    	// 모달 외부 클릭 시 닫기
    	window.addEventListener('click', function(event) {
    	    let modals = document.querySelectorAll('.modal');
    	    modals.forEach(function(modal) {
    	        if (event.target === modal) {
    	            modal.style.display = 'none';
    	        }
    	    });
    	});
    // 판매자 문의 코드 
    // 폼 제출 시 데이터 출력
    let inquiryCreate = document.querySelectorAll('.createInquiry');
    inquiryCreate.forEach(function(form) {// inquiryForms에 있는 각 요소에 대해 반복문을 실행합니다.
        form.addEventListener('submit', function(event) {// 각 요소에 대해 submit 이벤트 핸들러를 추가합니다.
            event.preventDefault(); // 폼을 제출할 때 페이지를 다시 로드하는 동작을 막습니다.
            
            /* button = form.querySelector('.sellerInquiry'); */
            // 입력된 내용 가져오기
            let inquiryContent = form.querySelector('.content').value;

            console.log(productId);
            console.log(memberId);
            
            // TODO: 서버로 데이터 전송
            let obj = {	
            	'productId' : productId,
            	'memberId' : memberId,
            	'inquiryContent' : inquiryContent
            }
            console.log(obj);
            
         	// $.ajax로 송수신
            $.ajax({
               type : 'POST', // 메서드 타입
               url : '../inquiryRest/register', // url
               headers : { // 헤더 정보
                  'Content-Type' : 'application/json' // json content-type 설정
               }, //'Content-Type' : 'application/json' 헤더 정보가 안들어가면 4050에러가 나온다.
               data : JSON.stringify(obj), // JSON으로 변환
               success : function(result) { // 전송 성공 시 서버에서 result 값 전송
                  console.log(result);
                  if(result == 1) {
                     alert('문의 성공');
                  } else if(result == 2){
                	  alert('삭제된 상품 입니다.');
                  } else{
                	  alert('이미 작성 하신 문의 입니다.');
                  }
               } // end success 
            }); // end ajax 
        });// end form()
    });// end inquiryCreate()
    
    let inquiryUpdate = document.querySelectorAll('.updateInquiry');
    inquiryUpdate.forEach(function(form) {// inquiryForms에 있는 각 요소에 대해 반복문을 실행합니다.
        form.addEventListener('submit', function(event) {// 각 요소에 대해 submit 이벤트 핸들러를 추가합니다.
            event.preventDefault(); // 폼을 제출할 때 페이지를 다시 로드하는 동작을 막습니다.
        	
            /* button = form.querySelector('.inquiryUpdate'); */
            
            let inquiryContent = form.querySelector('.content').value;
            
            console.log(productId);
            console.log(memberId);
            console.log(inquiryContent);

            let obj = {
            		'productId' : productId,
                	'memberId' : memberId,
                	'inquiryContent' : inquiryContent
            }      
            console.log(obj);
            
            // ajax 요청
            $.ajax({
               type : 'PUT', // 메서드 타입
               url : '../inquiryRest/modify',// 경로 
               headers : {
                  'Content-Type' : 'application/json' // json content-type 설정
               }, // 'Content - Type' : application/json; 헤더 정보가 안들어가면 4050에러가 나온다.
               data : JSON.stringify(obj), // JSON으로 변환
               success : function(result) { // 전송 성공 시 서버에서 result 값 전송
                  console.log(result);
                  if(result == 1) {
                    alert('문의 수정 성공!');
                  } else {
                  	alert('해당 문의가 없습니다.');
                  }
               }
            }); // ajax 
        });// end form()
    });// end inquiryUpdate()deleteInquiry
 	// 삭제 버튼을 클릭하면 선택된 댓글 삭제
    let inquiryDelete = document.querySelectorAll('.deleteInquiry');
    inquiryDelete.forEach(function(form) {// inquiryForms에 있는 각 요소에 대해 반복문을 실행합니다.
        form.addEventListener('submit', function(event) {// 각 요소에 대해 submit 이벤트 핸들러를 추가합니다.
            event.preventDefault(); // 폼을 제출할 때 페이지를 다시 로드하는 동작을 막습니다.
    	
        /* button = form.querySelector('.inquiryDelete'); */
            
    	/* let productId = $('#productId').val();
        let memberId = $('#memberId').val(); */
        console.log("productId : " + productId);
        console.log("memberId : " + memberId);
		
        let obj = {
        		'productId' : productId,
            	'memberId' : memberId
        }      
        console.log(obj);
        
       // ajax 요청
       $.ajax({
          type : 'DELETE', 
          url : '../inquiryRest/delete',
          headers : {
             'Content-Type' : 'application/json'
          },
          data : JSON.stringify(obj), // JSON으로 변환
          success : function(result) {
             console.log(result);
             if(result == 1) {
                alert('문의 삭제 성공!');
             } else {
            	alert('문의 삭제 실패!');
             }
          }
       }); 
    });// end form()
    });// end inquiryDelete()
    	loadImg();
    }); // end document.ready()

    // 이미지 관련 코드

    function loadImg(){
    	console.log($(document).find('img'));
        $(document).find('img').each(function(){
        	
            let target = $(this);
            let imgId = target.attr("alt");
            console.log("Requesting image with imgId:", imgId); // 콘솔 로그 추가
            $.ajax({
                method : 'GET',
                url : '../image/' + imgId,
                success : function(result){
                    console.log("Image successfully loaded for imgId:", imgId); // 콘솔 로그 추가
                    target.attr('src', result);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error("Error loading image for imgId:", imgId, textStatus, errorThrown); // 에러 로그 추가
                }
            }); // end ajax
        });
    } // end loadImg
    
    </script>
	
	
	
</body>
</html>