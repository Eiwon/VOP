package com.web.vop.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.web.vop.domain.DeliveryVO;


@Service
public interface OrderService {
	
	
	// 배송지 상세 조회 (배송 예정일)
	Date getExpectDateByPaymentId(int paymentId);
	
	// 배송지 상세 조회 (송장 번호)
	int getPaymentId(int paymentId);
	
	// 배송지 상세 조회 (받는 사람 , 받는 주소 , 배송 요청 사항 )
	DeliveryVO getMemberId(String memberId);
	
	// 배송지 등록 
	int registerDelivery(DeliveryVO deliveryVo);
	
	// 배송지 수정 
	int  updateDelivery(DeliveryVO deliveryVo);
	
	// 배송지 삭제
	int deleteDelivery(int deliveryId);
	
	
}
