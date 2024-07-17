<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
.side_bar {
	width: 150px;
	text-align: left;
	margin: 2%;
}
.side_bar a {
	display: block;
    margin: 3px;
    text-decoration: none;
}
	
</style>
</head>

<body>
		<div class="side_bar fixed-bottom">
			<div> 
				<ul id="my_info" class="list-group">
					<li class="list-group-item"><a href="../board/myInfo">내 정보 수정</a></li>
					<li class="list-group-item"><a href="../basket/main">장바구니</a></li>
					<li class="list-group-item"><a href="../order/orderlist">주문 목록</a></li>
					<li class="list-group-item"><a href="../review/list">리뷰 목록</a></li>
					<li class="list-group-item"><a href="../inquiry/myList">문의 목록</a></li>
					<li class="list-group-item"><a href="../Delivery/deliveryAddressList">배송지 관리</a></li>
					<li class="list-group-item"><a href="../coupon/myCoupon">쿠폰함</a></li>
					<li class="list-group-item"><a href="../membership/register">멤버십 등록</a></li>
					<li class="list-group-item"><a href="../seller/main">판매자 페이지</a></li>
					<sec:authorize access="hasRole('ROLE_관리자')">
						<li class="list-group-item"><a href="../board/admin">관리자 페이지</a></li>
					</sec:authorize>
					<sec:authorize access="!hasRole('ROLE_관리자')">
						<li class="list-group-item"><a onclick="consult()">1대1 상담</a> </li>
					</sec:authorize>
				</ul>
			</div>
		</div>
	<script type="text/javascript">
	function consult(){
		let targetUrl = '../board/consult';
		
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