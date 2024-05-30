package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.MessageVO;

@Mapper
public interface MessageMapper {
	
	// 메세지 등록
	public int insertMessage(MessageVO messageVO);
	
	// receiver id로 수신 가능한 모든 메세지 검색
	public List<MessageVO> selectByReceiverId(String receiverId);
	
	// 지정 메세지 삭제(client)
	public int deleteByIdExceptNotice(int messageId);

	// 지정 메세지 삭제(admin)
	public int deleteById(int messageId);
	
	// 지정 유저에 대한 메시지 삭제
	public int deleteByReceiverId(String receiverId);

	// 모든 공지 검색
	public List<MessageVO> selectNotice();
	
	// messageId로 검색
	public MessageVO selectById(int messageId);
}
