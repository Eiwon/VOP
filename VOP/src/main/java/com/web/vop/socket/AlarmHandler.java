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
	
	// �ش� �������� ���� ���� �˸� ���� (instanceAlarm�� ���������� ���� ��� ���� �ֽ�ȭ�� �̷����)
	public void sendAuthUpdateAlarm(String memberId, String content) {
		MessageVO returnMsg = new MessageVO();
		returnMsg.setReceiverId(memberId);
		returnMsg.setTitle("���� ���� �˸�");
		returnMsg.setContent(content);
		returnMsg.setType(TYPE_AUTH_UPDATE);
		unicast(returnMsg);
	} // end sendAuthUpdateAlarm
	
	// �ش� �������� �Ϲ� �佺Ʈ �޽��� ���� (returnUri�� ��Ʈ�ѷ� �̸����� �Է�, null�̸� �ٷΰ��� ��ư�� ���� �佺Ʈ ���)
	public void sendInstanceAlarm(String receiverId, String title, String content, String returnUri) {
		MessageVO returnMsg = new MessageVO();
		returnMsg.setType(TYPE_INSTANCE);
		returnMsg.setTitle(title);
		returnMsg.setContent(content);
		returnMsg.setReceiverId(receiverId);
		returnMsg.setCallbackInfo(returnUri);
		unicast(returnMsg);
	} // end sendInstanceAlarm
	
	// productId�κ��� �Ǹ��ڸ� �˻��Ͽ� instanceAlarm �۽�
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
