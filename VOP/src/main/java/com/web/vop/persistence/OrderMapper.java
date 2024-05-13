package com.web.vop.persistence;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.OrderVO;


@Mapper
public interface OrderMapper {
	// ����� �� ��ȸ (��� ������)
	public Date selectByExpectDeliveryDate(int paymentId); 
	
	// ����� �� ��ȸ (���� ��ȣ)
	public int selectByPaymentId(int paymentId); 
	
	// �ֹ� ���
	public int insertOrder(OrderVO orderVO);
	
	// paymentId�� �ֹ� �˻�
	public List<OrderVO> selectOrderByPaymentId(int paymentId);
	
	// ����� �� ��ȸ (�޴� ��� , �޴� �ּ� , ��� ��û ���� )
	public DeliveryVO selectByMemberId(String memberId);
	
	// ����� ��� 
	public int insertDelivery(DeliveryVO deliveryVo);
		
	// ����� ���� 
	public int updateDelivery(DeliveryVO deliveryVo);
	
	// ����� ����
	public int deleteDelivery(String memberId);

}
