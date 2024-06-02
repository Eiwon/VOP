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
	
	let socketUrl = "ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/alarm";
	let webSocket = new WebSocket(socketUrl);
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
	
	
	
	
	function sendAlert(){ //테스트 용
		
		let content = prompt('송신할 메시지를 입력하세요.');
		if(content != null){
			let msg = {};
			msg.type = 'alert';
			msg.content = content;
			webSocket.send(JSON.stringify(msg));
		}
	} // end sendAlert
		
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
	
	function sendInstanceMsg(){
		let instanceMsg = prompt('송신할 메시지를 입력하세요.');
		if(instanceMsg != null){
			let msg = {};
			msg.type = 'instanceMsg';
			msg.content = instanceMsg;
			webSocket.send(JSON.stringify(msg));
		}
	} // end sendInstanceMsg
	
	function sendReplyAlarm(productId){
		// 댓글 알림 보내기
		
		let msg = {};
		msg.type = 'replyAlarm';
		msg.callbackInfo = productId;
		
		webSocket.send(JSON.stringify(msg));
		
	} // end sendReplyAlarm 
	
	
	
	// 서버로부터 메시지 수신시 호출되는 함수들
	
	msgHandler.alert = function(msg){
		console.log('alert 메시지 수신');
		alert(msg.content);
	}; // 타입이 alert인 메시지 수신시 호출될 함수
	
	msgHandler.notice = function(msg){
		console.log('notice 메시지 수신');
		//showSocketNotification(msg, null);
	}; // 타입이 notice인 메시지 수신시 호출될 함수
	
	msgHandler.instanceMsg = function(msg){
		console.log('instance 메시지 수신');
		showSocketNotification(msg, null);
	}; // 타입이 instanceMsg인 메시지 수신시 호출될 함수 
	
	msgHandler.replyAlarm = function(msg){
		console.log('replyAlarm 메시지 수신');
		
		showSocketNotification(msg, function(){
			window.open('../product/detail?productId=' + msg.callbackInfo);
		});
	}; // 타입이 replyAlarm인 메시지 수신시 호출될 함수
	
	msgHandler.consultRequest = function(msg){
		console.log('consultRequest 메시지 수신 : ' + msg);
		let roomId = msg.receiverId;
		let isAccept = confirm("1대1 상담 요청 수신. 수락하시겠습니까?");
		
		if(isAccept){
			let targetUrl = '../board/consult?' + roomId;
				
			const popupStat = {
					'url' : targetUrl,
					'name' : 'popupConsult',
					'option' : 'width=800, height=1000, top=50, left=400'
			};
				
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
			} // end popup.onbeforeunload
		}
		
	} // end msgHandler.consultRequest
	
	
	function alarmPermitRequest(){ // 알림창 표시 기능 허가 요청 (허가 거부시 크롬 설정->개인정보보호 및 보안->사이트설정->알림에서 재설정 가능)
		let permission = Notification.permission;
		console.log('현재 알림창 설정 : ' + permission);
		Notification.requestPermission();
		
	} // end alarmPermitRequest
	
	function showSocketNotification(msg, onclickListener){
		/* let option = {
				body : msg.content,
				requireInteraction : true,
				timestamp : msg.dateCreated
		};
		let notification = new Notification(msg.content, option);
		notification.addEventListener('click', onclickListener); */
		let temp = confirm(msg.content);
		if(onclickListener != null || temp){
			onclickListener();
		}
		console.log(temp);
		//console.log(notification);
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