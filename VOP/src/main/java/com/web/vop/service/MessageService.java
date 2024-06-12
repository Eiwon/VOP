package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.MessageVO;
import com.web.vop.util.PageMaker;

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
	public String getSellerIdOf(int productId);
	
	// ��� �˾����� �˻�(����¡)
	public List<MessageVO> getAllPopup(PageMaker pageMaker); 
	
	// ��� �˾� ���� id �˻� 
	public List<Integer> getAllPopupId(); 
}
