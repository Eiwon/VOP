package com.web.vop.service;

import java.util.List;

import com.web.vop.domain.DeliveryVO;

public interface DeliveryService {
	
	// 배송지 등록
	int registerDelivery(DeliveryVO deliveryVo);

	// 배송지 수정 
	int  updateDelivery(DeliveryVO deliveryVo);
	
	// 배송지 삭제
	int deleteDelivery(String memberId);
	
	// 배송지 상세 조회 
	List<DeliveryVO> getMemberId(String memberId);
}
