package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.MessageVO;
import com.web.vop.util.Pagination;

@Mapper
public interface MessageMapper {
	
	// �޼��� ���
	public int insertMessage(MessageVO messageVO);
	
	// receiver id�� ���� ������ ��� �޼��� �˻�
	public List<MessageVO> selectByReceiverId(String receiverId);
	
	// ���� �޼��� ����
	public int deleteById(int messageId);
	
	// ���� ������ ���� �޽��� ����
	public int deleteByReceiverId(String receiverId);

	// ��� ���� �˻�
	public List<MessageVO> selectNotice();
	
	// messageId�� �˻�
	public MessageVO selectById(int messageId);
	
	// ��� �˾� ���� �˻� 
	public List<MessageVO> selectAllPopupPaging(Pagination pagination);
	
	// ��� �˾� ���� �˻� ��
	public int selectAllPopupCnt();
	
	// ��� �˾� ���� id �˻� 
	public List<Integer> selectAllPopupId(); 
}
