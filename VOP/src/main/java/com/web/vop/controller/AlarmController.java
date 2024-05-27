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
            	
            	
            	if(msgType.equals("init")) { // 첫 연결시
            		returnMsg = initHandler(session, message);
        //    		log.info(messageService);
        //    		List<MessageVO> messageList = messageService.getMyMessage(message.getWriterId());
            		//log.info("내 메세지 : " + messageList);
//            		if(messageList.size() > 0) {
//            			log.info("멀티 메세지 전송");
//            			messageList.add(returnMsg);
//            			unicastMultiMsg(messageList, message.getWriterId());
//            		}else {
            			unicast(returnMsg);
            		//}
            	}else if(msgType.equals("notice")) { // 모든 유저에게 송신 요청 
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
		log.info("연결된 전체 유저에게 메시지 전송");
		String jsonMsg = convertMsg(message);
		
		Iterator<String> iterator = connectionMap.keySet().iterator();

		String receiverId; 
		while(iterator.hasNext()) {
			receiverId = iterator.next();
//			if(receiverId.equals(message.getWriterId())) {
//				continue;
//			} // 자기 자신은 송신 대상에서 제외 (테스트를 위해 주석 처리)
			Session client = connectionMap.get(receiverId);
			if(client.isOpen()) {
				client.getAsyncRemote().sendText(jsonMsg);
			}
		}
		
	} // end broadcast
	
	private void unicast(MessageVO message) {
		log.info("단일 유저에게 메시지 전송");
		String jsonMsg = convertMsg(message);
		String receiverId = message.getReceiverId();
		if(connectionMap.containsKey(receiverId)) { // 수신 대상이 접속 중이면 바로 송신
			log.info("접속 유저");
			Session client = connectionMap.get(receiverId);
			if(client.isOpen()) {
				client.getAsyncRemote().sendText(jsonMsg);
				return;
			}
		} 
		// 접속 중이 아니면 db에 메시지 저장
		log.info("미접속 유저");
		messageService.registerMessage(message);
		
	} // end unicast
	
	private void unicastMultiMsg(List<MessageVO> messageList, String receiverId) {
		log.info("단일 유저에게 다수 메시지 전송");
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
		// 첫 연결시 할 일 : 연결된 아이디와 세션 등록, 
		MessageVO returnMsg = new MessageVO();
		
		connectionMap.put(messageVO.getWriterId(), session);
		returnMsg.setType("system");
		returnMsg.setReceiverId(messageVO.getWriterId());
		returnMsg.setContent("연결 등록 성공");
		
		return returnMsg;
	} // end initHandler
	
	private MessageVO noticeHandler(MessageVO messageVO) {
		MessageVO returnMsg = messageVO;
		// 관리자로부터 모든 유저에게 공지 요청시 할 일 : ??
		
		return returnMsg;
	} // end noticeHandler
	
	private MessageVO replyAlarmHandler(MessageVO messageVO) {
		messageVO.setContent("댓글이 등록되었습니다. 이동하시겠습니까?");
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
