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
	
	
	// 배송지 상세 조회 (배송 예정일)
	String getExpectDateByPaymentId(int paymentId);
	
	// 배송지 상세 조회 (송장 번호)
	int getPaymentId(int paymentId);
	
	// 주문 등록
	int registerOrder(OrderVO orderVO);
	
	// paymentId로 주문 검색
	public List<OrderViewDTO> getOrderByPaymentId(int paymentId);
	
	// 주문 목록 삭제
	int deleteOrderListByOrderId(int orderId); 
	
	// 주문 목록 조회 
	List<OrderViewDTO> getOrderListByMemberId(String memberId);
	
	// 주문 목록 (페이징)
	//public List<OrderViewDTO> search(PageMaker pageMaker);
}
