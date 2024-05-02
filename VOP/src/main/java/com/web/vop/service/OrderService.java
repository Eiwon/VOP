package com.web.vop.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.OrderVO;

@Service
public interface OrderService {
	
	// ���� ���̵�� �ֹ� ��ü ��ȸ
	public OrderVO selectAllOrder(int orderId);
	
	// ���� ��� ���� ��ȸ 
	public Date getExpectDate(Date expectDeliveryDate); 
	
}
