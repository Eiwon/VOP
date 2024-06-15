package com.web.vop.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.domain.OrderViewDTO;


@Service
public interface OrderService {
	
	
	// ����� �� ��ȸ (��� ������)
	Date getExpectDateByPaymentId(int paymentId);
	
	// ����� �� ��ȸ (���� ��ȣ)
	int getPaymentId(int paymentId);
	
	// �ֹ� ���
	int registerOrder(OrderVO orderVO);
	
	// paymentId�� �ֹ� �˻�
	public List<OrderVO> getOrderByPaymentId(int paymentId);
	
	// �ֹ� ��� ��ȸ 
	List<OrderViewDTO> getOrderListByMemberId(String memberId);
}
