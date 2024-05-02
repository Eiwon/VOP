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
	public OrderVO selectAllOrder(int orderId) { //����id�� �ֹ� ��ü ��ȸ
		log.info("OrderService selectAllOrder()");
		
		return null;
	}
	
	@Override
	public Date getExpectDate(Date expectDeliveryDate) { //��� ������ ��ȸ
		log.info("OrderService getExpectDate()");
		Date result = orderMapper.selectByExpectDeliveryDate(expectDeliveryDate);
		log.info("��ۿ����� : " + result);
		
		return result;
	}

	

}
