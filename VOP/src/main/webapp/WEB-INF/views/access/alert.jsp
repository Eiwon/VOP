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
	<!--  -->
	<script type="text/javascript">
		let alertMsg = '${alertVO.alertMsg}';
		let redirectUri = '${alertVO.redirectUri}';
		
		alert(alertMsg);
		if(redirectUri == 'close'){
			window.close();
		}else{
			location.href = '../' + redirectUri;		
		}
	</script>

</body>
</html>