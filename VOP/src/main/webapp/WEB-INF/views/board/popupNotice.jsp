<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>popup notice</title>
</head>
<body>

	<div style="height: 400px;">
		<h2 id="messageTitle">${messageVO.title }</h2>
	
		<div id="messageContent">
			${messageVO.content }
		</div>
	</div>
	<div>
		<input type="checkbox" id="chkNoMoreShow" onclick="noMoreShow()">
		하루동안 보지 않기
	</div>

	<script type="text/javascript">
		const messageId = '${messageVO.messageId}';
		
		function noMoreShow(){
			let chkNoMoreShow = $('#chkNoMoreShow');
			console.log(chkNoMoreShow.prop('checked'));
			if(chkNoMoreShow.prop('checked')){
				// 서버에 쿠키 생성 요청
			}
		} // end noMoreShow
	
	</script>
</body>
</html>