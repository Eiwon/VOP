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
		log.info("연결 성공 : " + memberId);
		log.info(session.getAttributes());
		
	} // end afterConnectionEstablished
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String memberId = session.getPrincipal().getName();
		log.info("연결 종료 : " + memberId);
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info(exception);
	} // end handleTransportError
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 클라이언트에서 상담 버튼 클릭시 socket open
		// 방 생성 요청을 서버에 보내고, 방 id를 생성하여 해당 방에 유저를 매핑
		// 생성된 방 id를 유저에게 return
		// ㄴ 방 id 생성 방식 : 클라이언트 계정 하나당 1개씩만 유지?(여러개 유지할 수 있게 하면 악용 가능성 ㅇ) 
		// => 방 id = 클라이언트 id
		// 
		// 접속 중인 모든 관리자 계정에게 알람 송신
		// 최초 수락한 1명만 연결되어야 함
		// 수락시 해당 방에 관리자가 이미 있다면 메시지 return
		// 수락시 해당 방에 관리자가 없다면 해당 방으로 연결
	
		// 메시지 형식 : 타입, 발신자 id, 수신할 방 id, 내용, 수신자 id
		
		String senderId = session.getPrincipal().getName();
		String roomId = session.getPrincipal().getName();
		
		
	}
	
	private Map<String, WebSocketSession> createRoom(String roomId){
		Map<String, WebSocketSession> roomMap = null;
		
		if(consultRoomList.containsKey(roomId)) {
			log.info("이미 존재하는 방 : " + roomId);
			roomMap = consultRoomList.get(roomId);
		}else {
			log.info("방 생성 : " + roomId);
			roomMap = new HashMap<>();
			consultRoomList.put(roomId, roomMap);
		}
		return roomMap;
	} // end createRoom
	
//	public void sendToRoom(MessageVO message) {
//		log.info("단일 유저에게 메시지 전송");
//		String receiverId = message.getReceiverId();
//		TextMessage jsonMsg = convertMsg(message);
//		if(consultRoomList.containsKey(receiverId)) { // 수신 대상이 접속 중이면 바로 송신
//			WebSocketSession client = consultRoomList.get(receiverId);
//			if(client.isOpen()) {
//				try {
//					client.sendMessage(jsonMsg);
//					return;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		} // end 접속 중인 유저 송신
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

