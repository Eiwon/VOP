<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<title>Insert title here</title>
</head>
<body>

	<button></button>
	<div class="chat_container">
		<div id="readArea">
			
		
		</div>
		<div id="writeArea">
			<input type="text" id="writeChat">
		</div>
	</div>


	<script type="text/javascript">
	
		let socketUrl = "ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/consult";
		let webSocket = new WebSocket(socketUrl);
		let tagReadArea = $('#readArea');
		let tagWriteArea = $('#writeArea');
		
	
		// 서버로부터 메시지를 받으면, 해당 메시지의 type에 맞는 함수를 찾아서 실행
		webSocket.onmessage = function(e){
			let msg = JSON.parse(e.data);
			console.log("receive message : " + msg);
		}; // end webSocket.onmessage
	
	
		// 웹소켓 연결 성공시 호출
		webSocket.onopen = function(e) {
			console.log("webSocket open");
		}; // end webSocket.onopen

		// 웹소켓 연결 종료시 호출
		webSocket.onclose = function(e) {
			console.log("webSocket close : " + e);
		}; // end webSocket.onclose

		// 웹소켓 에러 발생시 호출
		webSocket.onerror = function(e) {
			console.log("webSocket error : " + e);
		}; // end webSocket.onerror
		
		
		function printChat(msg){
			tagWriteArea.append(msg);
		}
		
	</script>

</body>
</html>