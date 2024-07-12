<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Alert!!</title>
</head>
<body>
	<!-- 서버측에서 alert를 띄운 후 페이지를 이동시키기 위한 페이지입니다 -->
	<!-- 
		AlertVO를 선언, alertMsg(alert에 띄울 메시지), redirectUri(alert를 띄운 후 이동할 경로) 입력하여 
		model에 넣은 후, 리턴 경로를 Constant.ALERT_PATH(이 파일의 경로)로 설정
		redirectUri에 close를 넣으면 현재 창을 닫습니다.(팝업에서 POST 제출 후 창 닫는 용도)
	-->
	
	<script type="text/javascript">
		let alertMsg = '${alertVO.alertMsg}';
		let redirectUri = '${alertVO.redirectUri}';
		
		alert(alertMsg);
		if(redirectUri == 'close'){
			window.close();
		}else if(redirectUri == 'back'){
			window.history.back();
		}else {
			location.href = '../' + redirectUri;		
		}
	</script>

</body>
</html>