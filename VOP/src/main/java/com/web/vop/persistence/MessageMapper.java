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
}
