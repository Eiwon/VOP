package com.web.vop.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.ChatMessageVO;
import com.web.vop.domain.ChatRoom;
import com.web.vop.domain.MessageVO;
import com.web.vop.service.MemberService;

import lombok.extern.log4j.Log4j;

@Log4j
public class ConsultHandler extends AbstractWebSocketHandler{

	@Autowired
	private ObjectMapper objectMapper;
	
	// key : memberId, value : roomId
	private static Map<String, String> consultConnMap;
	
	// key : roomId, value : chatroom
	@Autowired
	public Map<String, ChatRoom> consultRoomList;
	
	@Autowired
	public Map<String, WebSocketSession> alarmConnMap;
	
	@Autowired
	public MemberService memberService;
	
	private static final String TYPE_CONSULT_REQ = "consultRequest";
	private static final String TYPE_CONSULT_ACCEPT = "consultAccept";
	private static final String TYPE_CONSULTANT_EXIT = "consultantExit";
	private static final String TYPE_CLIENT_EXIT = "clientExit";
	private static final String TYPE_CHAT_MSG = "chatMessage";

	//private static final String TYPE_JOIN_REQ = "joinRequest";
	private static final String TYPE_JOIN_SUCCESS = "joinSuccess";
	private static final String TYPE_JOIN_FAIL = "joinFail";
	//private static final String TYPE_EXIT_MSG = "exitMessage";
	
	private static final int ROOM_STATE_AWAIT = 0;
	private static final int ROOM_STATE_CONSULTING = 1;
	private static final int ROOM_STATE_STOP = 2;
	private static final int ROOM_STATE_TERMINATE = 4;
	
	public ConsultHandler() {
		objectMapper = new ObjectMapper();
		consultConnMap = new HashMap<>();
	} // end ConsultHandler
	
