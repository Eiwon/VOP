package com.web.vop.socket;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.MessageVO;
import com.web.vop.service.MessageService;

import lombok.extern.log4j.Log4j;

@Log4j
public class AlarmHandler extends TextWebSocketHandler{

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	Map<String, WebSocketSession> alarmConnMap;
	
	private static final String TYPE_INSTANCE = "instanceAlarm";
	private static final String TYPE_AUTH_UPDATE = "authUpdateAlarm";
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

	} // end handleTextMessage
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("socket open");
		if(session.getPrincipal() == null) {
			return;
		}
		String memberId = session.getPrincipal().getName();
		log.info("연결 유저 id : " + memberId);
		alarmConnMap.put(memberId, session);
		log.info("접속 중인 유저 : " + alarmConnMap);
		
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
	
	
	public void broadcast(MessageVO message) {
		log.info("연결된 전체 유저에게 메시지 전송");
		TextMessage jsonMsg = convertMsg(message);
		
		Iterator<String> iterator = alarmConnMap.keySet().iterator();

		String receiverId; 
		while(iterator.hasNext()) {
			receiverId = iterator.next();
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
	
	public void unicast(MessageVO message) {
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
		messageService.registerMessage(message);
		
	} // end unicast
	
	// 해당 유저에게 권한 변경 알림 전송 (instanceAlarm과 동일하지만 수신 즉시 권한 최신화가 이루어짐)
	public void sendAuthUpdateAlarm(String memberId, String content) {
		MessageVO returnMsg = new MessageVO();
		returnMsg.setReceiverId(memberId);
		returnMsg.setTitle("권한 변경 알림");
		returnMsg.setContent(content);
		returnMsg.setType(TYPE_AUTH_UPDATE);
		unicast(returnMsg);
	} // end sendAuthUpdateAlarm
	
	// 해당 유저에게 일반 토스트 메시지 전송 (returnUri은 컨트롤러 이름부터 입력, null이면 바로가기 버튼이 없는 토스트 출력)
	public void sendInstanceAlarm(String receiverId, String title, String content, String returnUri) {
		MessageVO returnMsg = new MessageVO();
		returnMsg.setType(TYPE_INSTANCE);
		returnMsg.setTitle(title);
		returnMsg.setContent(content);
		returnMsg.setReceiverId(receiverId);
		returnMsg.setCallbackInfo(returnUri);
		unicast(returnMsg);
	} // end sendInstanceAlarm
	
	// productId로부터 판매자를 검색하여 instanceAlarm 송신
	public void sendInstanceAlarm(int productId, String title, String content, String returnUri) {
		MessageVO returnMsg = new MessageVO();
		String receiverId = messageService.getSellerIdOf(productId);
		returnMsg.setType(TYPE_INSTANCE);
		returnMsg.setTitle(title);
		returnMsg.setContent(content);
		returnMsg.setReceiverId(receiverId);
		returnMsg.setCallbackInfo(returnUri);
		unicast(returnMsg);
	} // end sendInstanceAlarm
	
//	private MessageVO convertMsg(String jsonMsg) {
//		MessageVO message = null;
//		try {
//			message = objectMapper.readValue(jsonMsg, MessageVO.class);
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		return message;
//	} // end convertMsg
	
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
