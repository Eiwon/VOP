package com.web.vop.persistence;

import org.apache.ibatis.annotations.Mapper;

import com.web.vop.domain.DeliveryVO;

@Mapper
public interface DeliveryMapper {
	
	// 배송지 등록
	int insertDelivery(DeliveryVO deliveryVO);
	
	
}