	// ���� ������ ����
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String memberId = session.getPrincipal().getName();
		log.info("���� ���� : " + memberId);
	} // end afterConnectionEstablished
	
	// ���� ����� ����
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	} // end afterConnectionClosed
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info(exception);
	} // end handleTransportError
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		log.info("�޽��� ���� : " + message.getPayload());
		
		ChatMessageVO chatMessageVO = convertMsg(message.getPayload());
		String senderId = session.getPrincipal().getName();
		String type = chatMessageVO.getType();
		chatMessageVO.setSenderId(senderId);
		ChatRoom chatRoom = null;
		
		switch(type) {
		case TYPE_CONSULT_REQ : { // ��� ��û
			// roomId ����
			String roomId = senderId;
			if(consultRoomList.containsKey(roomId)) {
				// �̹� �ִ� id�� �ش� �濡 ����
				chatRoom = consultRoomList.get(roomId);
				chatRoom.setState(ROOM_STATE_CONSULTING);
				chatRoom.getMemberList().put(senderId, session);
			}else {
				// ���ο� �� ����
				chatRoom = new ChatRoom();
				chatRoom.setRoomId(roomId);
				chatRoom.setState(ROOM_STATE_AWAIT);
				chatRoom.setMemberList(new HashMap<>());
				chatRoom.getMemberList().put(senderId, session);
				consultRoomList.put(roomId, chatRoom);
				callConsultant(roomId);
			}
			sendJoinSuccess(roomId, senderId); // �� ���� ���� ����
			
		}
		break;
		case TYPE_CONSULT_ACCEPT : { // �����ڰ� ��� ����
			String roomId = chatMessageVO.getRoomId();
			chatRoom = consultRoomList.get(roomId);
			if(chatRoom == null || chatRoom.getState() == ROOM_STATE_TERMINATE) {
				// ����� ���
				sendJoinFail(session, "����� ����Դϴ�.");
				break;
			}
			int roomState = chatRoom.getState();
			if(roomState == ROOM_STATE_CONSULTING) {
				// �ٸ� ���簡 ���� ����
				sendJoinFail(session, "�ٸ� ���簡 ���� ������ ����Դϴ�.");
				break;
			}
			// ���� ����
			chatRoom.getMemberList().put(senderId, session);
			chatRoom.setState(ROOM_STATE_CONSULTING);
			sendJoinSuccess(roomId, senderId);
		}
		break;
		case TYPE_CHAT_MSG : {
			sendMsgToRoom(chatMessageVO);
			break;
		} // end case chatMessage
		case TYPE_CONSULTANT_EXIT : { // ���� ����
			String roomId = chatMessageVO.getRoomId();
			chatRoom = consultRoomList.get(roomId);
			chatRoom.getMemberList().remove(senderId);
			// ���� �ο��� ������ �� ����, ������ ��� �ߴ� ���·� ����
			sendConsultantExit(roomId, senderId);
			if(chatRoom.getMemberList().size() == 0) {
				consultRoomList.remove(roomId);
			}else {
				chatRoom.setState(ROOM_STATE_STOP);
			}
			break;
		}
		case TYPE_CLIENT_EXIT : { // Ŭ���̾�Ʈ ����
			String roomId = chatMessageVO.getRoomId();
			chatRoom = consultRoomList.get(roomId);
			chatRoom.getMemberList().remove(senderId);
			// ���� �ο��� ������ �� ����, ������ ��� ���� ���·� ����
			sendClientExit(roomId, senderId);
			if(chatRoom.getMemberList().size() == 0) {
				consultRoomList.remove(roomId);
			}else {
				chatRoom.setState(ROOM_STATE_TERMINATE);
			}
			break;
		}
		} // end switch
		
	} // end handleTextMessage
	

	private void callConsultant(String roomId) throws IOException {
		// db���� ������ ��� �˻�
		log.info("callConsultant");
		List<String> adminList = memberService.getAdminId();
		ChatMessageVO adminCallMsg = new ChatMessageVO();
		adminCallMsg.setType(TYPE_CONSULT_REQ);
		adminCallMsg.setRoomId(roomId);
		TextMessage returnMsg = convertMsg(adminCallMsg);
		
		// ��� ���� ��� ������ id�� adminList���� �����ؾ� ��
		log.info("admin List : " + adminList);
		for(String adminId : adminList) { // ���� ���̰� ������� �ƴ� �����ڵ鿡�� �޽��� �۽�
			if(alarmConnMap.containsKey(adminId) && !consultConnMap.containsKey(adminId)) {
				WebSocketSession session = alarmConnMap.get(adminId);
				log.info("call to " + adminId);
				session.sendMessage(returnMsg);
			}
		}
	} // end callConsultant
	
	private void sendJoinSuccess(String roomId, String memberId) throws IOException {
		log.info("sendJoinSuccess");
		ChatMessageVO chatMessageVO = new ChatMessageVO();
		chatMessageVO.setType(TYPE_JOIN_SUCCESS);
		chatMessageVO.setRoomId(roomId);
		chatMessageVO.setSenderId(memberId);
		
		sendMsgToRoom(chatMessageVO);
	} // end sendJoinSuccess
	
	private void sendJoinFail(WebSocketSession session, String content) throws IOException {
		log.info("sendJoinFail");
		ChatMessageVO chatMessageVO = new ChatMessageVO();
		chatMessageVO.setType(TYPE_JOIN_FAIL);
		chatMessageVO.setContent(content);

		session.sendMessage(convertMsg(chatMessageVO));
		
	} // end sendJoinFail
	
	private void sendConsultantExit(String roomId, String senderId) throws IOException {
		log.info("���� ���� �޽���");
		ChatMessageVO chatMessageVO = new ChatMessageVO();
		chatMessageVO.setType(TYPE_CONSULTANT_EXIT);
		chatMessageVO.setContent("���簡 �����߽��ϴ�.");
		chatMessageVO.setSenderId(senderId);
		chatMessageVO.setRoomId(roomId);
		
		sendMsgToRoom(chatMessageVO);
	} // end sendConsultantExit
	
	private void sendClientExit(String roomId, String senderId) throws IOException {
		log.info("Ŭ���̾�Ʈ ���� �޽���");
		ChatMessageVO chatMessageVO = new ChatMessageVO();
		chatMessageVO.setType(TYPE_CLIENT_EXIT);
		chatMessageVO.setContent(senderId + "���� �����߽��ϴ�.");
		chatMessageVO.setSenderId(senderId);
		chatMessageVO.setRoomId(roomId);
		
		sendMsgToRoom(chatMessageVO);
	} // end sendConsultantExit
	
	// �ش� ���� �ο� ��ü���� �޽��� �۽�
	private void sendMsgToRoom(ChatMessageVO chatMessageVO) throws IOException {
		log.info("�Ϲ� ä�� �۽�");
		
		ChatRoom targetRoom = consultRoomList.get(chatMessageVO.getRoomId());
		
		if(targetRoom == null) {
			return;
		}
		
		TextMessage returnMsg = convertMsg(chatMessageVO);
		
		Map<String, WebSocketSession> memberList = targetRoom.getMemberList();
		Iterator<String> memberIter = memberList.keySet().iterator();
		WebSocketSession session = null;
		while(memberIter.hasNext()) {
			session = memberList.get(memberIter.next());
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

