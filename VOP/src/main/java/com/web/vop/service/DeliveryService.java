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
	
	// ����� �� ��ȸ 
	List<DeliveryVO> getMemberId(String memberId);
}
