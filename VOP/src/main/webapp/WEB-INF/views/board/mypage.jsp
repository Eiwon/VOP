<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="../include/header.jsp"></jsp:include>
<style type="text/css">

.info_container {
	width: 70%;
	margin: auto;
}
.inner_header {
	margin: 15px;
}	
.link_box {
	margin: 15px;
}
</style>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<!-- 부트스트랩 아이콘 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css">

<title>마이페이지</title>
<style>
    body {
        padding-top: 56px;
    }
    /* .info_container {
        display: flex;
        justify-content: center;
        padding: 2rem;
    } */
    .side_bar {
        width: 250px;
        margin-right: 2rem;
    }
    .side_bar ul {
        list-style: none;
        padding: 0;
    }
    .side_bar ul li {
        margin: 10px 0;
    }
    .side_bar ul li a {
        text-decoration: none;
        color: #007bff;
    }
    .side_bar ul li a:hover {
        text-decoration: underline;
    }
    

</style>
</head>

<body>

	<%-- <!-- 네비게이션 바 -->
	  <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
	        
	        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
	            <span class="navbar-toggler-icon"></span>
	        </button>
	        <div class="collapse navbar-collapse" id="navbarNav">
	            <ul class="navbar-nav ml-auto">
	                <li class="nav-item"><a class="nav-link" href="../order/orderlist">주문 목록</a></li>
	                <li class="nav-item"><a class="nav-link" href="../Delivery/deliveryAddressList">배송지 관리</a></li>
	                <li class="nav-item"><a class="nav-link" href="../coupon/myCoupon">쿠폰함</a></li>
	                <li class="nav-item"><a class="nav-link" href="../membership/register">멤버십 등록</a></li>
	                <li class="nav-item"><a class="nav-link" href="../seller/main">판매자 페이지</a></li>
	                <sec:authorize access="hasRole('ROLE_관리자')">
	                    <li class="nav-item"><a class="nav-link" href="admin">관리자 페이지</a></li>
	                </sec:authorize>
	                <li class="nav-item"><a class="nav-link" onclick="consult()">1대1 상담</a></li>
	            </ul>
	        </div>
	    </nav>
	     --%>
	<div class="info_container">
		<div class="inner_header">
			<h3>마이 페이지</h3>
		</div>
		<div class="container text-center">
  			<div class="row row-cols-5">
    			<div class="col link_box">
    			<a href="myInfo">
    				<img src="${pageContext.request.contextPath}/resources/myInfo.png" class="logo_img">
    				<div>내 정보</div>
    			</a>
    			</div>
    			<div class="col link_box">
    			<a href="../basket/main">
    				<img src="${pageContext.request.contextPath}/resources/basket.png" class="logo_img">
    				<div>장바구니</div>
    			</a>
    			</div>
    			<div class="col link_box">
    			<a href="../order/orderlist">
    				<img src="${pageContext.request.contextPath}/resources/order.png" class="logo_img">
    				<div>주문 목록</div>
    			</a>
    			</div>
   		 		<div class="col link_box">
   		 		<a href="../Delivery/deliveryAddressList">
   		 			<img src="${pageContext.request.contextPath}/resources/delivery.png" class="logo_img">
    				<div>배송지 관리</div>
   		 		</a>
   		 		</div>
    			<div class="col link_box">
    			<a href="../coupon/myCoupon">
    				<img src="${pageContext.request.contextPath}/resources/coupon.png" class="logo_img">
    				<div>쿠폰함</div>
    			</a>
    			</div>
    			<div class="col link_box">
    			<a href="../membership/register">
    				<img src="${pageContext.request.contextPath}/resources/membership.png" class="logo_img">
    				<div>멤버십</div>
    			</a>
    			</div>
    			<div class="col link_box">
    			<a href="../seller/main">
    				<img src="${pageContext.request.contextPath}/resources/seller.png" class="logo_img">
    				<div>판매자</div>
    			</a>
    			</div>
    			<sec:authorize access="hasRole('ROLE_관리자')">
    				<div class="col link_box">
    				<a href="admin">
    					<img src="${pageContext.request.contextPath}/resources/admin.png" class="logo_img">
    					<div>관리자</div>
    				</a>
    				</div>
    			</sec:authorize>
    			<sec:authorize access="!hasRole('ROLE_관리자')">
    				<div class="col link_box">
    				<a onclick="consult()">
    					<img src="${pageContext.request.contextPath}/resources/consult.png" class="logo_img">
    					<div>1대1 상담</div>
    				</a>
    				</div>
				</sec:authorize>
  			</div>
		</div>
		
	</div>
	
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    
	<script type="text/javascript">
	function consult(){
		let targetUrl = 'consult';
		
		const popupStat = {
				'url' : targetUrl,
				'name' : 'popupConsult',
				'option' : 'width=900, height=1000, top=50, left=400'
		};
		
		// 팝업 창 띄우기
		let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
		popup.onbeforeunload = function(){
			// 팝업 닫힐 때 실행
			console.log("팝업 닫힘");
		} // end popup.onbeforeunload
	} // end showSocketPopup
	</script>
</body>
</html>