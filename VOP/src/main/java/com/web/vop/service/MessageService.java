package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.MessageVO;

public interface MessageService {

	// �޼��� ���
	public int registerMessage(MessageVO messageVO);
		
	// receiver id�� ���� ������ ��� �޼��� �˻�
	public List<MessageVO> getMyMessage(String receiverId);
		
	// ���� �޼��� ����(client)
	public int removeExceptNotice(int messageId);

	// ���� �޼��� ����(admin)
	public int removeById(int messageId);
	
	// ���� ������ �޽��� ����
	public int removeByReceiverId(String receiverId);

	// ��� ���� �˻�
	public List<MessageVO> getNotice();
	
	// messageId�� �˻�
	public MessageVO getById(int messageId);
	
	// productId�� �Ǹ��� memberId �˻�
	String getSellerIdOf(int productId);
}
