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
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
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
</style>    
    
    <style>
	/* 로그인 , 회원가입 링크 -> 우측 정렬 안됨. */
		.container2 {
		    background-color: #f0f0f0;
		    width: 1800px;
		    padding: 30px;
		    margin: 5px;
		    
		}

		.container2.user-links2 {
		    margin-top: 20px;
		}
		
		.container2.user-links2 a {
		    margin-left: 10px;
		    margin-right: 0px; /*우측 여백 제거*/
		    text-align: right; /* 로그인과 회원가입 text를 우측 정렬 */
		    color: black;
		    text-decoration: none;
		    margin-left: 320px; /* 왼쪽 여백 추가 */
		}
		
		.container2.user-links2 a:hover {
		    color: black;
		}
	
	

		.container {
            text-align: center; /* 중앙 정렬 */
        }
	
        /*카테고리*/
        .category {
            position: relative;
            display: inline-block;
            border: 1px solid black; /*박스 추가*/
            width: 70px; /* 박스 너비 */
    		height: 20px; /* 박스 높이 */
            padding: 10px; /*박스 내부 padding 추가*/
            background-color: lightgreen; /*박스 색상 추가*/
            margin-top: 80px; /* 상단 여백 추가*/
            margin-bottom: 20px; /* 위 아래 여백 추가 */
            margin-right: 50px; /* 오른쪽 여백 추가 */
            margin-left: 320px; /* 왼쪽 여백 추가 */
        }
        
        .category:hover .subcategory {
            display: block; /* 부모 요소에 마우스를 올리면 서브 카테고리 표시 */
        }
        
        .subcategory {
           	display: none; /* 마우스 up 아닐땐 숨김 */
            position: absolute;
            top: 100%;
            left: 0;
            right: 10;
            background-color: #f9f9f9;
            padding: 10px;
            border: 1px solid #ccc;
            z-index: 1;
            font-size: 14px; 
        }
        
        .subcategory a {
            display: block;
            color: #333;
            text-decoration: none;
            padding: 5px 0;
        }
        
        .subcategory a:hover {
            background-color: #ddd;
        }
        
        
        
        
         /* VOP 링크 스타일링 */
        .vop-link {
            
            margin-right: 50px;
            margin-bottom: 30px; /* 위 아래 여백 추가 */
            color: blue; /* 링크 색상 지정 */
            text-decoration: none; /* 밑줄 제거 */
        }

        /* VOP 링크 호버 스타일링 */
        .vop-link:hover {
            color: darkblue; /* 호버 시 색상 변경 */
        }
        
         /* 검색창 스타일링 */
        .search-container {
            display: inline-block;
            margin-bottom: 20px; /* 위 아래 여백 추가 */
            
        }

        /* 검색 입력창 스타일링 */
        .search-input {
            padding: 8px 16px; /* 내부 여백 설정 */
            margin-right: 10px; /* 오른쪽 여백 설정 */
            border: 1px solid #ccc; /* 테두리 설정 */
            border-radius: 4px; /* 테두리 둥글게 설정 */
        }
        
        /* 검색 버튼 스타일링 */
        .search-button {
            padding: 8px 16px; /* 내부 여백 설정 */
            margin-right: 50px;
            background-color: #007bff; /* 배경색 지정 */
            color: #fff; /* 글자색 지정 */
            border: none; /* 테두리 없애기 */
            border-radius: 4px; /* 테두리 둥글게 설정 */
            cursor: pointer; /* 포인터 커서 설정 */
        }

        /* 검색 버튼 호버 스타일링 */
        .search-button:hover {
            background-color: #0056b3; /* 호버 시 배경색 변경 */
        }
        
        /* 마이페이지, 장바구니 링크 스타일링 */
	    .user-links {
	        display: inline-block;
	        margin-left: 20px; /* 검색창과 간격 조정 */
	    }
	
	    .user-links a {
	        margin-left: 10px; /* 링크들 간의 간격 조정 */
	        margin-right: 20px;
	        color: black;
	        text-decoration: none;
	    }
	
	    .user-links a:hover {
	        color: black;
	    }
	    
	    /* 마이페이지 - 하위 메뉴 숨김 상태로 설정*/
        
        /* 부모 링크에 position: relative; 스타일 적용 */
		.user-links {
		    position: relative;
		}
		
		/* 서브 메뉴에 position: absolute; 스타일 적용 */
		.submenu {
		    display: block; /* 줄 바꿈 */
		    position: absolute;
		    top: 100%;
		    left: 0;
		    background-color: #fff; /* 배경색 */
		    border: 1px solid #ccc; /* 테두리 */
		    padding: 10px;
		    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2); /* 그림자 효과 */
		}
		
		/* 부모 링크에 호버 시 서브 메뉴 표시 */
		.user-links:hover .submenu {
		    display: block;
		}
		
		.user-links .submenu {
		    display: none;
		    position: absolute;
		    /* 다른 스타일 속성들 */
		    /* 필요한 경우에 추가적인 스타일 지정 */
		}
		
		/* 서브 메뉴 항목 스타일링 */
		.submenu a {
		    display: block;
		    margin-bottom: 5px; /* 항목 간격 조정 */
		    text-decoration: none; /* 밑줄 제거 */
		    color: #333; /* 글자색 지정 */
		}
		
		/* 서브 메뉴 항목 호버 스타일링 */
		.submenu a:hover {
		    background-color: #f0f0f0; /* 호버 시 배경색 변경 */
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
   
	<div class="category">
        <span>카테고리</span>
        <div class="subcategory">
            <a href="#">패션의류/잡화</a>
            <a href="#">뷰티</a>
            <a href="#">출산/유아동</a>
            <a href="#">식품</a>
            <a href="#">주방용품</a>
            <a href="#">생활용품</a>
            <a href="#">홈인테리어</a>
            <a href="#">가전디지털</a>
            <a href="#">스포츠/레저</a>
            <a href="#">자동차 용품</a>
            <a href="#">도서/음반/DVD</a>
            <a href="#">완구/취미</a>
            <a href="#">문구/오피스</a>
            <a href="#">반려동물용품</a>
            <a href="#">헬스/건강식품</a>
        </div>
	</div>
		
	
	<!-- VOP 링크 추가 -->
    <a href="main" class="vop-link">VOP</a>
    
    <!--  JavaScript를 사용하여 페이지를 새로고침 -->
    <script>
    	document.querySelector('.vop-link').addEventListener('click', function() {
        	location.reload();
   	 });
	</script>
    
          
	<!-- 카테고리 드롭다운 -->
    <div class="search-container">
          <!-- 카테고리 선택 드롭다운 -->
          <select id="boxCategory">
            <option value="전체">전체</option>
            <option value="여성패션">여성패션</option>
            <option value="남성패션">남성패션</option>
            <option value="남녀 공용 의류">남녀 공용 의류</option>
            <option value="유아동 패션">유아동 패션</option>
            <option value="뷰티">뷰티</option>
            <option value="출산/유아동">출산/유아동</option>
            <option value="식품">식품</option>
            <option value="주방용품">주방용품</option>
            <option value="생활용품">생활용품</option>
            <option value="홈인테리어">홈인테리어</option>
            <option value="가전디지털">가전디지털</option>
            <option value="스포츠/레저">스포츠/레저</option>
            <option value="자동차 용품">자동차 용품</option>
            <option value="도서/음반/DVD">도서/음반/DVD</option>
            <option value="완구/취미">완구/취미</option>
            <option value="문구/오피스">문구/오피스</option>
            <option value="반려동물용품">반려동물용품</option>
            <option value="헬스/건강식품">헬스/건강식품</option>
          </select>  
        <!-- 검색 입력창 -->
        <input type="text" class="search-input" placeholder="검색어를 입력하세요">
        <!-- 검색 버튼 -->
        <button class="search-button">검색</button><br><br>
    </div>
    
	<!-- 마이페이지(mypage), 장바구니(basket) 링크 -->
    <div class="user-links">
        	<a href="mypage">마이페이지</a>
        	<div class="submenu">
        		<a href="orderlist">주문목록</a>
        		<a href="#">취소/반품</a>
        		<a href="#">찜리스트</a>
        	</div>	
        	<a href="basket">장바구니</a>
    </div>




<h1> 주문 목록 </h1>

<%-- <%
	// 세션 객체 가져오기
	HttpSession sessionJSP = request.getSession();
	// 세션에 저장된 memberId 가져오기
	String memberId = (String) sessionJSP.getAttribute("memberId");
%> --%>
	
	<!-- 회원id -->
	<h1>${memberDetails.getUsername() }</h1>

	
    <c:forEach items="${orderList}" var="order">
        <div class="order-box">
            <div class="order-details">
                <p>예상 배송일 : ${order.expectDeliveryDate}</p>
                	<!-- 이미지 목록 표시 -->
					<c:forEach items="${imageList}" var="image">
					    <img src="showImg?imgId=${image.imgId}" alt="${image.imgRealName}.${image.imgExtension}">
					</c:forEach>
                <div>
                    <p>상품명 : ${order.productName}</p>
                    <p>상품 가격 : ${order.productPrice} 원</p>
                    <p>상품 수량 : ${order.purchaseNum} 개</p>
                </div>
            </div>
            <div class="order-buttons">
                <a href=""><button>배송 조회</button></a>
                
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
	                
				<a href=""><button>교환/반품 신청</button></a>
            </div>
        </div>
    </c:forEach>
    
    <!-- 주문 목록이 비어있을 때 -->
    <c:if test="${empty orderList}">
        <div>
            <p>주문 목록이 비어 있습니다.</p>
        </div>
    </c:if>
  
<%-- <%  
    }   
%> --%>

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

    	// 각 판매자 문의 버튼에 대한 이벤트 리스너 추	가
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
                     window.location.href = '../board/orderlist';
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
                alert('댓글 삭제 성공!');
                window.location.href = '../board/orderlist';
             }
          }
       });
    });// end form()
    });// end inquiryDelete()
    
    }); // end document.ready()
    
    </script>
	
	
	
</body>
</html>