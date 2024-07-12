<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>

<div class="toast-container position-fixed bottom-0 end-0 p-3">
	
	<div class="toast instanceAlarm" role="alert">
		<div class="toast-header">
			<strong class="me-auto toast_title"></strong>
	      	<button type="button" class="btn-close" data-bs-dismiss="toast"></button>
	    </div>
	    <div class="toast-body">
	    	<span class="toast_content"></span>
	    </div>
	</div>
	
	<div class="toast linkedAlarm" role="alert">
		<div class="toast-header">
			<strong class="me-auto toast_title"></strong>
	      	<button type="button" class="btn-close" data-bs-dismiss="toast"></button>
	    </div>
	    <div class="toast-body">
	    	<span class="toast_content"></span>
	    	<div class="mt-2 pt-2 border-top">
      			<button type="button" class="btn btn-primary btn-sm">바로 이동하기</button>
    		</div>
	    </div>
	</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

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
		console.log("receive message : " + msg.content);
		
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
	
	
	
	// ---------------------메시지 타입에 따라 처리할 함수 설정----------------
	
	msgHandler.authUpdateAlarm = function(msg){
		console.log('권한 변경 메시지 수신');
		
		showToast(msg);
		
		$.ajax({
			method : 'GET',
			url : '../member/reload',
			success : function(result){
				console.log('권한 리로드 결과 : ' + result);
			}
		});
		
	} // end updateAuthAlarm
	
	msgHandler.instanceAlarm = function(msg){
		console.log('메시지 수신');
		
		showToast(msg);
	} // end alarm
	
	msgHandler.consultRequest = function(msg){
		console.log('consultRequest 메시지 수신 ' + msg);
		
		let req = confirm("1대1 상담 요청 수신. 수락하시겠습니까?");
		
		if(req){
			let targetUrl = '../board/consultAccept?roomId=' + msg.roomId;
			console.log('onclick : ' + targetUrl);
			const popupStat = {
					'url' : targetUrl,
					'name' : 'popupConsultAdmin',
					'option' : 'width=900, height=800, top=50, left=400'
			};
				
			// 팝업 창 띄우기
			let popup = window.open(popupStat.url, popupStat.name, popupStat.option);
			popup.onbeforeunload = function(){
				// 팝업 닫힐 때 실행
				console.log("팝업 닫힘");
			} // end popup.onbeforeunload
		}
		
	} // end msgHandler.consultRequest
	
	
	// -------------------------------------------------------------------------------
	
	
	function alarmPermitRequest(){ // 알림창 표시 기능 허가 요청 (허가 거부시 크롬 설정->개인정보보호 및 보안->사이트설정->알림에서 재설정 가능)
		let permission = Notification.permission;
		console.log('현재 알림창 설정 : ' + permission);
		
		if(permission != 'granted'){
			Notification.requestPermission();
		}
		
	} // end alarmPermitRequest
	
	
	// 토스트 메시지 출력 (메시지에 redirectUri가 설정되어 있지 않으면 버튼 없는 토스트 출력)
	function showToast(msg) {
		let tagToastContainer = $('.toast-container');
		let toast;
		let redirectUri = msg.callbackInfo;

		if(redirectUri === undefined || redirectUri === null || redirectUri === ''){
			console.log('normal toast');
			toast = $('.instanceAlarm');		
		}else {
			console.log('link toast');
			toast = $('.linkedAlarm');
			toast.find('button').click(function(){
				window.open('../' + redirectUri);
			});
		}
		
		toast.find('.toast_title').text(msg.title);
		toast.find('.toast_content').text(msg.content);
		
		bootstrap.Toast.getOrCreateInstance(toast).show();
		
	} // end showToast
	
	
	
	</script>
	
</body>
</html>