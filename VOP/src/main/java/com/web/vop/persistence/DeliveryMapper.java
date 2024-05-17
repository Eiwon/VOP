package com.web.vop.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.DeliveryVO;

@Mapper
public interface DeliveryMapper {
	
	// ����� ���
	int insertDelivery(DeliveryVO deliveryVo);
	
	// ����� ���� 
	public int updateDelivery(DeliveryVO deliveryVo);
		
	// ����� ����
	public int deleteDelivery(String memberId);
	
	// ����� �� ��ȸ (�޴� ��� , �޴� �ּ� , ��� ��û ���� )
	public List<DeliveryVO> selectByMemberId(String memberId);
	
	// ����� �� ��ȸ by deliveryId
	public DeliveryVO selectBydeliveryId(int deliveryId);
}
