package com.web.vop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.vop.domain.DeliveryVO;
import com.web.vop.persistence.DeliveryMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class DeliveryServiceImple implements DeliveryService{
	
	@Autowired
	private DeliveryMapper deliveryMapper;
	
	// 배송지 등록
	@Override
	public int registerDelivery(DeliveryVO deliveryVO) {
		log.info("registerDelivery()");
		int res = deliveryMapper.insertDelivery(deliveryVO);
		return res;
	} // end registerDelivery
	
	
}
