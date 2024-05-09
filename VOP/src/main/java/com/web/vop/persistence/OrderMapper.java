package com.web.vop.persistence;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.DeliveryVO;


@Mapper
public interface OrderMapper {
	// ����� �� ��ȸ (��� ������)
	public Date selectByExpectDeliveryDate(int paymentId); 
	
	// ����� �� ��ȸ (���� ��ȣ)
	public int selectByPaymentId(int paymentId); 
	
	// ����� �� ��ȸ (�޴� ��� , �޴� �ּ� , ��� ��û ���� )
	public DeliveryVO selectByMemberId(String memberId);
	
	// ����� ��� 
	public int insertDelivery(DeliveryVO deliveryVo);
		
	// ����� ���� 
	public int updateDelivery(DeliveryVO deliveryVo);
	
	// ����� ����
	public int deleteDelivery(String memberId);

}
