package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.DeliveryVO;

public interface DeliveryService {
	
	// ����� ���
	int registerDelivery(DeliveryVO deliveryVo);

	// ����� ���� 
	int  updateDelivery(DeliveryVO deliveryVo);
	
	// ����� ����
	int deleteDelivery(String memberId);
	
	// ����� �� ��ȸ by memberId 
	List<DeliveryVO> getMemberId(String memberId);
	
	// ����� �� ��ȸ by deliveryId
	DeliveryVO getDeliveryById(int deliveryId);
}
