package com.web.vop.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.persistence.OrderMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class OrderServiceImple implements OrderService {
	
	@Autowired
	OrderMapper orderMapper;
	
	@Override
	public Date getExpectDateByPaymentId(int paymentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPaymentId(int paymentId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DeliveryVO getMemberId(String memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int registerDelivery(DeliveryVO deliveryVo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateDelivery(DeliveryVO deliveryVo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteDelivery(int deliveryId) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	

}
