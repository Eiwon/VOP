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
	margin: 5px;
	margin-bottom: 20px;
}
.popupBody {
	height: 80%;
}
.popupFooter {
	height: 10%;
}
.body_container {
	width: 65%;
	margin: auto;
	margin-top: 10%;
}

</style>
<title>광고</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	<div class="body_container">
	<div class="popupHeader">
		<h2>${messageVO.title }</h2>
	</div>
	<div class="popupBody">
		<div class="input-group">
			<textarea class="form-control" rows="4" cols="25" readonly>${messageVO.content }</textarea>
		</div>
		<div>
			<c:if test="${messageVO.type eq 'coupon' }">
				<form action="../coupon/getCoupon" method="POST">
					<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
					<input type="hidden" name="couponId" value="${messageVO.callbackInfo }">
					<input type="submit" value="쿠폰 받기" class="btn btn-outline-primary">
				</form>
			</c:if>
		</div>
	</div>
	<div class="popupFooter">
		<input type="checkbox" id="chkNoMoreShow" onclick="noMoreShow()">
		하루동안 보지 않기
	</div>
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