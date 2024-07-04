<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication var="memberDetails" property="principal"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<style type="text/css">
	#readArea {
		width: 500px;
		height: 500px;
		overflow: scroll;
	}
	.consultantChat {
		text-align: left;
	}
	.myChat {
		text-align: right;
	}
</style>
<title>Insert title here</title>
</head>
<body>
	<c:set var="role" value="consultant"></c:set>
	<c:if test="${roomId == null }">
		<c:set var="role" value="client"></c:set>
		<button onclick="callConsultant()">상담사 연결</button>
		<button onclick="finishConsult()">상담 종료</button>
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
		const role = '${role }';
		const memberId = '${memberDetails.username }';
		let tagReadArea = $('#readArea');
		let tagWriteChat = $('#writeChat');
		let chatHandler = {};
		
		$(document).ready(function(){
			tagWriteChat.keydown(function(event){
				if(event.keyCode == 13){ // 엔터 키 입력시
					sendChat();
				}
			});
			connectWebSocket();
		});
		
		function sendChat(){
			let content = tagWriteChat.val();
			console.log(content);
			if(content.length > 0 || consultWebSocket != null){
				consultWebSocket.send(JSON.stringify({
					'type' : 'chatMessage',
					'roomId' : roomId,
					'content' : content
				}));
				tagWriteChat.val('');
			} 
		}
		
		function addToReadArea(tellerId, content){
			let chatBox = $('<div></div>');
			if(tellerId == memberId){
				chatBox.attr('class', 'myChat');
			}else{
				chatBox.attr('class', 'consultantChat');
			}
			chatBox.append(tellerId + ' : ');
			
			let contentStr = '';
			let start = 0, end;
			while(start < content.length){
				end = Math.min((start + 30), content.length);
				contentStr += content.substring(start, end) + '<br>';
				start = end;
			}
			chatBox.append(contentStr);
			tagReadArea.append(chatBox);
			tagReadArea.scrollTop = tagReadArea.scrollHeight; // 스크롤의 최상단 값을 최하단 값으로 변경 (스크롤 최대한 내리기)
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
				if(role == 'consultant'){ // 상담사라면 상담 수락 메시지
					consultWebSocket.send(JSON.stringify({
						type : 'consultAccept',
						roomId : roomId
					}));
				}
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
			addToReadArea('System', msg.senderId + ' 님이 입장했습니다.');
			if(roomId == ''){
				roomId = msg.roomId;			
			}
		} // end joinSuccess
		
		chatHandler.joinFail = function(msg){
			alert('연결 실패 : ' + msg.content);
			window.close();
		}
		
		chatHandler.chatMessage = function(msg){
			addToReadArea(msg.senderId, msg.content);
		}
		
		chatHandler.clientExit = function(msg){
			addToReadArea('System', msg.senderId + ' 님이 퇴장했습니다.');
		}
		
		chatHandler.consultantExit = function(msg){
			addToReadArea('System', '상담사가 퇴장했습니다.');
		} // end consultantExit
		
		
		function callConsultant(){
			consultWebSocket.send(JSON.stringify({
				type : 'consultRequest'
			}));	
		} // end callConsultant
		
		function exitRoom(){
			consultWebSocket.send(JSON.stringify({
				type : (role == 'client') ? 'clientExit' : 'consultantExit',
				roomId : roomId
			}));
		} // end exitRoom
		
		function finishConsult(){
			exitRoom();
			roomId = '';
		} // end finishConsult
		
	</script>

</body>
</html>