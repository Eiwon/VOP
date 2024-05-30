package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.MessageVO;

public interface MessageService {

	// 메세지 등록
	public int registerMessage(MessageVO messageVO);
		
	// receiver id로 수신 가능한 모든 메세지 검색
	public List<MessageVO> getMyMessage(String receiverId);
		
	// 지정 메세지 삭제(client)
	public int removeExceptNotice(int messageId);

	// 지정 메세지 삭제(admin)
	public int removeById(int messageId);
	
	// 지정 수신자 메시지 삭제
	public int removeByReceiverId(String receiverId);

	// 모든 공지 검색
	public List<MessageVO> getNotice();
	
	// messageId로 검색
	public MessageVO getById(int messageId);
	
	// productId로 판매자 memberId 검색
	String getSellerIdOf(int productId);
}
