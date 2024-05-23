package com.web.vop.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	
	// �⺻ ����� ��ȸ 
	boolean hasDefaultAddress(String memberId);
	
	// �⺻ ����� �����ϱ�
	void setDefaultDelivery(int deliveryId, String memberId);
	
	// memberId�� �⺻ ����� �˻�
	DeliveryVO getDefaultDelivery(String memberId);

}
