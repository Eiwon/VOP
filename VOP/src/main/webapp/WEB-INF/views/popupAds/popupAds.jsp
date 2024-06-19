<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
.popupHeader {
	height: 10%;
}
.popupBody {
	height: 80%;
}
.popupFooter {
	height: 10%;
}

</style>
<title>광고</title>
</head>
<body>
	<div class="popupHeader">
		<h2>${messageVO.title }</h2>
	</div>
	<div class="popupBody">
		<div>
			<h3>${messageVO.content }</h3>
		</div>
		<div>
			<c:if test="${messageVO.type eq 'coupon' }">
				<form action="../coupon/getCoupon" method="POST">
					<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
					<input type="hidden" name="couponId" value="${messageVO.callbackInfo }">
					<input type="submit" value="쿠폰 받기">
				</form>
			</c:if>
		</div>
	</div>
	<div class="popupFooter">
		<input type="checkbox" id="chkNoMoreShow" onclick="noMoreShow()">
		하루동안 보지 않기
	</div>

	<script type="text/javascript">
		const messageId = '${messageVO.messageId}';
	
		function noMoreShow(){
			let chkNoMoreShow = $('#chkNoMoreShow');
			
			if(chkNoMoreShow.prop('checked')){
				// 서버에 쿠키 생성 요청
				$.ajax({
					method : 'GET',
					url : '../popupAds/blockPopup?messageId=' + messageId,
					success : function(result){
						window.close();
					} // end success

				}); // end ajax
			}
		} // end noMoreShow
	
	</script>
	
</body>
</html>