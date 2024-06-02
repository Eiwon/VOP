package com.web.vop.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.MessageVO;
import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;

@Log4j
public class ConsultHandler extends AbstractWebSocketHandler{

	private static ObjectMapper objectMapper = null;
	
	@Autowired
	public Map<String, Map<String, WebSocketSession>> consultRoomList;
	
	@Autowired
	public Map<String, WebSocketSession> alarmConnMap;
	
	@Autowired
	public MemberService memberService;
	
	//@Autowired
	//public AlarmHandler alarmHandler;
	
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
		log.info("�޽��� ���� : " + message.getPayload());
		
		String senderId = session.getPrincipal().getName();
		String roomId = session.getPrincipal().getName();
		
		MessageVO messageVO = convertMsg(message.getPayload());
		String type = messageVO.getType();
		
		switch(type) {
		case "consultRequest": {
			Map<String, WebSocketSession> roomMap = createRoom(roomId);
			roomMap.put(senderId, session);
			callConsultant(roomId, senderId);
			break;
		}
		case "acceptConsultRequest" : {
			
		}
		
		}
		
	} // end handleTextMessage
	
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
	
	private void callConsultant(String roomId, String senderId) {
		// db���� ������ ��� �˻�
		List<String> adminList = memberService.getAdminId();
		MessageVO adminCallMsg = new MessageVO();
		adminCallMsg.setType("consultRequest");
		adminCallMsg.setWriterId(senderId);
		
		for(String adminId : adminList) { // ���� ���� �����ڵ鿡�� �޽��� �۽�
			if(alarmConnMap.containsKey(adminId)) { 
				WebSocketSession session = alarmConnMap.get(adminId);
				adminCallMsg.setReceiverId(adminId);
				try {
					session.sendMessage(convertMsg(adminCallMsg));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	} // end callConsultant

	
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

