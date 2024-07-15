package com.web.vop.persistence;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.DeliveryExpectDTO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;



@Mapper
public interface OrderMapper {
	// ����� �� ��ȸ (��� ������)
	public String selectByExpectDeliveryDate(int orderId); 
	
	// ����� �� ��ȸ (���� ��ȣ)
	public int selectByPaymentId(int paymentId); 
	
	// �ֹ� ���
	public int insertOrder(OrderVO orderVO);
	
	// paymentId�� �ֹ� �˻�
	public List<OrderViewDTO> selectOrderByPaymentId(int paymentId);
	
	// �ֹ� ��� ��ȸ 
	public List<OrderViewDTO> selectOrderListByMemberId(String memberId);

	// �ֹ� ��� ����
	public int deleteOrderListByOrderId(int orderId);
	
	// ��� ������ ��ȸ �����ٸ�
	 List<DeliveryExpectDTO> selectDeliveryExpect();
	
	// ��ǰ ��� ��ȸ (����¡)
	 //List<OrderViewDTO> selectByOrderlist(Pagination pagination);
	 
	// ��ǰ ��� �� (����¡)
	 //int selectByOrderlistCnt(Pagination pagination);
}
