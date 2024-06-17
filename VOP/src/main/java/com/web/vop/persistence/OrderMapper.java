package com.web.vop.persistence;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;


@Mapper
public interface OrderMapper {
	// ����� �� ��ȸ (��� ������)
	public Date selectByExpectDeliveryDate(int paymentId); 
	
	// ����� �� ��ȸ (���� ��ȣ)
	public int selectByPaymentId(int paymentId); 
	
	// �ֹ� ���
	public int insertOrder(OrderVO orderVO);
	
	// paymentId�� �ֹ� �˻�
	public List<OrderViewDTO> selectOrderByPaymentId(int paymentId);
	
	// �ֹ� ��� ��ȸ 
	public List<OrderViewDTO> selectOrderListByMemberId(String memberId);

}
