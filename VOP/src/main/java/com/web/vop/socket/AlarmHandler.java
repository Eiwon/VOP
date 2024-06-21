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
import com.fasterxml.jackson.databind.JsonMappingException;
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
	private static final String TYPE_REPLY = "replyAlarm";
	private static final String TYPE_ALERT = "alert";
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("socket message received : " + message.getPayload());
		MessageVO messageVO = convertMsg(message.getPayload());
		
		MessageVO returnMsg = null;
    	String msgType = messageVO.getType();
    	String memberId = session.getPrincipal().getName();
    	messageVO.setWriterId(memberId);
    	
    	if(msgType.equals(TYPE_ALERT)) {
    		broadcast(messageVO);
    	}
		
	} // end handleTextMessage
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("socket open");
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
	
	
//	private MessageVO noticeHandler(MessageVO messageVO, String writerId) {
//		MessageVO returnMsg = new MessageVO();
//		returnMsg.setReceiverId(writerId);
//		messageVO.setTitle("공지 사항");
//		messageVO.setReceiverId("all");
//		messageVO.setCallbackInfo(null);
//		
//		log.info(messageVO);
//		int res = messageService.registerMessage(messageVO);
//		
//		if(res == 1) {
//			returnMsg.setContent("공지 등록 성공");
//		}else {
//			returnMsg.setContent("공지 등록에 실패했습니다. 다시 시도해주세요.");
//		}
//		
//		return returnMsg;
//	} // end noticeHandler

	public void sendAlert(String msg, String receiverId) {
		MessageVO messageVO = new MessageVO();
		messageVO.setReceiverId(receiverId);
		messageVO.setContent(msg);
		messageVO.setType("alert");
		
		TextMessage jsonMsg = convertMsg(messageVO);
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
	} // end sendAlert
	
	public void sendReplyAlarm(int productId) {
		MessageVO returnMsg = new MessageVO();
		String receiverId = messageService.getSellerIdOf(productId);
		String redirectId = String.valueOf(productId);
		returnMsg.setContent("등록한 상품에 댓글이 등록되었습니다. 이동하려면 클릭하세요.");
		returnMsg.setType(TYPE_REPLY);
		returnMsg.setReceiverId(receiverId);
		returnMsg.setCallbackInfo(redirectId);
		unicast(returnMsg);
	} // end sendReplyAlarm
	
	public void sendInstanceAlarm(String title, String content, String receiverId) {
		MessageVO returnMsg = new MessageVO();
		returnMsg.setTitle(title);
		returnMsg.setContent(content);
		returnMsg.setType(TYPE_INSTANCE);
		returnMsg.setReceiverId(receiverId);
		unicast(returnMsg);
	} // end sendInstanceAlarm
	
	
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
