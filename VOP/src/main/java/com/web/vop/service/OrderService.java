package com.web.vop.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.web.vop.domain.OrderVO;

@Service
public interface OrderService {
	
	// 오더 아이디로 주문 전체 조회
	public OrderVO selectAllOrder(int orderId);
	
	// 예상 배송 일자 조회 
	public Date getExpectDate(Date expectDeliveryDate); 
	
}
