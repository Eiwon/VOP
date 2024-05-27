package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.MessageVO;

@Mapper
public interface MessageMapper {
	
	// �޼��� ���
	public int insertMessage(MessageVO messageVO);
	
	// receiver id�� ���� ������ ��� �޼��� �˻�
	public List<MessageVO> selectByReceiverId(String receiverId);
	
	// ���� �޼��� ����(client)
	public int deleteByIdExceptNotice(int messageId);

	// ���� �޼��� ����(admin)
	public int deleteById(int messageId);
}
