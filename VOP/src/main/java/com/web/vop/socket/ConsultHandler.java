package com.web.vop.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.web.vop.domain.ChatMessageVO;
import com.web.vop.domain.MessageVO;
import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;

@Log4j
public class ConsultHandler extends AbstractWebSocketHandler{

	@Autowired
	private ObjectMapper objectMapper;
	
	// key : memberId, value : roomId
	private static Map<String, String> consultConnMap;
	
	// key : roomId, value : { key : memberId, value : session }
	@Autowired
	public Map<String, Map<String, WebSocketSession>> consultRoomList;
	
	@Autowired
	public Map<String, WebSocketSession> alarmConnMap;
	
	@Autowired
	public MemberService memberService;
	
	public ConsultHandler() {
		objectMapper = new ObjectMapper();
		consultConnMap = new HashMap<>();
	} // end ConsultHandler
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String memberId = session.getPrincipal().getName();
		log.info("���� ���� : " + memberId);
	} // end afterConnectionEstablished
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String memberId = session.getPrincipal().getName();
		log.info("���� ���� : memberId : " + memberId + ", status : " + status);
		String roomId = consultConnMap.get(memberId);
		if(roomId != null) {
			consultRoomList.get(roomId).remove(memberId);
		}
		consultConnMap.remove(memberId);
		
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info(exception);
	} // end handleTransportError
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// Ŭ���̾�Ʈ���� ��� ��ư Ŭ���� socket open
		// �� ���� ��û�� ������ ������, �� id�� �����Ͽ� �ش� �濡 ������ �߰�
		// ������ �� id�� �������� return
		// �� �� id ���� ��� : Ŭ���̾�Ʈ ���� �ϳ��� 1������ ����?(������ ������ �� �ְ� �ϸ� �ǿ� ���ɼ� ��) 
		// => �� id = Ŭ���̾�Ʈ id
		// 
		// ���� ���� ��� ������ �������� �˶� �۽�
		// ���� ������ 1�� ����Ǿ�� ��
		// ������ �ش� �濡 �����ڰ� �̹� �ִٸ� �޽��� return
		// ������ �ش� �濡 �����ڰ� ���ٸ� �ش� ������ ����
	
		// �޽��� ����
		//  ����							�۽�
		// 1. �� ���� ��û				  ���� ����, ���� ����, ���� �ʴ�
		// 2. �Ϲ� �޽���					�Ϲ� �޽���							
		
		// �� ���� ��û => roomId�� ���ٸ� �� ���� �� ���� ���� �޽���, ��� ������ �ʴ� �޽���
		//  				�� �ִٸ� �ش� ������ ���� ���� ���� Ȯ��
		//						�� �����ϸ� ���� �� ���� ���� �޽���
		//						�� �Ұ����ϸ� ���� ���� �޽���
		log.info("�޽��� ���� : " + message.getPayload());
		
		String senderId = session.getPrincipal().getName();
		ChatMessageVO chatMessageVO = convertMsg(message.getPayload());
		String roomId = chatMessageVO.getRoomId();
		chatMessageVO.setSenderId(senderId);
		String type = chatMessageVO.getType();
		
		switch(type) {
		case "joinRequest" : { // ���� ��û
			if(roomId.length() == 0) { // roomId�� ���ٸ� �� ����, ���强�� �޽��� �۽�
				roomId = senderId;
				Map<String, WebSocketSession> roomMap = createRoom(roomId);
				roomMap.put(senderId, session);
				sendJoinSuccess(session, roomId);
				callConsultant(roomId); // ������ �ʴ� �޽��� �۽�
			}else {
				Map<String, WebSocketSession> roomMap = consultRoomList.get(roomId);
				if(roomMap == null) {
					log.info("�̹� ����� ����Դϴ�.");
					sendJoinFail(session, "�̹� ����� ����Դϴ�.");
				}else if(roomMap.size() == 1) {
					log.info("ä�ù� ����");
					roomMap.put(senderId, session);
					sendJoinSuccess(session, roomId);
					consultConnMap.put(senderId, roomId);
				}else {
					log.info("�ٸ� ���簡 ���� ����");
					sendJoinFail(session, "�ٸ� ���簡 ���� ����");
				}
			}
			break;
		} // end case joinRequest
		case "chatMessage" : {
			sendChatMessage(chatMessageVO);
			break;
		} // end case chatMessage
		} // end switch
		
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
	
	private void callConsultant(String roomId) throws IOException {
		// db���� ������ ��� �˻�
		List<String> adminList = memberService.getAdminId();
		ChatMessageVO adminCallMsg = new ChatMessageVO();
		adminCallMsg.setType("consultRequest");
		adminCallMsg.setRoomId(roomId);
		TextMessage returnMsg = convertMsg(adminCallMsg);
		
		// ��� ���� ��� ������ id�� adminList���� �����ؾ� ��
		
		for(String adminId : adminList) { // ���� ���̰� ������� �ƴ� �����ڵ鿡�� �޽��� �۽�
			if(alarmConnMap.containsKey(adminId) && !consultConnMap.containsKey(adminId)) {
				WebSocketSession session = alarmConnMap.get(adminId);
				session.sendMessage(returnMsg);
			}
		}
	} // end callConsultant
	
	private void sendJoinSuccess(WebSocketSession session, String roomId) throws IOException {
		log.info("sendJoinSuccess");
		ChatMessageVO chatMessageVO = new ChatMessageVO();
		chatMessageVO.setType("joinSuccess");
		chatMessageVO.setRoomId(roomId);
		
		session.sendMessage(convertMsg(chatMessageVO));
		
	} // end sendJoinSuccess
	
	private void sendJoinFail(WebSocketSession session, String content) throws IOException {
		log.info("sendJoinFail");
		ChatMessageVO chatMessageVO = new ChatMessageVO();
		chatMessageVO.setType("joinFail");
		chatMessageVO.setContent(content);

		session.sendMessage(convertMsg(chatMessageVO));
		
	} // end sendJoinFail
	
	private void sendChatMessage(ChatMessageVO chatMessageVO) throws IOException {
		log.info("�Ϲ� ä�� �۽�");
		
		Map<String, WebSocketSession> targetRoom = consultRoomList.get(chatMessageVO.getRoomId());
		TextMessage returnMsg = convertMsg(chatMessageVO);
		
		Iterator<String> memberIdIter = targetRoom.keySet().iterator();
		while(memberIdIter.hasNext()) {
			WebSocketSession session = targetRoom.get(memberIdIter.next());
			if(session.isOpen()) {
				session.sendMessage(returnMsg);
			}
		}
		
	} // end sendChatMessage
	
	
	private ChatMessageVO convertMsg(String jsonMsg) {
		ChatMessageVO message = null;
		try {
			message = objectMapper.readValue(jsonMsg, ChatMessageVO.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return message;
	} // end convertMsg
	
	private TextMessage convertMsg(ChatMessageVO message) {
		TextMessage jsonMsg = null;
		try {
			jsonMsg = new TextMessage(objectMapper.writeValueAsString(message));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonMsg;
	} // end convertMsg
}

