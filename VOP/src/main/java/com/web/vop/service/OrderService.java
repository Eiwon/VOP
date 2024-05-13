package com.web.vop.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.domain.OrderVO;


@Service
public interface OrderService {
	
	
	// ����� �� ��ȸ (��� ������)
	Date getExpectDateByPaymentId(int paymentId);
	
	// ����� �� ��ȸ (���� ��ȣ)
	int getPaymentId(int paymentId);
	
	// ����� �� ��ȸ (�޴� ��� , �޴� �ּ� , ��� ��û ���� )
	DeliveryVO getMemberId(String memberId);
	
	// ����� ��� 
	int registerDelivery(DeliveryVO deliveryVo);
	
	// ����� ���� 
	int  updateDelivery(DeliveryVO deliveryVo);
	
	// ����� ����
	int deleteDelivery(String memberId);
	
	// �ֹ� ��� ��ȸ 
	List<OrderVO> getOrderListByMemberId(String memberId);
}
