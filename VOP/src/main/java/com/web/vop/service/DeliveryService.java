package com.web.vop.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	
	// 기본 배송지 조회 
	boolean hasDefaultAddress(String memberId);
	
	// 기본 배송지 설정하기
	void setDefaultDelivery(int deliveryId, String memberId);
	
	// memberId로 기본 배송지 검색
	DeliveryVO getDefaultDelivery(String memberId);

}
