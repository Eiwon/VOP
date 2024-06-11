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
.container {
	flex-direction: row;
	display: flex;
}
	
</style>
<title>마이페이지</title>
</head>

<body>
	<div class="info_container">
		<div class="side_bar">
			<div> 
				<strong>내 정보</strong>
				<ul id="my_info">
					<li><a href="myInfo">내 정보 확인 / 수정</a></li>
					<li><a href="../order/orderlist">주문 목록</a></li>
					<li><a href="../Delivery/deliveryAddressList">배송지 관리</a></li>
					<li><a href="seller">판매자 페이지</a></li>
					<sec:authorize access="hasRole('ROLE_관리자')">
						<li><a href="admin">관리자 페이지</a></li>
					</sec:authorize>
					<li><a onclick="consult()">1대1 상담</a> </li>
					<%-- <sec:authorize access="!hasRole('ROLE_관리자')">
						<li><a onclick="consult()">1대1 상담</a> </li>
					</sec:authorize> --%>
				</ul>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	function consult(){
		let targetUrl = 'consult';
		
		const popupStat = {
				'url' : targetUrl,
				'name' : 'popupConsult',
				'option' : 'width=800, height=1000, top=50, left=400'
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