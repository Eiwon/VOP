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
<title>마이페이지</title>
</head>

<body>
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
		<%-- <div class="content_list">
			<div> 
				<ul id="my_info">
					<li><a href="myInfo">내 정보 확인 / 수정</a></li>
					<li><a href="../order/orderlist">주문 목록</a></li>
					<li><a href="../Delivery/deliveryAddressList">배송지 관리</a></li>
					<li><a href="../coupon/myCoupon">쿠폰함</a></li>
					<li><a href="../membership/register">멤버십 등록</a></li>
					<li><a href="../seller/main">판매자 페이지</a></li>
					<sec:authorize access="hasRole('ROLE_관리자')">
						<li><a href="admin">관리자 페이지</a></li>
					</sec:authorize>
					<sec:authorize access="!hasRole('ROLE_관리자')">
						<li><a onclick="consult()">1대1 상담</a> </li>
					</sec:authorize>
				</ul>
			</div>
		</div> --%>
	</div>
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