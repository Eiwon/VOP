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
		log.info("���� ���� id : " + memberId);
		alarmConnMap.put(memberId, session);
		log.info("���� ���� ���� : " + alarmConnMap);
		
		List<MessageVO> messageList = messageService.getMyMessage(memberId);
		// ������ ������ ���� �� ���� �޽��� �˻�
		messageService.removeByReceiverId(memberId);
		// ������ �޽����� DB���� ���� (���� ���н� �ٽ� DB�� ���)
		log.info("�� �޼��� : " + messageList);
		if(messageList != null) {
			for(MessageVO vo : messageList) { // ���� �۽�
				vo.setReceiverId(memberId); // receiverId ���� all�� �޽����� �����Ƿ� receiver �缳��
				unicast(vo);
			}
		}
	} // end afterConnectionEstablished
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("socket close");
		String memberId = session.getPrincipal().getName();
		log.info("���� ���� ���� id : " + memberId);
		alarmConnMap.remove(memberId);
	} // end afterConnectionClosed
	
	
	public void broadcast(MessageVO message) {
		log.info("����� ��ü �������� �޽��� ����");
		TextMessage jsonMsg = convertMsg(message);
		
		Iterator<String> iterator = alarmConnMap.keySet().iterator();

		String receiverId; 
		while(iterator.hasNext()) {
			receiverId = iterator.next();
//			if(receiverId.equals(message.getWriterId())) {
//				continue;
//			} // �ڱ� �ڽ��� �۽� ��󿡼� ���� (�׽�Ʈ�� ���� �ּ� ó��)
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
		log.info("���� �������� �޽��� ����");
		String receiverId = message.getReceiverId();
		TextMessage jsonMsg = convertMsg(message);
		if(alarmConnMap.containsKey(receiverId)) { // ���� ����� ���� ���̸� �ٷ� �۽�
			WebSocketSession client = alarmConnMap.get(receiverId);
			if(client.isOpen()) {
				try {
					client.sendMessage(jsonMsg);
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} // end ���� ���� ���� �۽�
		
		// ���� ���� �ƴϸ� db�� �޽��� ����
		log.info("������ �޽��� ���� : " + message);
		messageService.registerMessage(message);
		
	} // end unicast
	
	
//	private MessageVO noticeHandler(MessageVO messageVO, String writerId) {
//		MessageVO returnMsg = new MessageVO();
//		returnMsg.setReceiverId(writerId);
//		messageVO.setTitle("���� ����");
//		messageVO.setReceiverId("all");
//		messageVO.setCallbackInfo(null);
//		
//		log.info(messageVO);
//		int res = messageService.registerMessage(messageVO);
//		
//		if(res == 1) {
//			returnMsg.setContent("���� ��� ����");
//		}else {
//			returnMsg.setContent("���� ��Ͽ� �����߽��ϴ�. �ٽ� �õ����ּ���.");
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
		if(alarmConnMap.containsKey(receiverId)) { // ���� ����� ���� ���̸� �ٷ� �۽�
			WebSocketSession client = alarmConnMap.get(receiverId);
			if(client.isOpen()) {
				try {
					client.sendMessage(jsonMsg);
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} // end ���� ���� ���� �۽�
	} // end sendAlert
	
	public void sendReplyAlarm(int productId) {
		MessageVO returnMsg = new MessageVO();
		String receiverId = messageService.getSellerIdOf(productId);
		String redirectId = String.valueOf(productId);
		returnMsg.setContent("����� ��ǰ�� ����� ��ϵǾ����ϴ�. �̵��Ϸ��� Ŭ���ϼ���.");
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
