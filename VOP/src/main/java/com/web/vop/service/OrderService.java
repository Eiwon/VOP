package com.web.vop.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;
import com.web.vop.util.PageMaker;
import com.web.vop.util.Pagination;


@Service
public interface OrderService {
	
	
	// ����� �� ��ȸ (��� ������)
	String getExpectDateByPaymentId(int paymentId);
	
	// ����� �� ��ȸ (���� ��ȣ)
	int getPaymentId(int paymentId);
	
	// �ֹ� ���
	int registerOrder(OrderVO orderVO);
	
	// paymentId�� �ֹ� �˻�
	public List<OrderViewDTO> getOrderByPaymentId(int paymentId);
	
	// �ֹ� ��� ����
	int deleteOrderListByOrderId(int orderId); 
	
	// �ֹ� ��� ��ȸ 
	List<OrderViewDTO> getOrderListByMemberId(String memberId);
	
	// �ֹ� ��� (����¡)
	//public List<OrderViewDTO> search(PageMaker pageMaker);
}
