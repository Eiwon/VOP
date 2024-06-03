<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
	#readArea {
		width: 300px;
		height: 600px;
	}
</style>
<title>Insert title here</title>
</head>
<body>

	<c:if test="${roomId == null }">
		<button onclick="callConsultant()">상담사 연결</button>
	</c:if>
	<div class="chat_container">
		<div id="readArea">
			
		
		</div>
		<div id="writeArea">
			<input type="text" id="writeChat">
			<button onclick="sendChat()">전송</button>
		</div>
	</div>


	<script type="text/javascript">
	
		let consultSocketUrl = "ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/consult";
		let consultWebSocket = null;
		let roomId = '${roomId }';
		let tagReadArea = $('#readArea');
		let tagWriteChat = $('#writeChat');
		let chatHandler = {};
		
		$(document).ready(function(){
			if(roomId != ''){
				connectWebSocket();
			}
		});
		
		function callConsultant() {
			//if(consultWebSocket == null){
				connectWebSocket();
			//}
		}
		
		function connectWebSocket() {
			consultWebSocket = new WebSocket(consultSocketUrl);	
			
			// 서버로부터 메시지를 받으면, 해당 메시지의 type에 맞는 함수를 찾아서 실행
			consultWebSocket.onmessage = function(e){
				let msg = JSON.parse(e.data);
				console.log("receive message : " + msg);
				chatHandler[msg.type](msg);
			}; // end webSocket.onmessage
		
			// 웹소켓 연결 성공시 호출
			consultWebSocket.onopen = function(e) {
				console.log("webSocket open");
				consultWebSocket.send(JSON.stringify({
					type : 'joinRequest',
					roomId : roomId
				}));
				
			}; // end webSocket.onopen

			// 웹소켓 연결 종료시 호출
			consultWebSocket.onclose = function(e) {
				console.log("webSocket close : " + e);
			}; // end webSocket.onclose

			// 웹소켓 에러 발생시 호출
			consultWebSocket.onerror = function(e) {
				console.log("webSocket error : " + e);
			}; // end webSocket.onerror
		
		} // end connectWebSocket
		
		chatHandler.joinSuccess = function(msg){
			console.log('join success roomId : ' + msg.roomId);
			roomId = msg.roomId;
		}
		
		chatHandler.joinFail = function(msg){
			alert('연결 실패 : ' + msg.content);
			window.close();
		}
		
		chatHandler.chatMessage = function(msg){
			tagReadArea.append(msg.senderId + ' : ' + msg.content + '<br>');
		}
		
		function sendChat(){
			let content = tagWriteChat.val();
			console.log(content);
			if(consultWebSocket != null){
				consultWebSocket.send(JSON.stringify({
					'type' : 'chatMessage',
					'roomId' : roomId,
					'content' : content
				}));
			} 
		}
		
		
	</script>

</body>
</html>