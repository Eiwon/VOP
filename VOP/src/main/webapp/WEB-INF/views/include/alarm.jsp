<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
</head>
<body>

	<script type="text/javascript">
	
	let url = "ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/alarm";
	let webSocket = new WebSocket(url);
	let msgHandler = {};
	
	$(document).ready(function(){
		alarmPermitRequest();
	}); // end document.ready
	
	// 서버로부터 메시지를 받으면, 해당 메시지의 type에 맞는 함수를 찾아서 실행
	webSocket.onmessage = function(e){
		let msg = JSON.parse(e.data);
		console.log("receive message : " + msg);
		
		msgHandler[msg.type](msg); // type에 맞는 핸들러를 맵에서 찾아서 실행
	}; // end webSocket.onmessage
	
	// 웹소켓 연결 성공시 호출
	webSocket.onopen = function(e){
		console.log("webSocket open");
	}; // end webSocket.onopen
	
	// 웹소켓 연결 종료시 호출
	webSocket.onclose = function(e){
		console.log("webSocket close : " + e);
	}; // end webSocket.onclose
	
	// 웹소켓 에러 발생시 호출
	webSocket.onerror = function(e){
		console.log("webSocket error : " + e);
	}; // end webSocket.onerror
	
	
	
	
	function sendMsg(msg){ //테스트 용
		webSocket.send('${memberDetails.getUsername()}');
	} // end sendMsg
		
	function sendNotice(){
		// 모든 유저에게 공지 송신
		console.log('공지보내기');
		let notice = prompt('송신할 메시지를 입력하세요.');
		console.log('보낼 메시지 : ' + notice);
		
		if(notice != null){
			let msg = {};
			msg.type = 'notice';
			msg.content = notice;
			webSocket.send(JSON.stringify(msg));
		}
	} // end sendNotice
	
	
	function sendReplyAlarm(receiverId, productId){
		// 댓글 알림 보내기 (이 파일을 include 후, 댓글 등록시 이 함수가 실행되도록 하면 됩니다)
		
		let msg = {};
		msg.type = 'alarmReply';
		msg.receiver = receiverId;
		msg.callbackInfo = productId;
		
		webSocket.send(JSON.stringify(msg));
		
	} // end sendReplyAlarm 
	
	
	
	// 서버로부터 메시지 수신시 호출되는 함수들
	
	msgHandler.system = function(msg){
		console.log('system 메시지 수신');
	}; // 타입이 system인 메시지 수신시 호출될 함수
	
	msgHandler.notice = function(msg){
		console.log('notice 메시지 수신');
		//showSocketNotification(msg, null);
	}; // 타입이 notice인 메시지 수신시 호출될 함수
	
	msgHandler.broadcast = function(msg){
		console.log('broadcast 메시지 수신');
		alert(msg.content);
	}; // 타입이 broadcast인 메시지 수신시 호출될 함수 
	
	msgHandler.replyAlarm = function(msg){
		console.log('replyAlarm 메시지 수신');
		
		showNotification(msg, function(){
			window.open('../product/detail/productId=' + msg.callbackInfo);
		});
		
	}; // 타입이 alarm인 메시지 수신시 호출될 함수
	
	
	
	function alarmPermitRequest(){ // 알림창 표시 기능 허가 요청 (허가 거부시 크롬 설정->개인정보보호 및 보안->사이트설정->알림에서 재설정 가능)
		let permission = Notification.permission;
		console.log('현재 알림창 설정 : ' + permission);
		Notification.requestPermission();
		
	} // end alarmPermitRequest
	
	function showSocketNotification(msg, onclickListener){
		let option = {
				body : msg.content,
				requireInteraction : true,
				timestamp : msg.dateCreated
		};
		let notification = new Notification(msg.title, option);
		notification.addEventListener('click', onclickListener);
		console.log(notification);
	} // end showSocketNotification
	
	function showSocketPopup(){
		let targetUrl = '../board/popupNotice';
		
		const popupStat = {
				'url' : targetUrl,
				'name' : 'popupNotice',
				'option' : 'width=500, height=600, top=50, left=400'
		};
		
		// 팝업 창 띄우기
		let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
		popup.onbeforeunload = function(){
			// 팝업 닫힐 때 실행
			console.log("팝업 닫힘");
		} // end popup.onbeforeunload
	} // end showSocketPopup
	
	</script>
	
</body>
</html>