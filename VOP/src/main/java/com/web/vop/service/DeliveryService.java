package com.web.vop.service;

import java.util.List;


import com.web.vop.domain.DeliveryListDTO;
import com.web.vop.domain.DeliveryVO;

public interface DeliveryService {
	
	// 배송지 등록
	int registerDelivery(DeliveryVO deliveryVo);

	// 배송지 수정 
	int  updateDelivery(DeliveryVO deliveryVo);
	
	// 배송지 삭제
	int deleteDelivery(int deliveryId);
	
	// 배송지 상세 조회 by memberId 
	List<DeliveryVO> getMemberId(String memberId);
	
	// 배송지 상세 조회 by deliveryId and memberId
	DeliveryVO getDeliveryById(int deliveryId, String memberId);
	
	// 기본 배송지 카운트
	boolean hasDefaultAddress(String memberId);
	
	// 등록하는 deliveryId의 기본배송지를 1로 바꾸기
	int updateNewDefault(int deliveryId, String memberId);
	
	// 해당하는 memberId의 나머지 기본 배송지 목록을 0으로 바꾸기 
	int updateDefault(String memberId);
	
	// memberId로 기본 배송지 검색
	DeliveryVO getDefaultDelivery(String memberId);

	// 배송지 조회
	List<DeliveryListDTO> getDeliveryList(int paymentId);
}
