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
		width: 400px;
		height: 500px;
		overflow: scroll;
	}
	.yourChat {
		text-align: left;
	}
	.myChat {
		text-align: right;
	}
	.body_container {
		width: 60%;
		margin: auto;
	}
</style>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<title>상담실</title>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

	<div class="body_container">
		<c:set var="role" value="consultant"></c:set>
		<c:if test="${roomId == null }">
			<c:set var="role" value="client"></c:set>
			<button id="btnCall" class="btn btn-outline-primary">상담사 연결</button>
		</c:if>
		<button id="btnFinish" class="btn btn-outline-primary" disabled>상담 종료</button>
		<div class="chat_container">
			<div id="readArea"></div>
			<div id="writeArea">
				<input type="text" id="writeChat">
				<button class="btn btn-outline-primary" onclick="sendChat()">전송</button>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		
		let consultSocketUrl = "ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/consult";
		let consultWebSocket = null;
		let roomId = '${roomId }';
		const memberId = '${memberDetails.username }';
		const role = '${role }';
		
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
			
			$('#btnCall').click(function(){
				callConsultant();
			});
			$('#btnFinish').click(function(){
				finishConsult();
				alert('연결이 종료되었습니다');
			});
			
		});
		
		function sendChat(){
			let content = tagWriteChat.val();
			console.log(content);
			if(content.length > 0 && consultWebSocket.readyState != 3){
				consultWebSocket.send(JSON.stringify({
					'type' : 'chatMessage',
					'roomId' : roomId,
					'content' : content
				}));
				tagWriteChat.val('');
			} 
		}
		
		function addToReadArea(tellerId, content){
			let chatBox = $('<div class="card" style="width: 18rem;"></div>');
			
			if(tellerId == memberId){
				chatBox.addClass('myChat');
			}else{
				chatBox.addClass('yourChat');
			}
			let chatBody = $('<div class="card-body"></div>');
			
			chatBody.append('<h5 class="card-title">' + tellerId + '</h5>');
			chatBody.append('<p class="card-text">' + content + '</p>');
			
			chatBox.append(chatBody);
			
			tagReadArea.append(chatBox);
			tagReadArea.scrollTop(tagReadArea.prop('scrollHeight')); // 스크롤의 최상단 값을 최하단 값으로 변경 (스크롤 최대한 내리기)
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
				//connectWebSocket();
			}; // end webSocket.onclose

			// 웹소켓 에러 발생시 호출
			consultWebSocket.onerror = function(e) {
				console.log("webSocket error : " + e);
			}; // end webSocket.onerror
		
		} // end connectWebSocket
		
		chatHandler.joinSuccess = function(msg){
			console.log('join success roomId : ' + msg.roomId);
			addToReadArea('System', msg.senderId + ' 님이 입장했습니다.');
			if(roomId === '' || roomId === null || roomId === undefined){
				roomId = msg.roomId;			
			}
			if(memberId != msg.senderId){
				$('#btnFinish').attr('disabled', null);
				$('#btnCall').attr('disabled', 'disabled');
			}
		} // end joinSuccess
		
		chatHandler.joinFail = function(msg){
			alert('연결 실패 : ' + msg.content);
			//window.close();
		}
		
		chatHandler.chatMessage = function(msg){
			addToReadArea(msg.senderId, msg.content);
		}
		
		chatHandler.clientExit = function(msg){
			addToReadArea('System', msg.senderId + ' 님이 퇴장했습니다.');
			addToReadArea('System', '상담이 종료되었습니다.');
			consultWebSocket.close();
		}
		
		chatHandler.consultantExit = function(msg){
			addToReadArea('System', '상담사가 퇴장했습니다.');
			addToReadArea('System', '상담이 종료되었습니다.');
			consultWebSocket.close();
		} // end consultantExit
		
		
		function callConsultant(){
			if(consultWebSocket.readyState == 1){ // readyState == OPEN
				consultWebSocket.send(JSON.stringify({
					type : 'consultRequest'
				}));
			}else {
				alert('잠시 후 다시 시도해주세요');
			}
			
		} // end callConsultant
		
		function finishConsult(){
			consultWebSocket.close();
			roomId = '';
			$('#btnFinish').attr('disabled', 'disabled');
			//$('#btnCall').attr('disabled', null);
		} // end finishConsult
		
	</script>

</body>
</html>