package com.web.vop.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

	private static ObjectMapper objectMapper = null;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	Map<String, WebSocketSession> alarmConnMap;
	
	public AlarmHandler() {
		objectMapper = new ObjectMapper();
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("socket message received : " + message.getPayload());
		MessageVO messageVO = convertMsg(message.getPayload());
		
		MessageVO returnMsg = null;
    	String msgType = messageVO.getType();
    	String memberId = session.getPrincipal().getName();
    	messageVO.setWriterId(memberId);
    	
    	log.info(msgType.equals("notice"));
    	if(msgType.equals("notice")) { // �������� ��� ��û
    		returnMsg = noticeHandler(messageVO, memberId);
    		unicast(returnMsg);
    	}else if(msgType.equals("broadcast")){ // ���� ���� ��ü �������� �۽�
    		returnMsg = messageVO;
    		broadcast(returnMsg);
    	}else if(msgType.equals("replyAlarm")) { // ��� �˸� (Ŭ���� �̺�Ʈ �߻�)
    		returnMsg = replyAlarmHandler(messageVO, memberId);
    		unicast(returnMsg);
    	}else if(msgType.equals("instanceMsg")) { // ���� ���� �Ϲ� �˸�
    		returnMsg = messageVO;
    		unicast(returnMsg);
    	}
		
	} // end handleTextMessage
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("socket open");
		String memberId = session.getPrincipal().getName();
		log.info("���� ���� id : " + memberId);
		alarmConnMap.put(memberId, session);
		
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
	
	
	private void broadcast(MessageVO message) {
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
	
	private void unicast(MessageVO message) {
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
		if(!message.getType().equals("notice")) {
			messageService.registerMessage(message);
		}
	} // end unicast
	
	
	private MessageVO noticeHandler(MessageVO messageVO, String writerId) {
		MessageVO returnMsg = new MessageVO();
		returnMsg.setReceiverId(writerId);
		messageVO.setTitle("���� ����");
		messageVO.setReceiverId("all");
		messageVO.setCallbackInfo(null);
		
		log.info(messageVO);
		int res = messageService.registerMessage(messageVO);
		
		if(res == 1) {
			returnMsg.setContent("���� ��� ����");
		}else {
			returnMsg.setContent("���� ��Ͽ� �����߽��ϴ�. �ٽ� �õ����ּ���.");
		}
		
		return returnMsg;
	} // end noticeHandler
	

	private MessageVO replyAlarmHandler(MessageVO messageVO, String writerId) {
		MessageVO returnMsg = messageVO;
		returnMsg.setReceiverId(writerId);
		returnMsg.setContent("����� ��ǰ�� ����� ��ϵǾ����ϴ�. �̵��Ϸ��� Ŭ���ϼ���.");
		return returnMsg;
	} // end replyAlarmHandler

	
	
	
	
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
