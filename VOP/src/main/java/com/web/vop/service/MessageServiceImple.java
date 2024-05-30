package com.web.vop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.MessageVO;
import com.web.vop.persistence.MessageMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MessageServiceImple implements MessageService{

	@Autowired
	MessageMapper messageMapper;
	
	@Override
	public int registerMessage(MessageVO messageVO) {
		log.info("registerMessage");
		int res = messageMapper.insertMessage(messageVO);
		return res;
	} // end registerMessage

	@Override
	public List<MessageVO> getMyMessage(String receiverId) {
		log.info("getMyMessage");
		return messageMapper.selectByReceiverId(receiverId);
	} // end getMyMessage

	@Override
	public int removeExceptNotice(int messageId) {
		log.info("removeExceptNotice");
		return messageMapper.deleteByIdExceptNotice(messageId);
	} // end removceExceptNotice

	@Override
	public int removeById(int messageId) {
		log.info("removeById");
		return messageMapper.deleteById(messageId);
	} // end removeById

	@Override
	public int removeByReceiverId(String receiverId) {
		log.info("removeByReceiverId");
		return messageMapper.deleteByReceiverId(receiverId);
	} // end removeByReceiverId

	@Override
	public List<MessageVO> getNotice() {
		log.info("getNotice");
		return messageMapper.selectNotice();
	} // end getNotice

	@Override
	public MessageVO getById(int messageId) {
		log.info("getById");
		return messageMapper.selectById(messageId);
	} // end getById

}
