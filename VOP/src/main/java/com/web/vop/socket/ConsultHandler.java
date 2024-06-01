package com.web.vop.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.MessageVO;

import lombok.extern.log4j.Log4j;

@Log4j
public class ConsultHandler extends AbstractWebSocketHandler{

	private static ObjectMapper objectMapper = null;
	
	@Autowired
	public Map<String, Map<String, WebSocketSession>> consultRoomList;
	
	@Autowired
	public Map<String, WebSocketSession> alarmConnMap;
	
	public ConsultHandler() {
		objectMapper = new ObjectMapper();
	} // end ConsultHandler
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String memberId = session.getPrincipal().getName();
		log.info("���� ���� : " + memberId);
		log.info(session.getAttributes());
		
	} // end afterConnectionEstablished
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String memberId = session.getPrincipal().getName();
		log.info("���� ���� : " + memberId);
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info(exception);
	} // end handleTransportError
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// Ŭ���̾�Ʈ���� ��� ��ư Ŭ���� socket open
		// �� ���� ��û�� ������ ������, �� id�� �����Ͽ� �ش� �濡 ������ ����
		// ������ �� id�� �������� return
		// �� �� id ���� ��� : Ŭ���̾�Ʈ ���� �ϳ��� 1������ ����?(������ ������ �� �ְ� �ϸ� �ǿ� ���ɼ� ��) 
		// => �� id = Ŭ���̾�Ʈ id
		// 
		// ���� ���� ��� ������ �������� �˶� �۽�
		// ���� ������ 1�� ����Ǿ�� ��
		// ������ �ش� �濡 �����ڰ� �̹� �ִٸ� �޽��� return
		// ������ �ش� �濡 �����ڰ� ���ٸ� �ش� ������ ����
	
		// �޽��� ���� : Ÿ��, �߽��� id, ������ �� id, ����, ������ id
		
		String senderId = session.getPrincipal().getName();
		String roomId = session.getPrincipal().getName();
		
		
	}
	
	private Map<String, WebSocketSession> createRoom(String roomId){
		Map<String, WebSocketSession> roomMap = null;
		
		if(consultRoomList.containsKey(roomId)) {
			log.info("�̹� �����ϴ� �� : " + roomId);
			roomMap = consultRoomList.get(roomId);
		}else {
			log.info("�� ���� : " + roomId);
			roomMap = new HashMap<>();
			consultRoomList.put(roomId, roomMap);
		}
		return roomMap;
	} // end createRoom
	
//	public void sendToRoom(MessageVO message) {
//		log.info("���� �������� �޽��� ����");
//		String receiverId = message.getReceiverId();
//		TextMessage jsonMsg = convertMsg(message);
//		if(consultRoomList.containsKey(receiverId)) { // ���� ����� ���� ���̸� �ٷ� �۽�
//			WebSocketSession client = consultRoomList.get(receiverId);
//			if(client.isOpen()) {
//				try {
//					client.sendMessage(jsonMsg);
//					return;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		} // end ���� ���� ���� �۽�
//		
//	} // end unicast
	
	
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

