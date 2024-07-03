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
	
	// 연결 성공시 실행
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String memberId = session.getPrincipal().getName();
		log.info("연결 성공 : " + memberId);
	} // end afterConnectionEstablished
	
	// 연결 종료시 실행
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//		String memberId = session.getPrincipal().getName();
//		log.info("연결 종료 : memberId : " + memberId + ", status : " + status);
//		String roomId = consultConnMap.get(memberId);
//		if(roomId != null) {
//			consultRoomList.get(roomId).remove(memberId);
//		}
//		consultConnMap.remove(memberId);
//		if(roomId != null && consultRoomList.get(roomId).size() == 0) {
//			consultRoomList.remove(roomId);
//		}else {
//			sendMsgToRoom(getExitMsg(roomId, memberId));
//		}
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info(exception);
	} // end handleTransportError
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		log.info("메시지 수신 : " + message.getPayload());
		
		ChatMessageVO chatMessageVO = convertMsg(message.getPayload());
		String senderId = session.getPrincipal().getName();
		//String roomId = chatMessageVO.getRoomId();
		String type = chatMessageVO.getType();
		chatMessageVO.setSenderId(senderId);
		ChatRoom chatRoom = null;
		
		switch(type) {
		case TYPE_CONSULT_REQ : { // 상담 요청
			// roomId 생성
			String roomId = senderId;
			if(consultRoomList.containsKey(roomId)) {
				// 이미 있는 id면 해당 방에 입장
				chatRoom = consultRoomList.get(roomId);
				chatRoom.setState(ROOM_STATE_CONSULTING);
				chatRoom.getMemberList().put(senderId, session);
			}else {
				// 새로운 방 생성
				chatRoom = new ChatRoom();
				chatRoom.setRoomId(roomId);
				chatRoom.setState(ROOM_STATE_AWAIT);
				chatRoom.setMemberList(new HashMap<>());
				chatRoom.getMemberList().put(senderId, session);
				consultRoomList.put(roomId, chatRoom);
				callConsultant(roomId);
			}
			sendJoinSuccess(roomId, senderId); // 방 입장 성공 응답
			
		}
		break;
		case TYPE_CONSULT_ACCEPT : { // 관리자가 상담 수락
			String roomId = chatMessageVO.getRoomId();
			chatRoom = consultRoomList.get(roomId);
			if(chatRoom == null || chatRoom.getState() == ROOM_STATE_TERMINATE) {
				// 종료된 상담
				sendJoinFail(session, "종료된 상담입니다.");
				break;
			}
			int roomState = chatRoom.getState();
			if(roomState == ROOM_STATE_CONSULTING) {
				// 다른 상담사가 먼저 수락
				sendJoinFail(session, "다른 상담사가 먼저 수락한 상담입니다.");
				break;
			}
			// 상담사 입장
			chatRoom.getMemberList().put(senderId, session);
			chatRoom.setState(ROOM_STATE_CONSULTING);
			sendJoinSuccess(roomId, senderId);
		}
		break;
//		case TYPE_JOIN_REQ : { // 입장 요청
//			if(!StringUtils.hasText(roomId)) { // roomId가 없다면 방 생성, 입장성공 메시지 송신
//				roomId = senderId;
//				Map<String, WebSocketSession> roomMap = createRoom(roomId);
//				roomMap.put(senderId, session);
//				log.info(roomMap);
//				sendJoinSuccess(roomId, senderId);
//				callConsultant(roomId); // 관리자 초대 메시지 송신
//			}else {
//				Map<String, WebSocketSession> roomMap = consultRoomList.get(roomId);
//				log.info(roomMap);
//				if(roomMap == null) {
//					log.info("이미 종료된 상담입니다.");
//					sendJoinFail(session, "이미 종료된 상담입니다.");
//				}else if(roomMap.size() == 1) {
//					log.info("채팅방 입장");
//					roomMap.put(senderId, session);
//					sendJoinSuccess(roomId, senderId);
//					consultConnMap.put(senderId, roomId);
//				}else {
//					log.info("다른 상담사가 먼저 수락");
//					sendJoinFail(session, "다른 상담사가 먼저 수락");
//				}
//			}
//			break;
//		} // end case joinRequest
		case TYPE_CHAT_MSG : {
			sendMsgToRoom(chatMessageVO);
			break;
		} // end case chatMessage
		case TYPE_CONSULTANT_EXIT : { // 상담사 퇴장
			String roomId = chatMessageVO.getRoomId();
			chatRoom = consultRoomList.get(roomId);
			chatRoom.getMemberList().remove(senderId);
			// 남은 인원이 없으면 방 폭파, 있으면 상담 중단 상태로 변경
			if(chatRoom.getMemberList().size() == 0) {
				consultRoomList.remove(roomId);
			}else {
				chatRoom.setState(ROOM_STATE_STOP);
			}
			break;
		}
		case TYPE_CLIENT_EXIT : { // 클라이언트 퇴장
			String roomId = chatMessageVO.getRoomId();
			chatRoom = consultRoomList.get(roomId);
			chatRoom.getMemberList().remove(senderId);
			// 남은 인원이 없으면 방 폭파, 있으면 상담 종료 상태로 변경
			if(chatRoom.getMemberList().size() == 0) {
				consultRoomList.remove(roomId);
			}else {
				chatRoom.setState(ROOM_STATE_TERMINATE);
			}
			break;
		}
		} // end switch
		
	} // end handleTextMessage
	
//	private Map<String, WebSocketSession> createRoom(String roomId){
//		Map<String, WebSocketSession> roomMap = null;
//		
//		if(consultRoomList.containsKey(roomId)) {
//			log.info("이미 존재하는 방 : " + roomId);
//			roomMap = consultRoomList.get(roomId);
//		}else {
//			log.info("방 생성 : " + roomId);
//			roomMap = new HashMap<>();
//			consultRoomList.put(roomId, roomMap);
//		}
//		return roomMap;
//	} // end createRoom
	
	private void callConsultant(String roomId) throws IOException {
		// db에서 관리자 목록 검색
		log.info("callConsultant");
		List<String> adminList = memberService.getAdminId();
		ChatMessageVO adminCallMsg = new ChatMessageVO();
		adminCallMsg.setType(TYPE_CONSULT_REQ);
		adminCallMsg.setRoomId(roomId);
		TextMessage returnMsg = convertMsg(adminCallMsg);
		
		// 상담 중인 모든 관리자 id를 adminList에서 제거해야 함
		log.info("admin List : " + adminList);
		for(String adminId : adminList) { // 접속 중이고 상담중이 아닌 관리자들에게 메시지 송신
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
	
	// 해당 방의 인원 전체에게 메시지 송신
	private void sendMsgToRoom(ChatMessageVO chatMessageVO) throws IOException {
		log.info("일반 채팅 송신");
		
		// Map<String, WebSocketSession> targetRoom = consultRoomList.get(chatMessageVO.getRoomId());
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

