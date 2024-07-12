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

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="${_csrf.parameterName }" content="${_csrf.token }">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<jsp:include page="../include/header.jsp"></jsp:include>
<!-- 모달 스타일 창크기가 변하면는 같이변하게 하는기능 -->
<title>주문 목록</title>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<style>

	 .container {
        font-family: 'Arial', sans-serif; /* 폰트 설정 */
        padding: 20px; /* 전체적인 패딩 */
        width: 65%;
    }
    
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
    
    .order-details .details-text {
        text-align: left;
    }
    
    .order-details img {
        margin-right: 20px; /* 텍스트와 이미지 사이의 간격 */
        max-width: 150px; /* 이미지의 최대 너비 */
    }
    .order-buttons {
    	display: flex;
        flex-wrap: wrap;
        gap: 10px; /* 버튼 사이 간격 */
        margin-top: 10px;
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
            position: absolute; /* 절대 위치 설정 */
	        top: 10px; /* 위에서 10px 위치 */
	        right: 20px; /* 오른쪽에서 20px 위치 */
	        font-size: 28px; /* 버튼 크기 설정 */
	        font-weight: bold; /* 글자 두껍게 */
	        color: #000; /* 글자색 */
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

	<jsp:include page="../include/sideBar.jsp"/>

	<div class="container text-center">
	<br>
    <h1 class="mt-4 mb-4 text-center">${memberDetails.getUsername()}님의 주문 목록</h1><br><br>


    <div id="order-container">
        <c:forEach items="${orderList}" var="orderViewDTO">
            <c:set value="${orderViewDTO.orderVO }" var="orderVO"></c:set>
             <div class="card mb-3 order-box">
             	<div class="card-body">
                 <div class="row order-details">
                    <!-- 이미지 목록 표시 -->
                    <div class="col-md-2">
                          <img src="${orderViewDTO.imgUrl}" class="img-fluid">
                    </div> <!-- closing col-md-2 -->
                    <div class="col-md-10 details-text">
                        <c:if test= "${now <= orderViewDTO.formattedExpectedDeliveryDate}">
                            <p class="font-weight-bold text-primary">예상 배송일 : <span style="color: blue;"> ${orderViewDTO.formattedExpectedDeliveryDate}</p>
                        </c:if>
                        <c:if test= "${now > orderViewDTO.formattedExpectedDeliveryDate}">
                            <p class="font-weight-bold text-primary">배송 완료 ( ${orderViewDTO.formattedExpectedDeliveryDate} 일에 배송이 완료되었습니다!)</p>
                        </c:if>
                        <p>상품명 : ${orderVO.productName}</p>
                        <p>상품 가격 : ${orderVO.productPrice} 원</p>
                        <p>상품 수량 : ${orderVO.purchaseNum} 개</p>
                    </div> <!-- closing col-md-10 -->
                </div> <!-- closing row order-details -->
                <div class="order-buttons">
                    <a href="../Delivery/delivery?paymentId=${orderVO.paymentId}" class="d-inline-flex focus-ring py-1 px-2 text-decoration-none border rounded-3 mr-2">배송 조회</a>
                    
                    <!-- 리뷰 쓰기 코드 -->
                    <form action="../review/register" method="get" class="d-inline">
                        <input type="hidden" name="productId" value="${orderVO.productId}">
                        <button type="submit" class="d-inline-flex focus-ring focus-ring-primary py-1 px-2 text-decoration-none border rounded-2">리뷰 쓰기</button>
                    </form>

                    <!-- 리뷰 관리 코드 -->
                    <form action="../review/list" method="get">
                        <input type="hidden" name="memberId" value="${memberDetails.getUsername()}">
                        <button type="submit" class="d-inline-flex focus-ring py-1 px-2 text-decoration-none border rounded-3 mr-2">리뷰 관리</button>
                    </form>
                    
                    <!-- 문의 리스트 코드 -->
                    <form action="../inquiry/myList" method="get">
                        <input type="hidden" name="memberId" value="${memberDetails.getUsername()}">
                        <input type="hidden" name="productName" value="${orderVO.productName}">
                        <button type="submit" class="d-inline-flex focus-ring py-1 px-2 text-decoration-none border rounded-3 mr-2">문의 리스트</button>
                    </form>
                    
                    <!-- 판매자 문의 버튼 -->
                    <button class="sellerInquiry d-inline-flex focus-ring py-1 px-2 text-decoration-none border rounded-3 mr-2">판매자 문의</button>

                    <!-- 판매자 문의 등록 모달 -->
                    <div class="modal sellerModal createModal">
                        <div class="modal-content">
                            <span class="close">&times;</span>
                            <h2>판매자 문의</h2>
                            <form class="createInquiry">
                                <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
                                <input type="hidden" id="memberId" name="memberId" value="${memberDetails.getUsername()}">
                                <input type="hidden" id="productId" name="productId" value="${orderVO.productId}">
                                <label for="message">내용:</label><br>
                                <textarea class="content form-control" name="content" maxlength="150" style="resize: none;"></textarea><br>
                                <button type="submit" class="btn btn-success">판매자에게 1:1문의하기</button>
                            </form>    
                        </div><!-- closing modal-content -->
                    </div><!-- closing modal sellerModal createModal -->
                    
                    <!-- 판매자 문의 수정 버튼 -->
                    <button class="inquiryUpdate d-inline-flex focus-ring py-1 px-2 text-decoration-none border rounded-3 mr-2">판매자 문의 수정</button>
                    
                    <!-- 판매자 문의 수정 모달 -->
                    <div class="modal sellerModal updateModal">
                        <div class="modal-content">
                            <span class="close">&times;</span>
                            <h2>판매자 문의 수정</h2>
                            <form class="updateInquiry">
                                <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
                                <input type="hidden" id="memberId" name="memberId" value="${memberDetails.getUsername()}">
                                <input type="hidden" id="productId" name="productId" value="${orderVO.productId}">
                                <label for="message">내용:</label><br>
                                <textarea class="content form-control" name="content" maxlength="150" style="resize: none;"></textarea><br>
                                <button type="submit" class="btn btn-info">수정하기</button>
                            </form>    
                        </div><!-- closing modal-content -->
                    </div><!-- closing modal sellerModal updateModal -->
                    
                    <!-- 판매자 문의 삭제 버튼 -->
                    <button class="inquiryDelete d-inline-flex focus-ring py-1 px-2 text-decoration-none border rounded-3 mr-2">판매자 문의 삭제</button>
                    
                    <!-- 판매자 문의 삭제 모달 -->
                    <div class="modal sellerModal deleteModal">
                        <div class="modal-content">
                            <span class="close">&times;</span>
                            <form class="deleteInquiry">
                                <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
                                <input type="hidden" id="memberId" name="memberId" value="${memberDetails.getUsername()}">
                                <input type="hidden" id="productId" name="productId" value="${orderVO.productId}">
                                <button type="submit" class="btn btn-danger">삭제하시겠습니까?</button>
                            </form>
                        </div> <!-- closing modal-content -->
                    </div> <!-- closing modal sellerModal deleteModal -->
                    
                    <!-- 주문 목록 삭제 -->
                    <c:if test= "${now > orderViewDTO.formattedExpectedDeliveryDate}">
                        <button class="deleteOrderBtn d-inline-flex focus-ring py-1 px-2 text-decoration-none border rounded-3 mr-2" data-order-id="${orderVO.orderId}">주문 목록 삭제</button>
                    </c:if>
                    
                </div> <!-- closing order-buttons -->
             </div> <!-- closing card-body -->
            </div> <!-- closing card mb-3 order-box -->
        </c:forEach>
    </div> <!-- closing order-container -->
    
    <!-- 주문 목록이 비어있을 때 -->
    <c:if test="${empty orderList}">
        <div class="alert alert-info mt-3">
            주문 목록이 비어 있습니다.
        </div>
    </c:if>
    
	<!-- 배송지 관리 페이지 -->
    <a href="../Delivery/deliveryAddressList" class="btn btn-info mt-3">배송지 관리</a>
    	
   </div> <!-- closing container -->
    	
    <script type="text/javascript">
    
    const memberId = "${memberDetails.getUsername()}";
    
    $(document).ready(function(){
    	// 해당 버튼의 부모 요소로부터 productId 가져오기
        let productId;
        // 세션에서 memberId 가져오기
        let buttons;
        console.log('${orderList}');
     	// 공통 함수로 이벤트 리스너 추가
        function addModalEventListener(buttonClass, modalClass) {
            // 지정된 클래스 이름을 가진 모든 버튼 요소를 선택
            buttons = document.querySelectorAll(buttonClass);

            // 각 버튼에 대해 반복
            buttons.forEach(function(button) {
                // 버튼에 클릭 이벤트 리스너 추가
                button.addEventListener('click', function() {
                    // 지정된 클래스 이름을 가진 모달 요소를 선택
                    let modal = document.querySelector(modalClass);
                    // 모달을 보이도록 설정
                    modal.style.display = 'block';
                    // 클릭된 버튼의 부모 요소에서 productId 값을 가져옴
                    productId = button.closest('.order-box').querySelector('[name="productId"]').value;
                    console.log(productId);
                    console.log(memberId);
                });
            });// end buttons
        }// end addModalEventListener

    	// 각 판매자 문의 버튼에 대한 이벤트 리스너 추가
    	addModalEventListener('.sellerInquiry', '.createModal');

    	// 각 판매자 문의 수정 버튼에 대한 이벤트 리스너 추가
    	addModalEventListener('.inquiryUpdate', '.updateModal');
    	
    	// 각 판매자 문의 삭제 버튼에 대한 이벤트 리스너 추가
    	addModalEventListener('.inquiryDelete', '.deleteModal');

    	
    	// 모달 닫기 이벤트 리스너 추가
		let closeButtons = document.querySelectorAll('.modal .close');
		closeButtons.forEach(function(button) {
    		button.addEventListener('click', function() {
        		// 버튼의 부모 모달 요소 찾기
        		let modal = button.closest('.modal');
        		// 모달 숨기기
        		modal.style.display = 'none';

        		// 모달이 닫힐 때 입력된 내용 초기화
        		let contentField = modal.querySelector('.content');
        		if (contentField) { // null 체크
            		contentField.value = ''; // 내용 초기화
        		}
    		});
		});
		
    	// 여기 코드 문제점은 내용을 작성 하고 있을때 실수로 모달 바같을 클릭 해서 다시 작성하는 경우가 있을 수 있다.
    	// 모달 외부 클릭 시 닫기 이벤트 리스너를 추가합니다.
    	/* window.addEventListener('click', function(event) {
    	    // 모든 모달 요소를 가져옵니다.
    	    let modals = document.querySelectorAll('.modal');
    	    
    	    // 각 모달 요소에 대해 반복문을 실행합니다.
    	    modals.forEach(function(modal) {
    	        // 클릭된 요소가 모달 요소인 경우 모달을 닫습니다.
    	        if (event.target === modal) {
    	            modal.style.display = 'none';
    	        }
    	        
    	        // 모달 내에 .content 클래스를 가진 요소를 찾습니다.
    	        let contentElement = modal.querySelector('.content');
    	        
    	        // .content 요소가 존재하는지 확인합니다.
    	        if (contentElement) {
    	            // .content 요소의 값을 빈 문자열로 설정하여 초기화합니다.
    	            contentElement.value = '';
    	        }
    	    });
    	}); */

    // 판매자 문의 코드 
    // 폼 제출 시 데이터 출력
    let inquiryCreate = document.querySelectorAll('.createInquiry');
    inquiryCreate.forEach(function(form) {// inquiryForms에 있는 각 요소에 대해 반복문을 실행합니다.
        form.addEventListener('submit', function(event) {// 각 요소에 대해 submit 이벤트 핸들러를 추가합니다.
            event.preventDefault(); // 폼을 제출할 때 페이지를 다시 로드하는 동작을 막습니다.
            
            /* button = form.querySelector('.sellerInquiry'); */
            // 입력된 내용 가져오기
            let inquiryContent = form.querySelector('.content').value;
            console.log(inquiryContent);
            console.log(productId);
            console.log(memberId);
            if(inquiryContent && inquiryContent.trim() !== ''){
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
                      'Content-Type' : 'application/json', // json content-type 설정
                      'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
                   }, //'Content-Type' : 'application/json' 헤더 정보가 안들어가면 4050에러가 나온다.
                   data : JSON.stringify(obj), // JSON으로 변환
                   success : function(result) { // 전송 성공 시 서버에서 result 값 전송
                      console.log(result);
                      if(result == 1) {
                         alert('문의 성공');
                      } else if(result == 2){
                    	  alert('삭제된 상품 입니다.');
                      } else {
                    	  alert('이미 작성 하신 문의 입니다.');
                      }
                   	  // 폼의 부모 모달 요소 찾기
                      let modal = form.closest('.modal');
                   	  // 모달 숨기기
          	          modal.style.display = 'none';
          	     	  // 모달 내의 .content 요소 값 초기화
                      modal.querySelector('.content').value = '';
                   } // end success 
                }); // end ajax 
            } else {
            	alert('내용을 입력 해주세요.');
            }
            
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
            
            if(inquiryContent && inquiryContent.trim() !== ''){
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
                      'Content-Type' : 'application/json', // json content-type 설정
                      'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
                   }, // 'Content - Type' : application/json; 헤더 정보가 안들어가면 4050에러가 나온다.
                   data : JSON.stringify(obj), // JSON으로 변환
                   success : function(result) { // 전송 성공 시 서버에서 result 값 전송
                      console.log(result);
                      if(result == 1) {
                        alert('문의 수정 성공!');
                        
                      } else {
                      	alert('해당 문의가 없습니다.');
                      }
                   	  // 폼의 부모 모달 요소 찾기
                      let modal = form.closest('.modal');
                   	  // 모달 숨기기
          	          modal.style.display = 'none';
          	     	  // 모달 내의 .content 요소 값 초기화
                      modal.querySelector('.content').value = '';
                   }// end success
                }); // ajax 
            } else {
            	alert('내용을 입력 해주세요.');
            }
        });// end form()
    });// end inquiryUpdate()deleteInquiry
    
 	// 삭제 버튼을 클릭하면 선택된 댓글 삭제
    let inquiryDelete = document.querySelectorAll('.deleteInquiry');
    inquiryDelete.forEach(function(form) {// inquiryForms에 있는 각 요소에 대해 반복문을 실행합니다.
        form.addEventListener('submit', function(event) {// 각 요소에 대해 submit 이벤트 핸들러를 추가합니다.
            event.preventDefault(); // 폼을 제출할 때 페이지를 다시 로드하는 동작을 막습니다.
            
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
             'Content-Type' : 'application/json',
             'X-CSRF-TOKEN' : $('meta[name="${_csrf.parameterName }"]').attr('content')
          },
          data : JSON.stringify(obj), // JSON으로 변환
          success : function(result) {
             console.log(result);
             if(result == 1) {
                alert('문의 삭제 성공!');
             } else {
            	alert('문의 삭제 실패!');
             }
          	 // 폼의 부모 모달 요소 찾기
             let modal = form.closest('.modal');
          	  // 모달 숨기기
 	          modal.style.display = 'none';
          }
       }); 
    });// end form()
    });// end inquiryDelete()
    
    
     // 주문 목록 삭제 ajax 요청
    $('.deleteOrderBtn').on('click', function() {
    	let orderId = $(this).data('order-id');
        
		console.log("orderId : " + orderId);
		console.log(typeof orderId);
		
		
        if (confirm('정말로 이 주문을 삭제하시겠습니까?')) {
            $.ajax({
                type: 'DELETE',
                url: 'deleteOrder/' + orderId,
                headers: {
                    'X-CSRF-TOKEN': '${_csrf.token}'
                },
                success: function(response) {
                    console.log('주문 목록이 삭제되었습니다.');
                    window.location.href = "../order/orderlist"; 
                },
                error: function(error) {
                    console.error('주문 목록 삭제 도중 오류 발생: ', error);
                    alert('주문 목록 삭제 도중 오류가 발생했습니다. 다시 시도해 주세요.');
                }
            });//end ajax delete
        }//end if
    });// end $('.deleteOrderBtn')
     
     
    }); // end document.ready()

    
    </script>

</body>
</html>