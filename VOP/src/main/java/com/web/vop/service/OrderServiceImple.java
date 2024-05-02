package com.web.vop.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.MemberVO;
import com.web.vop.domain.OrderVO;
import com.web.vop.persistence.OrderMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class OrderServiceImple implements OrderService {
	
	@Autowired
	OrderMapper orderMapper;
	
	@Override
	public OrderVO selectAllOrder(int orderId) { //오더id로 주문 전체 조회
		log.info("OrderService selectAllOrder()");
		
		return null;
	}
	
	@Override
	public Date getExpectDate(Date expectDeliveryDate) { //배송 예정일 조회
		log.info("OrderService getExpectDate()");
		Date result = orderMapper.selectByExpectDeliveryDate(expectDeliveryDate);
		log.info("배송예정일 : " + result);
		
		return result;
	}

	

}
