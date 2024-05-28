package com.web.vop.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.vop.domain.MessageVO;
import com.web.vop.service.MessageService;

import lombok.extern.log4j.Log4j;

@ServerEndpoint("/alarm")
@Log4j
public class AlarmController extends Endpoint{

	private static HashMap<String, Session> connectionMap = null;
	private static ObjectMapper objectMapper = null;
	
	@Autowired
	public MessageService messageService;
	
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		log.info("websocket onOpen()");
		
		if(connectionMap == null) {
			connectionMap = new HashMap<>();
		}
		if(objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		
        session.addMessageHandler(new MessageHandler.Whole<String>() {
             public void onMessage(String jsonMsg) {
            	log.info("receive msg : " + jsonMsg);
            	MessageVO message = convertMsg(jsonMsg);
            	MessageVO returnMsg = null;
            	String msgType = message.getType();
            	
            	
            	if(msgType.equals("init")) { // ù �����
            		returnMsg = initHandler(session, message);
        //    		log.info(messageService);
        //    		List<MessageVO> messageList = messageService.getMyMessage(message.getWriterId());
            		//log.info("�� �޼��� : " + messageList);
//            		if(messageList.size() > 0) {
//            			log.info("��Ƽ �޼��� ����");
//            			messageList.add(returnMsg);
//            			unicastMultiMsg(messageList, message.getWriterId());
//            		}else {
            			unicast(returnMsg);
            		//}
            	}else if(msgType.equals("notice")) { // ��� �������� �۽� ��û 
            		returnMsg = noticeHandler(message);
            		broadcast(returnMsg);
            	}else if(msgType.equals("replyAlarm")) {
            		returnMsg = replyAlarmHandler(message);
            		unicast(returnMsg);
            	}
            	
             }
        });
	} // end onOpen
	
	

	@Override
	public void onClose(Session session, CloseReason closeReason) {
		log.info("websocket onClose()");
	} // end onClose
	
	@Override
	public void onError(Session session, Throwable thr) {
		log.info("websocket onError()");
		log.info(thr);
	} // end onError
	
	private void broadcast(MessageVO message) {
		log.info("����� ��ü �������� �޽��� ����");
		String jsonMsg = convertMsg(message);
		
		Iterator<String> iterator = connectionMap.keySet().iterator();

		String receiverId; 
		while(iterator.hasNext()) {
			receiverId = iterator.next();
//			if(receiverId.equals(message.getWriterId())) {
//				continue;
//			} // �ڱ� �ڽ��� �۽� ��󿡼� ���� (�׽�Ʈ�� ���� �ּ� ó��)
			Session client = connectionMap.get(receiverId);
			if(client.isOpen()) {
				client.getAsyncRemote().sendText(jsonMsg);
			}
		}
		
	} // end broadcast
	
	private void unicast(MessageVO message) {
		log.info("���� �������� �޽��� ����");
		String jsonMsg = convertMsg(message);
		String receiverId = message.getReceiverId();
		if(connectionMap.containsKey(receiverId)) { // ���� ����� ���� ���̸� �ٷ� �۽�
			log.info("���� ����");
			Session client = connectionMap.get(receiverId);
			if(client.isOpen()) {
				client.getAsyncRemote().sendText(jsonMsg);
				return;
			}
		} 
		// ���� ���� �ƴϸ� db�� �޽��� ����
		log.info("������ ����");
		messageService.registerMessage(message);
		
	} // end unicast
	
	private void unicastMultiMsg(List<MessageVO> messageList, String receiverId) {
		log.info("���� �������� �ټ� �޽��� ����");
		String jsonMsg = null;
		
		try {
			jsonMsg = objectMapper.writeValueAsString(messageList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Session client = connectionMap.get(receiverId);
		client.getAsyncRemote().sendText(jsonMsg);
	} // end unicastMultiMsg
	
	private MessageVO initHandler(Session session, MessageVO messageVO) {
		// ù ����� �� �� : ����� ���̵�� ���� ���, 
		MessageVO returnMsg = new MessageVO();
		
		connectionMap.put(messageVO.getWriterId(), session);
		returnMsg.setType("system");
		returnMsg.setReceiverId(messageVO.getWriterId());
		returnMsg.setContent("���� ��� ����");
		
		return returnMsg;
	} // end initHandler
	
	private MessageVO noticeHandler(MessageVO messageVO) {
		MessageVO returnMsg = messageVO;
		// �����ڷκ��� ��� �������� ���� ��û�� �� �� : ??
		
		return returnMsg;
	} // end noticeHandler
	
	private MessageVO replyAlarmHandler(MessageVO messageVO) {
		messageVO.setContent("����� ��ϵǾ����ϴ�. �̵��Ͻðڽ��ϱ�?");
		return messageVO;
	} // end replyAlarmHandler
	
	
	
	private MessageVO convertMsg(String jsonMsg) {
		MessageVO message = null;
		try {
			message = objectMapper.readValue(jsonMsg, MessageVO.class);
			log.info(message.getWriterId());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return message;
	} // end convertMsg
	
	private String convertMsg(MessageVO message) {
		String jsonMsg = null;
		try {
			jsonMsg = objectMapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonMsg;
	} // end convertMsg
}
