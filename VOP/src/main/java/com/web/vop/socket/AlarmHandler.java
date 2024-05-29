package com.web.vop.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.MessageVO;
import com.web.vop.service.MessageService;

import lombok.extern.log4j.Log4j;

@Log4j
public class AlarmHandler extends TextWebSocketHandler{

	private static ObjectMapper objectMapper = null;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	Map<String, WebSocketSession> alarmConnMap;
	
	public AlarmHandler() {
		objectMapper = new ObjectMapper();
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("socket message received : " + message.getPayload());
		MessageVO messageVO = convertMsg(message.getPayload());
		
		MessageVO returnMsg = null;
    	String msgType = messageVO.getType();
    	String memberId = session.getPrincipal().getName();
    	messageVO.setWriterId(memberId);
    	
    	log.info(msgType.equals("notice"));
    	if(msgType.equals("notice")) { // 공지사항 등록 요청
    		returnMsg = noticeHandler(messageVO, memberId);
    		unicast(returnMsg);
    	}else if(msgType.equals("broadcast")){ // 접속 중인 전체 유저에게 송신
    		returnMsg = messageVO;
    		broadcast(returnMsg);
    	}else if(msgType.equals("replyAlarm")) { // 댓글 알림 (클릭시 이벤트 발생)
    		returnMsg = replyAlarmHandler(messageVO, memberId);
    		unicast(returnMsg);
    	}else if(msgType.equals("instanceMsg")) { // 유저 지정 일반 알림
    		returnMsg = messageVO;
    		unicast(returnMsg);
    	}
		
	} // end handleTextMessage
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("socket open");
		String memberId = session.getPrincipal().getName();
		log.info("연결 유저 id : " + memberId);
		alarmConnMap.put(memberId, session);
		
		List<MessageVO> messageList = messageService.getMyMessage(memberId);
		// 연결이 끊어진 동안 못 받은 메시지 검색
		messageService.removeByReceiverId(memberId);
		// 가져온 메시지는 DB에서 삭제 (전송 실패시 다시 DB에 등록)
		log.info("내 메세지 : " + messageList);
		if(messageList != null) {
			for(MessageVO vo : messageList) { // 전부 송신
				vo.setReceiverId(memberId); // receiverId 값이 all인 메시지도 있으므로 receiver 재설정
				unicast(vo);
			}
		}
	} // end afterConnectionEstablished
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("socket close");
		String memberId = session.getPrincipal().getName();
		log.info("연결 종료 유저 id : " + memberId);
		alarmConnMap.remove(memberId);
	} // end afterConnectionClosed
	
	
	private void broadcast(MessageVO message) {
		log.info("연결된 전체 유저에게 메시지 전송");
		TextMessage jsonMsg = convertMsg(message);
		
		Iterator<String> iterator = alarmConnMap.keySet().iterator();

		String receiverId; 
		while(iterator.hasNext()) {
			receiverId = iterator.next();
//			if(receiverId.equals(message.getWriterId())) {
//				continue;
//			} // 자기 자신은 송신 대상에서 제외 (테스트를 위해 주석 처리)
			WebSocketSession client = alarmConnMap.get(receiverId);
			if(client.isOpen()) {
				try {
					client.sendMessage(jsonMsg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} // end iterator
		
	} // end broadcast
	
	private void unicast(MessageVO message) {
		log.info("단일 유저에게 메시지 전송");
		String receiverId = message.getReceiverId();
		TextMessage jsonMsg = convertMsg(message);
		if(alarmConnMap.containsKey(receiverId)) { // 수신 대상이 접속 중이면 바로 송신
			WebSocketSession client = alarmConnMap.get(receiverId);
			if(client.isOpen()) {
				try {
					client.sendMessage(jsonMsg);
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} // end 접속 중인 유저 송신
		
		// 접속 중이 아니면 db에 메시지 저장
		log.info("저장할 메시지 정보 : " + message);
		if(!message.getType().equals("notice")) {
			messageService.registerMessage(message);
		}
	} // end unicast
	
	
	private MessageVO noticeHandler(MessageVO messageVO, String writerId) {
		MessageVO returnMsg = new MessageVO();
		returnMsg.setReceiverId(writerId);
		messageVO.setTitle("공지 사항");
		messageVO.setReceiverId("all");
		messageVO.setCallbackInfo(null);
		
		log.info(messageVO);
		int res = messageService.registerMessage(messageVO);
		
		if(res == 1) {
			returnMsg.setContent("공지 등록 성공");
		}else {
			returnMsg.setContent("공지 등록에 실패했습니다. 다시 시도해주세요.");
		}
		
		return returnMsg;
	} // end noticeHandler
	

	private MessageVO replyAlarmHandler(MessageVO messageVO, String writerId) {
		MessageVO returnMsg = messageVO;
		returnMsg.setReceiverId(writerId);
		returnMsg.setContent("등록한 상품에 댓글이 등록되었습니다. 이동하려면 클릭하세요.");
		return returnMsg;
	} // end replyAlarmHandler

	
	
	
	
	private MessageVO convertMsg(String jsonMsg) {
		MessageVO message = null;
		try {
			message = objectMapper.readValue(jsonMsg, MessageVO.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return message;
	} // end convertMsg
	
	private TextMessage convertMsg(MessageVO message) {
		TextMessage jsonMsg = null;
		try {
			jsonMsg = new TextMessage(objectMapper.writeValueAsString(message));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonMsg;
	} // end convertMsg
	
}
