<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="memberDetails" property="principal"/>
</sec:authorize>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>

	<script type="text/javascript">

	let webSocket = new WebSocket("ws://localhost:8080/vop/alarm");
	let msgHandler = {};
	
	msgHandler.system = function(msg){
		console.log('system 메시지 수신');
	}; // 타입이 system인 메시지 수신시 호출될 함수
	
	msgHandler.notice = function(msg){
		console.log('notice 메시지 수신');
		alert(msg.content);
	}; // 타입이 notice인 메시지 수신시 호출될 함수
	
	msgHandler.replyAlarm = function(msg){
		console.log('replyAlarm 메시지 수신');
		
		let alarmResult = confirm(msg.content);
		
		// 해당 댓글로 이동 버튼 클릭시 이동
		if(alarmResult){
			location.href = '../product/detail/productId=' + msg.callbackInfo;
		}
		
	}; // 타입이 alarm인 메시지 수신시 호출될 함수
	
	
	
	
	webSocket.onmessage = function(e){
		let msg = JSON.parse(e.data);
		console.log("receive message : " + msg);
		
		msgHandler[msg.type](msg); // type에 맞는 핸들러를 맵에서 찾아서 실행
	}; // end webSocket.onmessage
	
	webSocket.onopen = function(e){
		console.log("webSocket open");
		sendInitMsg();
	}; // end webSocket.onopen
	
	webSocket.onclose = function(e){
		console.log("webSocket close : " + e);
	}; // end webSocket.onclose
	
	webSocket.onerror = function(e){
		console.log("webSocket error : " + e);
	}; // end webSocket.onerror
	
	
	
	
	function sendMsg(msg){
		webSocket.send('${memberDetails.getUsername()}');
	} // end sendMsg
		
	function sendInitMsg(){
		// 연결 성공시 client 정보 전송
		let msg = {};
		msg.type = 'init';
		msg.writerId = '${memberDetails.getUsername()}';
		webSocket.send(JSON.stringify(msg));
	} // end sendInitMsg

	function sendNotice(){
		// 모든 유저에게 공지 송신
		console.log('공지보내기');
		let notice = prompt('송신할 메시지를 입력하세요.');
		console.log('보낼 메시지 : ' + notice);
		
		if(notice != null){
			let msg = {};
			msg.type = 'notice';
			msg.writerId = '${memberDetails.getUsername()}';
			msg.content = notice;
			webSocket.send(JSON.stringify(msg));
		}
		
	} // end sendNotice
	
	function sendReplyAlarm(receiverId, productId){
		// 댓글 알람
		
		let msg = {};
		msg.type = 'alarmReply';
		msg.writerId = '${memberDetails.getUsername()}';
		msg.receiver = receiverId;
		msg.callbackInfo = productId;
		
		webSocket.send(JSON.stringify(msg));
		
	} // end sendReplyAlarm
	
	</script>
	
</body>
</html>