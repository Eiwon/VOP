package com.web.vop.service;

import java.util.List;


import com.web.vop.domain.DeliveryListDTO;
import com.web.vop.domain.DeliveryVO;

public interface DeliveryService {
	
	// ����� ���
	int registerDelivery(DeliveryVO deliveryVo);

	// ����� ���� 
	int  updateDelivery(DeliveryVO deliveryVo);
	
	// ����� ����
	int deleteDelivery(int deliveryId);
	
	// ����� �� ��ȸ by memberId 
	List<DeliveryVO> getMemberId(String memberId);
	
	// ����� �� ��ȸ by deliveryId and memberId
	DeliveryVO getDeliveryById(int deliveryId, String memberId);
	
	// �⺻ ����� ī��Ʈ
	boolean hasDefaultAddress(String memberId);
	
	// ����ϴ� deliveryId�� �⺻������� 1�� �ٲٱ�
	int updateNewDefault(int deliveryId, String memberId);
	
	// �ش��ϴ� memberId�� ������ �⺻ ����� ����� 0���� �ٲٱ� 
	int updateDefault(String memberId);
	
	// memberId�� �⺻ ����� �˻�
	DeliveryVO getDefaultDelivery(String memberId);

	// ����� ��ȸ
	List<DeliveryListDTO> getDeliveryList(int paymentId);
}
